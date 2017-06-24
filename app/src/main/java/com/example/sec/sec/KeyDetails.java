package com.example.sec.sec;

/**
 * Created by Kamil on 18.01.2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class KeyDetails extends AppCompatActivity {
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_details);

        extras = getIntent().getExtras();

        TextView tv_email = (TextView) findViewById(R.id.tv_contact_email);
        tv_email.setText(extras.getString("email"));

        TextView tv_generated = (TextView) findViewById(R.id.tv_date_generated);
        tv_generated.setText(extras.getString("date_generation"));

        Button btn_back = (Button) findViewById(R.id.btn_back_to_contact);
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
                Intent intent = new Intent(KeyDetails.this, ListKeys.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        ImageButton btnCopyPublic = (ImageButton) findViewById(R.id.copyPublic);
        btnCopyPublic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", extras.getString("public_key"));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(KeyDetails.this, "Klucz publiczny został skopiowany", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton btnCopyPrivate = (ImageButton) findViewById(R.id.copyPrivate);
        btnCopyPrivate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", extras.getString("private_key"));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(KeyDetails.this, "Klucz prywatny został skopiowany", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void editKey(View view){
        Bundle extras = getIntent().getExtras();
        String email=extras.getString("email");
        Intent intent = new Intent(getApplicationContext(), ImportKey.class);
        intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }

    public void delKey(View view) {
        Database db = new Database(KeyDetails.this);
        String email=extras.getString("email");
        int id = db.getID(email);
        db.deleteKey(id);
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "Poprawnie usunięto", duration);
        toast.show();
        Intent ListKeys = new Intent(getApplicationContext(), ListKeys.class);
        ListKeys.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(ListKeys);
    }
}
