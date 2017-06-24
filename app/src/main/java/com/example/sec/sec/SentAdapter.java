// CustomAdapter.java
package com.example.sec.sec;

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

public class SentAdapter extends BaseAdapter {

    private List<MailContent> items;
    private Context context;
    private LayoutInflater inflater;

    public SentAdapter(Context _context, List<MailContent> _items){
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
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MailContent mc = items.get(position);

        View view = convertView;

        if(view == null)
            view = inflater.inflate(R.layout.sent_item, null);


        TextView subject = (TextView) view.findViewById(R.id.tv_subject);
        TextView to = (TextView) view.findViewById(R.id.tv_to);

        subject.setText(mc.getSubject());
        to.setText(mc.getTo());

        return view;
    }

}
