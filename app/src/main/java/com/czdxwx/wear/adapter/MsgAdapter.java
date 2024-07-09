package com.czdxwx.wear.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.czdxwx.wear.R;
import com.czdxwx.wear.entity.Msg;

import java.util.List;

public class MsgAdapter extends BaseQuickAdapter<Msg, QuickViewHolder> {

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable Msg msg) {
        assert msg != null;
//        quickViewHolder.setText(R.id.device_name,msg.getName())
//                .setText(R.id.device_owner,device.getOwner()+"的设备")
//                .setText(R.id.is_online,device.getIsOnline()==1?"设备在线":"设备离线")
//                .setTextColor(R.id.is_online,device.getIsOnline()==1? Color.BLACK:0xFF808080)
//                .setImageResource(R.id.imageView,device.getIsOnline()==1?R.mipmap.is_online:R.mipmap.is_off);
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new QuickViewHolder(R.layout.layout_device_adapter, parent);
    }


    public MsgAdapter(List<Msg> msgs) {
        super(msgs);
    }


}