package com.example.minigames.gameBasic.caroGame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.example.minigames.R;
import com.example.minigames.gameBasic.caroGame.CaroGameActivity;

public class Board_HumVsHum extends View {

    private int m = 18, n = 32;

    public Boolean firstPlayerX = false;

    private static int empty_cell = 0;
    private static int x_cell = 1;
    private static int o_cell = -1;

    private int grid_width = 25;
    private int grid_height = 25;
    private int grid_size = 40;
    public int playerTurn = 0;

    int mX, mY;

    private int[][] arr = new int[m][n];

    private boolean isFirstInit = true;

    int mSizeLast = 0;

    private void clear() {
        arr = new int[m][n];
        System.err.println("onCreate boardchess");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                this.arr[i][j] = empty_cell;
            }
        }
        invalidate();
    }

    public Board_HumVsHum(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Board_HumVsHum(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        DisplayMetrics displayMetric = new DisplayMetrics();
        String cell_char = new String();

        System.err.println("call to Chessboard onDraw");
        super.onDraw(canvas);
        // Get the size of the screen
        System.err.println("get window size");

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetric);
        System.err.println("done get window size");
        int width = displayMetric.widthPixels;
        int height = displayMetric.heightPixels;
        System.out.println("Measure: " + "width= " + width + "; height= " + height);

        // Calculate the size of the board and device
        // The number of squares is m
        grid_size = width / m;
        System.out.println("Calculate: " + "m= " + m + "; n= " + n);
        m = width / grid_size;
        mSizeLast = width % grid_size;
        n = height / grid_size;
        System.out.println("Value after drawing: " + "m= " + m + "; n= " + n + "grid_sze" + grid_size);
        grid_width = m;
        grid_height = n;
        System.out.println("Value after drawing: " + "grid_height= " + grid_height + "; grid_width= " + grid_width + ";grid_sze" + grid_size);

        if (isFirstInit) {
            clear();
            isFirstInit = false;
        }

        System.err.println("new paint");
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        for (int i = 0; i < grid_width; i++) {
            for (int j = 0; j < grid_height; j++) {
                System.err.println("draw one cell of the board " + i + " " + j + " " + grid_height + " " + grid_width);
                if (arr[i][j] == empty_cell) {
                    paint.setColor(Color.WHITE); // Set color for empty cell
                    cell_char = " ";
                } else if (arr[i][j] == x_cell) {
                    paint.setColor(Color.RED); // Set color for X-cell
                    cell_char = "X";
                } else if (arr[i][j] == o_cell) {
                    paint.setColor(Color.BLUE); // Set color for O-cell
                    cell_char = "O";
                }

                // Fill rectangle
                paint.setTextSize(grid_size / 2);
                paint.setStyle(Paint.Style.FILL);
                int xright = (i + 1) * grid_size;
                if ((i + 1) == grid_width) {
                    xright += mSizeLast;
                }
                canvas.drawRect(new Rect(i * grid_size, j * grid_size, xright, (j + 1) * grid_size), paint);

                // Border rectangle
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                canvas.drawRect(new Rect(i * grid_size, j * grid_size, xright, (j + 1) * grid_size), paint);
                Rect bounds = new Rect();
                paint.getTextBounds("X", 0, 1, bounds);
                canvas.drawText(cell_char, 0, 1, i * grid_size + (grid_size - bounds.width()) / 2,
                        j * grid_size + (bounds.height() + (grid_size - bounds.height()) / 2), paint);
            }
        }
    }

    private void showConfirmRetake(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Caro game");
        builder.setIcon(R.drawable.warning);
        builder.setMessage("Wrong move, please move again!");
        builder.setNegativeButton("OK", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    private void showWinner(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Caro Game");
        builder.setIcon(R.drawable.congra);
        if (playerTurn == 0) {
            if (firstPlayerX == true) { // First Human is X
                builder.setMessage("!!!!!!    Congratulation O player    !!!!!!");
            }
            if (firstPlayerX == false) {
                builder.setMessage("!!!!!!    Congratulation X player    !!!!!!");
            }
        } else if (playerTurn == 1) {
            if (firstPlayerX == true) {
                builder.setMessage("!!!!!!    Congratulation X player    !!!!!!");
            }
            if (firstPlayerX == false) {
                builder.setMessage("!!!!!!    Congratulation O player    !!!!!!");
            }
        }
        builder.setPositiveButton("OK", (dialog, which) -> {
            final AlertDialog.Builder newbuilder = new AlertDialog.Builder(getContext());
            newbuilder.setTitle("New game");
            newbuilder.setMessage("Do you want start new game?");
            newbuilder.setIcon(R.drawable.newgame);

            newbuilder.setPositiveButton("Yes", (dialog1, which1) -> clear());
            newbuilder.setNegativeButton("No", (dialog12, id) -> {
                dialog12.cancel();
                ((CaroGameActivity)getContext()).onSupportNavigateUp();
            });
            newbuilder.create().show();
        });
        builder.create().show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mX = (int) event.getX();
                mY = (int) event.getY();

                System.out.println("Action down " + mX + "  " + mY + " " + (int) mX / grid_size + " " + (int) mY / grid_size);

                if (this.arr[(int) mX / grid_size][(int) mY / grid_size] == empty_cell) {
                    if (playerTurn == 0) {
                        if (firstPlayerX == true) {
                            this.arr[(int) mX / grid_size][(int) mY / grid_size] = x_cell;
                        }
                        if (firstPlayerX == false) {
                            this.arr[(int) mX / grid_size][(int) mY / grid_size] = o_cell;
                        }
                        // Transfer turns to Human 2
                        playerTurn = 1;
                    }
                    else if (playerTurn == 1) {
                        if (firstPlayerX == false) {
                            this.arr[(int) mX / grid_size][(int) mY / grid_size] = x_cell;
                        }
                        if (firstPlayerX == true) {
                            this.arr[(int) mX / grid_size][(int) mY / grid_size] = o_cell;
                        }
                        playerTurn = 0;
                    }
                } else {
                    showConfirmRetake();
                }

                int hang = (int) mX / grid_size;
                int cot = (int) mY / grid_size;
                int dem = 1;
                int index = 1;

                // Check the horizontal row
                while (hang + index < m && arr[hang][cot] == arr[hang + index][cot]) {
                    dem++;
                    index++;
                }
                index = 1;
                while (hang - index > 0 && arr[hang][cot] == arr[hang - index][cot]) {
                    dem++;
                    index++;
                }
                if (dem >= 5) {
                    showWinner();
                }

                // Check the vertical column
                dem = 1;
                index = 1;
                while (cot + index < n && arr[hang][cot] == arr[hang][cot + index]) {
                    dem++;
                    index++;
                }
                index = 1;
                while (cot - index > 0 && arr[hang][cot] == arr[hang][cot - index]) {
                    dem++;
                    index++;
                }
                if (dem >= 5) {
                    showWinner();
                }

                // Check the left diagonal
                dem = 1;
                index = 1;
                while (hang + index < m && cot + index < n && arr[hang][cot] == arr[hang + index][cot + index]) {
                    dem++;
                    index++;
                }
                index = 1;
                while (cot - index > 0 && hang - index > 0 && arr[hang][cot] == arr[hang - index][cot - index]) {
                    dem++;
                    index++;
                }
                if (dem >= 5) {
                    showWinner();
                }

                // Check the right diagonal
                dem = 1;
                index = 1;
                while (hang + index < m && cot - index > 0 && arr[hang][cot] == arr[hang + index][cot - index]) {
                    dem++;
                    index++;
                }
                index = 1;
                while (cot + index < n && hang - index > 0 && arr[hang][cot] == arr[hang - index][cot + index]) {
                    dem++;
                    index++;
                }
                if (dem >= 5) {
                    showWinner();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("Action move");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("Action up");
                break;
            case MotionEvent.ACTION_CANCEL:
                System.out.println("Action cancel");
                break;
            case MotionEvent.ACTION_OUTSIDE:
                System.out.println("Action outside");
                break;
        }
        this.invalidate();
        return super.onTouchEvent(event);
    }
}
