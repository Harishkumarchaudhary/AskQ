package com.example.dell.askq;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Dell on 08-06-2019.
 */

public class MyUser implements Serializable {
    @PropertyName("username")
    public String displayname;
    private String email;
    private String uid;

    public MyUser() {
    }

    public MyUser(String displayname, String email) {
        this.displayname = displayname;
        this.email = email;
    }
    public MyUser(String displayname, String email,String uid){
        this.displayname = displayname;
        this.email = email;
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

   /* public void setUid(String uid) {
        this.uid = uid;
    }*/

    public String getDisplayname() {
        return displayname;
    }

  /*  public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }*/

    public String getEmail() {
        return email;
    }

    /*public void setEmail(String email) {
        this.email = email;
    }*/
}
