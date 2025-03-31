package com.sinhvien.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountActivity extends AppCompatActivity {
    private MyDataBase db;
    private DatabaseHelper databaseHelper;
    private TextView profileName;
    private Button btnSavePayment, btnSignout, btnBack, btnLinkAccounts, btnViewMyPosts;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_profile);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Khai báo và ánh xạ các ID từ layout
        profileName = findViewById(R.id.profileName);
        btnSavePayment = findViewById(R.id.btnSavePayment);
        btnSignout = findViewById(R.id.btnSignout);
        btnBack = findViewById(R.id.btnBack);
        btnLinkAccounts = findViewById(R.id.btnLinkAccounts);
        btnViewMyPosts = findViewById(R.id.btnViewMyPosts);
        db = new MyDataBase(this);

        // Hiển thị username từ DatabaseHelper
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = db.getUsername(databaseHelper.getUserId(user.getUid()));
        if (username != null) {
            profileName.setText(username);
        }
        else
            profileName.setText("User");

        // Lưu thông tin thanh toán (tạm thời chỉ hiển thị Toast)
        btnSavePayment.setOnClickListener(v -> {
            Toast.makeText(this, "Không có thông tin để lưu!", Toast.LENGTH_SHORT).show();
        });

        // Đăng xuất
        btnSignout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            finish();
        });

        // Quay lại
        btnBack.setOnClickListener(v -> finish());

        // Chuyển sang trang liên kết tài khoản
        btnLinkAccounts.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, LinkAccountsActivity.class);
            startActivity(intent);
        });

        // Chuyển sang trang xem bài viết của tôi
        btnViewMyPosts.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ViewMyPostActivity.class);
            startActivity(intent);
        });
    }
}