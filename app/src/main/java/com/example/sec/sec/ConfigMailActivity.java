package com.example.sec.sec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Kamil on 10.04.2017.
 */
public class ConfigMailActivity extends AppCompatActivity {

    private MailClientHandler handler;
    private String name;
    private String smtp;
    private String imap;
    private String email;
    private String password;
    private String TLS = "no";
    private String SSL = "no";
    private Button btnSave;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_mail_layout);

        handler = new MailClientHandler(getApplicationContext());


        btnSave = (Button)findViewById(R.id.button);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                EditText et_name = (EditText) findViewById(R.id.editText3);
                name = et_name.getText().toString();
                EditText et_smtp = (EditText) findViewById(R.id.editText2);
                smtp = et_smtp.getText().toString();
                EditText et_imap = (EditText) findViewById(R.id.imap);
                imap = et_imap.getText().toString();
                EditText et_email = (EditText) findViewById(R.id.editText4);
                email = et_email.getText().toString();
                EditText et_pass = (EditText) findViewById(R.id.editText5);
                password = et_pass.getText().toString();
                CheckBox ch_ssl = (CheckBox)findViewById(R.id.checkBox);
                CheckBox ch_tls = (CheckBox)findViewById(R.id.checkBox2);

                if(ch_ssl.isChecked())
                    SSL = "yes";
                if(ch_tls.isChecked())
                    TLS = "yes";

                handler.saveConfig(name,smtp,imap,email,password,SSL,TLS);
            }
        });
    }
}
