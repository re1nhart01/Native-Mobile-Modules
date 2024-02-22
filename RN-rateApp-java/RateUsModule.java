package com.appName.rateUsPopup;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewException;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;


public class RateUsModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private ReactApplicationContext ctx;
    private final String PACKAGE = "com.appName";
    public RateUsModule(ReactApplicationContext context) {
        super(context);
        this.ctx = context;
    }

    @Override
    public String getName() {
        return "RateUsModule";
    }

    @ReactMethod(isBlockingSynchronousMethod = false)
    private void rateUs(Promise promise) {
        ReviewManager manager = ReviewManagerFactory.create(this.ctx);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        WritableMap map = Arguments.createMap();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(this.ctx.getCurrentActivity(), reviewInfo);
                flow.addOnCompleteListener(tasking -> {
                    if (tasking.isSuccessful()) {
                        map.putBoolean("isResolved", true);
                        map.putInt("code", 0000);
                        promise.resolve(map);
                    } else {
                        promise.reject(new Exception("rateUs exception"));
                    }
                });
            } else {
                @ReviewErrorCode int reviewErrorCode = ((ReviewException) task.getException()).getErrorCode();
                map.putBoolean("isResolved", false);
                map.putInt("code", reviewErrorCode);
                promise.resolve(map);
            }
        });
    }

    @ReactMethod(isBlockingSynchronousMethod = false)
    private void goToMarket(Promise promise) {
        try
        {
            final String appPackageName = this.ctx.getPackageName();
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.ctx.startActivity(intent);

            } catch (android.content.ActivityNotFoundException anfe) {
                this.ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        catch (ActivityNotFoundException e)
        {
            Log.e("goToMarket.Exception", e.toString());
            promise.reject(e.toString());
        }
    }

    @Override
    public void onActivityResult(Activity activity, int i, int i1, @Nullable Intent intent) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}