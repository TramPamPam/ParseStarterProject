package com.parse.starter;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
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
        parseObject.put("alert",mNotification.tickerText);
        parseObject.put("title","UPDATE_STATUS");
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {
                    Log.v("GOT","Saved ");
                } else {
                    // Отказ
                    Log.v("GOT","Did not saved ");
                }
            }
        });
        Log.v("GOT", "parseObject" + parseObject);
//        Log.v("GOT","notif tickerText "+mNotification.tickerText);
//        Log.v("GOT","notif toString() "+mNotification.toString());
        Log.v("GOT","event.getText() "+event.getText());
        Log.v("GOT","event "+event);
//        Log.v("GOT","event.getAction() "+event.getAction());

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
        Log.v("GOT","onServiceConnected");
    }
}
