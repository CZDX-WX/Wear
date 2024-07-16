package com.czdxwx.wear.pages;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.czdxwx.wear.databinding.ActivityShowAlertBinding;
import com.czdxwx.wear.entity.Alert;
import com.czdxwx.wear.network.ApiClient;

public class ShowAlertActivity extends AppCompatActivity {

    private Alert alert;
    private ActivityShowAlertBinding binding;
    private ApiClient apiClient;
    private static final String TAG = "ShowAlertActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定试图
        binding = ActivityShowAlertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiClient = ApiClient.getInstance(this);
        // 获取传入的 Intent
        Intent intent = getIntent();
        // 检查 Intent 是否包含名为 "alert" 的额外数据
        if (intent.hasExtra("alert")) {
            // 从 Intent 中获取 Alert 对象
            alert = (Alert) intent.getSerializableExtra("alert");
        } else {
            // 处理 Intent 中没有包裹数据的情况
            Toast.makeText(this, "No alert data found", Toast.LENGTH_SHORT).show();
        }

        //? 获取pic
        apiClient.getPicByTime(alert.getDateTime().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "请求的pic是：" + response);
                Bitmap bitmap = decodeBase64ToBitmap(response);
                Log.d(TAG, "解码后的的pic是：" + bitmap.toString());
                binding.ivState.setImageBitmap(bitmap);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "获取状态图错误", error);

            }
        });

        //? 获取map
        apiClient.getImageByCoordinates(alert.getLongitude() + "," + alert.getLatitude(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                binding.ivMap.setImageBitmap(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MainActivity", "获取地图的错误" + error.getMessage());


            }
        });

        binding.tvAlertTitle.setText(alert.getAlertTitle());
        binding.tvProvince.setText(alert.getProvince());
        binding.tvAlertContent.setText(alert.getAlertContent());
        binding.tvCity.setText(alert.getCity());
        binding.tvDistinct.setText(alert.getDistrict());
        binding.tvTime.setText(alert.getDateTime().toString());
        binding.tvTemperature.setText(alert.getTemperature());
        binding.tvAlertContent.setText(alert.getAlertContent());

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