package com.example.devyug.userinterface;

/**
 * Created by devyug on 20/9/17.
 */

public class user {
    String email;
    public user(String Name,String Email)
    {
        name=Name;
        email=Email;
    }
    user()
    {}


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
}
