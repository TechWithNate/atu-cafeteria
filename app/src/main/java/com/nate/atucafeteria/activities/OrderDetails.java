package com.nate.atucafeteria.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.nate.atucafeteria.R;

public class OrderDetails extends AppCompatActivity {
    private ImageView foodImage;
    private ImageView addOrder;
    private ImageView subOrder;
    private TextView foodName;
    private TextView foodPrice;
    private TextView quantity;
    private MaterialButton addBtn;
    private int orderQuantity = 1;

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
        //quantity.setText(String.valueOf(orderQuantity));
        addOrder.setOnClickListener(view -> {
            orderQuantity++;
            Log.d("TAG", "onCreate: "+orderQuantity);
            quantity.setText(String.valueOf(orderQuantity));
        });
        subOrder.setOnClickListener(view -> {
            if (orderQuantity > 0){
                orderQuantity--;
                Log.d("TAG", "onCreate: "+orderQuantity);
                quantity.setText(String.valueOf(orderQuantity));
            }else {
                subOrder.setBackgroundColor(R.color.main_light1);
            }
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
}