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

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.ViewHolder> {

   private ArrayList<FoodModel> models;
   private Context context;
   private FoodItemClicked foodItemClicked;


   public interface FoodItemClicked{
       void foodClicked(int position);
   }

    public AdminOrdersAdapter(ArrayList<FoodModel> models, Context context, FoodItemClicked foodItemClicked) {
        this.models = models;
        this.context = context;
        this.foodItemClicked = foodItemClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodModel foodModel = models.get(position);
        Glide.with(context).load(foodModel.getImageUrl()).into(holder.foodImage);
        holder.foodName.setText(foodModel.getName());
        holder.foodPrice.setText(foodModel.getPrice());
        holder.deliveryStatus.setText(foodModel.getDeliveryStatus());
        holder.dateDelivered.setText(foodModel.getOrderTime());
        holder.itemView.setOnClickListener(view -> foodItemClicked.foodClicked(position));

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView foodImage;
        private TextView foodName, foodPrice, dateDelivered, deliveryStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            dateDelivered = itemView.findViewById(R.id.date_delivered);
            deliveryStatus = itemView.findViewById(R.id.delivery_status);
        }
    }
}
