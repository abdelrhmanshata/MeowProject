package com.example.meowproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.meowproject.Model.Appointment;
import com.example.meowproject.R;

import java.util.ArrayList;

public class AppointmentAdapter extends BaseAdapter {
    Context context;
    ArrayList<Appointment> appointments;

    public AppointmentAdapter(Context context, ArrayList<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @Override
    public int getCount() {
        return appointments.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.list_item_appointment, viewGroup, false);

        TextView userName = view.findViewById(R.id.userName);
        TextView userPhone = view.findViewById(R.id.userPhone);
        TextView appointmentID = view.findViewById(R.id.appointmentID);
        TextView appointmentDate = view.findViewById(R.id.appointmentDate);
        TextView appointmentServicesName = view.findViewById(R.id.appointmentServicesName);
        TextView appointmentTotalPrice = view.findViewById(R.id.appointmentTotalPrice);

        userName.setText(appointments.get(i).getUserName());
        userPhone.setText(appointments.get(i).getPhone());
        appointmentID.setText("ID : " + appointments.get(i).getID());
        appointmentDate.setText(appointments.get(i).getDate());
        String ServicesName = appointments.get(i).getServicesName().replace(",", "\n");
        ServicesName = ServicesName.substring(0,ServicesName.length()-2);
        appointmentServicesName.setText(ServicesName);
        if (appointments.get(i).isWasPickUp()) {
            appointmentTotalPrice.setText(appointments.get(i).getTotalPrice() + " SAR Was Pick Up");
        } else {
            appointmentTotalPrice.setText(appointments.get(i).getTotalPrice() + " SAR");
        }

        return view;
    }
}