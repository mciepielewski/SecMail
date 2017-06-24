package com.example.sec.sec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NewContact extends Activity {
	
	private ContactHandler handler;
	private String name;
	private String lastname;
	private String email;
	private String public_key;

	boolean IsEmpty(String s, EditText e) {
		if (s.equals("")) {
			e.setHintTextColor(Color.RED);
			return true;
		} else
			return false;
	}
	
	FileChooser fileChooser;
	String pathPublic;
	String namePublic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_contact);
		
		handler = new ContactHandler(getApplicationContext());
		final RadioGroup Radiogrupa = (RadioGroup) findViewById(R.id.RadioGrupa);

		RadioButton bZeSchowka = (RadioButton) findViewById(R.id.radioZeSchowka);

		bZeSchowka.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				EditText et_address = (EditText) findViewById(R.id.et_publickey);
				et_address.setEnabled(true);
				EditText publicKey = (EditText) findViewById(R.id.et_publickey);
				publicKey.setText("");
			}
		});
		RadioButton bZpliku = (RadioButton) findViewById(R.id.radioZpliku);



		bZpliku.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				EditText et_address = (EditText) findViewById(R.id.et_publickey);
				et_address.setEnabled(false);
						fileChooser = new FileChooser(NewContact.this);
						fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
							@Override
							public void fileSelected(File file) throws IOException {
								pathPublic = file.getParent();
								namePublic = file.getName();
								if(file.getName().endsWith(".txt") || file.getName().endsWith(".pgp")) {
									EditText publicKey = (EditText) findViewById(R.id.et_publickey);
									StringBuilder text = new StringBuilder();
									BufferedReader br = null;
									try {
										br = new BufferedReader(new FileReader(file));
										String line;
										while ((line = br.readLine()) != null) {
											text.append(line);
											Log.i("Test", "text : " + text + " : end");
											text.append('\n');
										}
									} catch (IOException e) {
										e.printStackTrace();

									} finally {
										br.close();
									}

									publicKey.setText(text.toString());
								}
								else{
									Toast.makeText(NewContact.this,"Niepoprawny format pliku klucza publicznego", Toast.LENGTH_LONG).show();
								}

							}
						}).showDialog();
					}
				});


		Button btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				EditText et_name = (EditText) findViewById(R.id.et_name);
				name = et_name.getText().toString();
				
				EditText et_phone = (EditText) findViewById(R.id.et_lastname);
				lastname = et_phone.getText().toString();
				
				EditText et_email = (EditText) findViewById(R.id.et_email);
				email = et_email.getText().toString();

				EditText et_address = (EditText) findViewById(R.id.et_publickey);
				public_key = et_address.getText().toString();

				//validation starts here
				boolean empty_name=IsEmpty(name,et_name);
				boolean empty_lastname=IsEmpty(lastname,et_phone);
				boolean empty_email=IsEmpty(email,et_email);
				boolean empty_key=IsEmpty(public_key,et_address);

				if(empty_name==true || empty_lastname==true || empty_email==true || empty_key==true)
				{
					Context context = getApplicationContext();
					CharSequence text = "Wszystkie pola muszą być wypełnione";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					return;
				}

				if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
				{
					Context context = getApplicationContext();
					CharSequence text = "Niepoprawny adres email";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					return;
				}
				//validation ends here

				Contact contact = new Contact();
				contact.setName(name);
				contact.setLastname(lastname);
				contact.setEmail(email);
				contact.setPublicKey(public_key);
				
				Boolean added = handler.addContactDetails(contact);
				if(added){
					Intent intent = new Intent(NewContact.this, Contacts.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "Kontakt nie został dodany. Spróbuj jeszcze raz", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
	}
	
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(imageUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            ImageView imageView = (ImageView) findViewById(R.id.iv_user_photo);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
         
        }
	}*/

}
