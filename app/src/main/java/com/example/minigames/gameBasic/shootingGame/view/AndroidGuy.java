package com.example.minigames.gameBasic.shootingGame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.minigames.R;

public class AndroidGuy {

    float x;
    float y;
    float stepX = 10; // Guy's step in (x,y) direction
    float stepY = 5; // Gives speed of motion, larger means faster speed
    int lowerX, lowerY, upperX, upperY;

    Bitmap android_guy;

    private Context mContext;

    public AndroidGuy(int color, Context c) {
        mContext = c;
        // Create a bitmap from the supplied image (the image is the icon that is part of the app)
        android_guy = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.rock),50,50, false);

        // Increase speed after 10s
        final Handler handler = new Handler(Looper.getMainLooper());
        Runnable task = new Runnable() {
            @Override
            public void run() {
                stepY += 3;
                Log.d("testt","increase speed");
                handler.postDelayed(this, 10 * 1000);
            }
        };
        handler.post(task);
    }

    public void setBounds(int lx, int ly, int ux, int uy) {
        lowerX = lx;
        lowerY = ly;
        upperX = ux;
        upperY = uy;

        x = (float) ((upperX-50)*Math.random());
        y = 0;
    }

    public boolean move() {
        // Get new (x,y) position. Movement is always in vertical direction downwards
        y += stepY;
        // Detect when the guy reaches the bottom of the screen
        // Restart at a random location at the top of the screen
        if (y + 50 > upperY) {
            x = (float) ((upperX-50)*Math.random());
            y = 0;
            SoundEffects.INSTANCE.playSound(SoundEffects.SOUND_GUY);
            return false;
        } else {
            return true;
        }
    }

    public void reset() {
        x = (float) ((upperX-50)*Math.random());
        y = 0;
        stepY = 5;
    }

    public RectF getRect() {
        return new RectF(x,y,x+50,y+50);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(android_guy, x, y, null);
    }
}
