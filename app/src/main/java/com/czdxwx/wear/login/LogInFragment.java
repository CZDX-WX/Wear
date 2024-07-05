package com.czdxwx.wear.login;

import android.content.Intent;
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
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.czdxwx.wear.R;
import com.czdxwx.wear.network.ApiClient;
import com.czdxwx.wear.pages.TabActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class LogInFragment extends AuthFragment {

    //http请求
    private ApiClient apiClient;
    // 受保护的视图成员变量
    protected List<TextInputEditText> views;

//    private final SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);; // 添加SharedPreferences变量


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        caption.setText(getString(R.string.log_in_label));
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_log_in));

        //****************************************************初始化 views列表
        views = new ArrayList<>();
        views.add(view.findViewById(R.id.email_input_edit));
        views.add(view.findViewById(R.id.password_input_edit));

        for (TextInputEditText editText : views) {
            if (editText.getId() == R.id.password_input_edit) {
                final TextInputLayout inputLayout = view.findViewById(R.id.password_input);
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                inputLayout.setTypeface(boldTypeface);
                editText.addTextChangedListener(new TextWatcherAdapter() {
                    @Override
                    public void afterTextChanged(Editable editable) {
                        inputLayout.setPasswordVisibilityToggleEnabled(editable.length() > 0);
                    }
                });
            }
            editText.setOnFocusChangeListener((temp, hasFocus) -> {
                if (!hasFocus) {
                    boolean isEnabled = editText.getText().length() > 0;
                    editText.setSelected(isEnabled);
                }
            });
        }


        // 在caption上添加点击事件监听器
        caption.setOnClickListener(v -> {
//            // 获取输入的邮箱和密码
//            String email = views.get(0).getText().toString();
//            String password = views.get(1).getText().toString();
//
//            // 模拟验证过程，实际应用中应该发送HTTP请求到数据库中进行验证
//            apiClient.validateCredentials("username", "password", new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Toast.makeText(requireContext(), "Login Successful: " + response.toString(), Toast.LENGTH_SHORT).show();
                    // 保存登录状态
//                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
                    //                     跳转到下一个Activity
                    Intent intent = new Intent(getContext(), TabActivity.class);
                    startActivity(intent);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    // Handle error
//                    Toast.makeText(requireContext(), "邮箱或密码不正确", Toast.LENGTH_SHORT).show();
//                }
//            });

        });

//        // 检查是否已经登录过，如果是，则直接跳转到下一个Activity
//        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
//            Intent intent = new Intent(getContext(), MainActivity.class);
//            startActivity(intent);
//        }
    }



    @Override
    public int authLayout() {
        return R.layout.login_fragment;
    }

    @Override
    public void fold() {
        lock = false;
        Rotate transition = new Rotate();
        transition.setEndAngle(-90f);
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
        final float padding = getResources().getDimension(R.dimen.folded_label_padding) / 2;
        set.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {
            }

            @Override
            public void onTransitionStart(@NonNull Transition transition, boolean isReverse) {
            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition, boolean isReverse) {
                caption.setTranslationX(-padding);
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();
            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition) {
            }

            @Override
            public void onTransitionPause(@NonNull Transition transition) {
            }

            @Override
            public void onTransitionResume(@NonNull Transition transition) {
            }
        });

        TransitionManager.beginDelayedTransition(parent, set);
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, caption.getTextSize() / 2);
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params = getParams();
        params.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias = 0.5f;
        caption.setLayoutParams(params);
        caption.setTranslationX((float) caption.getWidth() / 8 - padding);
    }

    @Override
    public void clearFocus() {
        for (View view : views) view.clearFocus();
    }
}

