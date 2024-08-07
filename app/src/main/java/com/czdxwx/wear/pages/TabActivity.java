package com.czdxwx.wear.pages;

import static com.czdxwx.wear.fragments.BlankFragment.updateEmergencyNumber;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.czdxwx.wear.R;
import com.czdxwx.wear.adapter.MyPagerAdapter;
import com.czdxwx.wear.dialog.CustomNumberInputDialog;
import com.czdxwx.wear.entity.TabEntity;
import com.czdxwx.wear.fragments.AlertVM;
import com.czdxwx.wear.fragments.DeviceViewModel;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;


public class TabActivity extends AppCompatActivity implements CustomNumberInputDialog.CustomNumberInputDialogListener{

    private static final String TAG = "TabActivity";
    private MMKV mmkv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tab);
        MMKV.initialize(this);
        mmkv = MMKV.defaultMMKV();
        initUI();
        initData();
    }


    /*********************************初始化数据*********************************/

    private DeviceViewModel deviceViewModel;
    private AlertVM alertViewModel;

    private void initData() {

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        deviceViewModel.fetchDevices();
        deviceViewModel.getDevices().observe(this, devices -> {
            if (devices != null) {

            } else {
                Toast.makeText(TabActivity.this, "Error fetching devices", Toast.LENGTH_SHORT).show();
            }
        });


        alertViewModel = new ViewModelProvider(this).get(AlertVM.class);
        alertViewModel.fetchAlerts();
        alertViewModel.getAlerts().observe(this, alerts -> {
            if (alerts != null) {

            } else {
                Toast.makeText(TabActivity.this, "Error fetching alerts", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void refreshDevices() {
        deviceViewModel.fetchDevices();
    }
    public void refreshAlerts() {
        alertViewModel.fetchAlerts();
    }

    /**************************UI********************************/
    private CommonTabLayout mTabLayout;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;

    private final ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private final String[] mTitles = {"设备", "消息", "远程", "个人"};
    private final int[] mIconUnselectIds = {
            R.mipmap.tab_device_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_remote_unselect, R.mipmap.tab_more_unselect};
    private final int[] mIconSelectIds = {
            R.mipmap.tab_device_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_remote_select, R.mipmap.tab_more_select};

    private void initUI() {

        viewPager = findViewById(R.id.view_pager);
        // 初始化 ViewPager
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        mTabLayout = findViewById(R.id.tl);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabLayout.setCurrentTab(position);
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


    }


    @Override
    public void applyNumber(String number) {
        // Store number in MMKV
        mmkv.encode("Number", number);
        // 更新紧急电话号码显示
        updateEmergencyNumber();
    }
}