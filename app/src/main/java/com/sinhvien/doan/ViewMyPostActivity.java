package com.sinhvien.doan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

public class ViewMyPostActivity extends AppCompatActivity {
    private RecyclerView rvMyPosts;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> myPostsList;
    private MyDataBase myDatabase;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_post);

        // Khởi tạo
        rvMyPosts = findViewById(R.id.rvMyPosts);
        Button btnBack = findViewById(R.id.btnBack);
        myPostsList = new ArrayList<>();
        myDatabase = new MyDataBase(this);
        databaseHelper = new DatabaseHelper(this);

        // Lấy user_id từ Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            int userId = databaseHelper.getUserId(user.getUid());
            loadMyPosts(userId); // Tải bài viết của người dùng
        }

        // Cấu hình RecyclerView
        rvMyPosts.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(this, myPostsList);
        rvMyPosts.setAdapter(recipeAdapter);

        // Xử lý nút Quay lại
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadMyPosts(int userId) {
        Cursor cursor = myDatabase.getRecipeByUserId(userId); // Truy vấn theo user_id
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_TEN_RECIPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_INGREDIENTS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_STEPS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_IMG_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_DOKHO))
                );
                myPostsList.add(recipe);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}