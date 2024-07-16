package com.czdxwx.wear.alert;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.czdxwx.wear.R;
import com.hjq.toast.Toaster;


public class ToastHelper {
    private ToastHelper() {
    }


    public static void showOk(Context context, String s) {
        context = context.getApplicationContext();
        View v = LayoutInflater.from(context).inflate(R.layout.d_toast_customs, null);
        ImageView imageView = v.findViewById(R.id.iv_icon);
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.d_ic_toast_ok));
        //! 有问题
        Toaster.setView(v.getId());
        Toaster.setGravity(Gravity.CENTER, 0, 0);
        Toaster.show(s);
    }

    public static void showOther(Context context, String s) {
        context = context.getApplicationContext();
        View v = LayoutInflater.from(context).inflate(R.layout.d_toast_customs, null);
        ImageView imageView = v.findViewById(R.id.iv_icon);
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.d_ic_toast_other));
        //! 有问题
        Toaster.setView(v.getId());
        Toaster.setGravity(Gravity.CENTER, 0, 0);
        Toaster.show(s);
    }
}
