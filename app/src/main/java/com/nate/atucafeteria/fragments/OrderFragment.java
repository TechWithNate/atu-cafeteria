package com.nate.atucafeteria.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.adapters.OrdersAdapter;
import com.nate.atucafeteria.models.FoodModel;

import java.util.ArrayList;

public class OrderFragment extends Fragment {

    private View view;
    private ArrayList<FoodModel> foodModels;
    private RecyclerView ordersRecycler;
    private OrdersAdapter ordersAdapter;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_fragment, container, false);
        initViews();
        ordersRecycler.setHasFixedSize(true);
        ordersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersAdapter = new OrdersAdapter(foodModels, getContext());
        ordersRecycler.setAdapter(ordersAdapter);

        fetchFromFirebase();

        return view;
    }

    private void initViews(){
        foodModels = new ArrayList<>();
        ordersRecycler = view.findViewById(R.id.orders_recycler);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void fetchFromFirebase() {
        DatabaseReference allOrders = FirebaseDatabase.getInstance().getReference("cafeteria_orders").child(firebaseAuth.getUid()).child("orders");
        allOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    FoodModel model = snapshot.getValue(FoodModel.class);
                    if (model != null){
                        foodModels.add(model);
                    }else {
                        Toast.makeText(getContext(), "Order not found", Toast.LENGTH_SHORT).show();
                    }
                }

                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

}
