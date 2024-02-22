package com.appName.services.softInputMode;

import android.app.Activity;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class SoftInputModeModule extends ReactContextBaseJavaModule {
    public SoftInputModeModule(ReactApplicationContext context) {
        super(context);
    }
    
    @NonNull
    @Override
    public String getName() {
        return "SoftInputModeModule";
    }
    
    
    @ReactMethod(isBlockingSynchronousMethod = false)
    public void updateMode(final int mode, Promise promise) {
        final Activity activity = getCurrentActivity();
        // unspecified = 0;
        // resize = 16;
        // pan = 32;
        // nothing 48;
        final int inlineMode = mode != 0 && mode != 16 && mode != 32 && mode != 48 ? 16 : mode;
        if (activity == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    activity.getWindow().setSoftInputMode(inlineMode);
                    promise.resolve(true);
                } catch (Exception ex) {
                    promise.resolve(false);
                }
            }
        });
    }
    
}