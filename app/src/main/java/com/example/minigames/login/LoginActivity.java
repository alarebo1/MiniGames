package com.example.minigames.login;

import android.os.Bundle;

import com.example.minigames.R;
import com.example.minigames.base.BaseActivity;
import com.example.minigames.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    @Override
    protected boolean isHaveRightMenu() {
        return false;
    }

    @Override
    protected boolean isHaveBackMenu() {
        return false;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.action_login);
    }

    @Override
    protected void subscribeUi() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
