package com.example.minigames.gameBasic.sudoku;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.example.minigames.R;
import com.example.minigames.base.BaseFragment;
import com.example.minigames.base.DialogInstruction;
import com.example.minigames.databinding.FragmentSudokuGamePlayBinding;
import com.example.minigames.firebase.FirebaseHelper;
import com.example.minigames.gameBasic.shop.cData;
import com.example.minigames.gameBasic.sudoku.model.Board;
import com.example.minigames.ultis.Utils;

public class SudokuGamePlayFragment extends BaseFragment<FragmentSudokuGamePlayBinding> {

    private final String TAG = this.getClass().getSimpleName();

    private TextView clickedCell;
    private int clickedGroup;
    private int clickedCellId;
    private Board startBoard;
    private Board currentBoard;
    public int sudoScore =0;
    cData dt = new cData();


    // Variable to save point for game
    long mStartTime = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_sudoku_game_play;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.sudoku_game_title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void subscribeUi() {
        binding.setAction(this);
        int difficulty = getArguments().getInt("difficulty", 0);

        ArrayList<Board> boards = readGameBoards(difficulty);
        startBoard = chooseRandomBoard(boards);
        currentBoard = new Board();
        currentBoard.copyValues(startBoard.getGameCells());

        int cellGroupFragments[] = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 1; i < 10; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getChildFragmentManager().findFragmentById(cellGroupFragments[i - 1]);
            thisCellGroupFragment.setGroupId(i);
        }

        // Appear all values from the current board
        CellGroupFragment tempCellGroupFragment;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int column = j / 3;
                int row = i / 3;

                int fragmentNumber = (row * 3) + column;
                tempCellGroupFragment = (CellGroupFragment) getChildFragmentManager().findFragmentById(cellGroupFragments[fragmentNumber]);
                int groupColumn = j % 3;
                int groupRow = i % 3;

                int groupPosition = (groupRow * 3) + groupColumn;
                int currentValue = currentBoard.getValue(i, j);

                if (currentValue != 0) {
                    tempCellGroupFragment.setValue(groupPosition, currentValue);
                }
            }
        }
    }

    private Board chooseRandomBoard(ArrayList<Board> boards) {
        int randomNumber = (int) (Math.random() * boards.size());
        return boards.get(randomNumber);
    }

    private ArrayList<Board> readGameBoards(int difficulty) {
        ArrayList<Board> boards = new ArrayList<>();
        int fileId;
        if (difficulty == 1) {
            fileId = R.raw.normal;
        } else if (difficulty == 0) {
            fileId = R.raw.easy;
        } else {
            fileId = R.raw.hard;
        }

        InputStream inputStream = getResources().openRawResource(fileId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                Board board = new Board();
                // Read all lines in the board
                for (int i = 0; i < 9; i++) {
                    String rowCells[] = line.split(" ");
                    for (int j = 0; j < 9; j++) {
                        if (rowCells[j].equals("-")) {
                            board.setValue(i, j, 0);
                        } else {
                            board.setValue(i, j, Integer.parseInt(rowCells[j]));
                        }
                    }
                    line = bufferedReader.readLine();
                }
                boards.add(board);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        // Reading from internal storage
        String fileName = "boards-";
        if (difficulty == 0) {
            fileName += "easy";
        } else if (difficulty == 1) {
            fileName += "normal";
        } else {
            fileName += "hard";
        }

        FileInputStream fileInputStream;
        try {
            fileInputStream = getActivity().openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader internalBufferedReader = new BufferedReader(inputStreamReader);
            String line = internalBufferedReader.readLine();
            line = internalBufferedReader.readLine();
            while (line != null) {
                Board board = new Board();
                // read all lines in the board
                for (int i = 0; i < 9; i++) {
                    String rowCells[] = line.split(" ");
                    for (int j = 0; j < 9; j++) {
                        if (rowCells[j].equals("-")) {
                            board.setValue(i, j, 0);
                        } else {
                            board.setValue(i, j, Integer.parseInt(rowCells[j]));
                        }
                    }
                    line = internalBufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                }
                boards.add(board);
                line = internalBufferedReader.readLine();
            }
            internalBufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boards;
    }

    private boolean isStartPiece(int group, int cell) {
        int row = ((group - 1) / 3) * 3 + (cell / 3);
        int column = ((group - 1) % 3) * 3 + ((cell) % 3);
        return startBoard.getValue(row, column) != 0;
    }

    private boolean checkAllGroups() {
        int cellGroupFragments[] = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 0; i < 9; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getChildFragmentManager().findFragmentById(cellGroupFragments[i]);
            if (!thisCellGroupFragment.checkGroupCorrect()) {
                return false;
            }
        }
        return true;
    }

    public void onCheckBoardButtonClicked() {
        currentBoard.isBoardCorrect();
        if (checkAllGroups() && currentBoard.isBoardCorrect()) {
            Toast.makeText(getContext(), getString(R.string.board_correct), Toast.LENGTH_SHORT).show();
            long time = Utils.millisecondToSecond(System.currentTimeMillis() - mStartTime);
            sudoScore = 100/(int)time;
            dt.setSudo(this);
            dt.updateData();
            FirebaseHelper.getInstance().getUserDao().updateGamePoint(3, time);
        } else {
            Toast.makeText(getContext(), getString(R.string.board_incorrect), Toast.LENGTH_SHORT).show();
        }
    }

    public void onShowInstructionsButtonClicked() {
        DialogInstruction dialogInstruction = DialogInstruction.newInstance(R.layout.dialog_instruction_sudoku);
        dialogInstruction.show(getChildFragmentManager(), "instruction");
    }

    public void onFragmentInteraction(int groupId, int cellId, View view) {

        if (mStartTime == 0) {
            mStartTime = System.currentTimeMillis();
        }

        clickedCell = (TextView) view;
        clickedGroup = groupId;
        clickedCellId = cellId;

        int row = ((clickedGroup - 1) / 3) * 3 + (clickedCellId / 3);
        int column = ((clickedGroup - 1) % 3) * 3 + ((clickedCellId) % 3);

        if (!isStartPiece(groupId, cellId)) {
            int number = TextUtils.isEmpty(clickedCell.getText().toString()) ? 1 : Integer.parseInt(clickedCell.getText().toString());
            ChooseNumberDiaglogFragment chooseNumber = ChooseNumberDiaglogFragment.newInstance(number, false);
            chooseNumber.setCallback(new ChooseNumberDiaglogFragment.Callback() {
                @Override
                public void chooseNumber(int number, boolean isUnsure) {
                    clickedCell.setText(String.valueOf(number));
                    currentBoard.setValue(row, column, number);
                    if (isUnsure) {
                        clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell_unsure));
                    } else {
                        clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
                    }

                    if (currentBoard.isBoardFull()) {
                        binding.buttonCheckBoard.setVisibility(View.VISIBLE);
                    } else {
                        binding.buttonCheckBoard.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void removePiece() {
                    clickedCell.setText("");
                    clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
                    currentBoard.setValue(row, column, 0);
                    binding.buttonCheckBoard.setVisibility(View.INVISIBLE);
                }
            });
            chooseNumber.show(getChildFragmentManager(), "chooseNumber");
        } else {
            Toast.makeText(getContext(), getString(R.string.start_piece_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            ((SudokuGameActivity)getActivity()).showMenuRight(true);
        } catch (Exception e){}
    }
}
