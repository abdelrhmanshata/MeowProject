package com.example.meowproject.Model;

import com.example.meowproject.R;

import java.util.ArrayList;

public class Services {

    public static Service service1 = new Service(
            1,
            "Water Shower",
            "* Shampooing with water\n* Hair Drying\n* Hair Combing\n* Nail Trimming\n* Cleaning Eyes,Ears,Teeth\n* Powder\n* Perfume",
            120,
            R.drawable.service1
    );

    public static Service service2 = new Service(
            2,
            "Dry Shower",
            "* Shampooing with water\n* Hair Combing\n* Nail Trimming\n* Cleaning Eyes,Ears,Teeth\n* Powder\n* Perfume",
            100,
            R.drawable.service2
    );

    public static Service service3 = new Service(
            3,
            "Deep Shower",
            "* Shampooing with water\n* Nourishing Conditioner\n* Remove hair tangels\n* Hair Combing\n* Hair Drying\n* Nail Trimming\n* Cleaning Eyes,Ears,Teeth\n* Powder\n* Perfume",
            160,
            R.drawable.service3
    );
    public static Service service4 = new Service(
            4,
            "Saving-Hair Cutting",
            "* Remove Hair Tangels\n* Cut Hair With Scissors\n* Cut Hair with electric shaver(Levels 0-4)",
            140,
            R.drawable.service4
    );

    public static ArrayList<Service> myCartServices = new ArrayList<>();



}
