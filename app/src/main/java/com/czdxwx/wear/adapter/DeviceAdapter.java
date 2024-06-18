package com.czdxwx.wear.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.czdxwx.wear.R;
import com.czdxwx.wear.entity.Device;

import java.util.List;


public class DeviceAdapter extends BaseQuickAdapter<Device, QuickViewHolder> {

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable Device device) {
        assert device != null;
        quickViewHolder.setText(R.id.device_name,device.getName())
                .setText(R.id.device_owner,device.getOwner()+"的设备")
                .setText(R.id.is_online,device.getIsOnline()==1?"设备在线":"设备离线")
                .setTextColor(R.id.is_online,device.getIsOnline()==1?Color.BLACK:0xFF808080)
                .setImageResource(R.id.imageView,device.getIsOnline()==1?R.mipmap.is_online:R.mipmap.is_off);
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new QuickViewHolder(R.layout.layout_device_adapter, parent);
    }



    public DeviceAdapter(List<Device> devices) {
        super(devices);
    }


}
