package com.sinhvien.doan;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changeUsername extends AppCompatActivity {

    private EditText edtName;
    private Button btnBack, btnSave;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        edtName = findViewById(R.id.edtName);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        databaseHelper = new DatabaseHelper(this);

        btnSave.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null) {
                String username = edtName.getText().toString();
                if(!username.isEmpty()) {
                    int userId = databaseHelper.getUserId(user.getUid());
                    databaseHelper.updateUsername(userId, username);
                    Toast.makeText(this, "Thay đổi tên thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(this, "Vui lòng nhập thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}