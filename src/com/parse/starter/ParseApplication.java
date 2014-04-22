package com.parse.starter;

import android.content.Intent;
import android.util.Log;
import com.parse.*;

import android.app.Application;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "Yy9Qh3o47hGEY6aKPFCVPrg4Dgv7DIG4HRCaBauP", "KY14rxBB3xClwVNmWDwZcUPEYGBWbwS1NTw9nmfD");


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);

        PushService.setDefaultPushCallback(this, ParseStarterProjectActivity.class);

        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
        defaultACL.setPublicReadAccess(true);
        ParseUser.enableAutomaticUser();
//        GetDataCallback getDataCallback = new GetDataCallback() {
//            @Override
//            public void done(byte[] bytes, ParseException e) {
//                Log.v("GOT", "something");
//            }
//        };
	}

}
