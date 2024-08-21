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
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.activities.OrderDetails;
import com.nate.atucafeteria.adapters.FoodAdapter;
import com.nate.atucafeteria.models.FoodModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements FoodAdapter.ItemClickedListener {

    private View view;
    private TextView location;
    private SearchView searchBar;
    private ImageSlider slider;
    private RecyclerView foodRecycler;
    private FoodAdapter foodAdapter;
    private ArrayList<FoodModel> foodModels;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        initViews();

        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel("https://cdn.pixabay.com/photo/2023/09/05/12/44/mug-8235059_1280.jpg", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://cdn.pixabay.com/photo/2016/09/15/19/24/salad-1672505_1280.jpg", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://cdn.pixabay.com/photo/2014/04/22/02/56/pizza-329523_1280.jpg", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://cdn.pixabay.com/photo/2017/08/30/11/39/glass-2696759_1280.jpg", ScaleTypes.FIT));

        slider.setImageList(imageList, ScaleTypes.FIT);
        foodModels = new ArrayList<>();

        foodModels.add(new FoodModel("1", "https://nkechiajaeroh.com/wp-content/uploads/2020/12/Nigerian-fried-rice-recipe-main-photo-3.jpg", "Fried Rice", "GHC 12.00", "GHC 15.00", "10-15 mins"));
        foodModels.add(new FoodModel("2", "https://kumasiafricancousin.com/wp-content/uploads/2023/10/IMG-20231019-WA0034.jpg", "Banku With Tilapia", "GHC 20.00", "GHC 25.00", "5-7 mins"));
        foodModels.add(new FoodModel("3", "https://cdn.pixabay.com/photo/2016/11/29/12/45/beverage-1869598_1280.jpg", "Coffee with Milk", "GHC 10.00", "GHC 15.00", ""));
        foodModels.add(new FoodModel("4", "https://cdn.pixabay.com/photo/2014/01/09/10/14/kimchi-fried-rice-241051_1280.jpg", "Jollof with 2ps Grilled Chicken", "GHC 35.00", "GHC 50.00", "10-20 mins"));
        foodModels.add(new FoodModel("5", "https://141832043.cdn6.editmysite.com/uploads/1/4/1/8/141832043/s457762805251037124_p59_i2_w2000.jpeg", "Kenkey with peppered chicken", "GHC 19.50", "GHC 22.00", "15-30 mins"));
        foodModels.add(new FoodModel("6", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6JHMsjp0BRwNCmMAlYHJafuWP5cRk9kH4xg&s", "Yam Chips with Chilli Gizzay", "GHC 15.00", "GHC 20.00", "5 - 12 mins"));
        foodModels.add(new FoodModel("7", "https://cdn.pixabay.com/photo/2019/09/26/18/23/republic-of-korea-4506696_1280.jpg", "6pcs Chicken", "GHC 9.00", "GHC 12.00", "0-15 mins"));
        foodModels.add(new FoodModel("8", "https://cdn.pixabay.com/photo/2020/05/26/09/38/coca-cola-5222420_1280.jpg", "Bottle Coke", "GHC 12.00", "GHC 18.00", "20-30mins"));
        foodModels.add(new FoodModel("9", "https://foodienotachef.com/wp-content/uploads/2021/02/ovengrilledtilapifried-1.png", "Grilled Tilapia", "GHC 17.00", "GHC 22.00", "7 - 15 mins"));
        foodModels.add(new FoodModel("10", "https://www.letsfryaway.com/wp-content/uploads/2019/07/2pcsfries.jpg", "Potato chips with 2ps Chicken", "GHC 42.00", "GHC 50.00", "3 - 5 mins"));
        foodModels.add(new FoodModel("11", "https://api.vip.foodnetwork.ca/wp-content/uploads/2023/01/groundnut-soup-feat.jpg", "Rice Balls and groundnut soup", "GHC 34.00", "GHC 40.00", "5 - 12 mins"));

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
