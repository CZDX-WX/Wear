package com.czdxwx.wear.cards;


import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.czdxwx.wear.R;

import java.util.List;


public class SliderAdapter extends RecyclerView.Adapter<SliderCard> {

    // 卡片数量
    private final int count;
    // 位图列表
    private final List<Bitmap> content;
    // 点击监听器
    private final View.OnClickListener listener;

    // 构造函数，初始化内容数组、卡片数量和点击监听器
    public  SliderAdapter(List<Bitmap> content, int count, View.OnClickListener listener) {
        this.content = content;
        this.count = count;
        this.listener = listener;
    }

    // 创建新的ViewHolder，当RecyclerView需要一个新的ViewHolder时会调用此方法
    @NonNull
    @Override
    public SliderCard onCreateViewHolder(ViewGroup parent, int viewType) {
        // 从布局文件中加载视图
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_slider_card, parent, false);

        // 如果有设置点击监听器，则为视图设置点击事件
        if (listener != null) {
            view.setOnClickListener(listener);
        }

        // 返回新的SliderCard对象
        return new SliderCard(view);
    }

    // 绑定ViewHolder，当RecyclerView需要展示一个数据时会调用此方法
    @Override
    public void onBindViewHolder(SliderCard holder, int position) {
        // 设置卡片内容，使用数组长度取模确保数组下标不越界
        holder.setContent(content.get(position % content.size()));
    }
    // 当ViewHolder被回收时调用此方法，可以在这里清理资源
    @Override
    public void onViewRecycled(SliderCard holder) {
        // 清理卡片内容
        holder.clearContent();
    }

    // 返回RecyclerView的项目数量
    @Override
    public int getItemCount() {
        return count;
    }

}
