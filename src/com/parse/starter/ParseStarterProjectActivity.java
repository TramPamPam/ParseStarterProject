package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.parse.*;


import java.io.Serializable;
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

//        getActionBar().setTitle("Parse Demo");
//
////        getSupportActionBar().setIcon(R.drawable.ic_action_social_person);
//        getActionBar().setHomeButtonEnabled(true);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        Log.v("FET","onCreate");
//        createList();

        updateList();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MainDatabaseHelper mDbHelper = MainDatabaseHelper.getInstance(getApplicationContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.refresh:
                Toast toast = Toast.makeText(getApplicationContext(), "Refreshing...",Toast.LENGTH_SHORT);
                toast.show();

                updateList();
                return true;
            case R.id.search:
                Intent intent = new Intent(ParseStarterProjectActivity.this, SearchableActivity.class);
                ArrayList<SerializableParseObject> serializableParseObjects= SerializableParseObject.makeSerializableList(feedList);
                intent.putExtra("feed", serializableParseObjects);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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


    protected void updateList(){//(ArrayList<ParseObject> feedList) {

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

        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,	long id) {
                Object o = feedListView.getItemAtPosition(position);
                ParseObject parseObject = (ParseObject) o;

                Log.v("DBH", "selected parseObject:");
                Log.v("DBH", "... .getString(\"id\"):"+parseObject.getString("id"));
                Log.v("DBH", "... .getString(\"title\"):"+parseObject.getString("title"));
                Log.v("DBH", "... .getString(\"alert\"):"+parseObject.getString("alert"));
                MainDatabaseHelper mDbHelper = MainDatabaseHelper.getInstance(getApplicationContext());
                if( mDbHelper.findById(parseObject.getString("id")) )
                    Log.v("DBH", "found parseObject :)");
                else
                    Log.v("DBH", "not found parseObject :(");

                Intent intent = new Intent(ParseStarterProjectActivity.this, EditorActivity.class);
                intent.putExtra("obj", new SerializableParseObject(parseObject));
                startActivity(intent);
            }
        });
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
