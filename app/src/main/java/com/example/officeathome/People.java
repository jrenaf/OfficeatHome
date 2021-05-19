package com.example.officeathome;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class People {

    //public String uid;
    public String email;
    public String username;
    public String password;
    public String level;
    public boolean availability;
    public String myDep;

    public People() {
        // Default constructor required for calls to DataSnapshot.getValue(People.class)
    }

    public People(String email, String username, String password, String level, boolean availability,
                  String dep) {
        //this.uid = uid;
        this.email = email;
        this.username = username;
        this.password = password;
        this.level = level;
        this.availability = availability;
        this.myDep = dep;
    }
}
