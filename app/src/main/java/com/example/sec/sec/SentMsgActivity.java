package com.example.sec.sec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by Krzysztow on 12.04.2017.
 */

public class SentMsgActivity extends AppCompatActivity {

    private MailClientHandler handler;
    private List<MailContent> mails;
    private ListView lv;
    SentAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_msg);

        handler = new MailClientHandler(getApplicationContext());
        lv = (ListView) findViewById(R.id.lv_mails_list);

        loadMails();
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(SentMsgActivity.this, MailDetails.class);
                intent.putExtra("subject", mails.get(position).getSubject());
                intent.putExtra("body", mails.get(position).getBody());
                intent.putExtra("from", mails.get(position).getFrom());
                intent.putExtra("to", mails.get(position).getTo());

                try{
                    Thread.sleep(200);
                    startActivity(intent);
                }catch(Exception e){
                }
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
            if(handler.delSentMessage(m.getId()))
                Toast.makeText(getApplicationContext(), "Wiadomość usunięta", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Nie udało się usunąć wiadomośći", Toast.LENGTH_LONG).show();
        return true;
    }

    private void loadMails(){
        mails = handler.getMails();
        Collections.sort(mails);
        adapter = new SentAdapter(this, mails);
        lv.setAdapter(adapter);
    }
}
