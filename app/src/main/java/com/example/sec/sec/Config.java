package com.example.sec.sec;

/**
 * Created by dzemik on 24.04.2017.
 */

public class Config {
    private int id;
    private String smtp;
    private String ssl;
    private String tls;
    private String name;
    private String email;
    private String pass;
    private String imap;
    public Config(){
        smtp = "";
        imap="";
        ssl = "no";
        tls = "no";
        name = "";
        email = "";
        pass = "";
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getImap() {
        return imap;
    }

    public void setImap(String imap) {
        this.imap = imap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTls() {
        return tls;
    }

    public void setTls(String tls) {
        this.tls = tls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSsl() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
    }
}
