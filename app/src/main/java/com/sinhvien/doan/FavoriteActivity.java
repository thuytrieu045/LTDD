package com.sinhvien.doan;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView rvFavorites;
    private ProductAdapter productAdapter;
    private RecipeAdapter recipeAdapter;
    private List<Product> favoriteProducts;
    private List<Recipe> favoriteRecipes;
    private MyDataBase myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Khởi tạo
        rvFavorites = findViewById(R.id.rvFavorites);
        favoriteProducts = new ArrayList<>();
        favoriteRecipes = new ArrayList<>();
        myDataBase = new MyDataBase(this);

        // Load dữ liệu yêu thích
        loadFavoriteProducts();
        loadFavoriteRecipes();

        // Cài đặt adapter
        if (!favoriteProducts.isEmpty()) {
            productAdapter = new ProductAdapter(this, favoriteProducts);
            rvFavorites.setLayoutManager(new LinearLayoutManager(this));
            rvFavorites.setAdapter(productAdapter);
        } else if (!favoriteRecipes.isEmpty()) {
            recipeAdapter = new RecipeAdapter(this, favoriteRecipes);
            rvFavorites.setLayoutManager(new LinearLayoutManager(this));
            rvFavorites.setAdapter(recipeAdapter);
        }

        // Nút quay lại
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadFavoriteProducts() {
        SQLiteDatabase db = myDataBase.getReadableDatabase();
        Cursor cursor = db.query(MyDataBase.TABLE_FAVORITE_PRODUCTS, null, null, null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBase.COT_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBase.COT_PRODUCT_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBase.COT_PRODUCT_DESC));
                int imageResource = cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBase.COT_PRODUCT_IMAGE));
                
                favoriteProducts.add(new Product(id, name, description, "", imageResource));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void loadFavoriteRecipes() {
        SQLiteDatabase db = myDataBase.getReadableDatabase();
        Cursor cursor = db.query(MyDataBase.TABLE_FAVORITE_RECIPES, null, null, null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBase.COT_FAV_RECIPE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBase.COT_FAV_RECIPE_NAME));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBase.COT_FAV_RECIPE_INGREDIENTS));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBase.COT_FAV_RECIPE_IMAGE));
                
                favoriteRecipes.add(new Recipe(id, name, ingredients, "", 0, imageUrl, 0, 0, ""));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
