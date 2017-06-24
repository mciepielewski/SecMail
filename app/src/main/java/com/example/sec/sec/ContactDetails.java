package com.example.sec.sec;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetails extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.contact_details);	

		Bundle extras = getIntent().getExtras();
		
		TextView tv_name = (TextView) findViewById(R.id.tv_contact_name);
		tv_name.setText(extras.getString("name"));
		
		TextView tv_phone = (TextView) findViewById(R.id.tv_contact_lastname);
		tv_phone.setText(extras.getString("lastname"));
		
		TextView tv_email = (TextView) findViewById(R.id.tv_contact_email);
		tv_email.setText(extras.getString("email"));
		
		Button btn_back = (Button) findViewById(R.id.btn_back_to_contact);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				Intent intent = new Intent(ContactDetails.this, Contacts.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});
		ImageButton copyK = (ImageButton)findViewById(R.id.copyKey);
		copyK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle extras = getIntent().getExtras();
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("label", extras.getString("public_key"));
				clipboard.setPrimaryClip(clip);

			}
		});
	}

	public void delContact(View view) {
		ContactHandler db = new ContactHandler(ContactDetails.this);
		Bundle extras = getIntent().getExtras();
		String email=extras.getString("email");
		int id = db.getID(email);
		db.deleteContact(id);
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, "Poprawnie usunięto", duration);
		toast.show();
		Intent contacts = new Intent(getApplicationContext(), Contacts.class);
		startActivity(contacts);
	}

	public void goMessage(View view) {
		Bundle extras = getIntent().getExtras();
		String fullName,key;
		fullName = extras.getString("lastname")+ " "+extras.getString("name");
		key = extras.getString("public_key");
		Intent intent = new Intent(getApplicationContext(), GenMessages.class);
		intent.putExtra("fullName",fullName);
		intent.putExtra("key",key);
		startActivity(intent);
		finish();
	}
	
}
