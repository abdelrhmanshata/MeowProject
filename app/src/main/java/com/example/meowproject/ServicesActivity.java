package com.example.meowproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meowproject.Model.Services;

public class ServicesActivity extends AppCompatActivity {

    ImageView imageBack, IVCart;
    ImageView BtnService1, BtnService2, BtnService3, BtnService4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        //
        imageBack = findViewById(R.id.imageBack);
        IVCart = findViewById(R.id.IVCart);

        BtnService1 = findViewById(R.id.service1);
        BtnService2 = findViewById(R.id.service2);
        BtnService3 = findViewById(R.id.service3);
        BtnService4 = findViewById(R.id.service4);
        //

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        IVCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        BtnService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, SingleServiceActivity.class);
                intent.putExtra("SERVICE", Services.service1);
                startActivity(intent);
            }
        });
        BtnService2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, SingleServiceActivity.class);
                intent.putExtra("SERVICE", Services.service2);
                startActivity(intent);
            }
        });
        BtnService3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, SingleServiceActivity.class);
                intent.putExtra("SERVICE", Services.service3);
                startActivity(intent);
            }
        });
        BtnService4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, SingleServiceActivity.class);
                intent.putExtra("SERVICE", Services.service4);
                startActivity(intent);
            }
        });

    }
}