package com.parse.starter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SerialaziblePOListAdapter extends BaseAdapter{

    private final ArrayList<SerializableParseObject> listData;
    private final LayoutInflater layoutInflater;
    private final Context mContext;

    public SerialaziblePOListAdapter(Context context, ArrayList<SerializableParseObject> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SerializableParseObject newsItem = (SerializableParseObject) listData.get(position);
        holder.headlineView.setText(newsItem.getString("title"));
        holder.reportedDateView.setText(newsItem.getString("alert"));

        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reportedDateView;
    }

}
