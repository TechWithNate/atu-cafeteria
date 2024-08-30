package com.nate.atucafeteria.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nate.atucafeteria.R;

import java.util.HashMap;
import java.util.Map;

public class AddMenu extends AppCompatActivity {

    private ImageView foodImage;
    private MaterialToolbar topAppBar;
    private EditText foodName;
    private EditText foodPrice;
    private EditText discountedPrice;
    private EditText readyTime;
    private ProgressBar progressBar;
    private MaterialButton addMenuBtn;
    private Uri imageUri;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private String imageUriAccessToken;
    private DatabaseReference databaseReference;
    private String menuID;


    private static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        topAppBar.setNavigationOnClickListener(v -> finish());

        foodImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        addMenuBtn.setOnClickListener(v -> checkFields());
    }

    private void initViews(){
        foodImage = findViewById(R.id.food_image);
        foodName = findViewById(R.id.food_name);
        foodPrice = findViewById(R.id.food_price);
        discountedPrice = findViewById(R.id.discount_price);
        readyTime = findViewById(R.id.ready_time);
        progressBar = findViewById(R.id.progress_bar);
        addMenuBtn = findViewById(R.id.add_menu_btn);
        topAppBar = findViewById(R.id.topAppBar);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("cafeteria_orders");
        menuID = databaseReference.push().getKey();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            foodImage.setImageURI(imageUri);
        }
    }

    private void checkFields() {
        if (imageUri == null){
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        } else if (foodName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter food name", Toast.LENGTH_SHORT).show();
        }else if (foodPrice.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter food price", Toast.LENGTH_SHORT).show();
        }else if (discountedPrice.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Discounted Price", Toast.LENGTH_SHORT).show();
        }else if (readyTime.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter the time food will be available", Toast.LENGTH_SHORT).show();
        }else {
            uploadImage();
        }
    }


    private void uploadImage() {
        progressBar.setVisibility(View.VISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference("menu_images");
        StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
        imageRef.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUriAccessToken = uri.toString();
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    createMenu();
                });
            }
        });
    }

    private void createMenu() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", menuID);
        userMap.put("name", foodName.getText().toString());
        userMap.put("price", foodPrice.getText().toString());
        userMap.put("oldPrice", discountedPrice.getText().toString());
        userMap.put("readyTime", readyTime.getText().toString());
        userMap.put("imageUrl", imageUriAccessToken);
        uploadMenuData(userMap);
    }

    private void uploadMenuData(Map<String, Object> menuMap) {
        databaseReference.child("menu").child(menuID).setValue(menuMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddMenu.this, "Profile Created", Toast.LENGTH_SHORT).show();
                navigateToHome();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddMenu.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(AddMenu.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void navigateToHome() {
        startActivity(new Intent(AddMenu.this, AllAdminMenu.class));
        finish();
    }


}