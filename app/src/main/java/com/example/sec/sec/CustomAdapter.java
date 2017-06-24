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

public class CustomAdapter extends BaseAdapter {
	
	private List<Contact> items;
	private Context context;
	private LayoutInflater inflater;
	
	public CustomAdapter(Context _context, List<Contact> _items){
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
		Contact contact = items.get(position);
		
		View view = convertView;
		
		if(view == null)
			view = inflater.inflate(R.layout.contact_item, null);
		
		
		TextView name = (TextView) view.findViewById(R.id.tv_full_name);
		TextView lastname = (TextView) view.findViewById(R.id.tv_lastname);
		
		name.setText(contact.getName());
		lastname.setText(contact.getLastname());
		
		return view;
	}

}
