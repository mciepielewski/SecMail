package com.example.sec.sec;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class FAQactivity extends AppCompatActivity{

    TextView a1,a2,a3,a4,a5;
    TextView r1,r2,r3,r4,r5;
    int black = Color.parseColor("#000000");
    int white = Color.parseColor("#ffffff");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        r1 = (TextView)findViewById(R.id.r1);
        r2 = (TextView)findViewById(R.id.r2);
        r3 = (TextView)findViewById(R.id.r3);
        r4 = (TextView)findViewById(R.id.r4);
        r5 = (TextView)findViewById(R.id.r5);

        r1.setVisibility(View.GONE);
        r2.setVisibility(View.GONE);
        r3.setVisibility(View.GONE);
        r4.setVisibility(View.GONE);
        r5.setVisibility(View.GONE);

        a1 = (TextView)findViewById(R.id.a1);
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r1.getVisibility() == View.GONE) {
                    r1.setVisibility(View.VISIBLE);
                    a1.setTextColor(black);
                }
                else {
                    r1.setVisibility(View.GONE);
                    a1.setTextColor(white);
                }
            }
        });

        a2 = (TextView)findViewById(R.id.a2);
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r2.getVisibility() == View.GONE) {
                    r2.setVisibility(View.VISIBLE);
                    a2.setTextColor(black);
                }
                else {
                    r2.setVisibility(View.GONE);
                    a2.setTextColor(white);
                }
            }
        });

        a3 = (TextView)findViewById(R.id.a3);
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r3.getVisibility() == View.GONE) {
                    r3.setVisibility(View.VISIBLE);
                    a3.setTextColor(black);
                }
                else {
                    r3.setVisibility(View.GONE);
                    a3.setTextColor(white);
                }
            }
        });

        a4 = (TextView)findViewById(R.id.a4);
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r4.getVisibility() == View.GONE) {
                    r4.setVisibility(View.VISIBLE);
                    a4.setTextColor(black);
                }
                else {
                    r4.setVisibility(View.GONE);
                    a4.setTextColor(white);
                }
            }
        });

        a5 = (TextView)findViewById(R.id.a5);
        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r5.getVisibility() == View.GONE) {
                    r5.setVisibility(View.VISIBLE);
                    a5.setTextColor(black);
                }
                else {
                    r5.setVisibility(View.GONE);
                    a5.setTextColor(white);
                }
            }
        });
    }

}
