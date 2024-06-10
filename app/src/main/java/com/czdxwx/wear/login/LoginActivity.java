package com.czdxwx.wear.login;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.czdxwx.wear.R;
import com.czdxwx.wear.databinding.ActivityLoginBinding;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

  // 使用ViewBinding实例替换ButterKnife
  private ActivityLoginBinding binding;
  private List<ImageView> sharedElements;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // 使用ViewBinding来绑定视图
    binding = ActivityLoginBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);

    // 将ButterKnife的视图查找替换为ViewBinding
    sharedElements = List.of(binding.logo, binding.first, binding.second, binding.last);

    // ButterKnife.findById(this, R.id.pager) -> binding.pager
    final AnimatedViewPager pager = binding.pager;

    // ButterKnife.findById(this, R.id.scrolling_background) -> binding.scrollingBackground
    final ImageView background = binding.scrollingBackground;

    int[] screenSize = screenSize();

    for (ImageView element : sharedElements) {
      @ColorRes int color = element.getId() != R.id.logo ? R.color.white_transparent : R.color.color_logo_log_in;
      DrawableCompat.setTint(element.getDrawable(), ContextCompat.getColor(this, color));
    }

    // 加载大图像并调整大小
    Glide.with(this).load(R.drawable.busy)
            .asBitmap()
            .override(screenSize[0] * 2, screenSize[1])
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .into(new ImageViewTarget<Bitmap>(background) {
              @Override
              protected void setResource(Bitmap resource) {
                background.setImageBitmap(resource);
                background.post(() -> {
                  // 滚动到图像的最左边缘并触发缩放动画
                  background.scrollTo(-background.getWidth() / 2, 0);
                  ObjectAnimator xAnimator = ObjectAnimator.ofFloat(background, View.SCALE_X, 4f, background.getScaleX());
                  ObjectAnimator yAnimator = ObjectAnimator.ofFloat(background, View.SCALE_Y, 4f, background.getScaleY());
                  AnimatorSet set = new AnimatorSet();
                  set.playTogether(xAnimator, yAnimator);
                  set.setDuration(getResources().getInteger(R.integer.duration));
                  set.start();
                });
                pager.post(() -> {
                  AuthAdapter adapter = new AuthAdapter(getSupportFragmentManager(), pager, background, sharedElements);
                  pager.setAdapter(adapter);
                });
              }
            });
  }

  private int[] screenSize() {
    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return new int[]{size.x, size.y};
  }
}
