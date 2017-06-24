package com.example.sec.sec;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by dzemik on 24.04.2017.
 */

public class NewConfig extends AppCompatActivity {

    private MailClientHandler handler;
    private String name;
    private String smtp;
    private String imap;
    private String email;
    private String password;
    private String TLS = "no";
    private String SSL = "no";
    private Button btnSave;

    boolean IsEmpty(String s, EditText e) {
        if (s.equals("")) {
            e.setHintTextColor(Color.RED);
            return true;
        } else
            return false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_config);
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

                //validation starts here
                boolean empty_name=IsEmpty(name,et_name);
                boolean empty_smtp=IsEmpty(smtp,et_smtp);
                boolean empty_imap=IsEmpty(smtp,et_imap);
                boolean empty_email=IsEmpty(email,et_email);
                boolean empty_password=IsEmpty(password,et_pass);

                if(empty_name==true || empty_smtp ==true || empty_imap ==true || empty_email==true || empty_password==true)
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

                Config config = new Config();
                config = handler.getConfig();
                if(config.getName()!="" || config.getEmail()!=""){
                    handler.editConfiguration(name,smtp,imap,email,password,SSL,TLS);
                }
                else
                    handler.saveConfig(name,smtp,imap,email,password,SSL,TLS);
            }
        });
    }
}
