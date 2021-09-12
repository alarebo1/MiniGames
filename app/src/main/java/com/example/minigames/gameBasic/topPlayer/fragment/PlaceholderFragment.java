package com.example.minigames.gameBasic.topPlayer.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Collections;

import com.example.minigames.R;
import com.example.minigames.base.BaseFragment;
import com.example.minigames.databinding.FragmentTopPlayerBinding;
import com.example.minigames.firebase.FirebaseHelper;
import com.example.minigames.gameBasic.topPlayer.fragment.adapter.TopPlayerAdapter;

public class PlaceholderFragment extends BaseFragment<FragmentTopPlayerBinding> {

    private static final String ARG_SECTION_NUMBER = "section_number";

    TopPlayerAdapter mAdapter;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_top_player;
    }

    @Override
    protected String getActionBarTitle() {
        return null;
    }

    @Override
    protected void subscribeUi() {

        int game = getArguments().getInt(ARG_SECTION_NUMBER,1);
        binding.listItem.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listItem.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new TopPlayerAdapter(getContext(),game);
        binding.listItem.setAdapter(mAdapter);

        FirebaseHelper.getInstance().getUserDao().getTopUsers(game)
        .observe(getViewLifecycleOwner(), users -> {
            if(game == 4 || game == 5) {
                Collections.reverse(users);
            }
            mAdapter.updateData(users);
            mAdapter.notifyDataSetChanged();
        });
    }
}
