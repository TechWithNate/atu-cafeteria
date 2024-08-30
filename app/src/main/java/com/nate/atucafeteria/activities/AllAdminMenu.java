package com.nate.atucafeteria.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.nate.atucafeteria.adapters.AdminFoodAdapter;
import com.nate.atucafeteria.models.FoodModel;

import java.util.ArrayList;

public class AllAdminMenu extends AppCompatActivity {

    private RecyclerView foodRecycler;
    private ArrayList<FoodModel> foodModels;
    private AdminFoodAdapter adminFoodAdapter;
    private MaterialToolbar topAppBar;
    private ExtendedFloatingActionButton addMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_admin_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        topAppBar.setOnMenuItemClickListener(item -> {
            int menuId = item.getItemId();
            if (menuId == R.id.all_menu){
                startActivity(new Intent(AllAdminMenu.this, AdminHome.class));
                finish();
            } else if (menuId == R.id.logout) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(AllAdminMenu.this, Login.class));
                finish();
            }
            return false;
        });


        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllAdminMenu.this, AddMenu.class));
            }
        });

        foodRecycler.setHasFixedSize(true);
        foodRecycler.setLayoutManager(new LinearLayoutManager(this));
        adminFoodAdapter = new AdminFoodAdapter(this, foodModels, position -> {
            Intent intent = new Intent(AllAdminMenu.this, AdminEditMenu.class);
            intent.putExtra("id", foodModels.get(position).getId());
            startActivity(intent);
        });
        foodRecycler.setAdapter(adminFoodAdapter);


        foodRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && addMenu.isExtended()) {
                    // User scrolled down and the FAB is currently extended - shrink it
                    addMenu.shrink();
                } else if (dy < 0 && !addMenu.isExtended()) {
                    // User scrolled up and the FAB is currently not extended - extend it
                    addMenu.extend();
                }
            }
        });

        fetchMenuFromDB();

    }

    private void fetchMenuFromDB() {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cafeteria_orders");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    foodModels.clear();
                    for (DataSnapshot snapshot: dataSnapshot.child("menu").getChildren()){
                        Log.d("AllAdminMenu", "Snapshot Value: " + snapshot.getValue());
                        FoodModel model = snapshot.getValue(FoodModel.class);
                        if (model != null){
                            foodModels.add(model);
                        }else {
                            Toast.makeText(AllAdminMenu.this, "Menu not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                   adminFoodAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(AllAdminMenu.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


    }

    private void initViews(){
        foodRecycler = findViewById(R.id.food_recycler);
        foodModels = new ArrayList<>();
        topAppBar = findViewById(R.id.topAppBar);
        addMenu = findViewById(R.id.add_item);
    }


}