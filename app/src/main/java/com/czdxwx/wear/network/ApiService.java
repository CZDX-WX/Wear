package com.czdxwx.wear.network;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.czdxwx.wear.entity.Device;
import com.czdxwx.wear.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
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



    public void fetchDevices(final Response.Listener<List<Device>> listener, final Response.ErrorListener errorListener) {
        String url = Constants.DEVICE_GET_Devices;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Device>>() {}.getType();
                        List<Device> devices = gson.fromJson(response.toString(), listType);
                        listener.onResponse(devices);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }


}
