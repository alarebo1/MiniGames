package com.example.minigames.gameBasic.caroGame;

import com.example.minigames.R;
import com.example.minigames.base.BaseFragment;
import com.example.minigames.databinding.FragmentCaroGameComVsHumBinding;

public class CaroGameComvsHumanFragment extends BaseFragment<FragmentCaroGameComVsHumBinding> {

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_caro_game_com_vs_hum;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.caro_game_com_vs_human_fragment_label);
    }

    @Override
    protected void subscribeUi() {
    }
}