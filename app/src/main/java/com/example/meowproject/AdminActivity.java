package com.example.meowproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import com.example.meowproject.Adapter.AppointmentAdapter;
import com.example.meowproject.Model.Appointment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class AdminActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refAppointments = database.getReference("Appointments");

    AppCompatImageView logout;

    ListView listAppointment;
    AppointmentAdapter appointmentAdapter;
    ArrayList<Appointment> appointments;
    ProgressBar progressCircle;

    TextInputEditText inputSelectDate;
    ImageButton clearAll, selectDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        logout = findViewById(R.id.logout);
        progressCircle = findViewById(R.id.progressCircle);
        listAppointment = findViewById(R.id.listAppointment);
        inputSelectDate = findViewById(R.id.inputSelectDate);
        clearAll = findViewById(R.id.clearAll);
        selectDate = findViewById(R.id.selectDate);
        appointments = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(this, appointments);
        listAppointment.setAdapter(appointmentAdapter);

        //
        getAllAppointments("");

        // Actions
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(AdminActivity.this, RegisterActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(AdminActivity.this);
                Toasty.warning(AdminActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        listAppointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String Phone = appointments.get(position).getPhone() + "\n";
                String contact = "+966" + Phone; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, (view, year1, month1, dayOfMonth) -> {
                    // Show Select Day
                    inputSelectDate.setText(dayOfMonth + "-" + (month1 + 1) + "-" + year1);
                    getAllAppointments(dayOfMonth + "-" + (month1 + 1) + "-" + year1);
                }, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSelectDate.setText("");
                getAllAppointments("");
            }
        });

    }

    public void getAllAppointments(String date) {
        progressCircle.setVisibility(View.VISIBLE);
        refAppointments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointments.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    // if date is empty return all appointments
                    if (date.isEmpty()) {
                        appointments.add(appointment);
                    }
                    // else return only appointments the date equal inputDate
                    else {
                        if (appointment.getDate().contains(date)) appointments.add(appointment);
                    }
                }
                appointmentAdapter.notifyDataSetChanged();

                progressCircle.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressCircle.setVisibility(View.GONE);
            }
        });
    }


}