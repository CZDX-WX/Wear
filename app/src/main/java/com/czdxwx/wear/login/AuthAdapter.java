package com.czdxwx.wear.login;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.czdxwx.wear.R;

import java.util.List;

public class AuthAdapter extends FragmentStatePagerAdapter implements AuthFragment.Callback {

  // 动画ViewPager
  private final AnimatedViewPager pager;
  // 用于存储AuthFragment的稀疏数组
  private final SparseArray<AuthFragment> authArray;
  // 共享元素的列表
  private final List<ImageView> sharedElements;
  // 认证背景
  private final ImageView authBackground;
  // 缩放因子
  private float factor;

  // 构造函数
  AuthAdapter(FragmentManager manager,
              AnimatedViewPager pager,
              ImageView authBackground,
              List<ImageView> sharedElements) {
    super(manager);
    this.authBackground = authBackground;
    this.pager = pager;
    this.authArray = new SparseArray<>(getCount());
    this.sharedElements = sharedElements;
    pager.setDuration(350); // 设置动画持续时间

    // 计算缩放因子
    final float textSize = pager.getResources().getDimension(R.dimen.folded_size);
    final float textPadding = pager.getResources().getDimension(R.dimen.folded_label_padding);
    factor = 1 - (textSize + textPadding) / (pager.getWidth());
  }

  // 获取指定位置的Fragment
  @Override
  public AuthFragment getItem(int position) {
    AuthFragment fragment = authArray.get(position);
    if (fragment == null) {
      fragment = position != 1 ? new LogInFragment() : new SignUpFragment();
      authArray.put(position, fragment);
      fragment.setCallback(this); // 设置回调
    }
    return fragment;
  }

  // 显示指定的Fragment
  @Override
  public void show(AuthFragment fragment) {
    final int index = authArray.keyAt(authArray.indexOfValue(fragment));
    pager.setCurrentItem(index, true); // 切换到指定页面
    shiftSharedElements(getPageOffsetX(fragment), index == 1);
    for (int jIndex = 0; jIndex < authArray.size(); jIndex++) {
      if (jIndex != index) {
        authArray.get(jIndex).fold(); // 折叠其他页面
      }
    }
  }

  // 获取页面偏移量
  private float getPageOffsetX(AuthFragment fragment) {
    int pageWidth = fragment.requireView().getWidth();
    return pageWidth - pageWidth * factor;
  }

  // 移动共享元素
  private void shiftSharedElements(float pageOffsetX, boolean forward) {
    final Context context = pager.getContext();
    AnimatorSet shiftAnimator = new AnimatorSet();

    for (View view : sharedElements) {
      float translationX = forward ? pageOffsetX : -pageOffsetX;
      float temp = view.getWidth() / 3f;
      translationX -= forward ? temp : -temp;
      ObjectAnimator shift = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, translationX);
      shiftAnimator.playTogether(shift);
    }

    // 改变共享元素颜色
    int color = ContextCompat.getColor(context, forward ? R.color.color_logo_sign_up : R.color.color_logo_log_in);
    DrawableCompat.setTint(sharedElements.get(0).getDrawable(), color);

    // 背景滚动动画
    int offset = authBackground.getWidth() / 2;
    ObjectAnimator scrollAnimator = ObjectAnimator.ofInt(authBackground, "scrollX", forward ? offset : -offset);
    shiftAnimator.playTogether(scrollAnimator);
    shiftAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    shiftAnimator.setDuration(pager.getResources().getInteger(R.integer.duration) / 2);
    shiftAnimator.start();
  }

  // 调整缩放比例
  @Override
  public void scale(boolean hasFocus) {
    final float scale = hasFocus ? 1 : 1.4f;
    final float logoScale = hasFocus ? 0.75f : 1f;
    View logo = sharedElements.get(0);

    AnimatorSet scaleAnimation = new AnimatorSet();
    scaleAnimation.playTogether(ObjectAnimator.ofFloat(logo, View.SCALE_X, logoScale));
    scaleAnimation.playTogether(ObjectAnimator.ofFloat(logo, View.SCALE_Y, logoScale));
    scaleAnimation.playTogether(ObjectAnimator.ofFloat(authBackground, View.SCALE_X, scale));
    scaleAnimation.playTogether(ObjectAnimator.ofFloat(authBackground, View.SCALE_Y, scale));
    scaleAnimation.setDuration(200);
    scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    scaleAnimation.start();
  }

  // 获取页面宽度比例
  @Override
  public float getPageWidth(int position) {
    return factor;
  }

  // 获取页面数量
  @Override
  public int getCount() {
    return 2;
  }
}

