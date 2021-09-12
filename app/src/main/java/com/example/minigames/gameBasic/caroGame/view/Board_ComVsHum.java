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
import com.example.minigames.firebase.FirebaseHelper;
import com.example.minigames.gameBasic.caroGame.CaroGameActivity;
import com.example.minigames.gameBasic.shop.cData;
import com.example.minigames.ultis.Utils;

public class Board_ComVsHum extends View {

    private int m = 18, n = 32;
    public int caroScore = 0;
    cData dt = new cData();



    public Boolean firstPlayerX = false;

    private static int empty_cell = 0;
    private static int x_cell = 1;
    private static int o_cell = -1;
    int maxi = 0;
    int maxj = 0;

    private int grid_width = 25;
    private int grid_height = 25;
    private int grid_size = 40;
    public int playerTurn = 0;

    int mX, mY;

    private int[][] arr = new int[m][n];

    private boolean isFirstInit = true;

    // Variable to save point for game
    long mStartTime = 0;

    int mSizeLast = 0;

    public Board_ComVsHum(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Board_ComVsHum(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        DisplayMetrics displayMetric = new DisplayMetrics();
        String cell_char = new String();
        System.err.println("call to Chessboard onDraw MultiBoardChess");
        super.onDraw(canvas);

        // Get the size of the screen
        System.err.println("get window size MultiBoardChess");
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetric);
        System.err.println("done get window size MultiBoardChess");
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
            System.err.println("onCreate Obj_MultiBoardChess");
            arr = new int[m][n];
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    this.arr[i][j] = empty_cell;
                }
            }
            isFirstInit = false;
        }

        System.err.println("new paint MultiBoardChess");
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        for (int i = 0; i < grid_width; i++) {
            for (int j = 0; j < grid_height; j++) {
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
                canvas.drawText(cell_char, 0, 1, i * grid_size + (grid_size - bounds.width()) / 2, j * grid_size + (bounds.height() + (grid_size - bounds.height()) / 2), paint);
            }
        }
    }

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

    private void showConfirmRetake() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Caro Game");
        builder.setIcon(R.drawable.warning);
        builder.setMessage("Wrong move, please move again!");
        builder.setNegativeButton("OK", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    public void showWinDialog(boolean humWin) {
        if (humWin) {

            long time = Utils.millisecondToSecond(System.currentTimeMillis() - mStartTime);

            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Caro game");
            builder.setIcon(R.drawable.congra);
            builder.setMessage(this.getContext().getString(R.string.alert_win, time));
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
                dialog.cancel();
            });
            builder.create().show();
            caroScore = 100/(int)time;
            dt.setCaro(this);
            dt.updateData();
            FirebaseHelper.getInstance().getUserDao().updateGamePoint(1, time);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Caro game");
            builder.setIcon(R.drawable.congra);
            builder.setMessage("The winner is Computer!");
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
                dialog.cancel();
            });
            builder.create().show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mStartTime == 0) {
                    mStartTime = System.currentTimeMillis();
                }

                mX = (int) event.getX();
                mY = (int) event.getY();
                System.out.println("Action down " + mX + "  " + mY + " " + (int) mX / grid_size + " " + (int) mY / grid_size);

                // Turn of Human
                if (playerTurn == 0) {
                    System.out.println("playTurn = 0" + " Human");
                    if (this.arr[(int) mX / grid_size][(int) mY / grid_size] == empty_cell) {
                        // Turn of Human if Human is X
                        if (firstPlayerX == true) {
                            this.arr[(int) mX / grid_size][(int) mY / grid_size] = x_cell;
                        }
                        // Turn of Human if Human is O
                        if (firstPlayerX == false) {
                            this.arr[(int) mX / grid_size][(int) mY / grid_size] = o_cell;
                        }

                        playerTurn = 1;
                        int rowhuman = mX / grid_size;
                        int colhuman = mY / grid_size;
                        int counthuman = 1;
                        int index = 1;
                        while (rowhuman + index < m && arr[rowhuman][colhuman] == arr[rowhuman + index][colhuman]) {
                            counthuman++;
                            index++;
                        }
                        index = 1;
                        while (rowhuman - index > 0 && arr[rowhuman][colhuman] == arr[rowhuman - index][colhuman]) {
                            counthuman++;
                            index++;
                        }
                        if (counthuman >= 5) {
                            showWinDialog(true);
                        }

                        counthuman = 1;
                        index = 1;
                        while (colhuman + index < n && arr[rowhuman][colhuman] == arr[rowhuman][colhuman + index]) {
                            counthuman++;
                            index++;
                        }
                        index = 1;
                        while (colhuman - index > 0 && arr[rowhuman][colhuman] == arr[rowhuman][colhuman - index]) {
                            counthuman++;
                            index++;
                        }
                        if (counthuman >= 5) {
                            showWinDialog(true);
                        }

                        counthuman = 1;
                        index = 1;
                        while (rowhuman + index < m && colhuman + index < n && arr[rowhuman][colhuman] == arr[rowhuman + index][colhuman + index]) {
                            counthuman++;
                            index++;
                        }
                        index = 1;
                        while (colhuman - index > 0 && rowhuman - index > 0 && arr[rowhuman][colhuman] == arr[rowhuman - index][colhuman - index]) {
                            counthuman++;
                            index++;
                        }
                        if (counthuman >= 5) {
                            showWinDialog(true);
                        }

                        counthuman = 1;
                        index = 1;
                        while (rowhuman + index < m && colhuman - index > 0 && arr[rowhuman][colhuman] == arr[rowhuman + index][colhuman - index]) {
                            counthuman++;
                            index++;
                        }
                        index = 1;
                        while (colhuman + index < n && rowhuman - index > 0 && arr[rowhuman][colhuman] == arr[rowhuman - index][colhuman + index]) {
                            counthuman++;
                            index++;
                        }
                        if (counthuman >= 5) {
                            showWinDialog(true);
                        }

                        this.invalidate();

                        if (counthuman >= 5) {
                            showWinDialog(true);
                        }
                    }
                    
                    // Turn of Computer
                    // playTurn != 0 is Computer
                    if (playerTurn == 1) {
                        System.out.println("playTurn = 1" + " Computer");

                        int ArrayPointBlock[][] = new int[m][n];

                        for (int i = 0; i < arr.length; i++) {
                            for (int j = 0; j < arr[0].length; j++) {
                                int row = (int) i;
                                int col = (int) j;
                                int index = 1;
                                int count_x_cell = 1;
                                int count_o_cell = 1;

                                if (arr[row][col] == empty_cell) {
                                    // Computer is O, Human is X
                                    if (firstPlayerX == false) {


                                        count_x_cell = 1;
                                        index = 1;

                                        // Check the horizontal row
                                        while (row + index < m && arr[row + index][col] == x_cell) {
                                            count_x_cell++;
                                            index++;
                                        }
                                        index = 1;
                                        while (row - index > 0 && arr[row - index][col] == x_cell) {
                                            count_x_cell++;
                                            index++;
                                        }

                                        switch (count_x_cell) {
                                            case 5:
                                                if (ArrayPointBlock[i][j] < 10)
                                                    ArrayPointBlock[i][j] = 10;
                                                break;
                                            case 4:
                                                if (ArrayPointBlock[i][j] < 8)
                                                    ArrayPointBlock[i][j] = 8;
                                                break;
                                            case 3:
                                                if (ArrayPointBlock[i][j] < 6)
                                                    ArrayPointBlock[i][j] = 6;
                                                break;
                                            case 2:
                                                if (ArrayPointBlock[i][j] < 4)
                                                    ArrayPointBlock[i][j] = 4;
                                                break;
                                        }

                                        // Check the vertical column
                                        count_x_cell = 1;
                                        index = 1;
                                        while (col + index < n && arr[row][col + index] == x_cell) {
                                            count_x_cell++;
                                            index++;
                                        }
                                        index = 1;
                                        while (col - index > 0 && arr[row][col - index] == x_cell) {
                                            count_x_cell++;
                                            index++;
                                        }
                                        switch (count_x_cell) {
                                            case 5:
                                                if (ArrayPointBlock[i][j] < 10)
                                                    ArrayPointBlock[i][j] = 10;
                                                break;
                                            case 4:
                                                if (ArrayPointBlock[i][j] < 8)
                                                    ArrayPointBlock[i][j] = 8;
                                                break;
                                            case 3:
                                                if (ArrayPointBlock[i][j] < 6)
                                                    ArrayPointBlock[i][j] = 6;
                                                break;
                                            case 2:
                                                if (ArrayPointBlock[i][j] < 4)
                                                    ArrayPointBlock[i][j] = 4;
                                                break;
                                        }

                                        // Check the left diagonal
                                        count_x_cell = 1;
                                        index = 1;
                                        while (row + index < m && col + index < n && arr[row + index][col + index] == x_cell) {
                                            count_x_cell++;
                                            index++;
                                        }
                                        index = 1;
                                        while (col - index > 0 && row - index > 0 && arr[row - index][col - index] == x_cell) {
                                            count_x_cell++;
                                            index++;
                                        }
                                        switch (count_x_cell) {
                                            case 5:
                                                if (ArrayPointBlock[i][j] < 10)
                                                    ArrayPointBlock[i][j] = 10;
                                                break;
                                            case 4:
                                                if (ArrayPointBlock[i][j] < 8)
                                                    ArrayPointBlock[i][j] = 8;
                                                break;
                                            case 3:
                                                if (ArrayPointBlock[i][j] < 6)
                                                    ArrayPointBlock[i][j] = 6;
                                                break;
                                            case 2:
                                                if (ArrayPointBlock[i][j] < 4)
                                                    ArrayPointBlock[i][j] = 4;
                                                break;
                                        }

                                        // Check the right diagonal
                                        count_x_cell = 1;
                                        index = 1;
                                        while (row + index < m && col - index > 0 && arr[row + index][col - index] == x_cell) {
                                            count_x_cell++;
                                            index++;
                                        }
                                        index = 1;
                                        while (col + index < n && row - index > 0 && arr[row - index][col + index] == x_cell) {
                                            count_x_cell++;
                                            index++;
                                        }
                                        switch (count_x_cell) {
                                            case 5:
                                                if (ArrayPointBlock[i][j] < 10)
                                                    ArrayPointBlock[i][j] = 10;
                                                break;
                                            case 4:
                                                if (ArrayPointBlock[i][j] < 8)
                                                    ArrayPointBlock[i][j] = 8;
                                                break;
                                            case 3:
                                                if (ArrayPointBlock[i][j] < 6)
                                                    ArrayPointBlock[i][j] = 6;
                                                break;
                                            case 2:
                                                if (ArrayPointBlock[i][j] < 4)
                                                    ArrayPointBlock[i][j] = 4;
                                                break;
                                        }

                                        // Compare when Human is X, Computer is O
                                        // Check the horizontal row
                                        count_o_cell = 1;
                                        index = 1;
                                        while (row + index < m && arr[row + index][col] == o_cell) {
                                            count_o_cell++;
                                            index++;
                                        }
                                        index = 1;
                                        while (row - index > 0 && arr[row - index][col] == o_cell) {
                                            count_o_cell++;
                                            index++;
                                        }
                                        switch (count_o_cell) {
                                            case 5:
                                                if (ArrayPointBlock[i][j] < 9)
                                                    ArrayPointBlock[i][j] = 9;
                                                break;
                                            case 4:
                                                if (ArrayPointBlock[i][j] < 7)
                                                    ArrayPointBlock[i][j] = 7;
                                                break;
                                            case 2:
                                                if (ArrayPointBlock[i][j] < 1)
                                                    ArrayPointBlock[i][j] = 1;
                                                break;
                                        }

                                        // Check the vertical row
                                        count_o_cell = 1;
                                        index = 1;
                                        while (col + index < n && arr[row][col + index] == o_cell) {
                                            count_o_cell++;
                                            index++;
                                        }
                                        index = 1;
                                        while (col - index > 0 && arr[row][col - index] == o_cell) {
                                            count_o_cell++;
                                            index++;
                                        }
                                        switch (count_o_cell) {
                                            case 5:
                                                if (ArrayPointBlock[i][j] < 9)
                                                    ArrayPointBlock[i][j] = 9;
                                                break;
                                            case 4:
                                                if (ArrayPointBlock[i][j] < 7)
                                                    ArrayPointBlock[i][j] = 7;
                                                break;
                                            case 2:
                                                if (ArrayPointBlock[i][j] < 1)
                                                    ArrayPointBlock[i][j] = 1;
                                                break;
                                        }

                                        count_o_cell = 1;
                                        index = 1;
                                        while (row + index < m && col + index < n && arr[row + index][col + index] == o_cell) {
                                            count_o_cell++;
                                            index++;
                                        }
                                        index = 1;
                                        while (col - index > 0 && row - index > 0 && arr[row - index][col - index] == o_cell) {
                                            count_o_cell++;
                                            index++;
                                        }
                                        switch (count_o_cell) {
                                            case 5:
                                                if (ArrayPointBlock[i][j] < 9)
                                                    ArrayPointBlock[i][j] = 9;
                                                break;
                                            case 4:
                                                if (ArrayPointBlock[i][j] < 7)
                                                    ArrayPointBlock[i][j] = 7;
                                                break;
                                            case 2:
                                                if (ArrayPointBlock[i][j] < 1)
                                                    ArrayPointBlock[i][j] = 1;
                                                break;
                                        }

                                        count_o_cell = 1;
                                        index = 1;
                                        while (row + index < m && col - index > 0 && arr[row + index][col - index] == o_cell) {
                                            count_o_cell++;
                                            index++;
                                        }
                                        index = 1;
                                        while (col + index < n && row - index > 0 && arr[row - index][col + index] == o_cell) {
                                            count_o_cell++;
                                            index++;
                                        }

                                        switch (count_o_cell) {
                                            case 4:
                                                if (ArrayPointBlock[i][j] < 9)
                                                    ArrayPointBlock[i][j] = 9;
                                                break;
                                            case 3:
                                                if (ArrayPointBlock[i][j] < 7)
                                                    ArrayPointBlock[i][j] = 7;
                                                break;
                                            case 2:
                                                if (ArrayPointBlock[i][j] < 1)
                                                    ArrayPointBlock[i][j] = 1;
                                                break;
                                        }
                                    }

                                    // Computer is X, Human is O
                                    if (firstPlayerX == true) {
                                        count_o_cell = 1;
                                        index = 1;

                                        while (row + index < m && arr[row + index][col] == o_cell) {
                                            count_o_cell++;
                                            index++;
                                        }
                                        index = 1;
                                        while (row - index > 0 && arr[row - index][col] == o_cell) {
                                            count_o_cell++;
                                            index++;
                                        }
                                        switch (count_o_cell) {
                                            case 5:
                                                if (ArrayPointBlock[i][j] < 10)
                                                    ArrayPointBlock[i][j] = 10;
                                                break;
                                            case 4:
                                                if (ArrayPointBlock[i][j] < 8)
                                                    ArrayPointBlock[i][j] = 8;
                                                break;
                                            case 3:
                                                if (ArrayPointBlock[i][j] < 6)
                                                    ArrayPointBlock[i][j] = 6;
                                                break;
                                            case 2:
                                                if (ArrayPointBlock[i][j] < 4)
                                                    ArrayPointBlock[i][j] = 4;
                                                break;
                                        }
                                        
                                        count_x_cell = 1;
                                        index = 1;
                                        while (row + index < m && arr[row + index][col] == x_cell) {
                                            count_x_cell++;
                                            index++;
                                        }
                                        index = 1;
                                        while (row - index > 0 && arr[row - index][col] == x_cell) {
                                            count_x_cell++;
                                            index++;
                                        }
                                        switch (count_x_cell) {
                                            case 5:
                                                if (ArrayPointBlock[i][j] < 9)
                                                    ArrayPointBlock[i][j] = 9;
                                                break;
                                            case 4:
                                                if (ArrayPointBlock[i][j] < 7)
                                                    ArrayPointBlock[i][j] = 7;
                                                break;
                                            case 2:
                                                if (ArrayPointBlock[i][j] < 1)
                                                    ArrayPointBlock[i][j] = 1;
                                                break;
                                        }
                                    }
                                    this.invalidate();
                                }
                            }
                        }
                        for (int i = 0; i < ArrayPointBlock.length; i++) {
                            for (int j = 0; j < ArrayPointBlock[0].length; j++) {
                                if (ArrayPointBlock[i][j] > ArrayPointBlock[maxi][maxj]) {
                                    maxi = i;
                                    maxj = j;
                                    ArrayPointBlock[i][j] = ArrayPointBlock[maxi][maxj];
                                }
                            }
                        }
                        
                        int rowCom = maxi;
                        int colCom = maxj;
                        int count_o_cell = 1;
                        int count_x_cell = 1;
                        int index = 1;
                        
                        if (playerTurn == 0) {
                            if (firstPlayerX == true) {
                                this.arr[(int) maxi][(int) maxj] = o_cell;
                            }
                            if (firstPlayerX == false) {
                                this.arr[(int) maxi][(int) maxj] = x_cell;
                            }
                        } else if (playerTurn == 1) {
                            if (firstPlayerX == false) {

                                count_x_cell = 1;
                                index = 1;

                                // Check the horizontal row
                                while (rowCom + index < m && arr[rowCom + index][colCom] == x_cell) {
                                    count_x_cell++;
                                    index++;
                                }
                                index = 1;
                                while (rowCom - index > 0 && arr[rowCom - index][colCom] == x_cell) {
                                    count_x_cell++;
                                    index++;
                                }
                                this.arr[(int) maxi][(int) maxj] = x_cell;

                                if (count_x_cell >= 5) {
                                    showWinDialog(false);
                                }

                                // Check the vertical column
                                count_x_cell = 1;
                                index = 1;
                                while (colCom + index < n && arr[rowCom][colCom + index] == x_cell) {
                                    count_x_cell++;
                                    index++;
                                }
                                index = 1;
                                while (colCom - index > 0 && arr[rowCom][colCom - index] == x_cell) {
                                    count_x_cell++;
                                    index++;
                                }
                                if (count_x_cell >= 5) {
                                    showWinDialog(false);
                                }

                                // Check the left diagonal
                                count_x_cell = 1;
                                index = 1;
                                while (rowCom + index < m && colCom + index < n && arr[rowCom + index][colCom + index] == x_cell) {
                                    count_x_cell++;
                                    index++;
                                }
                                index = 1;
                                while (colCom - index > 0 && rowCom - index > 0 && arr[rowCom - index][colCom - index] == x_cell) {
                                    count_x_cell++;
                                    index++;
                                }
                                this.arr[(int) maxi][(int) maxj] = x_cell;
                                if (count_x_cell >= 5) {
                                    showWinDialog(false);
                                }

                                // Check the right diagonal
                                count_x_cell = 1;
                                index = 1;
                                while (rowCom + index < m && colCom - index > 0 && arr[rowCom + index][colCom - index] == x_cell) {
                                    count_x_cell++;
                                    index++;
                                }
                                index = 1;
                                while (colCom + index < n && rowCom - index > 0 && arr[rowCom - index][colCom + index] == x_cell) {
                                    count_x_cell++;
                                    index++;
                                }
                                this.arr[(int) maxi][(int) maxj] = x_cell;

                                if (count_x_cell >= 5) {
                                    showWinDialog(false);
                                }
                            }

                            if (firstPlayerX == true) {

                                count_o_cell = 1;
                                index = 1;

                                // Check the horizontal row
                                while (rowCom + index < m && arr[rowCom + index][colCom] == o_cell) {
                                    count_o_cell++;
                                    index++;
                                }
                                index = 1;
                                while (rowCom - index > 0 && arr[rowCom - index][colCom] == o_cell) {
                                    count_o_cell++;
                                    index++;
                                }
                                this.arr[(int) maxi][(int) maxj] = o_cell;
                                if (count_o_cell >= 5) {
                                    showWinDialog(false);
                                }

                                // Check the vertical column
                                count_o_cell = 1;
                                index = 1;
                                while (colCom + index < n && arr[rowCom][colCom + index] == o_cell) {
                                    count_o_cell++;
                                    index++;
                                }
                                index = 1;
                                while (colCom - index > 0 && arr[rowCom][colCom - index] == o_cell) {
                                    count_o_cell++;
                                    index++;
                                }
                                this.arr[(int) maxi][(int) maxj] = o_cell;
                                if (count_o_cell >= 5) {
                                    showWinDialog(false);
                                }

                                // Check the left diagonal
                                count_o_cell = 1;
                                index = 1;
                                while (rowCom + index < m && colCom + index < n && arr[rowCom + index][colCom + index] == o_cell) {
                                    count_o_cell++;
                                    index++;
                                }
                                index = 1;
                                while (colCom - index > 0 && rowCom - index > 0 && arr[rowCom - index][colCom - index] == o_cell) {
                                    count_o_cell++;
                                    index++;
                                }
                                this.arr[(int) maxi][(int) maxj] = o_cell;
                                if (count_o_cell >= 5) {
                                    showWinDialog(false);
                                }

                                // Check the right diagonal
                                count_o_cell = 1;
                                index = 1;
                                while (rowCom + index < m && colCom - index > 0 && arr[rowCom + index][colCom - index] == o_cell) {
                                    count_o_cell++;
                                    index++;
                                }
                                index = 1;
                                while (colCom + index < n && rowCom - index > 0 && arr[rowCom - index][colCom + index] == o_cell) {
                                    count_o_cell++;
                                    index++;
                                }
                                this.arr[(int) maxi][(int) maxj] = o_cell;

                                if (count_o_cell >= 5) {
                                    showWinDialog(false);
                                }
                            }
                        }
                        this.invalidate();

                        playerTurn = 0;
                        System.out.println("Human is playing chess");
                    } else {
                        showConfirmRetake();
                    }
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
        return super.onTouchEvent(event);
    }
}
