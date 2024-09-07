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


        foodModels.add(new FoodModel("1", "https://cdn-images-1.medium.com/max/726/1*XDjyQ6dBVMdybEySuQlHGw.jpeg", "Fufu", "GHC 20.00", "GHC 25.00", "5-7 mins", "fufu"));
        foodModels.add(new FoodModel("2", "https://i.ytimg.com/vi/J8IDVNy8BEE/maxresdefault.jpg", "Banku", "GHC 15.00", "GHC 20.00", "5-7 mins", "banku"));
        foodModels.add(new FoodModel("3", "https://eatwellabi.com/wp-content/uploads/2021/09/How-to-cook-yam-720x630.jpg", "Boiled Yam", "GHC 10.00", "GHC 15.00", "12 mins", "yam"));
        foodModels.add(new FoodModel("4", "https://vaya.in/recipes/wp-content/uploads/2018/02/Boiled-rice.jpg", "Boiled Rice with Stew", "GHC 15.00", "GHC 18.00", "10-20 mins", "rice"));
        foodModels.add(new FoodModel("5", "https://lifeloveandgoodfood.com/wp-content/uploads/2023/03/chicken_fried_rice00032a-1200x1200-1.jpg", "Fried Rice", "GHC 19.50", "GHC 22.00", "15-30 mins", "fried_rice"));
        foodModels.add(new FoodModel("6", "https://metrotvonline.com/wp-content/uploads/2024/03/Ghana-Waakye.jpg.webp", "Waakye with Stew", "GHC 15.00", "GHC 20.00", "5 - 12 mins", "waakye"));
        foodModels.add(new FoodModel("7", "https://www.lubzonline.com/wp-content/uploads/2021/02/E10DB4E6-4731-4829-A5F1-B36B0AC88C98-scaled.jpeg", "Jollof Rice", "GHC 19.00", "GHC 22.00", "0-15 mins", "jollof"));


//        foodModels.add(new FoodModel("1", "https://nkechiajaeroh.com/wp-content/uploads/2020/12/Nigerian-fried-rice-recipe-main-photo-3.jpg", "Fried Rice", "GHC 12.00", "GHC 15.00", "10-15 mins"));
//        foodModels.add(new FoodModel("2", "https://kumasiafricancousin.com/wp-content/uploads/2023/10/IMG-20231019-WA0034.jpg", "Banku With Tilapia", "GHC 20.00", "GHC 25.00", "5-7 mins"));
//        foodModels.add(new FoodModel("3", "https://cdn.pixabay.com/photo/2016/11/29/12/45/beverage-1869598_1280.jpg", "Coffee with Milk", "GHC 10.00", "GHC 15.00", ""));
//        foodModels.add(new FoodModel("4", "https://cdn.pixabay.com/photo/2014/01/09/10/14/kimchi-fried-rice-241051_1280.jpg", "Jollof with 2ps Grilled Chicken", "GHC 35.00", "GHC 50.00", "10-20 mins"));
//        foodModels.add(new FoodModel("5", "https://141832043.cdn6.editmysite.com/uploads/1/4/1/8/141832043/s457762805251037124_p59_i2_w2000.jpeg", "Kenkey with peppered chicken", "GHC 19.50", "GHC 22.00", "15-30 mins"));
//        foodModels.add(new FoodModel("6", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6JHMsjp0BRwNCmMAlYHJafuWP5cRk9kH4xg&s", "Yam Chips with Chilli Gizzay", "GHC 15.00", "GHC 20.00", "5 - 12 mins"));
//        foodModels.add(new FoodModel("7", "https://cdn.pixabay.com/photo/2019/09/26/18/23/republic-of-korea-4506696_1280.jpg", "6pcs Chicken", "GHC 9.00", "GHC 12.00", "0-15 mins"));
//        foodModels.add(new FoodModel("8", "https://cdn.pixabay.com/photo/2020/05/26/09/38/coca-cola-5222420_1280.jpg", "Bottle Coke", "GHC 12.00", "GHC 18.00", "20-30mins"));
//        foodModels.add(new FoodModel("9", "https://foodienotachef.com/wp-content/uploads/2021/02/ovengrilledtilapifried-1.png", "Grilled Tilapia", "GHC 17.00", "GHC 22.00", "7 - 15 mins"));
//        foodModels.add(new FoodModel("10", "https://www.letsfryaway.com/wp-content/uploads/2019/07/2pcsfries.jpg", "Potato chips with 2ps Chicken", "GHC 42.00", "GHC 50.00", "3 - 5 mins"));
//        foodModels.add(new FoodModel("11", "https://api.vip.foodnetwork.ca/wp-content/uploads/2023/01/groundnut-soup-feat.jpg", "Rice Balls and groundnut soup", "GHC 34.00", "GHC 40.00", "5 - 12 mins"));



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

        //fetchMenuFromDB();

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