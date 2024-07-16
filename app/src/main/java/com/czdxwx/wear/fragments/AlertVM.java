package com.czdxwx.wear.fragments;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.czdxwx.wear.entity.Alert;
import com.czdxwx.wear.network.ApiClient;

import java.util.List;

public class AlertVM extends AndroidViewModel {
    private static final String TAG = "警告viewModel";
    private MutableLiveData<List<Alert>>  alertsLiveData;
    private final ApiClient apiClient;

    public AlertVM(@NonNull Application application) {
        super(application);
        apiClient = ApiClient.getInstance(application);
        alertsLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Alert>> getAlerts() {
        return alertsLiveData;
    }

    public void fetchAlerts() {
        apiClient.fetchAlerts(new Response.Listener<List<Alert>>() {
            @Override
            public void onResponse(List<Alert> response) {
                alertsLiveData.setValue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                alertsLiveData.setValue(null); // 或者处理错误信息
            }
        });
    }
}
