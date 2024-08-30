package com.nate.atucafeteria.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.adapters.AdminOrdersAdapter;
import com.nate.atucafeteria.models.FoodModel;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity {

    private ExtendedFloatingActionButton addMenu;
    private MaterialToolbar topAppBar;
    private RecyclerView allFoodRecycler;
    private ArrayList<FoodModel> foodModels;
    private ImageView backBtn;
    private AdminOrdersAdapter ordersAdapter;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

//        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int menuId = item.getItemId();
//                if (menuId == R.id.all_menu){
//                    startActivity(new Intent(AdminHome.this, AllAdminMenu.class));
//                    finish();
//                } else if (menuId == R.id.logout) {
//                    firebaseAuth.signOut();
//                    startActivity(new Intent(AdminHome.this, Login.class));
//                    finish();
//                }
//                return false;
//            }
//        });

        allFoodRecycler.setHasFixedSize(true);
        allFoodRecycler.setLayoutManager(new LinearLayoutManager(this));
        ordersAdapter = new AdminOrdersAdapter(foodModels, this, position -> {
            // Handle item click here
            //Toast.makeText(this, "This is: "+position, Toast.LENGTH_SHORT).show();
            openOrderDetailActivity(foodModels.get(position));
        });
        allFoodRecycler.setAdapter(ordersAdapter);



//        allFoodRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (dy > 0 && addMenu.isExtended()) {
//                    // User scrolled down and the FAB is currently extended - shrink it
//                    addMenu.shrink();
//                } else if (dy < 0 && !addMenu.isExtended()) {
//                    // User scrolled up and the FAB is currently not extended - extend it
//                    addMenu.extend();
//                }
//            }
//        });


        fetchAllOrdersFromFirebase();
    }

    private void initViews(){
        addMenu = findViewById(R.id.add_item);
        allFoodRecycler = findViewById(R.id.all_orders);
        backBtn = findViewById(R.id.back_btn);
        topAppBar = findViewById(R.id.topAppBar);

        foodModels = new ArrayList<>();


        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void fetchAllOrdersFromFirebase() {
        DatabaseReference allOrders = FirebaseDatabase.getInstance().getReference("cafeteria_orders");
        allOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodModels.clear(); // Clear the existing list to avoid duplicates

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) { // Iterate over each user
                    for (DataSnapshot orderSnapshot : userSnapshot.child("orders").getChildren()) { // Iterate over each order
                        FoodModel model = orderSnapshot.getValue(FoodModel.class);
                        if (model != null) {
                            foodModels.add(model); // Add order to the list
                        }
                    }
                }

                if (foodModels.isEmpty()) {
                    Toast.makeText(AdminHome.this, "No orders found", Toast.LENGTH_SHORT).show();
                }

                ordersAdapter.notifyDataSetChanged(); // Notify the adapter about data changes
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminHome.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openOrderDetailActivity(FoodModel foodModel){
        Intent intent = new Intent(AdminHome.this, AdminOrderDetails.class);
        intent.putExtra("food", foodModel);
        startActivity(intent);
    }


}