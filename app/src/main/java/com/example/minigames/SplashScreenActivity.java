package com.example.minigames;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.core.app.ActivityOptionsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.minigames.base.BaseActivity;
import com.example.minigames.databinding.ActivitySplashScreenBinding;
import com.example.minigames.firebase.FirebaseHelper;
import com.example.minigames.firebase.dao.UserDao;
import com.example.minigames.login.LoginActivity;

public class SplashScreenActivity extends BaseActivity<ActivitySplashScreenBinding> {

    private boolean isLogined = false;

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
        return R.layout.activity_splash_screen;
    }

    @Override
    protected String getActionBarTitle() {
        return null;
    }

    @Override
    protected void subscribeUi() {
        UserDao userDao = FirebaseHelper.getInstance().getUserDao();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            isLogined = true;
        }
    }

    private synchronized void checkUser() {
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getBaseContext(),
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        Intent intent;
        if (isLogined) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent, bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(2500);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkUser();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.imgLogo.startAnimation(animation1);
    }
}
