package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

public class ParseStarterProjectActivity extends Activity {
	/** Called when the activity is first created. */
    private ListView feedListView = null;
    private ProgressBar progressbar;
    public ArrayList<ParseObject> feedList = new ArrayList<ParseObject>();

    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        ParseAnalytics.trackAppOpened(getIntent());
        startService(new Intent(this, NotificationService.class));
        progressbar = (ProgressBar) findViewById(R.id.progressBar);

        Log.v("FET","onCreate");
//        createList();

        updateList();

//        Log.v("FET","feedList "+feedList);

    }


    public void createList(){
        for (int i = 0; i<10; i++){
            ParseObject parseObject = new ParseObject("Bu");
            parseObject.put("alert",String.format("Some text %d",i));
            parseObject.put("title","SAMPLE");
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
            feedList.add(parseObject);
        }
        if (feedList.size() >= 10)
            updateList();
    };


    public void updateList(){//(ArrayList<ParseObject> feedList) {

        feedListView = (ListView) findViewById(R.id.custom_list);
        feedListView.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.VISIBLE);

        Log.v("FET", "Before list" + feedList);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ReceivedPush");  //ReceivedPush
//        Log.v("FET","query "+query);
//        query.whereEqualTo("title", "SAMPLE");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    Log.v("FET", "Retrieved " + list.size() + " objects");
                    Log.v("FET", "Retrieved list" + list);
                    updateListView(list);
                } else {
                    Log.d("FET", "Error: " + e.getMessage());
                }
            }
        });

    }

    private void updateListView(List<ParseObject> list) {
        progressbar.setVisibility(View.GONE);
        feedList = (ArrayList<ParseObject>) list;
        feedListView.setAdapter(new ParseObjectsListAdapter(this, feedList));

//        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position,	long id) {
//                    Object o = feedListView.getItemAtPosition(position);
//                    ParseObject newsData = (ParseObject) o;
//
//                    Intent intent = new Intent(FeedListActivity.this, FeedDetailsActivity.class);
//                    intent.putExtra("feed", newsData);
//                    startActivity(intent);
//        });

    }

    static void done(){
        Log.v("FET","Rtr nthng");
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            // If a new post has been added, update
//            // the list of posts
//            updateList();
//        }
//    }
}
