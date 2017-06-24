package com.example.sec.sec;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kamil on 2017-04-20.
 */

public class MailDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_details);

        final Bundle extras = getIntent().getExtras();

        TextView tv_subject = (TextView) findViewById(R.id.tv_subject);
        tv_subject.setText(extras.getString("subject"));

        TextView tv_body = (TextView) findViewById(R.id.tv_body);
        tv_body.setText(extras.getString("body"));

        TextView tv_from = (TextView) findViewById(R.id.tv_from);
        tv_from.setText(extras.getString("from"));

        TextView tv_to = (TextView) findViewById(R.id.tv_to);
        tv_to.setText(extras.getString("to"));

        TextView tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(extras.getString("date"));

        Button btn_back = (Button) findViewById(R.id.btn_back_to_cMail);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
                Intent intent = new Intent(MailDetails.this, SentMsgActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        Button btn_copy = (Button) findViewById(R.id.copyMsg);
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", extras.getString("body"));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MailDetails.this, "Wiadomość została skopiowana do schowka", Toast.LENGTH_LONG).show();
            }
        });
    }
}
