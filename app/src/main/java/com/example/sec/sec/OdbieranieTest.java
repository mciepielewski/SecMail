package com.example.sec.sec;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;

/**
 * Created by Matje on 25.04.2017.
 */

public class OdbieranieTest extends AppCompatActivity {
    List<MailContent> mails;

    public OdbieranieTest(String email, String pass, String hostname) {
        mails = new ArrayList<MailContent>();

        // public OdbieranieTest(){
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //TextView t1=(TextView)findViewById(R.id.tekstID);
        String host = hostname;
        String username = email;
        String password = pass;
        //String provider  = "pop3";
        String provider = "imaps";
        try

        {
            Properties prop = new Properties();
            prop.setProperty("mail.imap.ssl.enable", "true");
            prop.setProperty("mail.imap.starttls.enable", "true");
            prop.setProperty("mail.store.protocol", "imaps");
            prop.setProperty("ssl.SocketFactory.provider", "my.package.name.ExchangeSSLSocketFactory");
            prop.setProperty("mail.imap.socketFactory.class", "my.package.name.ExchangeSSLSocketFactory");
            //Connect to the server
            Session session = Session.getDefaultInstance(prop, null);
            session.setDebug(true);

            Store store = session.getStore(provider);
            store.connect(host, username, password);

            //open the inbox folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // get a list of javamail messages as an array of messages
            Message[] messages = inbox.getMessages();

            //TreeSet treeSet = new TreeSet();

            for (int i = 0; i < messages.length; i++) {
                String subject = getSubject(messages[i]);
                if(!checkSecMessage(subject))
                    continue;
                String date = getDate(messages[i]);
                String from = getFrom(messages[i]);
                String content = getContent(messages[i]);

                MailContent mail = new MailContent();
                mail.setSubject(subject);
                mail.setBody(content);
                mail.setFrom(from);
                mail.setDate(date);
                mails.add(mail);
            }


            inbox.close(false);
            store.close();
        } catch (
                NoSuchProviderException nspe)

        {
            System.err.println("invalid provider name");
        } catch (
                MessagingException me)

        {
            System.err.println("messaging exception");
            me.printStackTrace();
        }

    }

    private String getFrom(Message javaMailMessage)
            throws MessagingException {
        String from = "";
        Address a[] = javaMailMessage.getFrom();
        if (a == null) return null;
        for (int i = 0; i < a.length; i++) {
            Address address = a[i];
            from = from + address.toString();
        }

        return from;
    }

    private String getContent(Message javaMailMessage)
            throws MessagingException{
        String myMail = "";
        try {
            Part messagePart = javaMailMessage;
            Object content = messagePart.getContent();
            // -- or its first body part if it is a multipart message --
            if (content instanceof Multipart) {
                messagePart = ((Multipart) content).getBodyPart(0);
            }
            // -- Get the content type --
            String contentType = messagePart.getContentType();
            // -- If the content is plain text, we can print it --
            if (contentType.startsWith("TEXT/PLAIN")
                    || contentType.startsWith("TEXT/HTML")) {
                InputStream is = messagePart.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                String thisLine = reader.readLine();
                while (thisLine != null) {
                    System.out.println(thisLine);
                    myMail = myMail + thisLine;
                    thisLine = reader.readLine();
                }


            }

        }catch(IOException e){

        }
        return myMail;
    }

    private String getSubject(Message javaMailMessage)
            throws MessagingException {
        String subject = javaMailMessage.getSubject();

        return subject;
    }

    /*private String removeQuotes(String stringToModify) {
        int indexOfFind = stringToModify.indexOf(stringToModify);
        if (indexOfFind < 0) return stringToModify;

        StringBuffer oldStringBuffer = new StringBuffer(stringToModify);
        StringBuffer newStringBuffer = new StringBuffer();
        for (int i = 0, length = oldStringBuffer.length(); i < length; i++) {
            char c = oldStringBuffer.charAt(i);
            if (c == '"' || c == '\'') {
                // do nothing
            } else {
                newStringBuffer.append(c);
            }

        }
        return new String(newStringBuffer);
    }*/

    private String getDate(Message javaMailMessage)  throws MessagingException{
        Date date = javaMailMessage.getSentDate();
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return str;
    }

    private Boolean checkSecMessage(String message){
        String search  = "#secmail#";

        if ( message.toLowerCase().indexOf(search.toLowerCase()) != -1 )
            return true;
        else
            return false;
    }

    public List<MailContent> getMails(){
        return mails;
    }
}




