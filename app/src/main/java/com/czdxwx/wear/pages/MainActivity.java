package com.czdxwx.wear.pages;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.czdxwx.wear.R;
import com.czdxwx.wear.cards.SliderAdapter;
import com.czdxwx.wear.entity.State;
import com.czdxwx.wear.network.ApiClient;
import com.deadline.statebutton.StateButton;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //! 初始化UI
        initUI();
    }

    private void initUI() {
        //! 绑定控件
        spinner = findViewById(R.id.spinner);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        temperatureSwitcher = (TextSwitcher) findViewById(R.id.ts_temperature);
        country1TextView = (TextView) findViewById(R.id.tv_country_1);
        country2TextView = (TextView) findViewById(R.id.tv_country_2);
        placeSwitcher = (TextSwitcher) findViewById(R.id.ts_place);
        clockSwitcher = (TextSwitcher) findViewById(R.id.ts_clock);
        descriptionsSwitcher = (TextSwitcher) findViewById(R.id.ts_description);
        mapSwitcher = (ImageSwitcher) findViewById(R.id.ts_map);

        getStates(()->{
            initSpinner();
            initRecyclerView();
            initCountryText();
            initSwitchers();
        });
    }

    //? 总的状态列表
    private List<State> states;
    //? 展示设备所在位置的图片
    private List<Bitmap> pics;

    //? 获取多少小时内的状态
    private double time = 0;

    //? 存放静态地图
    private List<Bitmap> maps;

    //? 循环列表的适配器
    private SliderAdapter sliderAdapter;

    //获取states数据，并且初始化数据
    @SuppressLint("NotifyDataSetChanged")
    private void getStates(Runnable runnable) {

        //! 获取states的线程
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 创建一个 CompletableFuture
            CompletableFuture<List<State>> completableFuture = new CompletableFuture<>();

            //! 获取states
            apiClient.fetchStates(time, new Response.Listener<List<State>>() {
                @Override
                public void onResponse(List<State> response) {
                    states = response;
                    completableFuture.complete(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: ", error);
                    completableFuture.completeExceptionally(error);
                }
            });

            // 等待 CompletableFuture 完成
            try {
                completableFuture.get();
            } catch (Exception e) {
                Log.e(TAG, "Error waiting for CompletableFuture", e);
            }
        });


        // CompletableFuture 完成后执行以下方法
        future.thenRun(() -> runOnUiThread(() -> {
            //更新
            sliderAdapter.notifyDataSetChanged();
            //! 取出每个状态，用来更新pics,maps,
            for (State state : states) {
                //? 获取maps
                maps.clear();
                apiClient.getImageByCoordinates(state.getLatitude() +state.getLongitude(), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        maps.add(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivity", "Error: " + error.getMessage());
                    }
                });
                //? 获取pics
                pics.clear();
                apiClient.getPicByTime(state.getTime().toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 将字节数组解码为 Bitmap
                        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        // 显示 Bitmap 到 ImageView
                        if (bitmap != null) {
                            pics.add(bitmap);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: ", error);
                    }
                });
            }
        }));

        runnable.run();
    }

    //初始化下拉栏
    private void initSpinner() {

        TitleBar stateBar = findViewById(R.id.state_bar);
        stateBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(TitleBar titleBar) {
                finish();
            }
        });
        spinner.setItems("半小时内", "2小时内", "24小时内", "3天内");
        //设置初始为第一个
        spinner.setSelectedIndex(0);
        spinner.setOnItemSelectedListener((view, position, id, item) -> {
            switch (position) {
                case 0:
                    time = 0.5;
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
        StateButton stateButton = findViewById(R.id.btn_refresh);
        stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStates(()->{});
            }
        });
    }


    // 初始化 RecyclerView
    private void initRecyclerView() {
        sliderAdapter = new SliderAdapter(maps, maps.size(), new OnCardClickListener());
        //card适配器
        recyclerView.setAdapter(sliderAdapter);
        //设置固定尺寸
        recyclerView.setHasFixedSize(true);
        //设置滑动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 当滚动停止时，调用 onActiveCardChange()
                    onActiveCardChange();
                }
            }
        });
        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();
        new CardSnapHelper().attachToRecyclerView(recyclerView);
    }

    // 初始化切换器
    private void initSwitchers() {

        temperatureSwitcher.setFactory(new TextViewFactory(R.style.TemperatureTextView, true));
        //初始从第一个开始
        temperatureSwitcher.setCurrentText(states.get(0).getTemperature());


        placeSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
        //初始从第一个开始
        placeSwitcher.setCurrentText(states.get(0).getCity());


        clockSwitcher.setFactory(new TextViewFactory(R.style.ClockTextView, false));
        //初始从第一个开始
        clockSwitcher.setCurrentText(states.get(0).getTime().toString());


        descriptionsSwitcher.setInAnimation(this, android.R.anim.fade_in);
        descriptionsSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        descriptionsSwitcher.setFactory(new TextViewFactory(R.style.DescriptionTextView, false));
        //初始从第一个开始
        descriptionsSwitcher.setCurrentText(states.get(0).getDistrict());

        mapSwitcher.setInAnimation(this, R.anim.fade_in);
        mapSwitcher.setOutAnimation(this, R.anim.fade_out);
        mapSwitcher.setFactory(new ImageViewFactory());
        //初始从第一个开始
        mapSwitcher.setImageDrawable(new BitmapDrawable(getResources(), maps.get(0)));


    }

    // 初始化省份
    // 文本
    private void initCountryText() {
        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);

        country1TextView.setX(countryOffset1);
        country2TextView.setX(countryOffset2);

        //初始从第一个开始
        country1TextView.setText(states.get(0).getProvince());
        country2TextView.setAlpha(0f);

        country1TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
        country2TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
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
        clockSwitcher.setText(states.get(pos).getTime().toString());

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
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageView;
        }
    }

    // 卡片点击监听器
    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm = (CardSliderLayoutManager) recyclerView.getLayoutManager();

            assert lm != null;
            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
                final Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(DetailsActivity.BUNDLE_IMAGE_ID, pics.get(activeCardPosition % pics.size()));

                final CardView cardView = (CardView) view;
                final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
                final ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(MainActivity.this, sharedView, "shared");
                startActivity(intent, options.toBundle());
            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }
    }

}
