package com.example.sec.sec;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kamil on 11.01.2017.
 */
public class decMessage extends AppCompatActivity{
    CircularProgressButton generate;
    EditText mEdit;
    EditText pEdit;
    String key=null;
    String defaultTextMess = " Wklej wiadomość...";
    String defaultTextPass = " Hasło";
    String defaultText = " Wybierz kontakt";
    String decodeText;
    private EditText filterText;
    private ArrayAdapter<String> listAdapter;
    RelativeLayout items;
    String text;
    String pass;
    String fText;
    MyAsyncTask myAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dec_mess_activity);

        mEdit   = (EditText)findViewById(R.id.fieldMessage);
        pEdit   = (EditText)findViewById(R.id.fieldPassword);
        mEdit.setText(defaultTextMess);
        pEdit.setText(defaultTextPass);

        generate = (CircularProgressButton) findViewById(R.id.btnWithText);

        items = (RelativeLayout)findViewById(R.id.items);
        filterText = (EditText)findViewById(R.id.editText);
        filterText.setText(defaultText);
        items.setVisibility(View.INVISIBLE);

        myAsyncTask = new MyAsyncTask();

        filterText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                items.setVisibility(View.VISIBLE);
                mEdit.setVisibility(View.INVISIBLE);

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

        ListView itemList = (ListView)findViewById(R.id.listView);

        Database db = new Database(getApplicationContext());
        List<String> lables = db.getAllLabels();
        String [] labels = new String[lables.size()];
        for(int i=0; i<lables.size(); i++)
            labels[i] = lables.get(i);
        Arrays.sort(labels);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, labels);

        itemList.setAdapter(listAdapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filterText.clearFocus();
                items.setVisibility(View.INVISIBLE);
                mEdit.setVisibility(View.VISIBLE);
                String label = parent.getItemAtPosition(position).toString();
                Database db = new Database(getApplicationContext());
                key = db.getPrivateKey(label);
                filterText.setText(label);
            }
        });

        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                decMessage.this.listAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        generate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                text = mEdit.getText().toString();
                pass = pEdit.getText().toString();
                fText = filterText.getText().toString();

                //validation starts here

                if(text.equals(defaultTextMess) || text.equals("") || pass.equals(defaultTextPass) || pass.equals("") || fText.equals(defaultText)){
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
                if (mEdit.getText().toString().equals(defaultTextMess)) {
                    mEdit.setText("");
                }
                return false;
            }
        });

        pEdit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (pEdit.getText().toString().equals(defaultTextPass)) {
                    pEdit.setText("");
                }
                return false;
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
                decodeText = PgpUtils.decrypt(text, key, pass);
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
                    ClipData clip = ClipData.newPlainText("label", decodeText);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(decMessage.this, "Wiadomość została skopiowana do schowka", Toast.LENGTH_LONG).show();
                }
            });

            mEdit.setText(decodeText);
        }
    }
}
