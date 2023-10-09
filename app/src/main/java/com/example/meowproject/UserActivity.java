package com.example.meowproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import com.example.meowproject.Model.Services;
import com.example.meowproject.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class UserActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Users = database.getReference("Users");


    ImageView imageLayout;
    AppCompatImageView logout;
    ProgressBar progressCircle;
    EditText inputFullName, inputPhone;
    Button btnUpdate;
    MaterialButton MyAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        imageLayout = findViewById(R.id.imageLayout);
        logout = findViewById(R.id.logout);
        progressCircle = findViewById(R.id.progressCircle);
        inputFullName = findViewById(R.id.inputFullName);
        inputPhone = findViewById(R.id.inputPhone);
        btnUpdate = findViewById(R.id.btnUpdate);
        MyAppointments = findViewById(R.id.MyAppointments);

        //

        getUserInfo();

        // Actions
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(UserActivity.this, RegisterActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(UserActivity.this);
                Toasty.warning(UserActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        MyAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MyAppointmentActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputFullName.getText().toString().isEmpty() || inputPhone.getText().toString().isEmpty()) {
                    Toasty.warning(UserActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                    return;
                }
                Users.child(CurrentUser.getUid()).child("fullName").setValue(inputFullName.getText().toString());
                Users.child(CurrentUser.getUid()).child("phone").setValue(inputPhone.getText().toString());
                Toasty.success(UserActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });

    }

    User user;

    void getUserInfo() {
        progressCircle.setVisibility(View.VISIBLE);
        Users.child(CurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    inputFullName.setText(user.getFullName());
                    inputPhone.setText(user.getPhone());
                    progressCircle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}