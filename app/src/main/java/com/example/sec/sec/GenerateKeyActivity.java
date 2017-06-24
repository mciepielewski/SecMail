package com.example.sec.sec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import org.spongycastle.openpgp.PGPKeyRingGenerator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kamil on 14.12.2016.
 */
public class GenerateKeyActivity extends AppCompatActivity {
    EditText mEdit;
    EditText pEdit;
    EditText prEdit;
    String email;
    char []password;
    TextView title;
    CircularProgressButton generate;
    String pgpPublicKey = null;
    String pgpSecretKey = null;
    MyAsyncTask myAsyncTask;

    boolean CzyPuste(String s, EditText e) {
        if (s.equals("")) {
            e.setHintTextColor(Color.RED);
            return true;
        } else
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generatekeyactivity);
        myAsyncTask = new MyAsyncTask();
        generate = (CircularProgressButton) findViewById(R.id.btnWithText);

        generate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mEdit = (EditText) findViewById(R.id.fieldmail);
                pEdit = (EditText) findViewById(R.id.fieldpass);
                prEdit = (EditText) findViewById(R.id.fieldpass2);
                email = mEdit.getText().toString();
                password = pEdit.getText().toString().toCharArray();

                String hash1 = pEdit.getText().toString();
                String hashrep = prEdit.getText().toString();
                CzyPuste(email, mEdit);
                CzyPuste(pEdit.getText().toString(), pEdit);
                CzyPuste(prEdit.getText().toString(), prEdit);
                if ((CzyPuste(email, mEdit)) == true || (CzyPuste(pEdit.getText().toString(), pEdit) == true) || (CzyPuste(prEdit.getText().toString(), prEdit) == true)) {
                    Context context = getApplicationContext();
                    CharSequence text = "Wszystkie pola muszą być wypełnione";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }

                if (!hash1.equals(hashrep)) {
                    Context context = getApplicationContext();
                    CharSequence text = "Podane hasła się nie zgadzają";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    pEdit.getText().clear();
                    prEdit.getText().clear();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Context context = getApplicationContext();
                    CharSequence text = "Niepoprawny adres email";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }

                title = (TextView) findViewById(R.id.title);

                myAsyncTask.execute();
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            generate.setIndeterminateProgressMode(true);
            generate.setProgress(50);
        }

        @Override
        protected Void doInBackground(Void... params) {
            final PGPKeyRingGenerator krgen = PgpUtils.generateKeyRingGenerator(password, email);
            pgpPublicKey = PgpUtils.genPGPPublicKey(krgen);
            pgpSecretKey = PgpUtils.genPGPPrivKey(krgen);
            Data data = new Data();
            data.setEmail(email);
            data.setPrivateKey(pgpSecretKey);
            data.setPublicKey(pgpPublicKey);

            Database db = new Database(GenerateKeyActivity.this);
            db.addKeys(data);

            return null;
        }

        @Override
        protected void onPostExecute(Void a) {
            generate.setProgress(100);
            final Intent intent = new Intent(getApplicationContext(), ListKeys.class);
            generate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
        }
    }

}
