package com.czdxwx.wear.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.czdxwx.wear.fragments.AboutFragment;
import com.czdxwx.wear.fragments.BlankFragment;
import com.czdxwx.wear.fragments.DeviceFragment;
import com.czdxwx.wear.fragments.MsgFragment;
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
                return DeviceFragment.newInstance();
            case 1:
                return MsgFragment.newInstance();
            case 2:
                return OnlineFragment.newInstance();
            case 4:
                return AboutFragment.newInstance();
            default:
                return BlankFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 4; // 总共的 Fragment 数量
    }
}