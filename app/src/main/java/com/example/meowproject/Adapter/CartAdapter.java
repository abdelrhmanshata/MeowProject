package com.example.meowproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meowproject.Model.Service;
import com.example.meowproject.R;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    Context context;
    ArrayList<Service> services;

    public CartAdapter(Context context, ArrayList<Service> services) {
        this.context = context;
        this.services = services;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item_cart, viewGroup, false);
        TextView serviceID = view.findViewById(R.id.serviceID);
        ImageView serviceImage = view.findViewById(R.id.serviceImage);
        TextView serviceName = view.findViewById(R.id.serviceName);
        TextView servicePrice = view.findViewById(R.id.servicePrice);

        serviceID.setText(services.get(i).getID() + "");
        serviceImage.setImageResource(services.get(i).getImage());
        serviceName.setText(services.get(i).getName());
        servicePrice.setText(services.get(i).getPrice() + "");

        return view;
    }
}