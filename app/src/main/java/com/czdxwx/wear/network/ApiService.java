package com.czdxwx.wear.network;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.czdxwx.wear.entity.Device;
import com.czdxwx.wear.entity.Result;
import com.czdxwx.wear.entity.State;
import com.czdxwx.wear.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


public class ApiService {
    private final Context context;

    public ApiService(Context context) {
        this.context = context;
    }

    public void validateCredentials(String email, String password, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        String url = Constants.VLIDATE_USER;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            Log.e("error", "validateCredentials: ", e); ;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    //获取设备列表
    public void fetchDevices(final Response.Listener<List<Device>> listener, final Response.ErrorListener errorListener) {
        String url = Constants.DEVICE_GET_Devices;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            Type resultType = new TypeToken<Result<List<Device>>>() {}.getType();
                            Result<List<Device>> result = gson.fromJson(response.toString(), resultType);
                            if (result.getCode() == 0) {
                                listener.onResponse(result.getData());
                            } else {
                                errorListener.onErrorResponse(new VolleyError(result.getMessage()));
                            }
                        } catch (JsonSyntaxException e) {
                            errorListener.onErrorResponse(new VolleyError("Failed to parse JSON"));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    //根据时间获取states
    public void fetchStates(double time,final Response.Listener<List<State>> listener, final Response.ErrorListener errorListener) {
        String url = Constants.STATE_GET_STATES+ "?time=" + time;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            Type resultType = new TypeToken<Result<List<State>>>() {}.getType();
                            Result<List<State>> result = gson.fromJson(response.toString(), resultType);
                            if (result.getCode() == 0) {
                                listener.onResponse(result.getData());
                            } else {
                                errorListener.onErrorResponse(new VolleyError(result.getMessage()));
                            }
                        } catch (JsonSyntaxException e) {
                            errorListener.onErrorResponse(new VolleyError("Failed to parse JSON"));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
