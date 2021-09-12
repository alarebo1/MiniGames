package com.example.minigames.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.minigames.R;
import com.example.minigames.databinding.DialogEditValueBinding;

public class InputValueDiaglogFragment extends DialogFragment {

    DialogEditValueBinding binding;

    private static String TAG_VALUE = "value";

    Callback mCallback;

    public static InputValueDiaglogFragment newInstance(String value) {
        Bundle args = new Bundle();
        args.putString(TAG_VALUE, value);
        InputValueDiaglogFragment fragment = new InputValueDiaglogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
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
        View root = inflater.inflate(R.layout.dialog_edit_value, null);
        binding = DataBindingUtil.bind(root);
        initLayout();
        binding.setActions(this);
        return root;
    }

    private void initLayout() {
        binding.edtValue.setText(getArguments().getString(TAG_VALUE, ""));
    }

    public void onClickSave() {
        if(mCallback != null){
            mCallback.editValue(binding.edtValue.getText().toString().trim());
            dismiss();
        }
    }

    interface Callback {
        void editValue(String value);
    }
}
