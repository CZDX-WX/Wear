package com.czdxwx.wear.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.czdxwx.wear.databinding.FragmentBlankBinding;
import com.czdxwx.wear.dialog.CustomNumberInputDialog;
import com.tencent.mmkv.MMKV;


public class BlankFragment extends Fragment implements View.OnClickListener{

    public static String TAG = "BlankFragment";
    private static FragmentBlankBinding viewBinding;
    private static MMKV mmkv;
    public BlankFragment() {
    }

    public static BlankFragment newInstance() {
        return new BlankFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMKV.initialize(requireContext());
        mmkv = MMKV.defaultMMKV();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewBinding = FragmentBlankBinding.inflate(inflater, container, false);

        // 初始化显示紧急电话号码
        updateEmergencyNumber();
        // 注册点击事件监听器
        viewBinding.layoutHeadImage.setOnClickListener(this);
        viewBinding.layoutNumber.setOnClickListener(this);
        viewBinding.layoutAbout.setOnClickListener(this);
        viewBinding.layoutSetting.setOnClickListener(this);
        return viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if (view == viewBinding.layoutHeadImage) {
           //TODO 未来实现
        } else if (view == viewBinding.layoutNumber) {
            //? 紧急号码
            CustomNumberInputDialog dialog = new CustomNumberInputDialog();
            dialog.show(getChildFragmentManager(), "CustomNumberInputDialog");
        } else if (view == viewBinding.layoutAbout) {
            //? 关于页
        } else if (view == viewBinding.layoutSetting) {

        }
    }


    @SuppressLint("SetTextI18n")
    public static void updateEmergencyNumber() {
        // 更新紧急电话号码显示
        String emergencyNumber = mmkv.decodeString("Number", "18360830086");
        viewBinding.tvNumber.setText("紧急电话号码:  " + emergencyNumber);
    }
}