package com.example.sec.sec;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        RelativeLayout btnMessages;
        RelativeLayout btnKeys;
        RelativeLayout btnContacts;
        RelativeLayout btnEmailClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnMessages = (RelativeLayout)findViewById(R.id.messages);
        btnKeys = (RelativeLayout)findViewById(R.id.keys);
        btnContacts = (RelativeLayout)findViewById(R.id.contacts);
        btnEmailClient=(RelativeLayout)findViewById(R.id.email_client);

        btnMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMessages = new Intent(getApplicationContext(), MessagesActivity.class);
                goMessages.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(goMessages);
                }catch(Exception e){
                }
            }
        });

        btnKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goKeys = new Intent(getApplicationContext(), ListKeys.class);
                goKeys.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(goKeys);
                }catch(Exception e){
                }
            }
        });

        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goContacts = new Intent(getApplicationContext(), Contacts.class);
                goContacts.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(goContacts);
                }catch(Exception e){
                }
            }
        });

        btnEmailClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goEmailClient = new Intent(getApplicationContext(), ClientMailActivity.class);
                goEmailClient.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                try{
                    Thread.sleep(200);
                    startActivity(goEmailClient);
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setMessage("Autorzy aplikacji:\n\nKamil Czubaszek\nMichał Ciepielewski\nMateusz Dworak\nPaweł Grzeszczyk\nMarcin Chęć").show();
        }else if (id == R.id.action_faq){
            Intent intent = new Intent(this, FAQactivity.class);
            this.startActivity(intent);
        }


        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_messages) {
            Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_contacts) {
            Intent intent = new Intent(MainActivity.this, Contacts.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        } else if (id == R.id.nav_keys) {
            Intent intent = new Intent(MainActivity.this, ListKeys.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else if (id == R.id.nav_client) {
            Intent intent = new Intent(MainActivity.this, ClientMailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
