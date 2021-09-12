package com.example.minigames.gameBasic.shootingGame;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.minigames.R;
import com.example.minigames.base.BaseActivity;
import com.example.minigames.base.DialogInstruction;
import com.example.minigames.databinding.ActivityShootingGameBinding;
import com.example.minigames.firebase.FirebaseHelper;
import com.example.minigames.gameBasic.shootingGame.view.SoundEffects;
import com.example.minigames.gameBasic.shop.cData;
import com.example.minigames.ultis.Utils;

public class ShootingActivity extends BaseActivity<ActivityShootingGameBinding> {

    private MediaPlayer player;
    private boolean play_music;
    public int gameScore =0;
    Menu menu;
    cData dt = new cData();

    @Override
    protected boolean isHaveRightMenu() {
        return true;
    }

    @Override
    protected boolean isHaveBackMenu() {
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_shooting_game;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.shooting_game_title);
    }

    @Override
    protected void subscribeUi() {
        binding.setAction(this);
        player = MediaPlayer.create(this, R.raw.braincandy);
        player.setLooping(true);
        play_music = true;

        SoundEffects.INSTANCE.setContext(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_shooting_game, menu);
        this.menu = menu;
        if (play_music) {
            menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_off_white_24dp);
        } else {
            menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_up_white_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_sound:
                if (play_music) {
                    player.pause();
                    play_music = false;
                    menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_up_white_24dp);
                } else {
                    player.start();
                    play_music = true;
                    menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_off_white_24dp);
                }
                break;
            case R.id.menu_information:
                binding.drawView.stopGame();
                DialogInstruction dialogInstruction = DialogInstruction.newInstance(R.layout.dialog_instruction_shooting);
                dialogInstruction.setCallBack(() -> binding.drawView.resumeGame());
                dialogInstruction.show(getSupportFragmentManager(),"caro_instruction");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(isHaveBackMenu()) {
            if(!binding.drawView.isGameOver) {
                binding.drawView.stopGame();
                Utils.showConfirmDialog(ShootingActivity.this, getString(R.string.game4_name),
                        getString(R.string.alert_game_4_out, binding.drawView.score.getScore()), (dialog, which) -> {
                            if (which == AlertDialog.BUTTON_POSITIVE) {
                                dialog.dismiss();
                                binding.drawView.resumeGame();
                            } else {
                                FirebaseHelper.getInstance().getUserDao().updateGamePoint(4, binding.drawView.score.getScore());
                                finish();
                                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_right_exit);
                            }
                        });
            }
        }
        return true;
    }

    public void gameOver(){
        Log.i("slow down",String.valueOf(binding.drawView.score.getScore()));
        FirebaseHelper.getInstance().getUserDao().updateGamePoint(4, binding.drawView.score.getScore());
        Utils.shootDialog(ShootingActivity.this, getString(R.string.game4_name),
                getString(R.string.alert_game_4_game_over, binding.drawView.score.getScore()), (dialog, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        binding.drawView.stopGame();
                        binding.drawView.resumeGame();
                    }
                    else {finish();}});
        dt.setSh(binding.drawView.score);
        dt.updateData();


    }

    @Override
    protected void onPause() {
        if (play_music)
            player.pause();
        binding.drawView.stopGame();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (play_music)
            player.start();
    }

    @Override
    protected void onDestroy() {
        player.stop();
        player.reset();
        player.release();
        player = null;
        play_music = false;
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moveLeftButton:
                binding.drawView.moveCannonLeft();
                break;
            case R.id.moveRightButton:
                binding.drawView.moveCannonRight();
                break;
            case R.id.shootButton:
                binding.drawView.shootCannon();
                break;
            default:
                break;
        }
    }
}
