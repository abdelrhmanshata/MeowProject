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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    ImageView imageBack;
    ProgressBar progressCircle;
    EditText inputEmail, inputPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //
        imageBack = findViewById(R.id.imageBack);
        progressCircle = findViewById(R.id.progressCircle);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        //

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressCircle.setVisibility(View.VISIBLE);
                String Email = inputEmail.getText().toString().trim();
                String Password = inputPassword.getText().toString().trim();

                if (Email.isEmpty() || Password.isEmpty()) {
                    progressCircle.setVisibility(View.INVISIBLE);
                    Toasty.warning(LoginActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    progressCircle.setVisibility(View.INVISIBLE);
                    Toasty.warning(LoginActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toasty.success(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    if (firebaseUser.getEmail().equals("admin@admin.com")) {
                                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }

                                    ActivityCompat.finishAffinity(LoginActivity.this);
                                } else {
                                    progressCircle.setVisibility(View.INVISIBLE);
                                    Toasty.error(LoginActivity.this, "Error+->" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}