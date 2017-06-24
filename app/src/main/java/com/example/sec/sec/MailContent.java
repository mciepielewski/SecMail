package com.example.sec.sec;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kamil on 2017-04-19.
 */

public class MailContent implements Comparable<MailContent>{
    private String subject;
    private String body;
    private String from;
    private String to;
    private String date;
    private Boolean readout;
    private int id;

    public String getSubject(){
        return subject;
    }

    public String getBody(){
        return body;
    }

    public String getFrom(){
        return from;
    }

    public String getTo(){
        return to;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public void setBody(String body){
        this.body = body;
    }

    public void setFrom(String from){
        this.from = from;
    }

    public void setTo(String to){
        this.to = to;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public void setReadout(Boolean readout){
        this.readout = readout;
    }

    public Boolean getReadout(){
        return readout;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    @Override
    public int compareTo(@NonNull MailContent mailContent) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date lDate=null;
        Date aDate=null;

        try {
            lDate = format.parse(date);
            aDate = format.parse(mailContent.getDate());
        }catch (ParseException e){

        }

        if (lDate.compareTo(aDate) < 0 ) {
            return 1;
        }
        else if (lDate.compareTo(aDate) > 0 ) {
            return -1;
        }
        else {
            return 0;
        }

    }
}
