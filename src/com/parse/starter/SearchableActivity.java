package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.parse.ParseObject;

import java.util.ArrayList;

public class SearchableActivity extends Activity {
    private ListView lv;
    private EditText et;
    private ArrayList<String> listview_array = new ArrayList<String>();
    private ArrayList<SerializableParseObject> array_sort= new ArrayList<SerializableParseObject>();
    int textlength=0;
    private ArrayList<SerializableParseObject> acceptedList;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        acceptedList = (ArrayList<SerializableParseObject>)this.getIntent().getSerializableExtra("feed");

        if (acceptedList.size()>=1)
            for (SerializableParseObject object: acceptedList){
                listview_array.add(object.getString("alert"));
            }

        lv = (ListView) findViewById(R.id.ListView01);
        et = (EditText) findViewById(R.id.EditText01);
        lv.setAdapter(new SerialaziblePOListAdapter(this,acceptedList));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,	long id) {
                Object o = lv.getItemAtPosition(position);
                SerializableParseObject parseObject = (SerializableParseObject) o;

                Intent intent = new Intent(SearchableActivity.this, EditorActivity.class);
                intent.putExtra("obj", parseObject);
                startActivity(intent);
            }
        });

        et.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
            }
            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after)
            {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                textlength = et.getText().length();
                array_sort.clear();
                for (int i = 0; i < listview_array.size(); i++)
                {
                    if (textlength <= listview_array.get(i).length())
                    {
                        if(et.getText().toString().equalsIgnoreCase(
                                (String)
                                        listview_array.get(i).subSequence(0,
                                                textlength)))
                        {
                            array_sort.add(acceptedList.get(i));
                        }
                    }
                }
                lv.setAdapter(new SerialaziblePOListAdapter(SearchableActivity.this,array_sort));
            }
        });
    }
}
