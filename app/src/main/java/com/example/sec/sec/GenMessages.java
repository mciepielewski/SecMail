package com.example.sec.sec;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import org.spongycastle.openpgp.PGPException;
import org.spongycastle.openpgp.PGPKeyRingGenerator;
import org.spongycastle.openpgp.PGPPublicKey;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Kamil on 03.01.2017.
 */
public class GenMessages extends AppCompatActivity {
    EditText mEdit;
    String key=null;
    CircularProgressButton generate;
    private String encodeText;
    String defaultTextMessage = " Wpisz wiadomość...";
    String defaultSubject = " Temat";
    String defaultText = " Wybierz kontakt";
    private EditText filterText;
    private ArrayAdapter<String> listAdapter;
    RelativeLayout items;
    ListView itemList;
    ContactHandler db;
    String text;
    String subject;
    String fText;
    MyAsyncTask myAsyncTask;
    Bundle intent;
    EditText mSubject;
    private Mail mail;
    private Button sendMsg;
    MailClientHandler dbMail;
    String toMail;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_mess_activity);

        generate = (CircularProgressButton)findViewById(R.id.btnWithText);
        mEdit = (EditText) findViewById(R.id.fieldMessage);
        mEdit.setText(defaultTextMessage);

        mSubject = (EditText) findViewById(R.id.fieldSubject);
        mSubject.setText(defaultSubject);

        items = (RelativeLayout)findViewById(R.id.items);
        filterText = (EditText)findViewById(R.id.editText);
        itemList = (ListView)findViewById(R.id.listView);

        intent = getIntent().getExtras();
        if(intent != null)
            filterText.setText(intent.getString("fullName"));
        else
            filterText.setText(defaultText);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        dbMail= new MailClientHandler(getApplicationContext());
        config = dbMail.getConfig();
        mail = new Mail(config.getEmail(), config.getPass(), config.getImap());
        sendMsg = (Button)findViewById(R.id.sendMessage);

        items.setVisibility(View.INVISIBLE);
        db = new ContactHandler(getApplicationContext());
        myAsyncTask = new MyAsyncTask();

        List<Contact> contacts = db.readAllContacts();
        String [] labels = new String[contacts.size()];

        for(int i=0; i<contacts.size(); i++){
            Contact contact = contacts.get(i);
            labels[i] = contact.getLastname() + " " + contact.getName();
        }

        Arrays.sort(labels);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, labels);

        itemList.setAdapter(listAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filterText.clearFocus();
                items.setVisibility(View.INVISIBLE);
                mEdit.setVisibility(View.VISIBLE);
                mSubject.setVisibility(View.VISIBLE);
                String label = parent.getItemAtPosition(position).toString();
                String data[];
                data = label.split(" ");

                Contact contact = db.getContact(data[0],data[1]);
                key = contact.getPublicKey();
                toMail = contact.getEmail();
                filterText.setText(label);
            }
        });

        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                GenMessages.this.listAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        filterText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                items.setVisibility(View.VISIBLE);
                mEdit.setVisibility(View.INVISIBLE);
                mSubject.setVisibility(View.INVISIBLE);

                if (filterText.getText().toString().equals(defaultText)) {
                    filterText.setText("");
                }
                return false;
            }
        });

        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    filterText.setText(defaultText);
                }else {
                    filterText.setText("");
                }
            }
        };

        filterText.setOnFocusChangeListener(listener);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intent != null)
                    key = intent.getString("key");
                text = mEdit.getText().toString();
                subject = mSubject.getText().toString();
                fText = filterText.getText().toString();

                //validation starts here

                if(text.equals(defaultTextMessage) || text.equals("") || subject.equals(defaultSubject) || subject.equals("") || fText.equals(defaultText)){
                    Context context = getApplicationContext();
                    CharSequence text = "Wszystkie pola muszą być wypełnione";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }else
                    myAsyncTask.execute();

                //validation ends here


            }
        });

        mEdit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mEdit.getText().toString().equals(defaultTextMessage)) {
                    mEdit.setText("");
                }
                return false;
            }
        });

        mSubject.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mSubject.getText().toString().equals(defaultSubject)) {
                    mSubject.setText("");
                }
                return false;
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                text = mEdit.getText().toString();
                subject = mSubject.getText().toString();
                fText = filterText.getText().toString();

                if(encodeText == null)
                    encodeText = text;

                if(text.equals(defaultTextMessage) || text.equals("") || subject.equals(defaultSubject) || subject.equals("") || fText.equals(defaultText)){
                    Context context = getApplicationContext();
                    CharSequence text = "Wszystkie pola muszą być wypełnione";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
                //validation ends here

                String[] toArr = {toMail}; // This is an array, you can add more emails, just separate them with a coma
                mail.setTo(toArr); // load array to setTo function
                mail.setFrom(config.getEmail()); // who is sending the email
                mail.setSubject(mSubject.getText().toString()+" #secmail#");
                mail.setBody(encodeText);
                MailContent mc = new MailContent();
                mc.setSubject(mSubject.getText().toString()+" #secmail#");
                mc.setBody(encodeText);
                mc.setFrom(config.getEmail());
                mc.setTo(toMail);
                mc.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                //Toast.makeText(CreateMsgActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                try {
                    // m.addAttachment("/sdcard/myPicture.jpg");  // path to file you want to attach
                    if(mail.send()) {
                        // success
                        dbMail.saveSent(mc);
                        dbMail.close();
                        Toast.makeText(GenMessages.this, "Wysłano pomyślnie", Toast.LENGTH_LONG).show();
                    } else {
                        // failure
                        Toast.makeText(GenMessages.this, "Nie udało się wysłać", Toast.LENGTH_LONG).show();
                    }
                } catch(Exception e) {
                    // some other problem
                    Toast.makeText(GenMessages.this, "Błąd z konfiguracją. Czy internet na pewno włączony?", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            generate.setIndeterminateProgressMode(true);
            generate.setProgress(50);
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (key != null) {
                encodeText = PgpUtils.encryptMessage(text, key);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void a) {
            generate.setProgress(100);
            generate.setCompleteText("Skopiuj wiadomość");

            generate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", encodeText);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(GenMessages.this, "Wiadomość została skopiowana do schowka", Toast.LENGTH_LONG).show();
                }
            });

            mEdit.setText(encodeText);
        }
    }
}
