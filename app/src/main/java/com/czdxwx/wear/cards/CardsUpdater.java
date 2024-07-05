package com.czdxwx.wear.cards;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.ramotion.cardslider.DefaultViewUpdater;

public class CardsUpdater extends DefaultViewUpdater {

    // 重写 updateView 方法
    @Override
    public void updateView(@NonNull View view, float position) {
        // 调用父类的 updateView 方法
        super.updateView(view, position);

        // 获取 CardView 对象
        final CardView card = ((CardView)view);
        // 获取 CardView 的第二个子视图（通常是叠加层）
        final View alphaView = card.getChildAt(1);
        // 获取 CardView 的第一个子视图（通常是图片视图）
        final View imageView = card.getChildAt(0);

        // 如果视图位置小于0
        if (position < 0) {
            // 获取视图的当前透明度
            final float alpha = view.getAlpha();
            // 将视图的透明度设置为完全不透明
            view.setAlpha(1f);
            // 设置 alphaView 的透明度，使其随 alpha 变化
            alphaView.setAlpha(0.9f - alpha);
            // 设置 imageView 的透明度，使其随 alpha 变化
            imageView.setAlpha(0.3f + alpha);
        } else {
            // 如果视图位置不小于0，设置 alphaView 的透明度为0（完全透明）
            alphaView.setAlpha(0f);
            // 设置 imageView 的透明度为1（完全不透明）
            imageView.setAlpha(1f);
        }

    }

}

