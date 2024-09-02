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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nate.atucafeteria.R;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private MaterialButton loginBtn;
    private ProgressBar progressBar;
    private TextView createAccount;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        loginBtn.setOnClickListener(view -> checkFields());
        createAccount.setOnClickListener(view -> openCreateAccount());
    }

    private void initViews() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccount = findViewById(R.id.create_acc_btn);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void checkFields(){
        //TODO: validate user input
        if (email.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        }else {
            loginUser(email.getText().toString(), password.getText().toString());
        }

    }

    private void loginUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        String userEmail = user.getEmail();
                                        if ("admin@atucafeteria.com".equals(userEmail)) {
                                            openAdminHomePage();
                                        } else {
                                            openHomePage();
                                        }
                                    }


                                    progressBar.setVisibility(View.GONE);
                                }else{
                                    Toast.makeText(Login.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void openAdminHomePage() {
        startActivity(new Intent(Login.this, AllAdminMenu.class));
        finish();
    }

    private void openHomePage() {
        startActivity(new Intent(Login.this, Home.class));
        finish();
    }

    private void openCreateAccount() {
        startActivity(new Intent(Login.this, CreateAccount.class));
        finish();
    }

}