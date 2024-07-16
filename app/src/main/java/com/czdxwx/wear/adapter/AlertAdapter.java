package com.czdxwx.wear.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.czdxwx.wear.R;
import com.czdxwx.wear.entity.Alert;

import java.util.List;

public class AlertAdapter extends BaseQuickAdapter<Alert, QuickViewHolder> {

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable Alert alert) {
        assert alert != null;
        int alertImageResource = 0; // 默认图片
        switch (alert.getAlertType()) {
            case 0:
                alertImageResource = R.drawable.ic_down;
                break;
            case 1:
                alertImageResource = R.drawable.ic_lost;
                break;
            case 2:
                alertImageResource = R.drawable.ic_help;
                break;
        }
        quickViewHolder.setText(R.id.alert__title, alert.getAlertTitle())
                .setText(R.id.alert_content, alert.getAlertContent())
                .setText(R.id.alert_time, alert.getDateTime().toString())
                .setImageResource(R.id.alert_type, alertImageResource);
    }


    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new QuickViewHolder(R.layout.layout_message_adapter, parent);
    }


    public AlertAdapter(List<Alert> alerts) {
        super(alerts);
    }


}