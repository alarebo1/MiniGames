package com.example.minigames.gameBasic.shootingGame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.example.minigames.R;

public class Cannon {

    float x = -1;
    float y = -1;
    float stepX = 30; // Cannon's step in x direction
    int lowerX, lowerY, upperX, upperY;
    private Paint paint; // The paint style, color used for drawing

    private Context mContext;

    private Drawable mCustomImage;

    public Cannon(int color, Context c) {
        paint = new Paint();
        paint.setColor(color);
        mContext = c;
        mCustomImage = mContext.getResources().getDrawable(R.drawable.icon_tank, null);
    }

    public void setBounds(int lx, int ly, int ux, int uy) {
        lowerX = lx;
        lowerY = ly;
        upperX = ux;
        upperY = uy;

        x = ux / 2;
        y = uy;
    }

    public void moveLeft() {
        // Get new (x,y) position of the canvas by moving it left
        // when the left button is clicked. Ensure that it does not
        // move off the screen.
        if (x - 40 > 0) {
            x -= stepX;
        }
    }

    public void moveRight() {
        // Get new (x,y) position of the canvas by moving it right
        // when the right button is clicked. Ensure that it does not
        // move off the screen.
        if (x + 40 < upperX) {
            x += stepX;
        }
    }

    public float getPosition() {
        return x;
    }

    // Draw the cannon on the canvas
    public void draw(Canvas canvas) {
        Rect imageBounds = new Rect((int) x - 40, (int) y - 80, (int) x + 40, (int) y);
        mCustomImage.setBounds(imageBounds);
        mCustomImage.draw(canvas);
    }
}
