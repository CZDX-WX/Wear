package com.czdxwx.wear.pages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.czdxwx.wear.R;
import com.czdxwx.wear.alert.ToastHelper;
import com.czdxwx.wear.databinding.ActivityShowAlertBinding;
import com.czdxwx.wear.entity.Alert;
import com.czdxwx.wear.network.ApiClient;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class ShowAlertActivity extends AppCompatActivity {

    private Alert alert;
    private ActivityShowAlertBinding binding;
    private static final String TAG = "ShowAlertActivity";
    private MMKV mmkv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定试图
        binding = ActivityShowAlertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MMKV.initialize(this);
        mmkv = MMKV.defaultMMKV();
        ApiClient apiClient = ApiClient.getInstance(this);
        Intent intent = getIntent();
        JPushInterface.setBadgeNumber(this, 0);
        binding.cardAlertInfo.tvTitle.setText("Notice");
        binding.cardAlertInfo.ivPicture.setImageResource(R.drawable.baseline_circle_notifications_24);
        binding.cardAlertProvince.tvTitle.setText("Province");
        binding.cardAlertProvince.ivPicture.setImageResource(R.drawable.ic_province);
        binding.cardAlertContent.tvTitle.setText("Content");
        binding.cardAlertContent.ivPicture.setImageResource(R.drawable.ic_content);
        binding.cardAlertCity.tvTitle.setText("City");
        binding.cardAlertCity.ivPicture.setImageResource(R.drawable.ic_city);
        binding.cardAlertDistinct.tvTitle.setText("District");
        binding.cardAlertDistinct.ivPicture.setImageResource(R.drawable.ic_distinct);
        binding.cardAlertTime.tvTitle.setText("Time");
        binding.cardAlertTime.ivPicture.setImageResource(R.drawable.ic_time);
        binding.cardAlertTemperature.tvTitle.setText("Temperature");
        binding.cardAlertTemperature.ivPicture.setImageResource(R.drawable.ic_weather);
        binding.stateBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(TitleBar titleBar) {
                finish();
            }
        });


        binding.btnCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ShowAlertActivity.this, Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED)//这个括号也真是难打，真的难打
                {
                    ActivityCompat.requestPermissions(ShowAlertActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    callSomeone();
                }
            }
        });

        if (intent.hasExtra("alert")) {
            // 从 Intent 中获取 Alert 对象
            alert = (Alert) intent.getSerializableExtra("alert");
        } else if (intent.hasExtra("jsonData")) {
            String jsonDataString = intent.getStringExtra("jsonData");
            // 解析 jsonData 字符串为 JSONObject
            JSONObject jsonDataObject = null;
            try {
                jsonDataObject = new JSONObject(jsonDataString);
                // 可以从 jsonDataObject 中获取具体的字段值
                alert = new Alert();
                alert.setAlertType(jsonDataObject.getInt("alertType"));
                alert.setAlertTitle(jsonDataObject.getString("alertTitle"));
                alert.setAlertContent(jsonDataObject.getString("alertContent"));
                alert.setLatitude(jsonDataObject.getString("latitude"));
                alert.setLongitude(jsonDataObject.getString("longitude"));
                alert.setProvince(jsonDataObject.getString("province"));
                alert.setCity(jsonDataObject.getString("city"));
                alert.setDistrict(jsonDataObject.getString("district"));
                alert.setTemperature(jsonDataObject.getString("temperature"));
                alert.setDateTime(jsonDataObject.getString("dateTime"));
//                int alertType = jsonDataObject.getInt("alertType");
//                String alertTitle = jsonDataObject.getString("alertTitle");
//                String alertContent = jsonDataObject.getString("alertContent");
//                String latitude = jsonDataObject.getString("latitude");
//                String longitude = jsonDataObject.getString("longitude");
//                String province = jsonDataObject.getString("province");
//                String city = jsonDataObject.getString("city");
//                String district = jsonDataObject.getString("district");
//                String temperature = jsonDataObject.getString("temperature");
//                String dateTime = jsonDataObject.getString("dateTime");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        Log.e(TAG, "alert: " + alert.toString());
        if (alert != null) {
            //? 获取pic
            apiClient.getAlertPicByTime(alert.getDateTime(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    decodeBase64ToBitmap(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ToastHelper.showOther(getApplicationContext(), "获取状态图失败");
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
                    ToastHelper.showOther(getApplicationContext(), "获取地图失败");
                    Log.e("MainActivity", "获取地图的错误" + error.getMessage());
                }
            });


            binding.cardAlertInfo.tvInfo.setText(alert.getAlertTitle());
            binding.cardAlertProvince.tvInfo.setText(alert.getProvince());
            binding.cardAlertContent.tvInfo.setText(alert.getAlertContent());
            binding.cardAlertCity.tvInfo.setText(alert.getCity());
            binding.cardAlertDistinct.tvInfo.setText(alert.getDistrict());
            binding.cardAlertTime.tvInfo.setText(alert.getDateTime());
            binding.cardAlertTemperature.tvInfo.setText(alert.getTemperature());
        } else {
            // 处理 Intent 中没有包裹数据的情况
            Toast.makeText(this, "No alert data found", Toast.LENGTH_SHORT).show();
        }

    }

    private void decodeBase64ToBitmap(String base64String) {
        // Check if base64String is valid
        if (base64String == null || base64String.isEmpty()) {
            Toast.makeText(ShowAlertActivity.this, "图片未获取到", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "图片为空");
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
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Log.d(TAG, "解码后的的pic是：" + bitmap.toString());
            binding.ivState.setImageBitmap(bitmap);
        } catch (IllegalArgumentException e) {
            Log.e("base64解析错误", e.getMessage());
            Toast.makeText(ShowAlertActivity.this, "解码失败", Toast.LENGTH_SHORT).show();
            binding.ivState.setImageResource(R.drawable.ic_fail);
        }


    }

    private void callSomeone() {
        try {
            String number = mmkv.decodeString("Number", "18360830086");
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        } catch (SecurityException e) {
            Log.e(TAG, "callSomeone: " + e.getMessage());
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permission, grantResult);
        if (requestCode == 1) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                callSomeone();
            } else {
                Toast.makeText(this, "你拒绝了这个权限", Toast.LENGTH_LONG).show();
            }
        }

    }
}