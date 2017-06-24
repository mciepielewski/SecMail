package com.example.sec.sec;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by Kamil on 2017-04-20.
 */

public class ImportKey extends AppCompatActivity {
    FileChooser fileChooser;
    String pathPublic;
    String pathPrivate;
    String namePublic;
    String namePrivate;
    String eEditName;
    EditText eEdit;
    EditText pubEdit;
    EditText privEdit;
    Boolean option=false;
    Boolean edit=false;
    Bundle intent;

    boolean IsEmptyField(String s) {
        if (s.equals("")) {
            return true;
        } else
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_key);

        final CheckBox file = (CheckBox)findViewById(R.id.checkBox3);
        final CheckBox cupboard = (CheckBox)findViewById(R.id.checkBox4);
        final Button publicKey = (Button) findViewById(R.id.choosePublic);
        final Button privateKey = (Button) findViewById(R.id.choosePrivate);
        final Button save = (Button) findViewById(R.id.saveBtn);
        final TextView tv1 = (TextView)findViewById(R.id.namePublic);
        final TextView tv2 = (TextView)findViewById(R.id.namePrivate);
        final TextView label1 = (TextView)findViewById(R.id.labelPublic);
        final TextView label2 = (TextView)findViewById(R.id.labelPrivate);
        TextView titleView = (TextView)findViewById(R.id.title);
        eEdit = (EditText)findViewById(R.id.fieldmail);
        pubEdit = (EditText)findViewById(R.id.pubEditText);
        privEdit = (EditText)findViewById(R.id.privEditText);

        intent = getIntent().getExtras();

        if(intent != null) {
            edit = true;
            eEdit.setText(intent.getString("email"));
            titleView.setText("Edytuj klucze");
        }

        file.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                option = true;
                cupboard.setVisibility(View.GONE);
                file.setVisibility(View.GONE);
                label1.setVisibility(View.VISIBLE);
                label2.setVisibility(View.VISIBLE);
                publicKey.setVisibility(View.VISIBLE);
                privateKey.setVisibility(View.VISIBLE);
            }
        });

        cupboard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                cupboard.setVisibility(View.GONE);
                file.setVisibility(View.GONE);
                pubEdit.setVisibility(View.VISIBLE);
                privEdit.setVisibility(View.VISIBLE);
            }
        });

        publicKey.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                fileChooser = new FileChooser(ImportKey.this);
                fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(File file) {
                        pathPublic = file.getParent();
                        namePublic = file.getName();

                        //Toast.makeText(ImportKey.this, pathPublic, Toast.LENGTH_SHORT).show();

                        tv1.setVisibility(View.VISIBLE);
                        tv1.setText(file.getName());
                    }
                }).showDialog();
            }
        });

        privateKey.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                fileChooser = new FileChooser(ImportKey.this);
                fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(File file) {
                        pathPrivate = file.getParent();
                        namePrivate = file.getName();
                        //Toast.makeText(ImportKey.this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        tv2.setVisibility(View.VISIBLE);
                        tv2.setText(file.getName());
                    }
                }).showDialog();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Data data = new Data();
                String pubKey;
                String privKey;
                if(option) {
                    pubKey = fileChooser.getContentFile(pathPublic, namePublic);
                    privKey = fileChooser.getContentFile(pathPrivate, namePrivate);
                }
                else{
                    pubKey = pubEdit.getText().toString();
                    privKey = privEdit.getText().toString();
                }

                //validation starts here
                boolean empty_public_key=IsEmptyField(pubKey);
                boolean empty_priv_key=IsEmptyField(privKey);
                eEditName = eEdit.getText().toString();

                if (empty_public_key==true || empty_priv_key==true)
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Obydwa pola kluczy muszą być wypełnione!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(eEditName).matches()) {
                    Context context = getApplicationContext();
                    CharSequence text = "Niepoprawny adres email";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
                //validation ends here

                data.setEmail(eEdit.getText().toString());
                data.setPrivateKey(privKey);
                data.setPublicKey(pubKey);

                Database db = new Database(ImportKey.this);
                if(!edit)
                    db.addKeys(data);
                else
                    db.updateKeys(data,db.getID(intent.getString("email")));

                Toast.makeText(ImportKey.this, "Klucze zostały zapisane w bazie danych", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ImportKey.this, ListKeys.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
