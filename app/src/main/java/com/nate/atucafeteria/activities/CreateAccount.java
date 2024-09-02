package com.nate.atucafeteria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nate.atucafeteria.R;

public class CreateAccount extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private MaterialButton createBtn;
    private ProgressBar progressBar;
    private TextView login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        createBtn.setOnClickListener(view ->
            checkFields());
        login.setOnClickListener(view -> openLogin());
    }

    private void initViews(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        createBtn = findViewById(R.id.create_btn);
        progressBar = findViewById(R.id.progress_bar);
        login = findViewById(R.id.login_btn);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void checkFields(){
        //TODO: validate user inputs
        if (email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        }else if (password.getText().length() < 8){
            Toast.makeText(this, "Password less that 8", Toast.LENGTH_SHORT).show();
        }else if (!password.getText().toString().equals(confirmPassword.getText().toString())){
            Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
        }else {
            createAccount(email.getText().toString(), password.getText().toString());
        }

    }

    private void createAccount(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progressBar.setVisibility(View.GONE);
                openCreateAccountPage();
            }else {
                Toast.makeText(CreateAccount.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(CreateAccount.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        });
    }

    private void openCreateAccountPage() {
        startActivity(new Intent(CreateAccount.this, CreateProfile.class));
        finish();
    }

    private void openHomePage() {
        Intent intent = new Intent(CreateAccount.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openLogin() {
        startActivity(new Intent(CreateAccount.this, Login.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            if (firebaseAuth.getCurrentUser().getEmail().equals("admin@atucafeteria.com")){
                startActivity(new Intent(CreateAccount.this, AllAdminMenu.class));
                finish();
            }else {
                openHomePage();
            }
        }

    }
}