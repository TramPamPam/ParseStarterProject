package com.parse.starter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CustomReceiver extends BroadcastReceiver {
    private static final String TAG = "GOT";
    private final Handler handler = new Handler();
    private boolean isSaved;

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            String action = intent.getAction();
//            Log.v(TAG,"onReceive"+intent.getDataString());
            if (intent.getAction()!=null){
                Log.v(TAG,"action"+action);
            }
            String receivedText = "";
            if (intent.getExtras()!=null){
                Log.v(TAG,"extras "+ intent.getExtras().toString());
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                ParseObject parseObject = new ParseObject("ReceivedPush");
                Iterator itr = json.keys();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    parseObject.put(key,json.getString(key));

                    receivedText += "..." + key + " => " + json.getString(key)+"\n";
                    Log.v(TAG, "..." + key + " => " + json.getString(key));
                }

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException ex) {
                        if (ex == null) {
                            setIsSaved(true);
                        } else {
                            // Отказ
                            setIsSaved(false);
                        }
                    }
                });
                Log.v(TAG,"parseObject "+parseObject);

            }
            // Post the UI updating code to our Handler
            final String finalReceivedText = receivedText;
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if (isSaved){
                        Toast.makeText(context, "saved\n"+finalReceivedText, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (JSONException e) {
            Log.v(TAG, "JSONException: " + e.getMessage());
        }
    }

    public void setIsSaved(boolean saved) {
        isSaved = saved;
    }
}