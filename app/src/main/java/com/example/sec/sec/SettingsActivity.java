package com.example.sec.sec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class SettingsActivity extends AppCompatActivity{
    RelativeLayout bPIN;
    RelativeLayout bzPIN;
    RelativeLayout bKLUCZ;
    RelativeLayout bODINSTALUJ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bPIN = (RelativeLayout)findViewById(R.id.Pin);
        bzPIN = (RelativeLayout)findViewById(R.id.zPin);
        bKLUCZ = (RelativeLayout)findViewById(R.id.klucz);
        bODINSTALUJ = (RelativeLayout)findViewById(R.id.uninstall);

        bPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goPIN = new Intent(getApplicationContext(), PIN.class);
                goPIN.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(goPIN);
                }catch(Exception e){
                }
            }
        });

        bzPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goChangePIN = new Intent(getApplicationContext(), changePIN.class);
                goChangePIN.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(goChangePIN);
                }catch(Exception e){
                }
            }
        });

        bKLUCZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goKlucz = new Intent(getApplicationContext(), LengthKey.class);
                goKlucz.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(goKlucz);
                }catch(Exception e){
                }
            }
        });

        bODINSTALUJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goUninstall = new Intent(getApplicationContext(), UninstallApplication.class);
                goUninstall.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(goUninstall);
                }catch(Exception e){
                }
            }
        });
    }
}
