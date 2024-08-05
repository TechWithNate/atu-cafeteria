package com.nate.atucafeteria.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.nate.atucafeteria.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private View view;
    private TextView location;
    private EditText searchBar;
    private ImageSlider slider;
    private RecyclerView foodRecycler;

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
        imageList.add(new SlideModel("https://i0.wp.com/www.gbcghanaonline.com/wp-content/uploads/2023/12/stc-bus-scaled.webp", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://ocdn.eu/images/pulscms/Yzk7MDA_/161adef903a830f9c15d43af06fa6023.jpg", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTa8uxz-7ibdT85xxDGM_BFLpNGkp7gTvPxpw&s", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://i0.wp.com/africantraveltimes.com/wp-content/uploads/2020/11/STC-Ghana-Buses.png?resize=700%2C430&ssl=1", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://i.ytimg.com/vi/fy0dIuYQSGA/maxresdefault.jpg", ScaleTypes.FIT));


        slider.setImageList(imageList, ScaleTypes.FIT);

        return view;
    }

    private void initViews(){
        location = view.findViewById(R.id.location);
        searchBar = view.findViewById(R.id.search_bar);
        slider = view.findViewById(R.id.image_slider);
        foodRecycler = view.findViewById(R.id.food_recycler);
    }



}
