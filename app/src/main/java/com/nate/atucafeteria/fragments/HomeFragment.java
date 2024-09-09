package com.nate.atucafeteria.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.card.MaterialCardView;
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.activities.OrderDetails;
import com.nate.atucafeteria.adapters.FoodAdapter;
import com.nate.atucafeteria.models.FoodModel;
import com.nate.atucafeteria.models.Ingredient;
import com.nate.atucafeteria.models.Ingredients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements FoodAdapter.ItemClickedListener {

    private View view;
    private TextView location;
    private SearchView searchBar;
    private ImageSlider slider;
    private RecyclerView foodRecycler;
    private FoodAdapter foodAdapter;
    private ArrayList<FoodModel> foodModels;
    private TextView welcomTxt;
    private MaterialCardView sliderCard;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        initViews();

        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel("https://www.cookwithnabeela.com/wp-content/uploads/2024/02/AlooSamosa3.webp", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://www.allrecipes.com/thmb/lkmpKIBFPRbfATPBoR8E57KimqM=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/45921-crispy-and-creamy-doughnuts-DDMFS-4x3-4266734872014eeebaeddbe56452e18d.jpg", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://www.elmundoeats.com/wp-content/uploads/2024/02/Crispy-spring-rolls.jpg", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://www.jocooks.com/wp-content/uploads/2015/06/jelly-filled-rolls-6-500x500.jpg", ScaleTypes.FIT));

        slider.setImageList(imageList, ScaleTypes.FIT);
        foodModels = new ArrayList<>();

        Ingredients ingredients = new Ingredients();



//        foodModels.add(new FoodModel(
//                "1",
//                "",
//                "https://i.ytimg.com/vi/XsW5_YOifxw/maxresdefault.jpg",
//                "Banku with Okro",
//                "GHC 12.00",
//                "GHC 15.00",
//                "10-15 mins",
//                "Grilled chicken",
//                "12:00 PM",
//                "Accra",
//                "Delivery",
//                ingredients.getIngredientsForFood("Fufu")));
        foodModels.add(new FoodModel("1", "https://cdn-images-1.medium.com/max/726/1*XDjyQ6dBVMdybEySuQlHGw.jpeg", "Fufu", "GHC 20.00", "GHC 25.00", "5-7 mins", "fufu"));
        foodModels.add(new FoodModel("2", "https://i.ytimg.com/vi/J8IDVNy8BEE/maxresdefault.jpg", "Banku", "GHC 15.00", "GHC 20.00", "5-7 mins", "banku"));
        foodModels.add(new FoodModel("3", "https://eatwellabi.com/wp-content/uploads/2021/09/How-to-cook-yam-720x630.jpg", "Boiled Yam", "GHC 10.00", "GHC 15.00", "12 mins", "yam"));
        foodModels.add(new FoodModel("4", "https://vaya.in/recipes/wp-content/uploads/2018/02/Boiled-rice.jpg", "Boiled Rice with Stew", "GHC 15.00", "GHC 18.00", "10-20 mins", "rice"));
        foodModels.add(new FoodModel("5", "https://lifeloveandgoodfood.com/wp-content/uploads/2023/03/chicken_fried_rice00032a-1200x1200-1.jpg", "Fried Rice", "GHC 19.50", "GHC 22.00", "15-30 mins", "fried_rice"));
        foodModels.add(new FoodModel("6", "https://metrotvonline.com/wp-content/uploads/2024/03/Ghana-Waakye.jpg.webp", "Waakye with Stew", "GHC 15.00", "GHC 20.00", "5 - 12 mins", "waakye"));
        foodModels.add(new FoodModel("7", "https://www.lubzonline.com/wp-content/uploads/2021/02/E10DB4E6-4731-4829-A5F1-B36B0AC88C98-scaled.jpeg", "Jollof Rice", "GHC 19.00", "GHC 22.00", "0-15 mins", "jollof"));
//        foodModels.add(new FoodModel("8", "https://cdn.pixabay.com/photo/2020/05/26/09/38/coca-cola-5222420_1280.jpg", "Bottle Coke", "GHC 12.00", "GHC 18.00", "20-30mins"));
//        foodModels.add(new FoodModel("9", "https://foodienotachef.com/wp-content/uploads/2021/02/ovengrilledtilapifried-1.png", "Grilled Tilapia", "GHC 17.00", "GHC 22.00", "7 - 15 mins"));
//        foodModels.add(new FoodModel("10", "https://www.letsfryaway.com/wp-content/uploads/2019/07/2pcsfries.jpg", "Potato chips with 2ps Chicken", "GHC 42.00", "GHC 50.00", "3 - 5 mins"));
//        foodModels.add(new FoodModel("11", "https://api.vip.foodnetwork.ca/wp-content/uploads/2023/01/groundnut-soup-feat.jpg", "Rice Balls and groundnut soup", "GHC 34.00", "GHC 40.00", "5 - 12 mins"));

        // Example usage


        foodRecycler.setHasFixedSize(true);
        foodRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        foodAdapter = new FoodAdapter(getContext(), foodModels, this);
        foodRecycler.setAdapter(foodAdapter);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                foodAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                foodAdapter.getFilter().filter(newText);
                welcomTxt.setVisibility(View.GONE);
                sliderCard.setVisibility(View.GONE);
                return false;
            }
        });


        return view;
    }

    private void initViews(){
        location = view.findViewById(R.id.location);
        searchBar = view.findViewById(R.id.search_bar);
        slider = view.findViewById(R.id.image_slider);
        foodRecycler = view.findViewById(R.id.food_recycler);
        welcomTxt = view.findViewById(R.id.welcome_txt);
        sliderCard = view.findViewById(R.id.slider_card);
    }


    @Override
    public void itemClicked(int position) {
        openOrderPage(foodModels.get(position));
        Toast.makeText(getContext(), "Food menu: "+position, Toast.LENGTH_SHORT).show();
    }

    private void openOrderPage(FoodModel foodModel) {
        Intent intent = new Intent(getContext(), OrderDetails.class);
        intent.putExtra("menu", foodModel);
        startActivity(intent);
    }
}
