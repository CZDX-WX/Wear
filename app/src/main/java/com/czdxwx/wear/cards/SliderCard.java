package com.czdxwx.wear.cards;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.czdxwx.wear.R;


public class SliderCard extends RecyclerView.ViewHolder {

    // ImageView 组件，用于显示图片
    private final ImageView imageView;

    // 构造函数，初始化ImageView
    public SliderCard(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
    }

    // 设置内容，通过Bitmap加载图片
    void setContent(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    // 清除内容，取消任务
    void clearContent() {
        imageView.setImageDrawable(null);
    }
}


