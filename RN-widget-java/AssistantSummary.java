package com.appName.Widgets.AssistantSummary;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import com.appName.R;
import com.appName.utilities.FetchBody;
import com.appName.utilities.Http;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * @author evgeniy kokaiko
 * Assistant summary widget
 * It uses userId to fetch data from api
 * if userId is not provided, then it will throw and exception into view;
 * userId is setting into rn context, uses sharedStorage module.
 */
public class AssistantSummary extends AppWidgetProvider {
    // constants
    private static final String serverUrl = "serverUrl";
    private static final String storageKey = "KEY";
    private static final int summary_assistant = R.id.summary_assistant;
    private static final int summary_holidays = R.id.summary_holidays;
    private static final int summary_birthdays = R.id.summary_birthdays;
    private static final int summary_reminders = R.id.summary_reminders;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        SharedPreferences storage = context.getSharedPreferences(AssistantSummary.storageKey, Context.MODE_PRIVATE);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.assistant_summary);
        String userId = storage.getString("userId", null);
        if (userId == null) {
            return;
        }

        HashMap <String, Object> requestData = new HashMap<>();
        requestData.put("userId", userId);
        FetchBody body = new FetchBody(serverUrl + "/widgets/summary", "POST", new HashMap < String, String > (), requestData);

        new Http(body, (arg) -> {
        try {
            final JSONObject json = new JSONObject(getOrDefault(arg, "data", ""));
            setFields(views,
                    json.optString("assistant_actions", "0"),
                    json.optString("active_reminders", "0"),
                    json.optString("active_birthdays", "0"),
                    json.optString("current_holidays", "0"));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } catch (JSONException ex) {
            setFields(views, "0", "0", "0", "0");
        };
        }).execute();
    }

    private static < T > T getOrDefault(HashMap < String, T > map, String key, T defaultValue) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return defaultValue;
        }
    }

    private static void setFields(RemoteViews views, String value1, String value2, String value3, String value4) {
        views.setTextViewText(summary_assistant, value1);
        views.setTextViewText(summary_reminders, value2);
        views.setTextViewText(summary_birthdays, value3);
        views.setTextViewText(summary_holidays, value4);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId: appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.println(Log.DEBUG, "NEC", "Created!");
    }

    @Override
    public void onDisabled(Context context) {
        Log.println(Log.DEBUG, "NEC", "Deleted!");
    }
}
