package com.example.sec.sec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 10.04.2017.
 */
public class MailClientHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MailClient";
    private static final int DATABASE_VERSION = 1;

    // --------------- konfiguracja konta pocztowego ----------------- //

    private static final String TABLE_CONFIGURATION = "configuration";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SMTP = "smtp";
    private static final String COLUMN_IMAP = "imap";
    private static final String COLUMN_SSL = "ssl";
    private static final String COLUMN_TLS = "tls";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASS = "pass";

    private String[] columnsConfig= {COLUMN_ID, COLUMN_SMTP, COLUMN_IMAP, COLUMN_SSL, COLUMN_TLS, COLUMN_NAME, COLUMN_EMAIL, COLUMN_PASS};

    // --------------- Ostatnia aktualizacja ------------------------ //
    private static final String TABLE_LASTUPDATE = "lastupdate";
    private static final String COLUMN_DATETIME = "lastupdate";

    private String[] columnsLastupdate= {COLUMN_ID, COLUMN_DATETIME};

    // --------------- wysłane wiadomości ------------------- //

    private static final String TABLE_SENT = "sent";
    private static final String COLUMN_SENT_SENDER = "sender";
    private static final String COLUMN_SENT_RECIPIENT = "recipient";
    private static final String COLUMN_SENT_MESSAGE = "message";
    private static final String COLUMN_SENT_SUBJECT = "subject";
    private static final String COLUMN_SENT_DATE = "date";

    private String[] columnsSent = {COLUMN_ID, COLUMN_SENT_SUBJECT ,COLUMN_SENT_MESSAGE, COLUMN_SENT_SENDER, COLUMN_SENT_RECIPIENT,COLUMN_SENT_DATE};

    // --------------- odebrane wiadomości ------------------- //

    private static final String TABLE_RECEIVED = "received";
    private static final String COLUMN_RECEIVED_MESSAGE = "received_message";
    private static final String COLUMN_RECEIVED_SUBJECT = "subject";
    private static final String COLUMN_RECEIVED_SENDER = "sender";
    private static final String COLUMN_READOUT = "readout";
    private static final String COLUMN_DATE = "date";

    private String[] columnsReceived = {COLUMN_ID, COLUMN_RECEIVED_SUBJECT,COLUMN_RECEIVED_MESSAGE,COLUMN_RECEIVED_SENDER,COLUMN_READOUT,COLUMN_DATE};

    public MailClientHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONFIGURATION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_SMTP + " TEXT,"
                + COLUMN_IMAP + " TEXT,"
                + COLUMN_SSL + " TEXT,"
                + COLUMN_TLS + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_LASTUPDATE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_DATETIME + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_SENT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_SENT_SUBJECT + " TEXT,"
                + COLUMN_SENT_MESSAGE + " TEXT,"
                + COLUMN_SENT_SENDER + " TEXT,"
                + COLUMN_SENT_RECIPIENT + " TEXT,"
                + COLUMN_SENT_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_RECEIVED + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_RECEIVED_SUBJECT + " TEXT,"
                + COLUMN_RECEIVED_MESSAGE + " TEXT,"
                + COLUMN_RECEIVED_SENDER + " TEXT,"
                + COLUMN_READOUT + " TEXT,"
                + COLUMN_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIGURATION);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENT);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEIVED);
        onCreate(db);
    }

    public void saveConfig(String name, String smtp, String imap, String email, String password, String SSL, String TLS){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues vals = new ContentValues();
        vals.put(COLUMN_NAME, name);
        vals.put(COLUMN_SMTP, smtp);
        vals.put(COLUMN_IMAP, imap);
        vals.put(COLUMN_EMAIL, email);
        vals.put(COLUMN_PASS, password);
        vals.put(COLUMN_SSL, SSL);
        vals.put(COLUMN_TLS, TLS);

        db.insert(TABLE_CONFIGURATION, null, vals);
        db.close();
    }


    public void editConfiguration(String name, String smtp, String imap, String email, String password, String SSL, String TLS){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues vals = new ContentValues();

        vals.put(COLUMN_NAME, name);
        vals.put(COLUMN_SMTP, smtp);
        vals.put(COLUMN_IMAP, imap);
        vals.put(COLUMN_EMAIL, email);
        vals.put(COLUMN_PASS, password);
        vals.put(COLUMN_SSL, SSL);
        vals.put(COLUMN_TLS, TLS);

        int i = db.update(TABLE_CONFIGURATION, vals, COLUMN_ID + " = ?",  new String[] { String.valueOf(1) });

        db.close();
    }

    public Config getConfig(){
        SQLiteDatabase db = this.getWritableDatabase();
        Config config = new Config();
        Cursor cursor = db.query(TABLE_CONFIGURATION, columnsConfig, null, null, null, null, null);

        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM configuration", null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }

        cur.close();


        if(!empty) {
            cursor = db.query(TABLE_CONFIGURATION, columnsConfig, null, null, null, null, null);
            cursor.moveToFirst();
            config.setId(cursor.getInt(0));
            config.setSmtp(cursor.getString(1));
            config.setImap(cursor.getString(2));
            config.setSsl(cursor.getString(3));
            config.setTls(cursor.getString(4));
            config.setName(cursor.getString(5));
            config.setEmail(cursor.getString(6));
            config.setPass(cursor.getString(7));
            cursor.close();
        }

        return config;
    }

    public void saveLastupdate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_DATETIME, date);
        db.insert(TABLE_LASTUPDATE, null, vals);
        db.close();
    }

    public void editLastupdate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_DATETIME, date);
        db.update(TABLE_LASTUPDATE, vals, COLUMN_ID + " = ?",  new String[] { String.valueOf(1) });
        db.close();
    }

    public String getLastupdate(){
        String date = "empty";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_LASTUPDATE, columnsLastupdate, null, null, null, null, null);
        Boolean is = cursor.moveToFirst();
        if(!is)
            return date;
        date = cursor.getString(1);
        cursor.moveToNext();
        cursor.close();
        return date;
    }

    public void saveSent(MailContent mail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_SENT_SUBJECT, mail.getSubject());
        vals.put(COLUMN_SENT_MESSAGE, mail.getBody());
        vals.put(COLUMN_SENT_SENDER, mail.getFrom());
        vals.put(COLUMN_SENT_RECIPIENT, mail.getTo().toString());
        vals.put(COLUMN_SENT_DATE, mail.getDate());

        db.insert(TABLE_SENT, null, vals);
        db.close();
    }

    public List<MailContent> getMails(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<MailContent> mails = new ArrayList<MailContent>();

        Cursor cursor = db.query(TABLE_SENT, columnsSent, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            MailContent mail = new MailContent();
            mail.setId(cursor.getInt(0));
            mail.setSubject(cursor.getString(1));
            mail.setBody(cursor.getString(2));
            mail.setFrom(cursor.getString(3));
            mail.setTo(cursor.getString(4));
            mail.setDate(cursor.getString(5));
            mails.add(mail);
            cursor.moveToNext();
        }

        // Make sure to close the cursor
        cursor.close();
        //Collections.sort(mails);
        return mails;
    }

    public void saveReceived(String subject, String sender, String message, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues vals = new ContentValues();
        vals.put(COLUMN_RECEIVED_SUBJECT, subject);
        vals.put(COLUMN_RECEIVED_MESSAGE, message);
        vals.put(COLUMN_RECEIVED_SENDER, sender);
        vals.put(COLUMN_READOUT, "false");
        vals.put(COLUMN_DATE, date);

        db.insert(TABLE_RECEIVED, null, vals);
        db.close();
    }

    public List<MailContent> getReceivedMails(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<MailContent> mails = new ArrayList<MailContent>();

        Cursor cursor = db.query(TABLE_RECEIVED, columnsReceived, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            MailContent mail = new MailContent();
            mail.setId(cursor.getInt(0));
            mail.setSubject(cursor.getString(1));
            mail.setBody(cursor.getString(2));
            mail.setFrom(cursor.getString(3));
            mail.setDate(cursor.getString(5));
            mails.add(mail);
            cursor.moveToNext();
        }

        // Make sure to close the cursor
        cursor.close();
        //Collections.sort(mails);
        return mails;
    }

    public boolean delRecMessage(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABLE_RECEIVED, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        db.close();

        if(i != 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean delSentMessage(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABLE_SENT, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        db.close();

        if(i != 0){
            return true;
        }else{
            return false;
        }
    }


}
