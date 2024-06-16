package com.czdxwx.wear.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.czdxwx.wear.R;
import com.czdxwx.wear.entity.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends BaseQuickAdapter<Device, QuickViewHolder> {
    private List<Device> devices = new ArrayList<>();

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable Device device) {
        quickViewHolder.setText(R.id.device_name,device.getName())
                .setText(R.id.device_owner,device.getOwner())
                .setText(R.id.is_online,device.getIsOnline()==1?"设备在线":"设备离线")
                .setImageResource(R.id.imageView,device.getIsOnline()==1?R.mipmap.is_online:R.mipmap.is_off);
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new QuickViewHolder(R.layout.layout_device_adapter, parent);
    }



    public DeviceAdapter() {
    }


}
