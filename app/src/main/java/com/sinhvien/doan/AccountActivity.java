package com.sinhvien.doan;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {
    private MyDataBase db;
    private DatabaseHelper databaseHelper;
    private TextView profileName;
    private Button btnSavePayment, btnSignout, btnBack, btnLinkAccounts, btnViewMyPosts, btnChangeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_profile);

        databaseHelper = new DatabaseHelper(this);
        db = new MyDataBase(this);

        // Ánh xạ UI
        profileName = findViewById(R.id.profileName);
        btnSavePayment = findViewById(R.id.btnSavePayment);
        btnSignout = findViewById(R.id.btnSignout);
        btnBack = findViewById(R.id.btnBack);
        btnLinkAccounts = findViewById(R.id.btnLinkAccounts);
        btnViewMyPosts = findViewById(R.id.btnViewMyPosts);
        btnChangeName = findViewById(R.id.btnEdtName);

        // Hiển thị username
        loadUserProfile();

        // Xử lý sự kiện các nút
        btnSavePayment.setOnClickListener(v ->
                Toast.makeText(this, "Không có thông tin để lưu!", Toast.LENGTH_SHORT).show()
        );

        btnChangeName.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), changeUsername.class))
        );

        btnSignout.setOnClickListener(v -> showLogoutDialog());
        btnBack.setOnClickListener(v -> finish());
        btnLinkAccounts.setOnClickListener(v ->
                startActivity(new Intent(AccountActivity.this, LinkAccountsActivity.class))
        );

        btnViewMyPosts.setOnClickListener(v ->
                startActivity(new Intent(AccountActivity.this, ViewMyPostActivity.class))
        );
    }

    private void loadUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            try {
                String username = db.getUsername(databaseHelper.getUserId(user.getUid()));
                profileName.setText(username != null ? username : "User");
            } catch (Exception e) {
                profileName.setText("User");
                e.printStackTrace(); // Debug lỗi nếu có
            }
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển về trang đăng nhập
                    Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
