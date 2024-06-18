package com.czdxwx.wear.network;


import android.content.Context;

import com.android.volley.Response;
import com.czdxwx.wear.entity.Device;
import com.czdxwx.wear.entity.State;

import org.json.JSONObject;

import java.util.List;

public class ApiClient {
    private static ApiClient instance;
    private ApiService apiService;

    private ApiClient(Context context) {
        apiService = new ApiService(context);
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public void validateCredentials(String username, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        apiService.validateCredentials(username, password, listener, errorListener);
    }

    public void fetchDevices(Response.Listener<List<Device>> listener, Response.ErrorListener errorListener) {
        apiService.fetchDevices(listener, errorListener);
    }

    public void fetchStates(double time, Response.Listener<List<State>> listener, Response.ErrorListener errorListener) {
        apiService.fetchStates(time,listener, errorListener);
    }
    // 其他API调用方法
}

