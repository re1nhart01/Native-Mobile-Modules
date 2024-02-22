package com.appName.utilities;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Http extends AsyncTask<String, String, HashMap<String, String>> {
    private HttpCallback<HashMap<String, String>> callback;
    private final FetchBody fetchBody;

    public Http(FetchBody fetchBody, HttpCallback<HashMap<String, String>> httpCallback) {
        this.callback = httpCallback;
        this.fetchBody = fetchBody;
    }

    @Override
    protected HashMap<String, String> doInBackground(String... uri) {
        OkHttpClient client = new OkHttpClient();
        Request request = this.fetchBody.generate().build();
        HashMap<String, String> responseDict = new HashMap<>();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                responseDict.put("statusCode", String.valueOf(response.code()));
                responseDict.put("data", response.body().string());
            } else {
                responseDict.put("statusCode", String.valueOf(response.code()));
                responseDict.put("data", null);
            }
        } catch (IOException e) {
            responseDict.put("statusCode", "500");
            responseDict.put("data", "");
        } catch (Exception e) {
            responseDict.put("statusCode", "500");
            responseDict.put("data", "");
        }
        return responseDict;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> result) {
        super.onPostExecute(result);
        try {
            this.callback.onCompleted(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            super.execute();
        }
    }
}
