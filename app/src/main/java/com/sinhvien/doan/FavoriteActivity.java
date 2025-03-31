package com.sinhvien.doan;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView favoritesRecyclerView;
    private FavoriteAdapter favoriteAdapter;
    private ArrayList<Recipe> favoriteRecipes;
    private DatabaseHelper databaseHelper;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Nút quay lại
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Khởi tạo RecyclerView
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách yêu thích từ database
        databaseHelper = new DatabaseHelper(this);
        String firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        int userId = databaseHelper.getUserId(firebaseUid);
        favoriteRecipes = getFavoriteRecipes(userId);

        // Cài đặt Adapter
        favoriteAdapter = new FavoriteAdapter(this, favoriteRecipes);
        favoritesRecyclerView.setAdapter(favoriteAdapter);
    }

    // Lấy danh sách công thức yêu thích của user
    private ArrayList<Recipe> getFavoriteRecipes(int userId) {
        ArrayList<Recipe> favoriteList = new ArrayList<>();
        Cursor cursor = databaseHelper.getUserFavorites(userId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_RECIPE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_TEN_RECIPE));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_INGREDIENTS));
                String imgUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_IMG_URL));

                favoriteList.add(new Recipe(recipeId, name, ingredients, imgUrl));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return favoriteList;
    }
}
