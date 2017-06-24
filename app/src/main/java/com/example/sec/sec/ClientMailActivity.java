package com.example.sec.sec;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


/**
 * Created by Krzysztow on 12.04.2017.
 */

public class ClientMailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{



    private RelativeLayout createMsg;
    private RelativeLayout sendMsg;
    private RelativeLayout receiveMsg;
    private RelativeLayout config;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_mail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createMsg = (RelativeLayout) findViewById(R.id.crateM);
        sendMsg = (RelativeLayout) findViewById(R.id.sendM);
        receiveMsg = (RelativeLayout) findViewById(R.id.receivedM);
        config = (RelativeLayout) findViewById(R.id.conf);

        createMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GenMessages.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(intent);
                }catch(Exception e){
                }
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SentMsgActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(intent);
                }catch(Exception e){
                }
            }
        });

        receiveMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReceivedMsgActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(intent);
                }catch(Exception e){
                }
            }
        });

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfClientMailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(intent);
                }catch(Exception e){
                }
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(0,0);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_messages) {
            Intent intent = new Intent(ClientMailActivity.this, MessagesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_contacts) {
            Intent intent = new Intent(ClientMailActivity.this, Contacts.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_keys) {
            Intent intent = new Intent(ClientMailActivity.this, ListKeys.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_client) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
