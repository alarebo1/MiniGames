package com.example.minigames.login.fragment;

import android.content.Intent;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.minigames.login.ResetPassDiaglogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import com.example.minigames.MainActivity;
import com.example.minigames.R;
import com.example.minigames.base.BaseFragment;
import com.example.minigames.databinding.FragmentLoginBinding;
import com.example.minigames.firebase.FirebaseHelper;
import com.example.minigames.ultis.LoadingDialog;
import com.example.minigames.ultis.Utils;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class LoginFragment extends BaseFragment<FragmentLoginBinding> {

    LoadingDialog loadingDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_login;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.action_login);
    }

    @Override
    protected void subscribeUi() {
        binding.setAction(this);
        loadingDialog = LoadingDialog.newInstance(getString(R.string.title_activity_login));
    }

    public void createAccount(){
        findNavController(this).navigate(R.id.action_loginFragment_to_registryFragment);
    }

    public void resetPassword(){
        ResetPassDiaglogFragment resetPassDiaglogFragment = ResetPassDiaglogFragment.newInstance();
        resetPassDiaglogFragment.setCallback(email -> {
            getChildFragmentManager().beginTransaction().add(loadingDialog, "").commitAllowingStateLoss();
            FirebaseAuth instance = FirebaseAuth.getInstance();
            instance.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (loadingDialog != null) {
                    loadingDialog.dismissAllowingStateLoss();
                }
                Utils.showAlertDialog(getContext(),getString(R.string.confirm_title),
                        getString(R.string.reset_pass_confirm));
            });
        });
        resetPassDiaglogFragment.show(getChildFragmentManager(),"resetPass");
    }

    public void login() {
        String email = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        String error = "";
        if (TextUtils.isEmpty(email) || !Utils.validate(email)) {
            error = getString(R.string.invalid_mail);
        } else if (TextUtils.isEmpty(password) || password.length() <= 5) {
            error = getString(R.string.invalid_password);
        }
        if (TextUtils.isEmpty(error)) {
            getChildFragmentManager().beginTransaction().add(loadingDialog, "").commitAllowingStateLoss();
            FirebaseHelper.getInstance().getUserDao().signIn(email, password, true)
                    .observe(this, authResultTask -> {
                        processResult(authResultTask);
                        if (loadingDialog != null) {
                            loadingDialog.dismissAllowingStateLoss();
                        }
                    });
        } else {
            Utils.showAlertDialog(getContext(),getString(R.string.confirm_title),error);
        }
    }

    private void processResult(Task<AuthResult> authResultTask) {
        if (authResultTask == null) {
            Utils.showAlertDialog(getContext(), getString(R.string.confirm_title), getString(R.string.verify_email));
            return;
        }
        if (authResultTask.isSuccessful()) {
            FirebaseUser firebaseUser1 = authResultTask.getResult().getUser();
            if (firebaseUser1 != null) {
                FirebaseHelper.getInstance().getUserDao().updateUserInfo(firebaseUser1.getUid(),
                        firebaseUser1.getEmail(), firebaseUser1.getDisplayName()).observe(getViewLifecycleOwner(), aBoolean -> {
                    startActivityWithAnimation(new Intent(getContext(), MainActivity.class));
                });

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
}
