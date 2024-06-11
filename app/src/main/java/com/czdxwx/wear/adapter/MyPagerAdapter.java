package com.czdxwx.wear.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.czdxwx.wear.fragments.AboutFragment;
import com.czdxwx.wear.fragments.BlankFragment;
import com.czdxwx.wear.fragments.DeviceFragment;
import com.czdxwx.wear.fragments.OnlineFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DeviceFragment();
            case 1:
                return new AboutFragment();
            case 2:
                return new OnlineFragment();
            default:
                return new BlankFragment();
        }
    }

    @Override
    public int getCount() {
        return 5; // 总共的 Fragment 数量
    }
}