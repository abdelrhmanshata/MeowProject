package com.example.meowproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meowproject.Adapter.CartAdapter;
import com.example.meowproject.Model.Appointment;
import com.example.meowproject.Model.Service;
import com.example.meowproject.Model.Services;
import com.example.meowproject.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class CartActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refAppointments = database.getReference("Appointments");
    DatabaseReference Users = database.getReference("Users");
    User user;
    ListView listCart;
    CartAdapter cartAdapter;

    ImageView imageLayout;
    RadioButton PickUp, DropOff;
    TextView PickUpPrice, totalPrice;

    TextInputEditText inputSelectTime, inputSelectDate;
    ImageButton selectTime, selectDate;
    EditText textNote;

    MaterialButton Done;
    ProgressBar progressCircle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //
        imageLayout = findViewById(R.id.imageLayout);
        listCart = findViewById(R.id.listCart);
        cartAdapter = new CartAdapter(this, Services.myCartServices);
        listCart.setAdapter(cartAdapter);

        PickUp = findViewById(R.id.PickUp);
        DropOff = findViewById(R.id.DropOff);
        PickUpPrice = findViewById(R.id.PickUpPrice);
        totalPrice = findViewById(R.id.totalPrice);

        inputSelectTime = findViewById(R.id.inputSelectTime);
        inputSelectDate = findViewById(R.id.inputSelectDate);
        selectTime = findViewById(R.id.selectTime);
        selectDate = findViewById(R.id.selectDate);
        textNote = findViewById(R.id.textNote);
        Done = findViewById(R.id.buttonDone);
        progressCircle = findViewById(R.id.progressCircle);
        //
        getUserInfo();

        //
        totalPrice.setText("Total Price : " + getTotalPrice(false) + " SAR");

        // Actions
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Services.myCartServices.remove(position);
                cartAdapter.notifyDataSetChanged();
            }
        });

        PickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickUpPrice.setVisibility(View.VISIBLE);
                totalPrice.setText("Total Price : " + getTotalPrice(true) + " SAR");
            }
        });
        DropOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickUpPrice.setVisibility(View.GONE);
                totalPrice.setText("Total Price : " + getTotalPrice(false) + " SAR");
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(CartActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, (view, year1, month1, dayOfMonth) -> {
                    // Show Select Day
                    inputSelectDate.setText(dayOfMonth + "-" + (month1 + 1) + "-" + year1);
                }, year, month, day);
                // show Future Date
                long DURATION = 1 * 24 * 60 * 60 * 1000;
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + DURATION);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(CartActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, (view, hourOfDay, minute) -> {
                    // Show Select Time
                    inputSelectTime.setText(hourOfDay + ":" + minute);
                }, mHour, mMinute, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });


        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAppointment();
            }
        });

    }

    int getTotalPrice(boolean isPickUp) {
        int Total = 0;
        for (Service service : Services.myCartServices) {
            Total += service.getPrice();
        }
        if (isPickUp) {
            return Total + 15;
        } else {
            return Total;
        }
    }

    void SaveAppointment() {

        if (Services.myCartServices.isEmpty()) {
            Toasty.warning(this, "The Cart Is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputSelectDate.getText().toString().isEmpty()) {
            Toasty.warning(this, "Please Select The Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputSelectTime.getText().toString().isEmpty()) {
            Toasty.warning(this, "Please Select The Time", Toast.LENGTH_SHORT).show();
            return;
        }

        String ID = System.currentTimeMillis() + "";
        String UserID = CurrentUser.getUid() + "";
        String UserName = user.getFullName() + "";
        String Phone = user.getPhone() + "";
        String ServicesName = "";
        for (Service service : Services.myCartServices) {
            ServicesName += "* " + service.getName() + ",";
        }
        String Date = inputSelectDate.getText().toString() + " / " + inputSelectTime.getText().toString();
        String Note = textNote.getText().toString().trim();
        int TotalPrice = getTotalPrice(PickUp.isChecked());
        boolean WasPickUp = PickUp.isChecked();

        Appointment appointment = new Appointment();
        appointment.setID(ID);
        appointment.setUserID(UserID);
        appointment.setUserName(UserName);
        appointment.setPhone(Phone);
        appointment.setServicesName(ServicesName);
        appointment.setDate(Date);
        appointment.setNote(Note);
        appointment.setTotalPrice(TotalPrice);
        appointment.setWasPickUp(WasPickUp);

        refAppointments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isFound = false;

                // read all Appointment To determine if the appointment is booked or not
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment value = snapshot.getValue(Appointment.class);
                    if (value.getDate().equals(Date)) {
                        isFound = true;
                        break;
                    }
                }

                if (isFound) {
                    Toasty.error(CartActivity.this, "Appointment Has Already Booked!\nPlease Choose Another Date", Toast.LENGTH_SHORT).show();
                } else {
                    refAppointments.child(appointment.getID()).setValue(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toasty.success(CartActivity.this, "Booked Successfully", Toast.LENGTH_SHORT).show();
                            Services.myCartServices.clear();
                            Intent intent = new Intent(CartActivity.this, MyAppointmentActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    void getUserInfo() {
        Users.child(CurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    Done.setVisibility(View.VISIBLE);
                    progressCircle.setVisibility(View.GONE);
                    Toasty.success(CartActivity.this, "Done", Toast.LENGTH_SHORT).show();

                } else {
                    Done.setVisibility(View.GONE);
                    progressCircle.setVisibility(View.VISIBLE);
                    Toasty.error(CartActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}