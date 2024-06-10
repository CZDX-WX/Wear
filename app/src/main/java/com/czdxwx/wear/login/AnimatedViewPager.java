package com.czdxwx.wear.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class AnimatedViewPager extends ViewPager {

  // 动画持续时间
  private int duration;

  // 构造函数：只带上下文参数
  public AnimatedViewPager(Context context) {
    super(context);
    postInitViewPager();
  }

  // 构造函数：带上下文和属性集参数
  public AnimatedViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    postInitViewPager();
  }

  // 初始化 ViewPager 方法
  private void postInitViewPager() {
    try {
      // 通过反射获取 ViewPager 的 mScroller 字段
      Field scroller = ViewPager.class.getDeclaredField("mScroller");
      scroller.setAccessible(true);
      // 创建自定义的 Scroller
      ScrollerCustomDuration mScroller = new ScrollerCustomDuration(getContext(), new DecelerateInterpolator());
      // 设置自定义的 Scroller
      scroller.set(this, mScroller);
    } catch (Exception ignored) {
      // 忽略异常
    }
  }

  // 拦截触摸事件的方法，返回 false 表示不拦截
  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return false;
  }

  // 处理触摸事件的方法，返回 false 表示不处理
  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    return false;
  }

  // 设置动画持续时间的方法
  public void setDuration(int duration) {
    this.duration = duration;
  }

  // 自定义的 Scroller 类，继承自 Scroller
  public class ScrollerCustomDuration extends Scroller {

    // 构造函数：带上下文和插值器参数
    ScrollerCustomDuration(Context context, Interpolator interpolator) {
      super(context, interpolator);
    }

    // 重载 startScroll 方法，带持续时间参数
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
      super.startScroll(startX, startY, dx, dy, AnimatedViewPager.this.duration);
    }

    // 重载 startScroll 方法，不带持续时间参数
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
      super.startScroll(startX, startY, dx, dy, AnimatedViewPager.this.duration);
    }
  }
}
