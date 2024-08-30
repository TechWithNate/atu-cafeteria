package com.nate.atucafeteria.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.activities.EditProfile;
import com.nate.atucafeteria.activities.Login;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private View view;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextView username, contact, gender, aboutUs, email;
    private MaterialButton logout, editBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        initViews();
        loadUserData();
        logout.setOnClickListener(view1 -> {
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), Login.class));
            getActivity().finish();
        });

        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfile.class);
            intent.putExtra("username", username.getText().toString());
            intent.putExtra("contact", contact.getText().toString());
            startActivity(intent);
        });

        return view;
    }

    private void initViews(){
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("cafeteria_orders");
        username = view.findViewById(R.id.username);
        contact = view.findViewById(R.id.contact);
        email = view.findViewById(R.id.email);
        gender = view.findViewById(R.id.gender);
        aboutUs = view.findViewById(R.id.about);
        logout = view.findViewById(R.id.logout);
        editBtn = view.findViewById(R.id.edit_profile);
    }


    private void loadUserData() {
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    String name = snapshot.child("username").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    username.setText(name);
                    email.setText(firebaseAuth.getCurrentUser().getEmail());
                    gender.setText(snapshot.child("gender").getValue(String.class));

                    contact.setText(snapshot.child("contact").getValue(String.class));


                } else {
                    Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

}
