package com.example.minigames.gameBasic.shootingGame.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

public class MissedGuy {

    private Paint paint;
    private int missed;

    public MissedGuy(int color) {
        paint = new Paint();
        // Set the font face and size of drawing text
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(40);
        paint.setColor(color);
        missed = 0;
    }

    public void incrementMissed() {
        missed++;
    }

    public void reset() {
        missed = 0;
    }

    public int getMissed(){return missed;}

    public void draw(Canvas canvas) {
        // Draw text on the canvas
        canvas.drawText("Missed : " + missed, 10, 130, paint);
    }
}
