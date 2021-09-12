package com.example.minigames.gameBasic.caroGame;

import com.example.minigames.R;
import com.example.minigames.base.BaseFragment;
import com.example.minigames.databinding.FragmentCaroGameDualBinding;

public class CaroGameDualFragment extends BaseFragment<FragmentCaroGameDualBinding> {

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_caro_game_dual;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.caro_game_dual_fragment_label);
    }

    @Override
    protected void subscribeUi() {
    }
}
