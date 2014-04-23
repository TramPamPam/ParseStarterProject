package com.parse.starter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.parse.*;

public class EditorActivity extends Activity {
    private SerializableParseObject selectedItem;
    private TextView titleTextView;
    private TextView alertTextView;
    private EditText titleEditText;
    private EditText alertEditText;
    private ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        selectedItem = (SerializableParseObject) this.getIntent().getSerializableExtra("obj");

        if (null != selectedItem) {

            titleTextView = (TextView) findViewById(R.id.title);
            titleTextView.setText(selectedItem.getString("title"));

            alertTextView = (TextView) findViewById(R.id.content);
            alertTextView.setText(selectedItem.getString("alert"));

            titleEditText = (EditText) findViewById(R.id.edit_title);
            titleEditText.setText(selectedItem.getString("title"));
            titleEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    titleTextView.setText(titleEditText.getText());
                    titleTextView.setVisibility(View.VISIBLE);
                    titleEditText.setVisibility(View.GONE);
                    updateFields();
                    return true;
                }
            });

            alertEditText = (EditText) findViewById(R.id.edit_content);
            alertEditText.setText(selectedItem.getString("alert"));
            alertEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    alertTextView.setText(alertEditText.getText());
                    Log.v("EDT", "alertEditText " + alertEditText.getText());
                    alertTextView.setVisibility(View.VISIBLE);
                    alertEditText.setVisibility(View.GONE);
                    updateFields();
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete:
                progressBar.setVisibility(View.VISIBLE);
                String myID = selectedItem.getObjectId();
                ParseQuery query = new ParseQuery("ReceivedPush");
                query.getInBackground(myID, new GetCallback() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        Log.v("EDT", "got " + object.getString("alert"));
                        if (object != null) {
                            try {
                                object.delete();
                                MainDatabaseHelper mDbHelper = MainDatabaseHelper.getInstance(getApplicationContext());
                                mDbHelper.deleteObject(object.getObjectId());
                                Log.v("EDT", "deleted");
                            } catch (ParseException e1) {
                                Log.v("EDT", ""+e1);
                                e1.printStackTrace();
                            }

                        } else {
                            Log.v("EDT", "got nothing");
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

                return true;
            case R.id.edit:

                alertTextView.setVisibility(View.GONE);
                titleTextView.setVisibility(View.GONE);

                alertEditText.setVisibility(View.VISIBLE);
                titleEditText.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateFields(){
        progressBar.setVisibility(View.VISIBLE);
        String myID = selectedItem.getObjectId();
        ParseQuery query = new ParseQuery("ReceivedPush");
        query.getInBackground(myID, new GetCallback() {
            @Override
            public void done(ParseObject object, ParseException e) {

                Log.v("EDT", "got " + object.getObjectId());
                if (object != null) {
                    object.put("title",titleTextView.getText().toString());
                    object.put("alert",alertTextView.getText().toString());
                    object.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Log.v("EDT", "update failed cause "+e);
                            }
                        }
                    });
                } else {
                    Log.v("EDT", "got nothing");
                }
            }
        });
        MainDatabaseHelper mDbHelper = MainDatabaseHelper.getInstance(getApplicationContext());
        mDbHelper.updateObject(myID,titleTextView.getText().toString(),alertTextView.getText().toString());

    }

}
