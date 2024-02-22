package com.appName.mail;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.Arrays;
import java.util.List;


public class MailClientsModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext ctx;
    private final String PACKAGE = "com.appName";
    private final PackageManager pm;
    public MailClientsModule(ReactApplicationContext context) {
        super(context);
        this.ctx = context;
        this.pm = this.ctx.getPackageManager();
    }

    public List<ResolveInfo> getMailList() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        final PackageManager packageManager = this.ctx.getPackageManager();
        // check packages (apps) for accessibility to be email clients
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);
        return list;
    }

    @ReactMethod(isBlockingSynchronousMethod = false)
    public void getListOfClients(Promise promise){
        try {
            List<ResolveInfo> list = this.getMailList();
            // array which usings for passing array into javascript
            WritableArray promiseArray = Arguments.createArray();
            for (ResolveInfo item : list) {
                // create rn object for pushing to js
                WritableMap map = new WritableNativeMap();
                //pass data from intent into object
                map.putString("applicationName", (String) item.loadLabel(this.pm));
                map.putString("name", item.activityInfo.name);
                map.putString("package", item.activityInfo.packageName);
                map.putString("process", item.activityInfo.processName);
                map.putString("parent", item.activityInfo.parentActivityName);
                //push to array which will be cast into js array
                promiseArray.pushMap(map);
            }
            promise.resolve(promiseArray);
        } catch (Exception e) {
            promise.reject("Sorry, you've got an error!");
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void setMailIntent(Intent intent,
                                    String subject,
                                    String[] to,
                                    String[]  cc,
                                    String[]  bcc,
                                    String body) {
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.putExtra(Intent.EXTRA_BCC, bcc);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, body);
    }


    private <T extends Object> T[] castReadableArray(ReadableArray data,  Class<? extends T[]> newType) {
        Object[] dataObjectArray = data.toArrayList().toArray();
        return Arrays.copyOf(dataObjectArray, dataObjectArray.length, newType);
    }

    @ReactMethod(isBlockingSynchronousMethod = false)
    public void sendMailByPkg
            (
            String pkg,
            String subject,
            ReadableArray to,
            ReadableArray  cc,
            ReadableArray  bcc,
            String body,
            Promise promise
            ){
       try {
           String[] arrayOfTo = this.castReadableArray(to, String[].class);
           String[] arrayOfCc = this.castReadableArray(cc, String[].class);
           String[] arrayOfBcc = this.castReadableArray(bcc, String[].class);
           WritableMap map = Arguments.createMap();
           List<ResolveInfo> listOfAccessibleClients = this.getMailList();
           Intent senderIntent = new Intent(Intent.ACTION_SENDTO);
           senderIntent.setData(Uri.parse("mailto:"));
           senderIntent.setPackage(pkg);
           senderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           this.setMailIntent(senderIntent, subject, arrayOfTo, arrayOfCc, arrayOfBcc, body);
           try {
            this.ctx.startActivity(senderIntent);
               map.putBoolean("isOK", true);
               map.putString("app", pkg);
               map.putString("title", (String) this.pm.getApplicationInfo(pkg, 0).loadLabel(this.pm));
               map.putString("error",  "");
            promise.resolve(map);
           } catch (ActivityNotFoundException actExc) {
               Intent fallbackIntent = new Intent(Intent.ACTION_SENDTO);
               fallbackIntent.setData(Uri.parse("mailto:"));
               this.setMailIntent(fallbackIntent, subject, arrayOfTo, arrayOfCc, arrayOfBcc, body);
               Intent chooserIntent = Intent.createChooser(fallbackIntent, "Selected email client is not installed. Select another...");
               chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               if (listOfAccessibleClients.size() >= 0) {
                   map.putBoolean("isOK", true);
                   map.putString("app", listOfAccessibleClients.get(0).activityInfo.packageName);
                   map.putString("title", (String) listOfAccessibleClients.get(0).loadLabel(this.pm));
                   map.putString("error",  "");
                   this.ctx.startActivity(chooserIntent);
               } else {
                   map.putBoolean("isOK", false);
                   map.putString("app", "com.unknown.email");
                   map.putString("title", "Unknown");
                   map.putString("error",  "Not available email clients");
               }
               promise.resolve(map);
           }
       } catch (Exception ex) {
           promise.reject(ex.toString());
       }
    }


    @Override
    public String getName() {
        return "MailClientsModule";
    }

}