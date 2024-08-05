package com.nate.atucafeteria.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.fragments.HomeFragment;
import com.nate.atucafeteria.fragments.OrderFragment;
import com.nate.atucafeteria.fragments.ProfileFragment;
import com.nate.atucafeteria.fragments.SearchFragment;

public class Home extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        initViews();
        replaceFragment(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home){
                    replaceFragment(new HomeFragment());
                    return true;
                }else if (itemId == R.id.search){
                    replaceFragment(new SearchFragment());
                    return true;
                }else if (itemId == R.id.orders){
                    replaceFragment(new OrderFragment());
                    return true;
                }else if (itemId == R.id.profile){
                    replaceFragment(new ProfileFragment());
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    private void initViews(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}