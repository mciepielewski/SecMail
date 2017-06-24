package com.example.sec.sec;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by dzemik on 24.04.2017.
 */

public class ActualConfig extends AppCompatActivity {
    private MailClientHandler handler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_config);

        handler = new MailClientHandler(getApplicationContext());

        Config config = new Config();
        config = handler.getConfig();
        if(config.getName()=="" || config.getEmail()==""){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Najpierw skonfiguruj klienta pocztowego!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ActualConfig.this, ConfClientMailActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {

            TextView et_name = (TextView) findViewById(R.id.editText2);
            TextView et_smtp = (TextView) findViewById(R.id.editText3);
            TextView et_imap = (TextView) findViewById(R.id.editTextImap);
            TextView et_email = (TextView) findViewById(R.id.editText4);
            TextView et_pass = (TextView) findViewById(R.id.editText5);
            CheckBox ch_ssl = (CheckBox) findViewById(R.id.checkBox);
            CheckBox ch_tls = (CheckBox) findViewById(R.id.checkBox2);

            et_name.setText(config.getName());
            et_smtp.setText(config.getSmtp());
            et_imap.setText(config.getImap());
            et_email.setText(config.getEmail());
            et_pass.setText(config.getPass());
            if (config.getSsl().equals("yes")) ch_ssl.setChecked(true);
                else ch_ssl.setChecked(false);

            if (config.getSsl() == "yes") ch_tls.setChecked(true);
                else ch_tls.setChecked(false);
        }

    }
}
