package com.parse.starter;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class NotificationService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // TODO Auto-generated method stub.
        //Code when the event is caught
        Notification mNotification = (Notification) event.getParcelableData();
        ParseObject parseObject = new ParseObject("ReceivedPush");
        parseObject.put("id",String.format("0%d",mNotification.hashCode()));
        parseObject.put("alert", mNotification.tickerText);
        parseObject.put("title","UPDATE_STATUS");
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {
                    Log.v("GOT", "Saved ");

                } else {
                    // Отказ
                    Log.v("GOT", "Did not saved cause: "+ex);
                }
            }
        });

        MainDatabaseHelper mDbHelper = MainDatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MainDatabaseHelper.FeedEntry.COLUMN_NAME_ENTRY_ID, parseObject.getString("id"));
        values.put(MainDatabaseHelper.FeedEntry.COLUMN_NAME_TITLE, parseObject.getString("title"));
        values.put(MainDatabaseHelper.FeedEntry.COLUMN_NAME_CONTENT, parseObject.getString("alert"));

        long newRowId;
        newRowId = db.insert(
                MainDatabaseHelper.FeedEntry.TABLE_NAME,
                null,
                values);
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub.

    }

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout = 100;
        setServiceInfo(info);
        Log.v("GOT","onServiceConnected");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
