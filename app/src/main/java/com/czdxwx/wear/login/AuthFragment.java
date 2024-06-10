package com.czdxwx.wear.login;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.czdxwx.wear.R;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

// 定义一个抽象类 AuthFragment，它继承自 Fragment
public abstract class AuthFragment extends Fragment {

  // 定义一个受保护的 Callback 对象
  protected Callback callback;

  // 受保护的视图成员变量
  protected VerticalTextView caption;
  protected ViewGroup parent;

  // 定义一个受保护的布尔值锁
  protected boolean lock;

  // 重写 Fragment 的 onCreate 方法
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 保留实例状态，以便在配置更改时保留 Fragment
    setRetainInstance(true);
  }

  // 重写 Fragment 的 onCreateView 方法
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // 加载布局
    View root = inflater.inflate(authLayout(), container, false);
    // 绑定视图
    caption = root.findViewById(R.id.caption);
    parent = root.findViewById(R.id.root);

    // 设置 root 的点击事件监听器
    parent.setOnClickListener(v -> unfold());

    // 设置键盘可见性事件监听器
    KeyboardVisibilityEvent.setEventListener(getActivity(), isOpen -> {
      // 当键盘可见性发生变化时调用 callback 的 scale 方法
      callback.scale(isOpen);
      if (!isOpen) {
        // 如果键盘不可见，则清除焦点
        clearFocus();
      }
    });
    return root;
  }

  // 设置回调方法
  public void setCallback(@NonNull Callback callback) {
    this.callback = callback;
  }

  // 定义一个抽象方法，用于返回布局资源 ID
  @LayoutRes
  public abstract int authLayout();

  // 定义一个抽象方法 fold
  public abstract void fold();

  // 定义一个抽象方法 clearFocus
  public abstract void clearFocus();

  // 展开视图的方法
  public void unfold() {
    if (!lock) {
      // 展开 caption 并设置其属性
      caption.setVerticalText(false);
      caption.requestLayout();
      Rotate transition = new Rotate();
      transition.setStartAngle(-90f);
      transition.setEndAngle(0f);
      transition.addTarget(caption);
      TransitionSet set = new TransitionSet();
      set.setDuration(getResources().getInteger(R.integer.duration));
      ChangeBounds changeBounds = new ChangeBounds();
      set.addTransition(changeBounds);
      set.addTransition(transition);
      TextSizeTransition sizeTransition = new TextSizeTransition();
      sizeTransition.addTarget(caption);
      set.addTransition(sizeTransition);
      set.setOrdering(TransitionSet.ORDERING_TOGETHER);
      caption.post(() -> {
        // 开始延迟过渡动画
        TransitionManager.beginDelayedTransition(parent, set);
        // 设置 caption 的属性
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.unfolded_size));
        caption.setTextColor(ContextCompat.getColor(getContext(), R.color.color_label));
        caption.setTranslationX(0);
        ConstraintLayout.LayoutParams params = getParams();
        params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        params.verticalBias = 0.78f;
        caption.setLayoutParams(params);
      });
      // 调用 callback 的 show 方法
      callback.show(this);
      // 锁定状态设置为 true
      lock = true;
    }
  }

  // 获取 ConstraintLayout.LayoutParams 类型的布局参数
  protected ConstraintLayout.LayoutParams getParams() {
    return (ConstraintLayout.LayoutParams) caption.getLayoutParams();
  }

  // 定义一个接口 Callback
  interface Callback {
    // 显示 AuthFragment
    void show(AuthFragment fragment);

    // 调整比例
    void scale(boolean hasFocus);
  }
}


