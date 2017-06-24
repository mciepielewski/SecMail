package com.example.sec.sec;

import org.spongycastle.openpgp.PGPPrivateKey;
import org.spongycastle.openpgp.PGPPublicKey;

import java.sql.Date;

/**
 * Created by Kamil on 16.12.2016.
 */
public class Data {
    private String email;
    private String publicKey;
    private String privateKey;
    private PGPPublicKey publick;
    private PGPPrivateKey privatek;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPublicKey(){
        return publicKey;
    }

    public void setPublicKey(String publicKey){
        this.publicKey = publicKey;
    }

    public String getPrivateKey(){
        return privateKey;
    }

    public void setPrivateKey(String privateKey){
        this.privateKey = privateKey;
    }


    public String getTime(){
        String czas;
        Date currentDate = new Date(System.currentTimeMillis());
        czas= currentDate.toString();
        return  czas;
    }
    public PGPPublicKey getPublick() {return publick; }

    public void setPublick(PGPPublicKey publick){
        this.publick = publick;
    }

    public PGPPrivateKey getPrivatek() {return privatek; }

    public void setPrivatek(PGPPrivateKey privatek){
        this.privatek = privatek;
    }

}
