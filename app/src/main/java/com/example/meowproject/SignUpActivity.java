package com.example.meowproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.meowproject.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Users = database.getReference("Users");

    ImageView imageBack;
    ProgressBar progressCircle;
    EditText inputFullName, inputEmail, inputPassword, inputPhone;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //
        imageBack = findViewById(R.id.imageBack);
        progressCircle = findViewById(R.id.progressCircle);
        inputFullName = findViewById(R.id.inputFullName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputPhone = findViewById(R.id.inputPhone);
        btnSignUp = findViewById(R.id.btnSignUp);
        //

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressCircle.setVisibility(View.VISIBLE);
                String FullName = inputFullName.getText().toString().trim();
                String Email = inputEmail.getText().toString().trim();
                String Password = inputPassword.getText().toString().trim();
                String Phone = inputPhone.getText().toString().trim();

                if (FullName.isEmpty() || Email.isEmpty() || Password.isEmpty() || Phone.isEmpty()) {
                    progressCircle.setVisibility(View.INVISIBLE);
                    Toasty.warning(SignUpActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    progressCircle.setVisibility(View.INVISIBLE);
                    Toasty.warning(SignUpActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth
                            .createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser CurrentUser = firebaseAuth.getCurrentUser();
                                    String userID = CurrentUser.getUid();
                                    // Upload UserModel In Database

                                    User user = new User();
                                    user.setID(userID);
                                    user.setFullName(FullName);
                                    user.setEmail(Email);
                                    user.setPassword(Password);
                                    user.setPhone(Phone);

                                    // Save UserData IN DataBase
                                    Users
                                            .child(userID)
                                            .setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toasty.success(SignUpActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                    ActivityCompat.finishAffinity(SignUpActivity.this);
                                                }
                                            });
                                }
                            }).addOnFailureListener(e -> {
                                progressCircle.setVisibility(View.INVISIBLE);
                                Toasty.error(SignUpActivity.this, "Exception -> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                }
            }
        });
    }
}