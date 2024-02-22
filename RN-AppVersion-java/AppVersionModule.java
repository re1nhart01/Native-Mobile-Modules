package com.appName.appVersionMarket;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;



public class AppVersionModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private final AppVersionImpl impl;
    public static final int MY_REQUEST_CODE = 2399;
    private ReactApplicationContext ctx;
    public AppVersionModule(ReactApplicationContext context) {
        super(context);
        this.ctx = context;
        this.impl = new AppVersionImpl();
    }

    @Override
    public String getName() {
        return "AppVersionModule";
    }

    @ReactMethod(isBlockingSynchronousMethod = false)
    public void hasUpdates(Promise promise) {
        try {
            final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(ctx);

            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            WritableMap map = new WritableNativeMap();
            appUpdateInfoTask.addOnSuccessListener( appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    map.putBoolean("result", false);
                    promise.resolve(map);
                } else {
                    map.putBoolean("result", true);
                    promise.resolve(map);
                }
            });
        } catch (Exception ex) {
            promise.reject(ex);
        }
    }


    @ReactMethod(isBlockingSynchronousMethod = false)
    public void checkForUpdates(Promise promise) {
        try {
            this.impl.CheckNativeForUpdate(this.ctx, this.getCurrentActivity());
            promise.resolve(true);
        } catch (Exception ex) {
            promise.reject(ex);
        }
    }

    @ReactMethod(isBlockingSynchronousMethod = false)
    public void resumeUpdates(Promise promise) {
        try {
            this.impl.ResumeNativeForUpdates(this.ctx, this.getCurrentActivity());
            promise.resolve(true);
        } catch (Exception ex) {
            promise.reject(ex);
        }
    }


    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                Log.e("onActivityResult", Integer.toString(resultCode));
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
    }
}