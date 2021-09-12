package com.example.minigames.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import com.example.minigames.R;
import com.example.minigames.SplashScreenActivity;
import com.example.minigames.base.BaseActivity;
import com.example.minigames.databinding.ActivitySettingBinding;
import com.example.minigames.firebase.FirebaseHelper;
import com.example.minigames.ultis.LoadingDialog;
import com.example.minigames.ultis.Utils;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    private static final int PICK_PHOTO_FOR_AVATAR = 100;
    private Menu menuList;
    private boolean mIsEnableEdit = false;

    LoadingDialog loadingDialog;
    String mLinkImage, mUserName, mPass;

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
        return R.layout.activity_setting;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.menu_setting);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuList = menu;
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        // Default disable menu dual play
        menu.findItem(R.id.menu_setting_save).setEnabled(false);
        return isHaveRightMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting_edit:
                menuList.findItem(R.id.menu_setting_edit).setEnabled(false);
                menuList.findItem(R.id.menu_setting_save).setEnabled(true);
                enableEdit(true);
                return true;
            case R.id.menu_setting_save:
                menuList.findItem(R.id.menu_setting_edit).setEnabled(true);
                menuList.findItem(R.id.menu_setting_save).setEnabled(false);
                enableEdit(false);
                saveData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityWithAnimation(intent);
        finish();
    }

    private void saveData() {
        getSupportFragmentManager().beginTransaction().add(loadingDialog, "").commitAllowingStateLoss();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(mUserName)
                .setPhotoUri(Uri.parse(mLinkImage))
                .build();

        FirebaseHelper.getInstance().getUserDao().updateUserName(mUserName);

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, getString(R.string.setting_upate_success), Toast.LENGTH_SHORT).show();
                    }
                    if (loadingDialog != null) {
                        loadingDialog.dismissAllowingStateLoss();
                    }
                });
        if (!TextUtils.isEmpty(mPass)) {
            currentUser.updatePassword(mPass);
        }
    }

    public void enableEdit(boolean isEnableEdit) {
        mIsEnableEdit = isEnableEdit;
        binding.imgEdit1.setVisibility(isEnableEdit ? View.VISIBLE : View.INVISIBLE);
        binding.imgEdit2.setVisibility(isEnableEdit ? View.VISIBLE : View.INVISIBLE);
        binding.txtEditPass.setVisibility(isEnableEdit ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void subscribeUi() {
        binding.setOnClick(this);
        loadingDialog = LoadingDialog.newInstance(null);
        getCurrentData();
    }

    public void getCurrentData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Uri photoUrl = currentUser.getPhotoUrl();
        mLinkImage = photoUrl != null ? photoUrl.toString() : "";
        mUserName = currentUser.getDisplayName();
        mUserName = TextUtils.isEmpty(mUserName) ? currentUser.getEmail() : mUserName;

        binding.txtMail.setText(currentUser.getEmail());
        if (!TextUtils.isEmpty(mLinkImage)) {
            Picasso.get().load(Uri.parse(mLinkImage)).placeholder(R.drawable.img_default_account).fit().into(binding.imgUser);
        }
        binding.txtName.setText(mUserName);

        FirebaseHelper.getInstance().getUserDao().getUser(currentUser.getUid()).observe(this, user -> {
            binding.layoutScore.txtGame1Title.setText(getString(R.string.setting_best_score_game1, String.valueOf(user.getScoreGame1())));
            binding.layoutScore.txtGame2Title.setText(getString(R.string.setting_best_score_game2, String.valueOf(user.getScoreGame2())));
            binding.layoutScore.txtGame3Title.setText(getString(R.string.setting_best_score_game3, String.valueOf(user.getScoreGame3())));
            binding.layoutScore.txtGame4Title.setText(getString(R.string.setting_best_score_game4, String.valueOf(user.getScoreGame4())));
            binding.layoutScore.txtGame5Title.setText(getString(R.string.setting_best_score_game5, String.valueOf(user.getScoreGame5())));
            binding.layoutScore.txtGame6Title.setText(getString(R.string.setting_best_score_game6, String.valueOf(user.getCurrency())));
        });
    }

    public void editName() {
        if (mIsEnableEdit) {
            InputValueDiaglogFragment edit = InputValueDiaglogFragment.newInstance(mUserName);
            edit.setCallback(value -> {
                mUserName = value;
                binding.txtName.setText(mUserName);
            });
            edit.show(getSupportFragmentManager(), "editUserName");
        }
    }

    public void setNewPass() {
        if (mIsEnableEdit) {
            InputValueDiaglogFragment edit = InputValueDiaglogFragment.newInstance("");
            edit.setCallback(value -> {
                mPass = value;
            });
            edit.show(getSupportFragmentManager(), "editPass");
        }
    }

    public void editImageProfile() {
        if (mIsEnableEdit) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                // Display an error
                return;
            }
            try {
                // Now you can do whatever you want with your input stream, save it as file, upload to a server, decode a bitmap...
                Bitmap bitmap = Utils.getBitmap(data.getData(), getApplication().getContentResolver());
                binding.imgUser.setImageBitmap(bitmap);
                // Upload image to Firebase
                getSupportFragmentManager().beginTransaction().add(loadingDialog, "").commitAllowingStateLoss();
                LiveData<String> updateImage = FirebaseHelper.getInstance().getUserDao().updateImage(bitmap);
                updateImage.observe(this, result -> {
                    if (result.contains("https")) {
                        mLinkImage = result;
                    } else {
                        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                    }
                    if (loadingDialog != null) {
                        loadingDialog.dismissAllowingStateLoss();
                    }
                });
            } catch (Exception e) {
                // Do nothing
            }
        }
    }
}
