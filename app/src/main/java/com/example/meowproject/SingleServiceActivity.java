package com.example.meowproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meowproject.Adapter.CommentAdapter;
import com.example.meowproject.Model.Comment;
import com.example.meowproject.Model.Service;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class SingleServiceActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refAppointments = database.getReference("Appointments");
    DatabaseReference refComments = database.getReference("Comments");
    DatabaseReference Users = database.getReference("Users");
    User user;

    Service service;

    ImageView imageBack, IVCart;
    ImageView serviceImage;
    TextView serviceName, servicePrice, serviceDescription;
    MaterialButton addCart;

    // Comment
    ListView listComments;
    CommentAdapter commentAdapter;
    List<Comment> comments;
    ImageButton sendComment;
    EditText commentText;
    ProgressBar progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_service);
        //
        service = (Service) getIntent().getSerializableExtra("SERVICE");
        //
        imageBack = findViewById(R.id.imageBack);
        IVCart = findViewById(R.id.IVCart);
        serviceImage = findViewById(R.id.serviceImage);
        serviceName = findViewById(R.id.serviceName);
        servicePrice = findViewById(R.id.servicePrice);
        serviceDescription = findViewById(R.id.serviceDescription);
        addCart = findViewById(R.id.addCart);
        //
        sendComment = findViewById(R.id.sendButton);
        commentText = findViewById(R.id.commentText);
        progressCircle = findViewById(R.id.progressCircle);
        listComments = findViewById(R.id.listComments);
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, comments);
        listComments.setAdapter(commentAdapter);
        //

        // Show Service Data
        serviceImage.setImageResource(service.getImage());
        serviceName.setText(service.getName());
        servicePrice.setText(service.getPrice() + " SAR");
        serviceDescription.setText(service.getDescription());

        //
        getUserInfo();

        //Show Service Comments
        ShowServiceComments(service.getID() + "");

        // Actions
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        IVCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleServiceActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Services.myCartServices.contains(service)) {
                    Toasty.info(SingleServiceActivity.this, "The service already exists", Toast.LENGTH_SHORT).show();
                } else {
                    Services.myCartServices.add(service);
                    Toasty.success(SingleServiceActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!commentText.getText().toString().isEmpty()) {
                    Comment comment = new Comment();
                    comment.setID(refComments.push().getKey());
                    comment.setUserName(user.getFullName());
                    comment.setComment(commentText.getText().toString());
                    comment.setCommentDate(getDateTime());

                    // Write Comment Into Item In Database
                    refComments
                            .child(service.getID() + "")
                            .child(comment.getID())
                            .setValue(comment)
                            .addOnSuccessListener(unused -> {
                                Toasty.success(SingleServiceActivity.this, "Comment sent successfully", Toast.LENGTH_SHORT).show();
                                commentText.setText("");
                            });
                } else {
                    Toasty.warning(SingleServiceActivity.this, "Can't Send Empty Comment !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ShowServiceComments(String serviceID) {
        progressCircle.setVisibility(View.VISIBLE);
        refComments
                .child(serviceID)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        comments.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Comment comment = snapshot.getValue(Comment.class);
                            comments.add(comment);
                        }
                        commentAdapter.notifyDataSetChanged();
                        progressCircle.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    String getDateTime() {
        return new SimpleDateFormat(" dd, MMMM, yyyy - hh:mm a", new Locale("en")).format(new Date());
    }

    void getUserInfo() {
        Users.child(CurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}