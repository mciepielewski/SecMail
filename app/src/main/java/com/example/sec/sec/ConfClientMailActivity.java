package com.example.sec.sec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;


/**
 * Created by Krzysztow on 12.04.2017.
 */

public class ConfClientMailActivity extends AppCompatActivity {


    private RelativeLayout actualConfig;
    private RelativeLayout newConfig;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_mail_layout);

        actualConfig = (RelativeLayout) findViewById(R.id.button);
        actualConfig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ConfClientMailActivity.this, NewConfig.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(intent);
                }catch(Exception e){
                }

            }
        });

        newConfig = (RelativeLayout) findViewById(R.id.button2);
        newConfig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ConfClientMailActivity.this, ActualConfig.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(intent);
                }catch(Exception e){
                }

            }
        });

    }
}
