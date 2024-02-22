package com.appName.utilities;

import org.json.JSONException;

public interface HttpCallback<T> {
    void onCompleted(T arg) throws JSONException;
}
