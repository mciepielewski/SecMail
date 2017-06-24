package com.example.sec.sec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kamil on 16.12.2016.
 */
public class Database extends SQLiteOpenHelper{
    public Database(Context context) {
        super(context, "keys.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table keys(" +
                        "id integer primary key autoincrement," +
                        "email text," +
                        "private_key text," +
                        "public_key text," +
                        "date_generation text);" +
                        "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void addKeys(Data data){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", data.getEmail());
        values.put("private_key", data.getPrivateKey());
        values.put("date_generation", data.getTime());
        values.put("public_key", data.getPublicKey());
        db.insertOrThrow("keys",null, values);
    }

    public List<Data> getAll(){
        List<Data> keys = new LinkedList<Data>();
        String[] columns={"email","private_key","public_key"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.query("keys",columns,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            Data data = new Data();
            data.setEmail(cursor.getString(0));
            data.setPrivateKey(cursor.getString(1));
            data.setPublicKey(cursor.getString(2));
            keys.add(data);
        }
        return keys;
    }

    public Data getKey(int id){
        Data data=new Data();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns={"id","email","private_key","public_key","date_generation"};
        String args[]={id+""};
        Cursor cursor=db.query("keys",columns,"id=?",args,null,null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
            data.setEmail(cursor.getString(1));
            data.setPrivateKey(cursor.getString(2));
            data.setPublicKey(cursor.getString(3));
        }
        return data;
    }

    public String getPublicKey(String email){
        SQLiteDatabase db = getReadableDatabase();
        String[] columns={"public_key"};
        String args[]={email+""};
        Cursor cursor=db.query("keys",columns,"email=?",args,null,null,null,null);
        String key=null;
        if(cursor!=null){
            cursor.moveToFirst();
            key = cursor.getString(0);
        }
        return key;
    }

    public String getPrivateKey(String email){
        SQLiteDatabase db = getReadableDatabase();
        String[] columns={"private_key"};
        String args[]={email+""};
        Cursor cursor=db.query("keys",columns,"email=?",args,null,null,null,null);
        String key=null;
        if(cursor!=null){
            cursor.moveToFirst();
            key = cursor.getString(0);
        }
        return key;
    }

    public void deleteKey(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] arguments={""+id};
        db.delete("keys", "id=?", arguments);
    }

    public int getID(String email) {
       SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery(
                "SELECT id  FROM  keys WHERE email='"+email+"'" , null);

        mCursor.moveToFirst();
        return mCursor.getInt(0);
    }

    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT email FROM keys";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public void updateKeys(Data data, int id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("email", data.getEmail());
        values.put("private_key", data.getPrivateKey());
        values.put("date_generation", data.getTime());
        values.put("public_key", data.getPublicKey());

        db.update("keys", values, "id" + " = ?",  new String[] { String.valueOf(id) });

        db.close();
    }


}
