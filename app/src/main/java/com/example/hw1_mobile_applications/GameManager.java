package com.example.hw1_mobile_applications;
import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameManager {

    private final int MAX_LIVES = 3;
    private final int NUM_POSITIONS = 3;
    private final int NUM_GHOSTS = 5;

    private ImageView[] hearts;
    private ImageView[][] ghosts;
    private ImageView[] girl;
    private TextView gameOverText;
    private View darkOverlay;
    private View restartButton;
    private Timer timer;
    private int lives;
    private int currentPosition;
    private int ghostIndex;
    private int ghostPosition;
    private Context context;
    private UIManager uiManager;
    private SoundManager soundManager;

    public GameManager(Context context, ImageView[] hearts, ImageView[][] ghosts, ImageView[] girl,
                       TextView gameOverText, View darkOverlay, View restartButton,
                       UIManager uiManager, SoundManager soundManager) {
        this.context = context;
        this.hearts = hearts;
        this.ghosts = ghosts;
        this.girl = girl;
        this.gameOverText = gameOverText;
        this.darkOverlay = darkOverlay;
        this.restartButton = restartButton;
        this.uiManager = uiManager;
        this.soundManager = soundManager;

        this.lives = MAX_LIVES;
        this.currentPosition = 1;
        this.ghostIndex = NUM_GHOSTS - 1;
        this.ghostPosition = new Random().nextInt(NUM_POSITIONS);

        uiManager.hideAllViews(ghosts, girl);
        girl[currentPosition].setVisibility(View.VISIBLE);

        this.restartButton.setOnClickListener(v -> restartGame());
    }

    public void startGame() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(() -> updateGame());
            }
        }, 0, 500);
    }

    private void updateGame() {
        if (ghostIndex > 0) {
            ghosts[ghostPosition][ghostIndex].setVisibility(View.INVISIBLE);
            ghosts[ghostPosition][--ghostIndex].setVisibility(View.VISIBLE);
        } else {
            if (currentPosition == ghostPosition) {
                soundManager.vibrate(1000);
                if (--lives >= 0) {
                    hearts[lives].setVisibility(View.INVISIBLE);
                }

                if (lives == 0) {
                    gameOver();
                    return;
                }
            }

            ghosts[ghostPosition][0].setVisibility(View.INVISIBLE);
            ghostPosition = new Random().nextInt(NUM_POSITIONS);
            ghostIndex = NUM_GHOSTS - 1;
            ghosts[ghostPosition][ghostIndex].setVisibility(View.VISIBLE);
        }
    }

    public void moveGirl(boolean toRight) {
        if (toRight && currentPosition < NUM_POSITIONS - 1) {
            girl[currentPosition].setVisibility(View.INVISIBLE);
            girl[++currentPosition].setVisibility(View.VISIBLE);
        } else if (!toRight && currentPosition > 0) {
            girl[currentPosition].setVisibility(View.INVISIBLE);
            girl[--currentPosition].setVisibility(View.VISIBLE);
        }
    }

    private void gameOver() {
        timer.cancel();
        soundManager.vibrate(2000);

        darkOverlay.setVisibility(View.VISIBLE);
        gameOverText.setVisibility(View.VISIBLE);
        restartButton.setVisibility(View.VISIBLE);

        Toast.makeText(context, "Game Over", Toast.LENGTH_LONG).show();
    }

    private void restartGame() {
        darkOverlay.setVisibility(View.GONE);
        gameOverText.setVisibility(View.GONE);
        restartButton.setVisibility(View.GONE);

        lives = MAX_LIVES;
        currentPosition = 1;
        ghostIndex = NUM_GHOSTS - 1;
        ghostPosition = new Random().nextInt(NUM_POSITIONS);

        for (ImageView heart : hearts) {
            heart.setVisibility(View.VISIBLE);
        }
        uiManager.hideAllViews(ghosts, girl);
        girl[currentPosition].setVisibility(View.VISIBLE);

        startGame();
    }

    public void stopGame() {
        if (timer != null) {
            timer.cancel();
        }
    }
    }