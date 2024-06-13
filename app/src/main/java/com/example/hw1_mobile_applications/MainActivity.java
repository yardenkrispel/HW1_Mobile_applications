package com.example.hw1_mobile_applications;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final int MAX_LIVES = 3;

    private ImageButton panel_BTN_Right;
    private ImageButton panel_BTN_Left;
    private ImageView[] panel_IMG_hearts;
    private ImageView[] panel_IMG_girl;
    private ImageView[] panel_IMG_left_ghosts, panel_IMG_mid_ghosts, panel_IMG_right_ghosts;
    private ImageView[][] panel_IMG_Vertical_Arrays;
    private TextView gameOverText;
    private View darkOverlay;
    private Button restartButton;
    private Timer timer = new Timer();
    int i = 3;
    int randChoose;

    private int current = 1;
    private int lives = MAX_LIVES;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView panel_IMG_background = findViewById(R.id.panel_IMG_background);
        Glide.with(this)
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFZRRs_y8bH-YFIszmX1bbIpZpBA1vhPhxew&usqp=CAU")
                .into(panel_IMG_background);

        hideSystemUI(this);

        findViews();

        initButtons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startUI();
    }

    private void startUI() {
        timer = new Timer();

        randChoose = (int) Math.floor(Math.random() * 3);
        panel_IMG_Vertical_Arrays[randChoose][i].setVisibility(View.VISIBLE);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> update_UI_logic());
            }
        }, 0, 500);
    }

    private void update_UI_logic() {
        if (i > 0) {
            panel_IMG_Vertical_Arrays[randChoose][i].setVisibility(View.INVISIBLE);
            panel_IMG_Vertical_Arrays[randChoose][i - 1].setVisibility(View.VISIBLE);
            i--;
        } else {
            if (current == randChoose) {
                vibrate(1000);
                lives--;
                panel_IMG_hearts[lives].setVisibility(View.INVISIBLE);
                if (lives != 0)
                    Toast.makeText(this, "Watch out", Toast.LENGTH_LONG).show();

                if (lives == 0) {
                    gameOver();
                }
            }

            panel_IMG_Vertical_Arrays[randChoose][0].setVisibility(View.INVISIBLE);
            randChoose = (int) Math.floor(Math.random() * 3);
            i = 4;
            panel_IMG_Vertical_Arrays[randChoose][i].setVisibility(View.VISIBLE);
        }
    }

    private void gameOver() {
        timer.cancel();

        vibrate(2000);
        darkOverlay.setVisibility(View.VISIBLE); // Show the dark overlay
        gameOverText.setVisibility(View.VISIBLE); // Show "GAME OVER" text
        restartButton.setVisibility(View.VISIBLE); // Show Restart button

        restartButton.setOnClickListener(v -> {
            restartGame();
        });
        Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_LONG).show();
    }
    private void restartGame() {
        darkOverlay.setVisibility(View.GONE);
        gameOverText.setVisibility(View.GONE);
        restartButton.setVisibility(View.GONE);

        // Reset game variables and UI elements
        lives = MAX_LIVES;
        current = 1;
        i = 3;

        // Show all hearts
        for (ImageView heart : panel_IMG_hearts) {
            heart.setVisibility(View.VISIBLE);
        }

        // Hide all ghosts and the girl
        for (ImageView[] ghosts : panel_IMG_Vertical_Arrays) {
            for (ImageView ghost : ghosts) {
                ghost.setVisibility(View.INVISIBLE);
            }
        }
        for (ImageView girl : panel_IMG_girl) {
            girl.setVisibility(View.INVISIBLE);
        }

        // Show the girl in the middle position
        panel_IMG_girl[1].setVisibility(View.VISIBLE);

        startUI(); // Restart the game logic
    }
    @Override
    protected void onStop() {
        super.onStop();
        stopUI();
    }

    private void stopUI() {
        timer.cancel();
    }

    private void move(boolean direction) {
        if (direction && current <= 1) {
            panel_IMG_girl[current].setVisibility(View.INVISIBLE);
            current++;
            panel_IMG_girl[current].setVisibility(View.VISIBLE);
        } else if (!direction && current >= 1) {
            panel_IMG_girl[current].setVisibility(View.INVISIBLE);
            current--;
            panel_IMG_girl[current].setVisibility(View.VISIBLE);
        }
    }

    private void findViews() {
        panel_BTN_Right = findViewById(R.id.panel_BTN_right);
        panel_BTN_Left = findViewById(R.id.panel_BTN_left);

        panel_IMG_hearts = new ImageView[]{
                findViewById(R.id.heart1),
                findViewById(R.id.heart2),
                findViewById(R.id.heart3),
        };

        panel_IMG_left_ghosts = new ImageView[]{
                findViewById(R.id.panel_IMG_s0_left),
                findViewById(R.id.panel_IMG_s1_l),
                findViewById(R.id.panel_IMG_s2_l),
                findViewById(R.id.panel_IMG_s3_l),
                findViewById(R.id.panel_IMG_s4_l),
        };

        panel_IMG_mid_ghosts = new ImageView[]{
                findViewById(R.id.panel_IMG_s0_mid),
                findViewById(R.id.panel_IMG_s1_m),
                findViewById(R.id.panel_IMG_s2_m),
                findViewById(R.id.panel_IMG_s3_m),
                findViewById(R.id.panel_IMG_s4_m),
        };

        panel_IMG_right_ghosts = new ImageView[]{
                findViewById(R.id.panel_IMG_s0_right),
                findViewById(R.id.panel_IMG_s1_r),
                findViewById(R.id.panel_IMG_s2_r),
                findViewById(R.id.panel_IMG_s3_r),
                findViewById(R.id.panel_IMG_s4_r),
        };

        panel_IMG_Vertical_Arrays = new ImageView[][]{
                panel_IMG_left_ghosts,
                panel_IMG_mid_ghosts,
                panel_IMG_right_ghosts};

        panel_IMG_girl = new ImageView[]{
                findViewById(R.id.panel_IMG_main_left),
                findViewById(R.id.panel_IMG_main_mid),
                findViewById(R.id.panel_IMG_main_right),
        };
        gameOverText = findViewById(R.id.game_over_text);
        darkOverlay = findViewById(R.id.dark_overlay);
        restartButton = findViewById(R.id.restart_button);

    }

    private void initButtons() {
        panel_BTN_Left.setOnClickListener(v -> {
            vibrate(100);
            // Right = True Left = False
            move(false);
        });

        panel_BTN_Right.setOnClickListener(v -> {
            vibrate(100);
            move(true);
        });
    }

    private void vibrate(int millisecond) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for the specified milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(millisecond, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            // Deprecated in API 26
            v.vibrate(millisecond);
        }
    }

    public static void hideSystemUI(Activity activity) {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        // Dim the Status and Navigation Bars
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);

        // Without - cut out display
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }
}