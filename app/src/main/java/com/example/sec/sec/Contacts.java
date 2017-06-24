// Main.java
package com.example.sec.sec;

import java.util.List;

import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Contacts extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ContactHandler handler;
    private List<Contact> contacts;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handler = new ContactHandler(getApplicationContext());
        Button btn_add_new = (Button) findViewById(R.id.btn_add_contact);
        btn_add_new.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Contacts.this, NewContact.class);
                startActivity(intent);
            }
        });

        lv = (ListView) findViewById(R.id.lv_contact_list);

        loadContactData();

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(Contacts.this, ContactDetails.class);
                intent.putExtra("id", contacts.get(position).getID());
                intent.putExtra("name", contacts.get(position).getName());
                intent.putExtra("lastname", contacts.get(position).getLastname());
                intent.putExtra("email", contacts.get(position).getEmail());
                intent.putExtra("public_key", contacts.get(position).getPublicKey());
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

    /*public boolean CzyImport(){
        if(bImportYesNo.isEnabled())
            return true;
        return false;

    }
*/
    private void loadContactData(){
        contacts = handler.readAllContacts();
        CustomAdapter adapter = new CustomAdapter(this, contacts);
        lv.setAdapter(adapter);
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
            Intent intent = new Intent(Contacts.this, MessagesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_contacts) {

        } else if (id == R.id.nav_keys) {
            Intent intent = new Intent(Contacts.this, ListKeys.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_client) {
            Intent intent = new Intent(Contacts.this, ClientMailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
