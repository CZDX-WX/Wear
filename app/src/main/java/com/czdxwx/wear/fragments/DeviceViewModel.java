package com.czdxwx.wear.fragments;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.czdxwx.wear.entity.Device;
import com.czdxwx.wear.network.ApiClient;

import java.util.List;

public class DeviceViewModel extends AndroidViewModel {
    private static final String TAG = "DeviceViewModel";
    private MutableLiveData<List<Device>> devicesLiveData;
    private final ApiClient apiClient;

    public DeviceViewModel(@NonNull Application application) {
        super(application);
        apiClient = ApiClient.getInstance(application);
        devicesLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Device>> getDevices() {
        return devicesLiveData;
    }

    public void fetchDevices() {
        apiClient.fetchDevices(new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> response) {
                devicesLiveData.setValue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                devicesLiveData.setValue(null); // 或者处理错误信息
            }
        });
    }
}