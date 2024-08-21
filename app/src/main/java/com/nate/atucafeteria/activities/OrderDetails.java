package com.nate.atucafeteria.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.models.FoodModel;

public class OrderDetails extends AppCompatActivity {
    private ImageView foodImage;
    private ImageView addOrder;
    private ImageView subOrder;
    private TextView foodName;
    private TextView foodPrice;
    private TextView quantity;
    private MaterialButton addBtn;
    private int orderQuantity = 1;
    private FoodModel foodModel;
    private double pricePerUnit;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        foodModel = getIntent().getParcelableExtra("menu");

        // Load image and set text
        Glide.with(this).load(foodModel.getImageUrl()).into(foodImage);
        foodName.setText(foodModel.getName());
        foodPrice.setText(foodModel.getPrice());
        addBtn.setText("Add\n"+foodModel.getPrice());

        // Extract price from string and convert to double
        pricePerUnit = extractPrice(foodModel.getPrice());

        addOrder.setOnClickListener(view -> {
            orderQuantity++;
            updatePrice();
            updateSubOrderButtonState();
        });

        subOrder.setOnClickListener(view -> {
            if (orderQuantity > 1) {
                orderQuantity--;
                updatePrice();
                updateSubOrderButtonState();
            }
        });

        updateSubOrderButtonState();  // Initial state check

        addBtn.setOnClickListener(view -> {
            // Create an Intent to navigate to the ProcessOrder class
            Intent intent = new Intent(OrderDetails.this, ProcessOrder.class);

            // Optionally, pass data to the ProcessOrder activity
            intent.putExtra("foodName", foodModel.getName());
            intent.putExtra("totalPrice", orderQuantity * pricePerUnit);
            intent.putExtra("foodImage", foodModel.getImageUrl());

            // Start the ProcessOrder activity
            startActivity(intent);
        });
    }

    private void initViews() {
        foodImage = findViewById(R.id.food_image);
        foodName = findViewById(R.id.food_name);
        foodPrice = findViewById(R.id.food_price);
        addOrder = findViewById(R.id.add_order);
        subOrder = findViewById(R.id.sub_order);
        quantity = findViewById(R.id.quantity);
        addBtn = findViewById(R.id.add_btn);
    }

    private void updatePrice() {
        double totalPrice = orderQuantity * pricePerUnit;
        addBtn.setText(String.format("Add\nGHC %.2f", totalPrice));
        quantity.setText(String.valueOf(orderQuantity));
    }

    private void updateSubOrderButtonState() {
        if (orderQuantity > 1) {
            subOrder.setAlpha(1.0f); // Make button fully visible
            subOrder.setEnabled(true);
        } else {
            subOrder.setAlpha(0.5f); // Make button semi-transparent
            subOrder.setEnabled(false);
        }
    }

    private double extractPrice(String priceString) {
        // Assuming the price format is "GHC 12.00", this will remove "GHC " and convert the rest to double
        return Double.parseDouble(priceString.replace("GHC ", ""));
    }
}

//
//
//public class OrderDetails extends AppCompatActivity {
//    private ImageView foodImage;
//    private ImageView addOrder;
//    private ImageView subOrder;
//    private TextView foodName;
//    private TextView foodPrice;
//    private TextView quantity;
//    private MaterialButton addBtn;
//    private int orderQuantity = 1;
//    private double pricePerUnit;
//    private FoodModel foodModel;
//
//    @SuppressLint("ResourceAsColor")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_order_details);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        initViews();
//        foodModel = getIntent().getParcelableExtra("menu");
//
//        // Load image and set text
//        Glide.with(this).load(foodModel.getImageUrl()).into(foodImage);
//        foodName.setText(foodModel.getName());
//        foodPrice.setText(foodModel.getPrice());
//
//        // Extract price from string and convert to double
//        pricePerUnit = extractPrice(foodModel.getPrice());
//
//        addOrder.setOnClickListener(view -> {
//            orderQuantity++;
//            updatePriceAndButton();
//            updateSubOrderButtonState();
//        });
//
//        subOrder.setOnClickListener(view -> {
//            if (orderQuantity > 1) {
//                orderQuantity--;
//                updatePriceAndButton();
//                updateSubOrderButtonState();
//            }
//        });
//
//        updatePriceAndButton(); // Initial price update
//        updateSubOrderButtonState(); // Initial state check
//    }
//
//    private void initViews() {
//        foodImage = findViewById(R.id.food_image);
//        foodName = findViewById(R.id.food_name);
//        foodPrice = findViewById(R.id.food_price);
//        addOrder = findViewById(R.id.add_order);
//        subOrder = findViewById(R.id.sub_order);
//        quantity = findViewById(R.id.quantity);
//        addBtn = findViewById(R.id.add_btn);
//    }
//
//    private void updatePriceAndButton() {
//        double totalPrice = orderQuantity * pricePerUnit;
//        quantity.setText(String.valueOf(orderQuantity));
//        addBtn.setText(String.format("Add GHC %.2f", totalPrice));
//    }
//
//    private void updateSubOrderButtonState() {
//        if (orderQuantity > 1) {
//            subOrder.setAlpha(1.0f); // Make button fully visible
//            subOrder.setEnabled(true);
//        } else {
//            subOrder.setAlpha(0.5f); // Make button semi-transparent
//            subOrder.setEnabled(false);
//        }
//    }
//
//    private double extractPrice(String priceString) {
//        // Assuming the price format is "GHC 12.00", this will remove "GHC " and convert the rest to double
//        return Double.parseDouble(priceString.replace("GHC ", ""));
//    }
//}
