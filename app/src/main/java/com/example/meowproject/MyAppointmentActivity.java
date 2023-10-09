package com.example.meowproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meowproject.Adapter.AppointmentAdapter;
import com.example.meowproject.Model.Appointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MyAppointmentActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refAppointments = database.getReference("Appointments");

    ImageView imageLayout;

    ListView listAppointment;
    AppointmentAdapter appointmentAdapter;
    ArrayList<Appointment> appointments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);

        imageLayout = findViewById(R.id.imageLayout);
        listAppointment = findViewById(R.id.listAppointment);
        appointments = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(this, appointments);
        listAppointment.setAdapter(appointmentAdapter);

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getMyAppointments();

        listAppointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String ID = "ID : " + appointments.get(position).getID() + "\n";
                String UserName = "User Name : " + appointments.get(position).getUserName() + "\n";
                String Phone = "Phone : " + appointments.get(position).getPhone() + "\n";
                String ServicesName = "Services : \n" + appointments.get(position).getServicesName().replace(",", "\n");
                String Date = "Date : " + appointments.get(position).getDate() + "\n";
                String Note = "Note : " + appointments.get(position).getNote() + "\n";
                String TotalPrice = "Total Price : " + appointments.get(position).getTotalPrice();
                if (appointments.get(position).isWasPickUp()) {
                    TotalPrice += " SAR Was Pick Up";
                }

                String contact = "+966 53 663 4715"; // use country code with your phone number
                String message = ID + UserName + Phone + ServicesName + Date + Note + TotalPrice;
                String url = "https://api.whatsapp.com/send?phone=" + contact + "&text=" + message;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    public void getMyAppointments() {
        refAppointments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointments.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        if (appointment.getUserID().equals(CurrentUser.getUid())) {
                            appointments.add(appointment);
                        }
                    }
                }
                Collections.reverse(appointments);
                appointmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}