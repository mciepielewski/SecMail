// ContactHandler
package com.example.sec.sec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ContactHandler extends SQLiteOpenHelper {
	
	// All variables about DB
	// Database name
	private static final String DATABASE_NAME = "contactBook";
	
	// Database version
	private static final int DATABASE_VERSION = 1;
	
	// Contacts table name
	private static final String TABLE_CONTACT = "contacts";
	
	// Table Column names
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_LASTNAME = "lastname";
	private static final String COLUMN_EMAIL = "email";
	private static final String COLUMN_PUBLICKEY = "public_key";
	
	private String[] columns= {COLUMN_ID, COLUMN_NAME, COLUMN_LASTNAME, COLUMN_EMAIL, COLUMN_PUBLICKEY};
	
	// Create database
	public ContactHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);		
	}

	
	// Create table
	@Override
	public void onCreate(SQLiteDatabase db) {		
		String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," 
				+ COLUMN_NAME + " TEXT,"
                + COLUMN_LASTNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PUBLICKEY + " TEXT"
				+ ")";
        db.execSQL(CREATE_TABLE);
	}
	
	// Drop table if older version exist
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
	    onCreate(db);
	}
	
	/*
	 * Handling Contact table using sql queries.
	 * */
	
	
	// Add Contact
	public boolean addContactDetails(Contact contact){
		// Get db writable
		SQLiteDatabase db = this.getWritableDatabase();
		
		// Get the values to insert
		ContentValues vals = new ContentValues();
		vals.put(COLUMN_NAME, contact.getName());
		vals.put(COLUMN_LASTNAME, contact.getLastname());
		vals.put(COLUMN_EMAIL, contact.getEmail());
		vals.put(COLUMN_PUBLICKEY, contact.getPublicKey());
		
		// Insert values into table
		long i = db.insert(TABLE_CONTACT, null, vals);
		// Close database
		db.close();
		
		 if(i != 0){
	        	return true;
	        }else{
	        	return false;
	        }
	}
	
	
	// Reading all contacts
	public List<Contact> readAllContacts(){
		// Get db writable
		SQLiteDatabase db = this.getWritableDatabase();
		
		// Define contacts list
		List<Contact> contacts = new ArrayList<Contact>();
		
		 Cursor cursor = db.query(TABLE_CONTACT, columns, null, null, null, null, null);

			    cursor.moveToFirst();
			    
			    while (!cursor.isAfterLast()) {
			    	Contact contact = new Contact();
			    	contact.setID(Integer.parseInt(cursor.getString(0)));
			    	contact.setName(cursor.getString(1));
			    	contact.setLastname(cursor.getString(2));
			    	contact.setEmail(cursor.getString(3));
			    	contact.setPublicKey(cursor.getString(4));
			    	contacts.add(contact);
			    	cursor.moveToNext();
			    }
			    
			    // Make sure to close the cursor
			    cursor.close();
				Collections.sort(contacts);
			    return contacts;
	}
	
	// Update contact contact
	public boolean editContact(Contact contact){
		SQLiteDatabase db = this.getWritableDatabase();
		 
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_NAME, contact.getName());
        vals.put(COLUMN_LASTNAME, contact.getLastname());

        int i = db.update(TABLE_CONTACT, vals, COLUMN_ID + " = ?",  new String[] { String.valueOf(contact.getID()) });
        
        db.close();
        
        if(i != 0){
        	return true;
        }else{
        	return false;
        }
		
	}

	public boolean deleteContact(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		
        int i = db.delete(TABLE_CONTACT, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        
        db.close();
        
        if(i != 0){
        	return true;
        }else{
        	return false;
        }
	}

	public Contact getContact(String lastname, String name){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM contacts WHERE name='" + name + "' AND lastname='" + lastname + "'", null);
		Contact contact = new Contact();

		if(cursor!=null){
			cursor.moveToFirst();
			contact.setEmail(cursor.getString(3));
			contact.setPublicKey(cursor.getString(4));
			contact.setName(cursor.getString(1));
			contact.setLastname(cursor.getString(2));
		}

		return contact;
	}

	public int getID(String email) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCursor = db.rawQuery(
				"SELECT id  FROM  contacts WHERE email='"+email+"'" , null);
		mCursor.moveToFirst();
		return mCursor.getInt(0);
	}

}
