package com.example.hw1_mobile_applications;

import android.view.View;
import android.widget.ImageView;

public class UIManager {

    public void hideAllViews(ImageView[][] ghosts, ImageView[] girl) {
        for (ImageView[] row : ghosts) {
            for (ImageView ghost : row) {
                ghost.setVisibility(View.INVISIBLE);
            }
        }
        for (ImageView g : girl) {
            g.setVisibility(View.INVISIBLE);
        }
    }
}