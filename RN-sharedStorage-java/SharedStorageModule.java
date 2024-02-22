package com.appName.sharedStorage;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.appName.Widgets.AssistantSummary.AssistantSummary;

public class SharedStorageModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext ctx;
    private final String storageKey;
    private final String userIdKey;
    public SharedStorageModule(ReactApplicationContext parentContext) {
        super(parentContext);
        this.ctx = parentContext;
        this.storageKey = "APP_PACK";
        this.userIdKey = "internal_userId";
    }

    @NonNull
    @Override
    public String getName() {
        return "SharedStorage";
    }

    @ReactMethod(isBlockingSynchronousMethod = false)
    public void setItem(String key, String value, Promise promise) {
        SharedPreferences.Editor storageEditor = this.ctx.getSharedPreferences(this.storageKey, Context.MODE_PRIVATE).edit();
        if (key.isEmpty() || value.isEmpty()) {
            promise.resolve("Please, to commit into RNStorage provide key/value");
            return;
        }
        storageEditor.putString(key, value);
        storageEditor.apply();
        Intent intent = new Intent(getCurrentActivity().getApplicationContext(), AssistantSummary.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getCurrentActivity().getApplicationContext()).getAppWidgetIds(new ComponentName(getCurrentActivity().getApplicationContext(), AssistantSummary.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getCurrentActivity().getApplicationContext().sendBroadcast(intent);
    }
}
