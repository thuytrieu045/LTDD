package com.sinhvien.doan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> lstRecipe;
    private List<Recipe> lstRecipeFull;
    private Context context;
    private MyDataBase myDataBase;

    public RecipeAdapter(Context context, List<Recipe> lstRecipe) {
        this.context = context;
        this.lstRecipe = new ArrayList<>(lstRecipe);
        this.lstRecipeFull = new ArrayList<>(lstRecipe);
        this.myDataBase = new MyDataBase(context);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View recipeView = inflater.inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe item = lstRecipe.get(position);
        holder.tvRecipeName.setText(item.getRecipeName());
        holder.tvIngredients.setText(item.getIngredients());

        // Load image using Glide
        Glide.with(context)
                .load(item.getImgSource())
                .placeholder(R.drawable.dessert)
                .error(R.drawable.donut_icon)
                .into(holder.imAvatar);

        // Kiểm tra trạng thái yêu thích
        boolean isFavorite = myDataBase.isRecipeFavorite(item.getRecipeId());
        holder.btnFavorite.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);

        // Xử lý sự kiện click vào biểu tượng yêu thích
        holder.btnFavorite.setOnClickListener(v -> {
            boolean newFavoriteState = !isFavorite;
            if (newFavoriteState) {
                myDataBase.addRecipeToFavorites(item.getRecipeId(), item.getRecipeName(), item.getIngredients(), item.getImgSource());
                Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                myDataBase.removeRecipeFromFavorites(item.getRecipeId());
                Toast.makeText(context, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
            }
            holder.btnFavorite.setImageResource(newFavoriteState ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        });

        // Khi nhấn vào item, mở RecipeDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", item.getRecipeId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lstRecipe.size();
    }

    // Thêm phương thức filter để tìm kiếm công thức
    public void filter(String query) {
        lstRecipe.clear();
        if (query.isEmpty()) {
            lstRecipe.addAll(lstRecipeFull);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Recipe recipe : lstRecipeFull) {
                if (recipe.getRecipeName().toLowerCase().contains(lowerCaseQuery)) {
                    lstRecipe.add(recipe);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imAvatar;
        TextView tvRecipeName, tvIngredients;
        ImageButton btnFavorite;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imAvatar = itemView.findViewById(R.id.ivAvatar);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvIngredients = itemView.findViewById(R.id.tvIngredients);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}