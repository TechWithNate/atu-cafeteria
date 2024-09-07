package com.nate.atucafeteria.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.models.FoodModel;
import com.nate.atucafeteria.models.Ingredient;
import com.nate.atucafeteria.models.Ingredients;

import java.util.ArrayList;
import java.util.List;

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
    private String ingredient;

    private ChipGroup chipGroup;
    //private List<String> ingredients;

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
        ingredient = foodModel.getIngred();

        ingredient = foodModel.getIngred();

        // Set up the ingredients based on the food type
        Ingredients ingredients = new Ingredients();
        List<Ingredient> ingredientList = new ArrayList<>();

        if (ingredient.equals("fufu")) {
            ingredientList = ingredients.getIngredientsForFood("Fufu");
        } else if (ingredient.equals("banku")) {
            ingredientList = ingredients.getIngredientsForFood("Banku");
        }else if (ingredient.equals("yam")){
            ingredientList = ingredients.getIngredientsForFood("Yam");
        }else if (ingredient.equals("rice")){
            ingredientList = ingredients.getIngredientsForFood("Rice");
        }else if (ingredient.equals("fried_rice")){
            ingredientList = ingredients.getIngredientsForFood("Fried_Rice");
        }else if (ingredient.equals("waakye")){
            ingredientList = ingredients.getIngredientsForFood("Waakye");
        }else if (ingredient.equals("jollof")){
            ingredientList = ingredients.getIngredientsForFood("Jollof");
        }

        addChipsToChipGroup(ingredientList);


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
        chipGroup = findViewById(R.id.chipGroup);
    }

    private void addChipsToChipGroup(@NonNull List<Ingredient> ingredientList) {
        for (Ingredient ingredient : ingredientList) {
            Chip chip = new Chip(this);
            chip.setText(ingredient.getName() + " GHC " + ingredient.getPrice());
            chip.setTextColor(getResources().getColor(android.R.color.black));
            chip.setChipBackgroundColorResource(R.color.chip_white); // Change as needed
            chip.setCheckable(true); // Make chips checkable


            chip.setChipIconResource(R.drawable.baseline_check);
            chip.setChipIconVisible(false);
            // Set a tag to store the price of the ingredient in the chip for easy access
            chip.setTag(ingredient.getPrice());

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                double ingredientPrice = (double) chip.getTag();
                if (isChecked) {
                    pricePerUnit += ingredientPrice; // Add ingredient price to total
                    chip.setChipIconVisible(true);
                    chip.setChipBackgroundColorResource(R.color.chip_dark);
                } else {
                    pricePerUnit -= ingredientPrice; // Subtract ingredient price from total
                    chip.setChipIconVisible(false);
                    chip.setChipBackgroundColorResource(R.color.chip_dark);
                }
                updatePrice();
            });

            chipGroup.addView(chip);
        }
    }


//    private void addChipsToChipGroup(@NonNull List<String> ingredientList) {
//        for (String ingredient : ingredientList) {
//            Chip chip = new Chip(this);
//            chip.setText(ingredient);
//            chip.setTextColor(getResources().getColor(android.R.color.black));
//            chip.setChipBackgroundColorResource(R.color.main_light1); // Change as needed
//            chip.setCheckable(true); // Optional: if you want them to be checkable
//            chipGroup.addView(chip);
//        }
//    }

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
