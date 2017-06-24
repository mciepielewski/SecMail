package com.example.sec.sec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by Krzysztow on 12.04.2017.
 */

public class ReceivedMsgActivity extends Activity{

    private MailClientHandler handler;
    private List<MailContent> mails;
    private ListView lv;
    SentAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_msg);

        handler = new MailClientHandler(getApplicationContext());
        lv = (ListView) findViewById(R.id.lv_mails_list);

        loadMails();
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(ReceivedMsgActivity.this, MailDetails.class);
                intent.putExtra("subject", mails.get(position).getSubject());
                intent.putExtra("body", mails.get(position).getBody());
                intent.putExtra("from", mails.get(position).getFrom());
                intent.putExtra("to", mails.get(position).getTo());
                intent.putExtra("date", mails.get(position).getDate());

                try{
                    Thread.sleep(200);
                    startActivity(intent);
                }catch(Exception e){
                }
            }
        });

        Button b2=(Button)findViewById(R.id.odbieranieIDButton);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config config = handler.getConfig();
                OdbieranieTest o =  new OdbieranieTest(config.getEmail(),config.getPass(),config.getImap());
                List<MailContent> mails = o.getMails();

                MailClientHandler handler = new MailClientHandler(ReceivedMsgActivity.this);
                String lastDate = handler.getLastupdate();
                int added = 0;
                for(int i=0; i<mails.size(); i++){
                    if(!lastDate.equals("empty")) {
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date lDate=null;
                        Date aDate=null;

                        try {
                            lDate = format.parse(lastDate);
                            aDate = format.parse(mails.get(i).getDate());
                        }catch (ParseException e){

                        }
                        if (lDate != null && aDate != null && lDate.compareTo(aDate) >= 0)
                            continue;
                        else if(lDate != null && aDate != null && lDate.compareTo(aDate) < 0) {
                            handler.editLastupdate(mails.get(i).getDate());
                            lastDate = mails.get(i).getDate();
                        }

                    }
                    else {
                        handler.saveLastupdate(mails.get(i).getDate());
                        lastDate = mails.get(i).getDate();
                    }
                    added++;
                    handler.saveReceived(mails.get(i).getSubject(),mails.get(i).getFrom(),mails.get(i).getBody(),mails.get(i).getDate());
                }

                if(added > 0)
                    Toast.makeText(ReceivedMsgActivity.this,"Pobrano "+added+ " nowych wiadomości z Twojej poczty!",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ReceivedMsgActivity.this,"Brak nowych wiadomości",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        menu.add(0, v.getId(), 0, "Usuń wiadomość");
    }

    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        MailContent m = (MailContent) adapter.getItem(info.position);

        if(item.getTitle().equals("Usuń wiadomość"))
            if(handler.delRecMessage(m.getId()))
                Toast.makeText(getApplicationContext(), "Wiadomość usunięta", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Nie udało się usunąć wiadomośći", Toast.LENGTH_LONG).show();
        return true;
    }

    private void loadMails(){
        mails = handler.getReceivedMails();
        Collections.sort(mails);
        adapter = new SentAdapter(this, mails);
        lv.setAdapter(adapter);
    }
}