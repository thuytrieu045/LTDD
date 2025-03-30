package com.sinhvien.doan;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {
    private EditText edtTitle, edtDifficulty,edtTime,edtIngredients,edtSteps,edtImage;
    private Spinner spinnerCategory;
    private String selectedCategory;
    private DatabaseHelper databaseHelper;
    private List<Product> listProduct;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        databaseHelper = new DatabaseHelper(this);

        edtTitle = findViewById(R.id.edtTitle);
        edtDifficulty = findViewById(R.id.edtDifficulty);
        edtTime = findViewById(R.id.edtTime);
        edtIngredients = findViewById(R.id.edtIngredients);
        edtSteps = findViewById(R.id.edtSteps);
        edtImage = findViewById(R.id.edtImageURL);
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        Button btnSubmit = findViewById(R.id.btnSave);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.recipe_categories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "";
            }
        });

        // Nút quay lại trang Home
        btnBackToHome.setOnClickListener(v -> finish());

        // Nút đăng bài
        btnSubmit.setOnClickListener(v -> sendRecipeData());
    }


    private void sendRecipeData()
    {
        String name = edtTitle.getText().toString().trim();
        String difficulty = edtDifficulty.getText().toString().trim();
        String ingredients = edtIngredients.getText().toString().trim();
        String steps = edtSteps.getText().toString();
        String urlImg = edtImage != null ? edtImage.getText().toString() : "";
        int time = 0;
        try {
            time = Integer.parseInt(edtTime.getText().toString().trim());
            if (time <= 0) {
                Toast.makeText(this, "Thời gian phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ cho thời gian!", Toast.LENGTH_SHORT).show();
            return;
        }


        if(name.isEmpty() || difficulty.isEmpty() || ingredients.isEmpty() || steps.isEmpty()
        || selectedCategory.isEmpty())
        {
            Toast.makeText(this, "Bạn phải điền hết ô trống còn lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu công thức vào database
        int categoryValue = 0;
        if(selectedCategory.equals("Sweet Dessert")) {
            categoryValue = 1;
        }
        else if(selectedCategory.equals("Breakfast")) {
            categoryValue = 2;
        }
        else if(selectedCategory.equals("Birthday Cake")) {
            categoryValue = 3;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String firebaseUid = user.getUid(); // Lấy Firebase UID
            int userId = databaseHelper.getUserId(firebaseUid); // Chuyển sang user_id

            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("recipe_name", name);
            values.put("difficulty", difficulty);
            values.put("ingredients", ingredients);
            values.put("steps", steps);
            values.put("user_id", userId);
            values.put("cate", categoryValue);
            values.put("img_src", urlImg);
            values.put("time", time);

            long newRowId = db.insert("recipes", null, values);
            if (newRowId == -1) {
                Toast.makeText(this, "Lưu công thức thất bại!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lưu công thức thành công!", Toast.LENGTH_SHORT).show();
            }
        }

        //Chuyển sang ViewPostActivity
        Intent intent = new Intent(AddRecipeActivity.this, ViewPostActivity.class);
        intent.putExtra("title", name);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("ingredients", ingredients);
        intent.putExtra("steps", steps);
        intent.putExtra("time", time);
        intent.putExtra("imageUrl", urlImg);
        startActivity(intent);
        finish();
    }
}
