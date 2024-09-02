package com.nate.atucafeteria.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nate.atucafeteria.R;
import com.nate.atucafeteria.models.FoodModel;

import java.util.ArrayList;
import java.util.List;

public class AdminFoodAdapter extends RecyclerView.Adapter<AdminFoodAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<FoodModel> foodModels;
    private ArrayList<FoodModel> foodModelsFull; // List to hold the original data
    private ItemClickedListener itemClickedListener;

    public interface ItemClickedListener {
        void itemClicked(int position);
    }

    public AdminFoodAdapter(Context context, ArrayList<FoodModel> foodModels, ItemClickedListener itemClickedListener) {
        this.context = context;
        this.foodModels = foodModels;
        this.foodModelsFull = new ArrayList<>(foodModels); // Initialize the full list
        this.itemClickedListener = itemClickedListener;
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
        holder.foodPrice.setText("CHC "+foodModel.getPrice());
        holder.oldPrice.setText("GHC "+foodModel.getOldPrice());
        holder.readyTime.setText(foodModel.getReadyTime());
        Glide.with(context).load(foodModel.getImageUrl()).into(holder.foodImage);

        holder.itemView.setOnClickListener(view -> itemClickedListener.itemClicked(position));
    }

    @Override
    public int getItemCount() {
        return foodModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<FoodModel> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(foodModelsFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (FoodModel item : foodModelsFull) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                foodModels.clear();
                foodModels.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}


//public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
//
//    private Context context;
//    private ArrayList<FoodModel> foodModels;
//    private ItemClickedListener itemClickedListener;
//
//
//    public interface ItemClickedListener{
//        void itemClicked(int position);
//    }
//
//    public FoodAdapter(Context context, ArrayList<FoodModel> foodModels, ItemClickedListener itemClickedListener) {
//        this.context = context;
//        this.foodModels = foodModels;
//        this.itemClickedListener = itemClickedListener;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_layout, parent, false);
//
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        FoodModel foodModel = foodModels.get(position);
//
//        holder.foodName.setText(foodModel.getName());
//        holder.foodPrice.setText(foodModel.getPrice());
//        holder.oldPrice.setText(foodModel.getOldPrice());
//        holder.readyTime.setText(foodModel.getReadyTime());
//        Glide.with(context).load(foodModel.getImageUrl()).into(holder.foodImage);
//
//        holder.itemView.setOnClickListener(view -> itemClickedListener.itemClicked(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return foodModels.size();
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        private ImageView foodImage;
//        private TextView foodName;
//        private TextView foodPrice;
//        private TextView oldPrice;
//        private TextView readyTime;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            foodImage = itemView.findViewById(R.id.food_image);
//            foodName = itemView.findViewById(R.id.food_name);
//            foodPrice = itemView.findViewById(R.id.food_price);
//            oldPrice = itemView.findViewById(R.id.old_price);
//            readyTime = itemView.findViewById(R.id.ready_time);
//
//            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//        }
//    }
//}
