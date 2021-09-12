package com.example.minigames.gameBasic.helicopterGame;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.minigames.R;
import com.example.minigames.base.BaseActivity;
import com.example.minigames.base.DialogInstruction;
import com.example.minigames.databinding.ActivityCaroGameBinding;
import com.example.minigames.firebase.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HelicopterGameActivity extends BaseActivity<ActivityCaroGameBinding> {

    GamePanel gamePanel;

    @Override
    protected boolean isHaveBackMenu() {
        return true;
    }

    @Override
    protected boolean isHaveRightMenu() {
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_helicopter_game;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_helicopter_game);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gamePanel = new GamePanel(this);
        setContentView(gamePanel);
    }

    @Override
    protected void subscribeUi() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseHelper.getInstance().getUserDao().getUser(currentUser.getUid()).observe(this, user -> {
            gamePanel.best = (int) user.getScoreGame5();
            gamePanel.invalidate();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        gamePanel.stop();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_information:
                DialogInstruction dialogInstruction = DialogInstruction.newInstance(R.layout.dialog_helicopter_instruction, true);
                dialogInstruction.show(getSupportFragmentManager(), "instruction");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
