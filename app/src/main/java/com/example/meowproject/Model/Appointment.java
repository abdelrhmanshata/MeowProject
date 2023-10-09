package com.example.meowproject.Model;

import java.io.Serializable;

public class Appointment implements Serializable {

    String ID , UserID , UserName , Phone;
    String ServicesName , Date , Note;
    int TotalPrice;
    boolean WasPickUp;

    public Appointment() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getServicesName() {
        return ServicesName;
    }

    public void setServicesName(String servicesName) {
        ServicesName = servicesName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }

    public boolean isWasPickUp() {
        return WasPickUp;
    }

    public void setWasPickUp(boolean wasPickUp) {
        WasPickUp = wasPickUp;
    }
}
