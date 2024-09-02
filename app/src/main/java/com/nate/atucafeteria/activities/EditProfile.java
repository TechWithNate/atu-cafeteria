package com.nate.atucafeteria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nate.atucafeteria.R;

import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private EditText username;
    private EditText contact;
    private MaterialButton edit;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();


        username.setText(getIntent().getStringExtra("username"));
        contact.setText(getIntent().getStringExtra("contact"));

        edit.setOnClickListener(v -> {
            editProfile();
        });

    }

    private void initViews(){
        username = findViewById(R.id.username);
        contact = findViewById(R.id.tel);
        edit = findViewById(R.id.edit);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void editProfile(){
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cafeteria_orders");

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (null != user){
            String uid = user.getUid();

            String name = username.getText().toString();
            String contactNumber = contact.getText().toString();
            Map<String, Object> userMap = Map.of("username", name, "contact", contactNumber);
            databaseReference.child(uid).updateChildren(userMap).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        }

    }

}