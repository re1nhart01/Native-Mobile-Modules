package com.appName.appVersionMarket;
import static com.appName.appVersionMarket.AppVersionModule.MY_REQUEST_CODE;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.jsoup.Jsoup;

public class AppVersionImpl {

    public String getStatusMarket(Activity activity) {
        String newVersion = null;
        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.appName&hl=ru&gl=US")
                    .timeout(30000)
                    .referrer("http://www.google.com")
                    .get()
                    .select("#yDmH0d > div.VfPpkd-Sx9Kwc.cC1eCc.UDxLd.PzCPDd.HQdjr.VfPpkd-Sx9Kwc-OWXEXe-FNFY6c > div.VfPpkd-wzTsW > div > div > div > div > div.fysCi > div:nth-child(3) > div:nth-child(1) > div.reAt0")
                    .first()
                    .text();
            return newVersion;
        } catch (Exception e) {
            return e.toString();
        }
    }

    public void CheckNativeForUpdate(Context ctx, Activity activity) {
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(ctx);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener( appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            activity,
                            MY_REQUEST_CODE
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void ResumeNativeForUpdates(Context ctx, Activity activity) {
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(ctx);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener( appUpdateInfo -> {
                try {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, activity, MY_REQUEST_CODE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        });
    }

}