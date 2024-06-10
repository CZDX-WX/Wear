package com.czdxwx.wear.login;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.czdxwx.wear.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SignUpFragment extends AuthFragment {

  // 受保护的视图成员变量
  protected List<TextInputEditText> views;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (view != null) {
      // 设置背景颜色
      view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_sign_up));
      // 设置标题文字
      caption.setText(getString(R.string.sign_up_label));

      // 初始化视图列表
      views = new ArrayList<>();
      // 将视图添加到列表中
      views.add(view.findViewById(R.id.password_input_edit));
      views.add(view.findViewById(R.id.confirm_password_edit));

      // 遍历视图列表，设置每个视图的特性
      for (TextInputEditText editText : views) {
        if (editText.getId() == R.id.password_input_edit) {
          // 找到对应的 TextInputLayout 并设置字体
          final TextInputLayout inputLayout = view.findViewById(R.id.password_input);
          final TextInputLayout confirmLayout = view.findViewById(R.id.confirm_password);
          Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
          inputLayout.setTypeface(boldTypeface);
          confirmLayout.setTypeface(boldTypeface);
          // 添加文本变化监听器
          editText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
              // 根据文本长度设置密码可见性切换按钮
              inputLayout.setPasswordVisibilityToggleEnabled(editable.length() > 0);
            }
          });
        }
        // 设置焦点变化监听器
        editText.setOnFocusChangeListener((temp, hasFocus) -> {
          if (!hasFocus) {
            // 失去焦点时，根据文本长度设置选中状态
            boolean isEnabled = editText.getText().length() > 0;
            editText.setSelected(isEnabled);
          }
        });
      }
      // 设置标题垂直显示
      caption.setVerticalText(true);
      // 调用自定义方法进行折叠处理
      foldStuff();
      // 设置标题的水平平移量
      caption.setTranslationX(getTextPadding());
    }
  }

  @Override
  public int authLayout() {
    // 返回布局资源 ID
    return R.layout.sign_up_fragment;
  }

  @Override
  public void clearFocus() {
    // 清除所有视图的焦点
    for (View view : views) view.clearFocus();
  }

  @Override
  public void fold() {
    // 解除锁定
    lock = false;
    // 创建旋转过渡效果
    Rotate transition = new Rotate();
    transition.setEndAngle(-90f);
    transition.addTarget(caption);
    // 创建过渡集合
    TransitionSet set = new TransitionSet();
    set.setDuration(getResources().getInteger(R.integer.duration));
    ChangeBounds changeBounds = new ChangeBounds();
    set.addTransition(changeBounds);
    set.addTransition(transition);
    TextSizeTransition sizeTransition = new TextSizeTransition();
    sizeTransition.addTarget(caption);
    set.addTransition(sizeTransition);
    set.setOrdering(TransitionSet.ORDERING_TOGETHER);
    // 添加过渡监听器
    set.addListener(new TransitionListenerAdapter() {
      @Override
      public void onTransitionEnd(@NonNull Transition transition) {
        super.onTransitionEnd(transition);
        // 过渡结束时，重新设置标题属性
        caption.setTranslationX(getTextPadding());
        caption.setRotation(0);
        caption.setVerticalText(true);
        caption.requestLayout();
      }
    });
    // 开始延迟过渡动画
    TransitionManager.beginDelayedTransition(parent, set);
    // 调用自定义方法进行折叠处理
    foldStuff();
    caption.setTranslationX((float) -caption.getWidth() / 8 + getTextPadding());
  }

  private void foldStuff() {
    // 设置标题文本大小和颜色
    caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, caption.getTextSize() / 2f);
    caption.setTextColor(Color.WHITE);
    // 设置标题布局参数
    ConstraintLayout.LayoutParams params = getParams();
    params.rightToRight = ConstraintLayout.LayoutParams.UNSET;
    params.verticalBias = 0.5f;
    caption.setLayoutParams(params);
  }

  private float getTextPadding() {
    // 返回标题的水平平移量
    return getResources().getDimension(R.dimen.folded_label_padding) / 2.1f;
  }
}


