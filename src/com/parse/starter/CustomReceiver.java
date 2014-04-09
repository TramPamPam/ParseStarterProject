package com.parse.starter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CustomReceiver extends BroadcastReceiver {
    private static final String TAG = "GOT";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            if (intent.getAction()!=null){
                Log.v(TAG,"action"+action);
                if (intent.getExtras()!=null){
                    Log.v(TAG,"extras "+ intent.getExtras().toString());
                    JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                    Iterator itr = json.keys();
                    while (itr.hasNext()) {
                        String key = (String) itr.next();
                        Log.v(TAG, "..." + key + " => " + json.getString(key));
                    }
                }
            }
        } catch (JSONException e) {
            Log.v(TAG, "JSONException: " + e.getMessage());
        }
    }
}