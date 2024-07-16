package com.czdxwx.wear.pages;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.czdxwx.wear.R;
import com.czdxwx.wear.adapter.EmptyViewAdapter;
import com.czdxwx.wear.cards.SliderAdapter;
import com.czdxwx.wear.entity.State;
import com.czdxwx.wear.network.ApiClient;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "MainActivity";
    //    private ActivityMainBinding viewBinding;
    private ApiClient apiClient;

    // 图片资源数组
//    private final int[] pics = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5};
    // 地图资源数组
//    private final int[] maps = {R.drawable.map_paris, R.drawable.map_seoul, R.drawable.map_london, R.drawable.map_beijing, R.drawable.map_greece};
    // 描述文字资源数组
//    private final int[] descriptions = {R.string.text1, R.string.text2, R.string.text3, R.string.text4, R.string.text5};
    // 国家名称数组
//    private final String[] countries = {"PARIS", "SEOUL", "LONDON", "BEIJING", "THIRA"};
    // 地点名称数组
//    private final String[] places = {"The Louvre", "Gwanghwamun", "Tower Bridge", "Temple of Heaven", "Aegeana Sea"};
    // 温度数组
//    private final String[] temperatures = {"21°C", "19°C", "17°C", "23°C", "20°C"};
    // 时间数组
//    private final String[] times = {"Aug 1 - Dec 15    7:00-18:00", "Sep 5 - Nov 10    8:00-16:00", "Mar 8 - May 21    7:00-18:00"};
    private EmptyViewAdapter mAdapter = new EmptyViewAdapter();
    //? 总的状态列表
    private List<State> states;
    //? 展示设备所在位置的图片
    private List<Bitmap> pics;

    //? 获取多少小时内的状态
    private int time;

    //? 存放静态地图
    private List<Bitmap> maps;

    //? 循环列表的适配器
    private SliderAdapter sliderAdapter;

    private boolean isCardSnapHelperAttached = false;
    private MaterialSpinner spinner;

    // 布局管理器
    private CardSliderLayoutManager layoutManger;
    private RecyclerView recyclerView;

    //切换器

    private ImageSwitcher mapSwitcher;
    private TextSwitcher temperatureSwitcher;
    private TextSwitcher placeSwitcher;
    private TextSwitcher clockSwitcher;
    private TextSwitcher descriptionsSwitcher;


    private TextView country1TextView;
    private TextView country2TextView;
    private int countryOffset1;
    private int countryOffset2;
    private long countryAnimDuration;
    private int currentPosition;


    private LinearLayout llOriginalContent;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化
        pics = new ArrayList<>();
        maps = new ArrayList<>();
        states = new ArrayList<>();
        apiClient = ApiClient.getInstance(this);
        time = 72;


        //! 初始化UI
        initUI();
    }


    private void initUI() {


        //! 执行一次即可
        spinner = findViewById(R.id.spinner);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });
        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();

        if (!isCardSnapHelperAttached) {
            new CardSnapHelper().attachToRecyclerView(recyclerView);
            isCardSnapHelperAttached = true; // 设置标志变量
        }


        temperatureSwitcher = (TextSwitcher) findViewById(R.id.ts_temperature);
        country1TextView = (TextView) findViewById(R.id.tv_country_1);
        country2TextView = (TextView) findViewById(R.id.tv_country_2);
        placeSwitcher = (TextSwitcher) findViewById(R.id.ts_place);
        clockSwitcher = (TextSwitcher) findViewById(R.id.ts_clock);
        descriptionsSwitcher = (TextSwitcher) findViewById(R.id.ts_description);
        mapSwitcher = (ImageSwitcher) findViewById(R.id.ts_map);
        llOriginalContent = findViewById(R.id.llOriginalContent);

        rv = findViewById(R.id.rv);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter.setStateViewLayout(this, R.layout.loading_view);
        mAdapter.setStateViewEnable(true);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStates();
            }
        });
        TitleBar stateBar = findViewById(R.id.state_bar);
        stateBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(TitleBar titleBar) {
                finish();
            }
        });
        spinner.setItems("1小时内", "2小时内", "24小时内", "3天内");
        //设置初始为第一个
        spinner.setSelectedIndex(0);
        spinner.setOnItemSelectedListener((view, position, id, item) -> {
            switch (position) {
                case 0:
                    time = 1;
                    break;
                case 1:
                    time = 2;
                    break;
                case 2:
                    time = 24;
                    break;
                case 3:
                    time = 72;
                    break;
                default:
                    break;
            }
        });
        mapSwitcher.setInAnimation(this, R.anim.fade_in);
        mapSwitcher.setOutAnimation(this, R.anim.fade_out);
        mapSwitcher.setFactory(new ImageViewFactory());
        temperatureSwitcher.setFactory(new TextViewFactory(R.style.TemperatureTextView, true));
        placeSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
        clockSwitcher.setFactory(new TextViewFactory(R.style.ClockTextView, false));
        descriptionsSwitcher.setInAnimation(this, android.R.anim.fade_in);
        descriptionsSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        descriptionsSwitcher.setFactory(new TextViewFactory(R.style.DescriptionTextView, false));

        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);
        country1TextView.setX(countryOffset1);
        country2TextView.setX(countryOffset2);
        country2TextView.setAlpha(0f);
        country1TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
        country2TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
        //************************************************************************************************

        //! 后续还要刷新

        getStates();
    }


    //获取states数据，并且初始化数据
    private void getStates() {
        // 隐藏原始内容
        llOriginalContent.setVisibility(View.GONE);
        // 显示 rv
        rv.setVisibility(View.VISIBLE);
        // 异步获取 states 数据
        apiClient.fetchStates(time, new Response.Listener<List<State>>() {
            @Override
            public void onResponse(List<State> response) {
                states.clear();
                states.addAll(response);
                if (!states.isEmpty()) {
                    refresh();
                } else {
                    // 数据为空的处理
                    mAdapter.setStateView(getEmptyDataView());
                    // 隐藏原始内容
                    llOriginalContent.setVisibility(View.GONE);
                    // 显示 rv
                    rv.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAdapter.setStateView(getErrorView());
                // 隐藏原始内容
                llOriginalContent.setVisibility(View.GONE);
                // 显示 rv
                rv.setVisibility(View.VISIBLE);
                Log.e(TAG, "onErrorResponse: ", error);
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh() {
        final CountDownLatch latch = new CountDownLatch(states.size() * 2);
        maps.clear();
        pics.clear();
        //! 取出每个状态，用来更新pics,maps,
        for (State state : states) {
//            Log.d(TAG, "状态的值为: "+state.toString());
            //? 获取pics
            apiClient.getPicByTime(state.getDateTime(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Log.d(TAG,"请求的pic是："+response);
                    Bitmap bitmap = decodeBase64ToBitmap(response);
//                    Log.d(TAG,"解码后的的pic是："+bitmap.toString());
                    pics.add(bitmap);
                    latch.countDown(); // 每次成功获取图片后减少 latch 的计数器
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "获取状态图错误", error);
                    latch.countDown(); // 每次成功获取图片后减少 latch 的计数器
                }
            });
            Log.d(TAG, "location为：" + state.getLongitude() + "," + state.getLatitude());
            //? 获取maps
            apiClient.getImageByCoordinates(state.getLongitude() + "," + state.getLatitude(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
//                    Log.d(TAG,"请求的map是："+response);
                    maps.add(response);
                    latch.countDown(); // 每次成功获取图片后减少 latch 的计数器
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("MainActivity", "获取地图的错误" + error.getMessage());
                    latch.countDown(); // 每次成功获取图片后减少 latch 的计数器
                }
            });

        }
        // 等待所有异步请求完成
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await(); // 等待所有异步请求完成
                } catch (InterruptedException e) {
                    Log.e("加载图片", Objects.requireNonNull(e.getMessage()));
                }
                // 在主线程中更新 UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 确保在所有异步请求完成后执行的代码
                        if (!pics.isEmpty()) {
                            Log.d(TAG, "pics 不是空的 ");
                            if (sliderAdapter == null) {
                                sliderAdapter = new SliderAdapter(pics, pics.size(), new OnCardClickListener());
                                recyclerView.setAdapter(sliderAdapter);
                            } else {
                                sliderAdapter.update(pics);
                                sliderAdapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(0);
                            }
                        }

                        if (!maps.isEmpty()) {
                            Log.d(TAG, "maps(0)是：" + maps.get(0).toString());
                            Drawable drawable = new BitmapDrawable(getResources(), maps.get(0));
                            mapSwitcher.setImageDrawable(drawable);
                        }

                        if (!states.isEmpty()) {
                            // 显示原始内容
                            llOriginalContent.setVisibility(View.VISIBLE);
                            // 隐藏RecyclerView
                            rv.setVisibility(View.GONE);
                        } else {
                            // 数据为空的处理
                            mAdapter.setStateView(getEmptyDataView());
                            // 隐藏原始内容
                            llOriginalContent.setVisibility(View.GONE);
                            // 显示 RecyclerView
                            rv.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        }).start();

        //初始从第一个开始
        country1TextView.setText(states.get(0).getProvince());
        //初始从第一个开始
        temperatureSwitcher.setCurrentText(states.get(0).getTemperature());
        //初始从第一个开始
        placeSwitcher.setCurrentText(states.get(0).getCity());
        //初始从第一个开始
        clockSwitcher.setCurrentText(states.get(0).getDateTime().toString());
        //初始从第一个开始
        descriptionsSwitcher.setCurrentText(states.get(0).getDistrict());

    }


    // 设置省份文本动画
    private void setCountryText(String text, boolean left2right) {
        final TextView invisibleText;
        final TextView visibleText;
        if (country1TextView.getAlpha() > country2TextView.getAlpha()) {
            visibleText = country1TextView;
            invisibleText = country2TextView;
        } else {
            visibleText = country2TextView;
            invisibleText = country1TextView;
        }

        final int vOffset;
        if (left2right) {
            invisibleText.setX(0);
            vOffset = countryOffset2;
        } else {
            invisibleText.setX(countryOffset2);
            vOffset = 0;
        }

        invisibleText.setText(text);

        final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
        final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
        final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", countryOffset1);
        final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(iAlpha, vAlpha, iX, vX);
        animSet.setDuration(countryAnimDuration);
        animSet.start();
    }

    // 当激活卡片变化时的处理
    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }
        onActiveCardChange(pos);
    }

    //切换卡片时，切换所有state
    private void onActiveCardChange(int pos) {
        int animH[] = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        setCountryText(states.get(pos).getProvince(), left2right);

        temperatureSwitcher.setInAnimation(MainActivity.this, animH[0]);
        temperatureSwitcher.setOutAnimation(MainActivity.this, animH[1]);
        temperatureSwitcher.setText(states.get(pos).getTemperature());

        placeSwitcher.setInAnimation(MainActivity.this, animV[0]);
        placeSwitcher.setOutAnimation(MainActivity.this, animV[1]);
        placeSwitcher.setText(states.get(pos).getCity());

        clockSwitcher.setInAnimation(MainActivity.this, animV[0]);
        clockSwitcher.setOutAnimation(MainActivity.this, animV[1]);
        clockSwitcher.setText(states.get(pos).getDateTime());

        descriptionsSwitcher.setText(states.get(pos).getDistrict());

        mapSwitcher.setImageDrawable(new BitmapDrawable(getResources(), maps.get(pos)));
        currentPosition = pos;
    }


    // TextView 工厂类
    private class TextViewFactory implements ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(MainActivity.this);

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            textView.setTextAppearance(styleId);
            return textView;
        }

    }

    // ImageView 工厂类
    private class ImageViewFactory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageView;
        }
    }

    // 卡片点击监听器
    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            final CardSliderLayoutManager lm = (CardSliderLayoutManager) recyclerView.getLayoutManager();
//
//            assert lm != null;
//            if (lm.isSmoothScrolling()) {
//                return;
//            }
//
//            final int activeCardPosition = lm.getActiveCardPosition();
//            if (activeCardPosition == RecyclerView.NO_POSITION) {
//                return;
//            }
//
//            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
//            if (clickedPosition == activeCardPosition) {
//                final Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//                intent.putExtra(DetailsActivity.BUNDLE_IMAGE_ID, pics.get(activeCardPosition % pics.size()));
//
//                final CardView cardView = (CardView) view;
//                final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
//                final ActivityOptions options = ActivityOptions
//                        .makeSceneTransitionAnimation(MainActivity.this, sharedView, "shared");
//                startActivity(intent, options.toBundle());
//            } else if (clickedPosition > activeCardPosition) {
//                recyclerView.smoothScrollToPosition(clickedPosition);
//                onActiveCardChange(clickedPosition);
//            }
        }
    }


    private View getEmptyDataView() {
        View notDataView = LayoutInflater.from(this).inflate(R.layout.empty_view, new FrameLayout(this), false);
        return notDataView;
    }

    private View getErrorView() {
        View errorView = LayoutInflater.from(this).inflate(R.layout.error_view, new FrameLayout(this), false);
        return errorView;
    }

    private Bitmap decodeBase64ToBitmap(String base64String) {
        // Check if base64String is valid
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }

        // Trim any leading or trailing whitespace
        base64String = base64String.trim();

        // Check if base64String is properly padded
        int padding = base64String.length() % 4;
        if (padding > 0) {
            base64String = base64String + "====".substring(padding);
        }

        // Decode base64String to bitmap
        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
