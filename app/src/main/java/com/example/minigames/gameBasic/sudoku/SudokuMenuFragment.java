package com.example.minigames.gameBasic.sudoku;

import android.os.Bundle;

import com.example.minigames.R;
import com.example.minigames.base.BaseFragment;
import com.example.minigames.databinding.FragmentSudokuMenuBinding;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class SudokuMenuFragment extends BaseFragment<FragmentSudokuMenuBinding> {

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_sudoku_menu;
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
    }

    public void onStartNewGameButtonClicked() {
        findNavController(this).navigate(R.id.action_sudokuMenuFragment_to_sudokuGameDifficultyFragment);
    }

    public void onAddNewBoardButtonClicked(){
        findNavController(this).navigate(R.id.action_sudokuMenuFragment_to_sudokuNewGameFragment);
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            ((SudokuGameActivity)getActivity()).showMenuRight(false);
        } catch (Exception e){}
    }
}
