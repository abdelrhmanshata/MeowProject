package com.example.meowproject.Model;

import java.io.Serializable;

public class Service implements Serializable {

    int ID;
    String Name, Description;
    int Price, Image;

    public Service() {
    }

    public Service(int ID, String name, String description, int price, int image) {
        this.ID = ID;
        Name = name;
        Description = description;
        Price = price;
        Image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
