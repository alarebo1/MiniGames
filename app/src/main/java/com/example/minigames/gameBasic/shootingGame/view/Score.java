package com.example.minigames.gameBasic.shootingGame.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

public class Score {

    private Paint paint;
    public int score;

    public Score(int color) {
        paint = new Paint();
        // Set the font face and size of drawing text
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(40);
        paint.setColor(color);
        score = 0;
    }

    public void incrementScore() {
        score++;
    }

    public int getScore() {

        return score;
    }

    public void draw(Canvas canvas) {
        // Draw text on the canvas
        canvas.drawText("Score : " + score, 10, 40, paint);
    }
}
