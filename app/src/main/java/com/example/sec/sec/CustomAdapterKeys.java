package com.example.sec.sec;

/**
 * Created by Kamil on 18.01.2017.
 */

import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapterKeys extends BaseAdapter {

    private List<Data> items;
    private Context context;
    private LayoutInflater inflater;

    public CustomAdapterKeys(Context _context, List<Data> _items){
        inflater = LayoutInflater.from(_context);
        this.items = _items;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data data = items.get(position);

        View view = convertView;

        if(view == null)
            view = inflater.inflate(R.layout.key_item, null);


        TextView name = (TextView) view.findViewById(R.id.tv_full_name);

        name.setText(data.getEmail());

        return view;
    }

}