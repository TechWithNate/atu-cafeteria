package com.nate.atucafeteria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.models.FoodModel;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FoodModel> foodModels;

    public FoodAdapter(Context context, ArrayList<FoodModel> foodModels) {
        this.context = context;
        this.foodModels = foodModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodModel foodModel = foodModels.get(position);

        holder.foodName.setText(foodModel.getName());
        holder.foodPrice.setText(foodModel.getPrice());
        holder.oldPrice.setText(foodModel.getOldPrice());
        holder.readyTime.setText(foodModel.getReadyTime());
        Glide.with(context).load(foodModel.getImageUrl()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return foodModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView foodImage;
        private TextView foodName;
        private TextView foodPrice;
        private TextView oldPrice;
        private TextView readyTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            oldPrice = itemView.findViewById(R.id.old_price);
            readyTime = itemView.findViewById(R.id.ready_time);

        }
    }
}
