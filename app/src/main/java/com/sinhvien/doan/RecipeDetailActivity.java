package com.sinhvien.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    private MyDataBase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Initialize Database
        myDatabase = new MyDataBase(this);

        // ✅ Get Recipe ID
        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        if (recipeId != -1) {
            loadRecipeDetails(recipeId);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy công thức", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadRecipeDetails(int recipeId) {
        Recipe recipe = myDatabase.getRecipeById(recipeId);
        if (recipe != null) {
            // ✅ Show Post in ViewPostActivity
            Intent intent = new Intent(RecipeDetailActivity.this, RecipePostActivity.class);
            intent.putExtra("title", recipe.getRecipeName());
            intent.putExtra("difficulty", "Trung bình");  // You can set real difficulty here
            intent.putExtra("ingredients", recipe.getIngredients());
            intent.putExtra("steps", recipe.getSteps());
            intent.putExtra("time", "40 phút");  // Set real time if available
            intent.putExtra("imageUrl", recipe.getImgSource());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Không tìm thấy công thức nấu ăn", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
