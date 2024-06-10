package com.czdxwx.wear.login;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.transition.Transition;
import androidx.transition.TransitionValues;

import com.czdxwx.wear.R;


public class Rotate extends Transition {

  // 自定义属性名称，用于存储旋转角度
  private static final String PROPNAME_ROTATION = "vpaliy:rotate:rotation";

  // 起始和结束角度
  private float startAngle;
  private float endAngle;

  // 无参构造函数
  public Rotate() {
  }

  // 带上下文和属性集参数的构造函数
  public Rotate(Context context, AttributeSet attrs) {
    super(context, attrs);
    if (attrs != null) {
      // 获取自定义属性
      TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Rotate);
      startAngle = array.getFloat(R.styleable.Rotate_start_angle, 0f);
      endAngle = array.getFloat(R.styleable.Rotate_end_angle, 0f);
      array.recycle();
    }
  }

  // 设置结束角度
  public void setEndAngle(float endAngle) {
    this.endAngle = endAngle;
  }

  // 设置起始角度
  public void setStartAngle(float startAngle) {
    this.startAngle = startAngle;
  }

  // 捕捉起始值
  @Override
  public void captureStartValues(TransitionValues transitionValues) {
    transitionValues.values.put(PROPNAME_ROTATION, startAngle);
  }

  // 捕捉结束值
  @Override
  public void captureEndValues(TransitionValues transitionValues) {
    transitionValues.values.put(PROPNAME_ROTATION, endAngle);
  }

  // 创建动画
  @Override
  public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                 TransitionValues endValues) {
    // 如果起始值或结束值为空，返回null
    if (startValues == null || endValues == null) {
      return null;
    }

    // 获取视图和旋转角度
    final View view = endValues.view;
    float startRotation = (Float) startValues.values.get(PROPNAME_ROTATION);
    float endRotation = (Float) endValues.values.get(PROPNAME_ROTATION);

    // 如果起始角度和结束角度不同，创建旋转动画
    if (startRotation != endRotation) {
      view.setRotation(startRotation);
      return ObjectAnimator.ofFloat(view, View.ROTATION, startRotation, endRotation);
    }
    return null;
  }
}
