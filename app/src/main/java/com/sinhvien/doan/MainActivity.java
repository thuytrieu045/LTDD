package com.sinhvien.doan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Xử lý sự kiện nhấn nút "Sweet Desert"
        Button button = findViewById(R.id.btnSweetDesert);
        Intent intent = new Intent(this, Category1Activity.class);
        button.setOnClickListener(v -> v.getContext().startActivity(intent));

        // Xử lý sự kiện nhấn nút "Breakfast"
        Button btnBreakfast = findViewById(R.id.btnBreakfast);
        btnBreakfast.setOnClickListener(v -> {
            Intent intent2 = new Intent(MainActivity.this, Category2Activity.class);
            startActivity(intent2);
        });

        // Xử lý sự kiện nhấn nút "Birthday Cake"
        Button btnbirthdaycake = findViewById(R.id.btnBirthdayCake);
        btnbirthdaycake.setOnClickListener(v -> {
            Intent intent3 = new Intent(MainActivity.this, Category3Activity.class);
            startActivity(intent3);
        });

        // Xử lý sự kiện nhấn nút "View Information"
        Button btnViewInfo = findViewById(R.id.btnViewInformation);
        btnViewInfo.setOnClickListener(v -> {
            Intent intent4 = new Intent(MainActivity.this, SpecialCakeActivity.class);
            startActivity(intent4);
        });

        // Xử lý sự kiện BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.navMenu);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.mnHome) {
                    Toast.makeText(MainActivity.this, "Đang ở Home", Toast.LENGTH_SHORT).show();
                    return true; // Đang ở trang Home, không cần chuyển
                } else if (itemId == R.id.mnPost) {
                    Toast.makeText(MainActivity.this, "Chuyển sang Post", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, AddRecipeActivity.class));
                    return true;
                } else if (itemId == R.id.mnFavorite) {
                    Toast.makeText(MainActivity.this, "Chuyển sang Favorite", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                    return true;
                } else if (itemId == R.id.mnSearch) {
                    Toast.makeText(MainActivity.this, "Chuyển sang Search", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, SearchActivity.class));
                    return true;
                } else if (itemId == R.id.mnAccount) {
                    Toast.makeText(MainActivity.this, "Chuyển sang Profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, AccountActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Xử lý tự động căn lề phù hợp với hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}