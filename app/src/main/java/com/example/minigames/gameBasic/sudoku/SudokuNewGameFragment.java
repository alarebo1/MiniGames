package com.example.minigames.gameBasic.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.minigames.R;
import com.example.minigames.base.BaseFragment;
import com.example.minigames.databinding.FragmentSudokuNewGameBinding;
import com.example.minigames.gameBasic.sudoku.model.Board;

public class SudokuNewGameFragment extends BaseFragment<FragmentSudokuNewGameBinding>
        implements ChooseNumberDiaglogFragment.Callback {

    private TextView clickedCell;
    private Board newBoard;
    private int clickedGroup;
    private int clickedCellId;
    private int selectedDifficulty = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_sudoku_new_game;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.add_new_board);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void subscribeUi() {
        binding.setActions(this);
        int cellGroupFragments[] = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 1; i < 10; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getChildFragmentManager().findFragmentById(cellGroupFragments[i - 1]);
            thisCellGroupFragment.setGroupId(i);
        }
        newBoard = new Board();
    }

    public void onContinueButtonClicked() {
        saveBoard(selectedDifficulty);
        Toast.makeText(getContext(), getString(R.string.successful_new_board), Toast.LENGTH_SHORT).show();
    }

    public void onDifficultyRadioButtonsClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButtonEasy:
                if (checked) {
                    selectedDifficulty = 0;
                }
                break;
            case R.id.radioButtonNormal:
                if (checked) {
                    selectedDifficulty = 1;
                }
                break;
            case R.id.radioButtonHard:
                if (checked) {
                    selectedDifficulty = 2;
                }
                break;
        }
    }

    private void saveBoard(int difficulty) {
        String fileName = "boards-";
        if (difficulty == 0) {
            fileName += "easy";
        } else if (difficulty == 1) {
            fileName += "normal";
        } else {
            fileName += "hard";
        }
        String content = newBoard.toString();
        System.out.println("new Board: " + content);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onFragmentInteraction(int groupId, int cellId, View view) {
        clickedCell = (TextView) view;
        clickedCellId = cellId;
        clickedGroup = groupId;
        int number = TextUtils.isEmpty(clickedCell.getText().toString()) ? 1 : Integer.parseInt(clickedCell.getText().toString());
        ChooseNumberDiaglogFragment chooseNumber = ChooseNumberDiaglogFragment.newInstance(number, true);
        chooseNumber.setCallback(this);
        chooseNumber.show(getChildFragmentManager(), "chooseNumber");
    }

    @Override
    public void chooseNumber(int number, boolean isUnsure) {
        int row = ((clickedGroup - 1) / 3) * 3 + (clickedCellId / 3);
        int column = ((clickedGroup - 1) % 3) * 3 + ((clickedCellId) % 3);

        newBoard.setValue(row, column, number);
        clickedCell.setText(String.valueOf(number));
        clickedCell.setTextColor(Color.BLACK);
        clickedCell.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void removePiece() {
        int row = ((clickedGroup - 1) / 3) * 3 + (clickedCellId / 3);
        int column = ((clickedGroup - 1) % 3) * 3 + ((clickedCellId) % 3);

        clickedCell.setText("");
        newBoard.setValue(row, column, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            ((SudokuGameActivity)getActivity()).showMenuRight(true);
        } catch (Exception e){}
    }
}
