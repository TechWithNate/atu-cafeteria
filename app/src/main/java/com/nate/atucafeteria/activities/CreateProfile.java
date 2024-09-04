package com.nate.atucafeteria.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.StorageReference;
import com.nate.atucafeteria.R;

import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {



    private EditText username;
    private EditText contact;
    private MaterialButton createProfile;
    private ProgressBar progressBar;
    private AutoCompleteTextView genderAutoComplete;
    private String gender;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();


        String[] genderOptions = {"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genderOptions);
        genderAutoComplete.setAdapter(adapter);
        genderAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            gender = (String) parent.getItemAtPosition(position);
        });

        createProfile.setOnClickListener(v -> checkFields());
    }

    private void initViews(){
        username = findViewById(R.id.username);
        genderAutoComplete = findViewById(R.id.gender);
        contact = findViewById(R.id.tel);
        createProfile = findViewById(R.id.create_profile_btn);
        progressBar = findViewById(R.id.progress_bar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    private void checkFields() {
        if (username.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }else if (contact.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Contact", Toast.LENGTH_SHORT).show();
        }else if (gender == null){
            Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
        }else if (contact.getText().toString().length() < 10){
            Toast.makeText(this, "Invalid Contact Number", Toast.LENGTH_SHORT).show();
        }else {
            createUser();
        }
    }

    private void createUser() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", username.getText().toString());
        userMap.put("gender", genderAutoComplete.getText().toString());
        userMap.put("contact", contact.getText().toString());
        sendUserData(userMap);
    }

    private void sendUserData(Map<String, Object> userMap) {
        databaseReference.child("cafeteria_orders").child(firebaseAuth.getUid()).setValue(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateProfile.this, "Profile Created", Toast.LENGTH_SHORT).show();
                navigateToHome();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(CreateProfile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private void navigateToHome() {
        startActivity(new Intent(CreateProfile.this, Login.class));
        finish();
    }

}