package com.example.minigames.login;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.minigames.R;
import com.example.minigames.databinding.DialogResetPassBinding;
import com.example.minigames.ultis.Utils;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassDiaglogFragment extends DialogFragment {

    DialogResetPassBinding binding;
    private CallBack mCallback;

    private static String TAG_VALUE = "value";

    public static ResetPassDiaglogFragment newInstance() {
        Bundle args = new Bundle();
        ResetPassDiaglogFragment fragment = new ResetPassDiaglogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCallback(CallBack callback){
        this.mCallback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_reset_pass, null);
        binding = DataBindingUtil.bind(root);
        initLayout();
        binding.setActions(this);
        return root;
    }

    public void onClickOK() {
        String email = binding.edtValue.getText().toString().trim();
        String error = "";
        if (TextUtils.isEmpty(email) || !Utils.validate(email)) {
            error = getString(R.string.invalid_mail);
        }
        if (TextUtils.isEmpty(error)) {
            dismiss();
            if(mCallback != null){
                mCallback.onClickReset(email);
            }

        } else {
            Utils.showAlertDialog(getContext(),getString(R.string.confirm_title),error);
        }

    }

    private void initLayout() {
        binding.edtValue.setText(getArguments().getString(TAG_VALUE, ""));
    }

    public interface CallBack {
        void onClickReset(String email);
    }
}
