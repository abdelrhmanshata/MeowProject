package com.example.meowproject.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String ID;
    private String FullName;
    private String Email;
    private String Password;
    private String Phone;

    public User() {
    }

    public User(String ID, String fullName, String email, String password, String phone) {
        this.ID = ID;
        FullName = fullName;
        Email = email;
        Password = password;
        Phone = phone;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
