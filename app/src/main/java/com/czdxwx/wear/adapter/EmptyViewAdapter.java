package com.czdxwx.wear.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.czdxwx.wear.databinding.LayoutAnimationBinding;
import com.czdxwx.wear.entity.Status;

public class EmptyViewAdapter extends BaseQuickAdapter<Status, EmptyViewAdapter.VH> {

    public static class VH extends RecyclerView.ViewHolder {
        public final LayoutAnimationBinding binding;

        public VH(ViewGroup parent) {
            super(LayoutAnimationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
            this.binding = LayoutAnimationBinding.bind(itemView);
        }
    }

    @Override
    public VH onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
        return new VH(parent);
    }

    @Override
    public void onBindViewHolder(VH holder, int position, Status item) {

    }
}

