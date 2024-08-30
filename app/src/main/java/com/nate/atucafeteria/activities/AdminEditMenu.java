package com.nate.atucafeteria.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

public class AdminEditMenu extends AppCompatActivity {

    private ImageView foodImage;
    private MaterialToolbar topAppBar;
    private EditText foodName;
    private EditText foodPrice;
    private EditText discountedPrice;
    private EditText readyTime;
    private ProgressBar progressBar;
    private MaterialButton editBtn, deleteBtn;
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
        setContentView(R.layout.activity_admin_edit_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();


        foodImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        editBtn.setOnClickListener(v -> checkFields());
        deleteBtn.setOnClickListener(v -> deleteMenu()); // Set up the delete button listener

    }

    private void initViews(){
        foodImage = findViewById(R.id.food_image);
        foodName = findViewById(R.id.food_name);
        foodPrice = findViewById(R.id.food_price);
        discountedPrice = findViewById(R.id.discount_price);
        readyTime = findViewById(R.id.ready_time);
        progressBar = findViewById(R.id.progress_bar);
        editBtn = findViewById(R.id.edit_btn);
        deleteBtn = findViewById(R.id.delete_btn);
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




    private void uploadImage() {
        progressBar.setVisibility(View.VISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference("menu_images");
        StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
        imageRef.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUriAccessToken = uri.toString();
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    editMenu();
                });
            }
        });
    }


    private void editMenu() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", foodName.getText().toString());
        userMap.put("price", foodPrice.getText().toString());
        userMap.put("oldPrice", discountedPrice.getText().toString());
        userMap.put("readyTime", readyTime.getText().toString());
        userMap.put("imageUrl", imageUriAccessToken);
        uploadMenuData(userMap);
    }


//    private void uploadMenuData(Map<String, Object> menuMap) {
//        databaseReference.child("menu").child(menuID).setValue(menuMap).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(AdminEditMenu.this, "Profile Created", Toast.LENGTH_SHORT).show();
//                navigateToHome();
//            } else {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(AdminEditMenu.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(e -> {
//            progressBar.setVisibility(View.GONE);
//            Toast.makeText(AdminEditMenu.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//        });
//    }






    ////

    private void checkFields() {
        if (imageUri == null) {
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        } else if (foodName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter food name", Toast.LENGTH_SHORT).show();
        } else if (foodPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter food price", Toast.LENGTH_SHORT).show();
        } else if (discountedPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Discounted Price", Toast.LENGTH_SHORT).show();
        } else if (readyTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter the time food will be available", Toast.LENGTH_SHORT).show();
        } else {
            fetchExistingData();
        }
    }

    // Fetch existing data from Firebase to compare
    private void fetchExistingData() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("menu").child(menuID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                // Existing data retrieved successfully
                Map<String, Object> existingData = (Map<String, Object>) task.getResult().getValue();
                checkForChanges(existingData);
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Compare existing data with current input fields
    private void checkForChanges(Map<String, Object> existingData) {
        boolean hasChanges = false;
        Map<String, Object> changes = new HashMap<>();

        // Compare image URL if the image has been changed
        if (imageUriAccessToken != null && !imageUriAccessToken.equals(existingData.get("imageUrl"))) {
            changes.put("imageUrl", imageUriAccessToken);
            hasChanges = true;
        }

        // Compare food name
        String newName = foodName.getText().toString();
        if (!newName.equals(existingData.get("name"))) {
            changes.put("name", newName);
            hasChanges = true;
        }

        // Compare food price
        String newPrice = foodPrice.getText().toString();
        if (!newPrice.equals(existingData.get("price"))) {
            changes.put("price", newPrice);
            hasChanges = true;
        }

        // Compare discounted price
        String newDiscountedPrice = discountedPrice.getText().toString();
        if (!newDiscountedPrice.equals(existingData.get("oldPrice"))) {
            changes.put("oldPrice", newDiscountedPrice);
            hasChanges = true;
        }

        // Compare ready time
        String newReadyTime = readyTime.getText().toString();
        if (!newReadyTime.equals(existingData.get("readyTime"))) {
            changes.put("readyTime", newReadyTime);
            hasChanges = true;
        }

        if (hasChanges) {
            // If changes exist, update only the changed fields
            uploadMenuData(changes);
        } else {
            // No changes, navigate back to Admin Homepage
            navigateToHome();
        }
    }

    // Upload only changed fields
    private void uploadMenuData(Map<String, Object> changes) {
        databaseReference.child("menu").child(menuID).updateChildren(changes)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminEditMenu.this, "Menu updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminEditMenu.this, "Error updating menu", Toast.LENGTH_SHORT).show();
                    }
                    navigateToHome();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminEditMenu.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToHome() {
        startActivity(new Intent(AdminEditMenu.this, AllAdminMenu.class));
        finish();
    }

    private void deleteMenu() {
        // Show a confirmation dialog before deleting
        new AlertDialog.Builder(this)
                .setTitle("Delete Menu Item")
                .setMessage("Are you sure you want to delete this menu item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Proceed to delete the item from the database
                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference.child("menu").child(menuID).removeValue()
                            .addOnCompleteListener(task -> {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(AdminEditMenu.this, "Menu item deleted successfully", Toast.LENGTH_SHORT).show();
                                    navigateToHome();
                                } else {
                                    Toast.makeText(AdminEditMenu.this, "Error deleting menu item", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AdminEditMenu.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    ////

}