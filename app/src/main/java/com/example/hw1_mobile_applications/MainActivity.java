package com.example.hw1_mobile_applications;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageButton panel_BTN_Right;
    private ImageButton panel_BTN_Left;
    private ImageView[] panel_IMG_hearts;
    private ImageView[] panel_IMG_girl;
    private ImageView[] panel_IMG_left_ghosts, panel_IMG_mid_ghosts, panel_IMG_right_ghosts;
    private ImageView[][] panel_IMG_Vertical_Arrays;
    private TextView gameOverText;
    private View darkOverlay;
    private Button restartButton;
    private GameManager gameManager;
    private UIManager uiManager;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideSystemUI(this);
        findViews();
        initButtons();

        uiManager = new UIManager();
        soundManager = new SoundManager(this);

        gameManager = new GameManager(this, panel_IMG_hearts, panel_IMG_Vertical_Arrays, panel_IMG_girl,
                gameOverText, darkOverlay, restartButton, uiManager, soundManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameManager.startGame();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameManager.stopGame();
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
            soundManager.vibrate(100);
            gameManager.moveGirl(false);
        });

        panel_BTN_Right.setOnClickListener(v -> {
            soundManager.vibrate(100);
            gameManager.moveGirl(true);
        });
    }

    public static void hideSystemUI(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }
}