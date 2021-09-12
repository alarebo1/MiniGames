package com.example.minigames.gameBasic.topPlayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.minigames.R;
import com.example.minigames.base.BaseActivity;
import com.example.minigames.databinding.ActivityTopPlayerBinding;
import com.example.minigames.gameBasic.topPlayer.fragment.SectionsPagerAdapter;

public class TopPlayerActivity extends BaseActivity<ActivityTopPlayerBinding> {

    Menu menu;
    private boolean play_music;
    private MediaPlayer player;

    @Override
    protected boolean isHaveRightMenu() {
        return false;
    }

    @Override
    protected boolean isHaveBackMenu() {
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_top_player;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_top_player);
    }

    @Override
    protected void subscribeUi() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        binding.viewPager.setAdapter(sectionsPagerAdapter);
        binding.tabs.setupWithViewPager(binding.viewPager);

        player = MediaPlayer.create(this, R.raw.win_sound);
        player.setLooping(true);
        play_music = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_main, menu);

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
        if (id == R.id.action_sound) {
            if (play_music) {
                player.pause();
                play_music = false;
                menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_up_white_24dp);
            } else {
                player.start();
                play_music = true;
                menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_off_white_24dp);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (play_music)
            player.pause();
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
}
