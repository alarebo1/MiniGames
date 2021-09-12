package com.example.minigames.login.fragment;

import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import com.example.minigames.MainActivity;
import com.example.minigames.R;
import com.example.minigames.base.BaseFragment;
import com.example.minigames.databinding.FragmentRegisterBinding;
import com.example.minigames.firebase.FirebaseHelper;
import com.example.minigames.ultis.LoadingDialog;
import com.example.minigames.ultis.Utils;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class RegisterFragment extends BaseFragment<FragmentRegisterBinding> {

    LoadingDialog loadingDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_register;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.action_register);
    }

    @Override
    protected void subscribeUi() {
        binding.setAction(this);
        loadingDialog = LoadingDialog.newInstance(getString(R.string.login_create_account_loading));
    }

    public void registry(){
        String email = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        String name = binding.name.getText().toString().trim();

        String error = "";
        if (TextUtils.isEmpty(email) || !Utils.validate(email)) {
            error = getString(R.string.invalid_mail);
        } else if (TextUtils.isEmpty(password) || password.length() <= 5) {
            error = getString(R.string.invalid_password);
        }else if(TextUtils.isEmpty(name)){
            error = getString(R.string.invalid_name);
        }
        if (TextUtils.isEmpty(error)){
            getChildFragmentManager().beginTransaction().add(loadingDialog, "").commitAllowingStateLoss();
            FirebaseHelper.getInstance().getUserDao().signUp(email,password,  name,true)
                    .observe(getViewLifecycleOwner(),authResultTask -> {
                        processResult(authResultTask);
                        if (loadingDialog != null) {
                            loadingDialog.dismissAllowingStateLoss();
                        }
                    });
        }else{
            Utils.showAlertDialog(getContext(),getString(R.string.confirm_title),error);
        }
    }

    private void processResult(Task<AuthResult> authResultTask) {
        if (authResultTask == null) {
            Utils.showAlertDialog(getContext(), getString(R.string.confirm_title), getString(R.string.verify_email),
                    (dialog, which) -> findNavController(RegisterFragment.this).popBackStack());
            return;
        }
        if (authResultTask.isSuccessful()) {
            FirebaseUser firebaseUser1 = authResultTask.getResult().getUser();
            if (firebaseUser1 != null) {
                startActivityWithAnimation(new Intent(getContext(), MainActivity.class));
            }
        } else {
            String error = null;
            try {
                throw authResultTask.getException();
            } catch (FirebaseAuthInvalidCredentialsException e) {
                error = getString(R.string.login_message_incorrect_pass_or_mail);
            } catch (FirebaseNetworkException e) {
                error = getString(R.string.message_network_error);
            } catch (Exception e) {
                error = e.getMessage();
            }

            if (!TextUtils.isEmpty(error)) {
                Utils.showAlertDialog(getContext(), getString(R.string.confirm_title), error);
            }
        }
    }

    public void backToLogin(){
        findNavController(this).popBackStack();
    }
}
