package com.sinhvien.doan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<Recipe> favoriteList;
    private Context context;

    public FavoriteAdapter(Context context, List<Recipe> favoriteList) {
        this.context = context;
        this.favoriteList = new ArrayList<>(favoriteList);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Recipe recipe = favoriteList.get(position);
        holder.tvRecipeName.setText(recipe.getRecipeName());
        holder.tvIngredients.setText(recipe.getIngredients());

        // Load hình ảnh bằng Glide
        Glide.with(context)
                .load(recipe.getImgSource())
                .placeholder(R.drawable.dessert)
                .error(R.drawable.donut_icon)
                .into(holder.imAvatar);

        // Khi click vào item, mở RecipeDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getRecipeId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imAvatar;
        TextView tvRecipeName, tvIngredients;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imAvatar = itemView.findViewById(R.id.ivAvatar);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvIngredients = itemView.findViewById(R.id.tvIngredients);
        }
    }
}
