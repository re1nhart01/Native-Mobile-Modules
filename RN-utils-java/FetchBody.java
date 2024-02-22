package com.appName.utilities;


import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FetchBody {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String method;
    private final String url;
    private final HashMap<String, String> headers;
    private final HashMap<String, Object> requestBody;
    public FetchBody(String url, String method, HashMap<String, String> headers, HashMap<String, Object> requestBody) {
        this.method = method;
        this.headers = headers;
        this.requestBody = requestBody;
        this.url = url;
    }
    public Request.Builder generate() {
        Request.Builder builder = new Request.Builder().url(this.url);
        String jsonBody = new JSONObject(this.requestBody).toString();
        RequestBody body = RequestBody.create(JSON, jsonBody);
        builder.method(this.method, this.method.equals("GET") || this.method.equals("HEAD") ? null : body);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.headers.forEach(builder::addHeader);
        }
        return builder;
    }
}
