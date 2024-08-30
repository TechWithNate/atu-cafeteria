package com.nate.atucafeteria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.models.FoodModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class AdminOrderDetails extends AppCompatActivity {



        private ImageView foodImage;
        private TextView foodName, deliveryType, location;
        private FoodModel foodModels;
        private DatabaseReference ordersRef;
        private MaterialButton completeOrder;
        private RelativeLayout locLayout;

        private SwitchCompat acceptSwitch, processingSwitch, onTheWaySwitch, deliveredSwitch;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_admin_order_details);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Initialize Firebase reference
            ordersRef = FirebaseDatabase.getInstance().getReference("cafeteria_orders");

            initViews();

            foodModels = getIntent().getParcelableExtra("food");

            if (null != foodModels) {
                Glide.with(this).load(foodModels.getImageUrl()).into(foodImage);
                foodName.setText(foodModels.getName() + " " + foodModels.getPrice());
                if (!foodModels.getLocation().equals("")){
                    location.setText(foodModels.getLocation());
                    deliveryType.setText("Delivery");
                }else{
                    deliveryType.setText("Pick Up");
                    locLayout.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
            }

            setupSwitchListeners();

            completeOrder.setOnClickListener(v -> {
                updateOrderStatus("Order Completed");
                startActivity(new Intent(AdminOrderDetails.this, AdminHome.class));
                finish();
            });


        }

        private void initViews() {
            foodImage = findViewById(R.id.food_image);
            foodName = findViewById(R.id.food_name);
            deliveryType = findViewById(R.id.delivery_type);
            location = findViewById(R.id.location);
            completeOrder = findViewById(R.id.complete_order);
            locLayout = findViewById(R.id.loc_layout);

            // Initialize Switches
            acceptSwitch = findViewById(R.id.accept_switch);
            processingSwitch = findViewById(R.id.processing_switch);
            onTheWaySwitch = findViewById(R.id.on_the_way_switch);
            deliveredSwitch = findViewById(R.id.delivered_switch);
        }

        private void setupSwitchListeners() {
            // Accept switch listener
            acceptSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    updateOrderStatus("Order Accepted");
//                    processingSwitch.setChecked(false);
//                    onTheWaySwitch.setChecked(false);
//                    deliveredSwitch.setChecked(false);
                }
            });

            // Processing switch listener
            processingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    updateOrderStatus("Processing Order");
//                    acceptSwitch.setChecked(false);
//                    onTheWaySwitch.setChecked(false);
//                    deliveredSwitch.setChecked(false);
                }
            });

            // On the way switch listener
            onTheWaySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    updateOrderStatus("Order on the way");
//                    acceptSwitch.setChecked(false);
//                    processingSwitch.setChecked(false);
//                    deliveredSwitch.setChecked(false);
                }
            });

            // Delivered switch listener
            deliveredSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    updateOrderStatus("Order Completed");
//                    acceptSwitch.setChecked(false);
//                    processingSwitch.setChecked(false);
//                    onTheWaySwitch.setChecked(false);
                }
            });
        }


        private void updateOrderStatus(String status) {
            if (foodModels != null) {
                // Assuming you have an order ID or user ID to identify the order
                String orderId = foodModels.getBuyerId();

                // Update the status in Firebase
                ordersRef.child(orderId).child("orders").child(foodModels.getId()).child("deliveryStatus").setValue(status)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AdminOrderDetails.this, "Order status updated to " + status, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AdminOrderDetails.this, "Failed to update order status", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "No order to update", Toast.LENGTH_SHORT).show();
            }
        }



//    private ImageView foodImage;
//    private TextView foodName;
//    private TextView deliveryType;
//    private TextView location;
//    private FoodModel foodModels;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_admin_order_details);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        initViews();
//        foodModels = getIntent().getParcelableExtra("food");
//
//        if (null != foodModels) {
//            Toast.makeText(this, "Food not null", Toast.LENGTH_SHORT).show();
//            Glide.with(this).load(foodModels.getImageUrl()).into(foodImage);
//            foodName.setText(foodModels.getName() +" "+ foodModels.getPrice());
//
//
//        }else {
//            Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//
//    private void initViews(){
//        foodImage = findViewById(R.id.food_image);
//        foodName = findViewById(R.id.food_name);
//        deliveryType = findViewById(R.id.delivery_type);
//        location = findViewById(R.id.location);
//
//    }

}