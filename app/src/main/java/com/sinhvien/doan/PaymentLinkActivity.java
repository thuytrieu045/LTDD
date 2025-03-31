package com.sinhvien.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PaymentLinkActivity extends AppCompatActivity {
    private TextView tvPaymentTitle;
    private EditText edtPaymentInfo;
    private Button btnSave;
    private Button btnBack;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_link);

        databaseHelper = new DatabaseHelper(this);
        tvPaymentTitle = findViewById(R.id.tvPaymentTitle);
        edtPaymentInfo = findViewById(R.id.edtPaymentInfo);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        String serviceName = getIntent().getStringExtra("service_name");
        if (serviceName != null) {
            tvPaymentTitle.setText("Liên kết tài khoản " + serviceName);
        }

        btnSave.setOnClickListener(v -> {
            String paymentInfo = edtPaymentInfo.getText().toString().trim();
            if (!validatePaymentInfo(serviceName, paymentInfo)) {
                return;
            }

            String columnName = getColumnName(serviceName);
            if (columnName != null) {
                savePaymentInfo(columnName, paymentInfo);
                Toast.makeText(this, "Đã lưu thông tin cho " + serviceName, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private String getColumnName(String serviceName) {
        switch (serviceName) {
            case "MoMo": return "momo_number";
            case "ZaloPay": return "zalopay_number";
            case "Vietcombank": return "vietcombank_account";
            case "MB Bank": return "mbbank_account";
            case "VietinBank": return "vietinbank_account";
            default: return null;
        }
    }

    private boolean validatePaymentInfo(String serviceName, String paymentInfo) {
        if (paymentInfo.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }

        switch (serviceName) {
            case "MoMo":
            case "ZaloPay":
                // Kiểm tra số điện thoại: 10 chữ số, bắt đầu bằng 03, 07, 08, 09
                String phonePattern = "^(03|07|08|09)\\d{8}$";
                if (!paymentInfo.matches(phonePattern)) {
                    Toast.makeText(this, "Số điện thoại không hợp lệ! Phải là 10 chữ số, bắt đầu bằng 03, 07, 08, 09.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "Vietcombank":
                // Kiểm tra số tài khoản Vietcombank: 13 chữ số
                if (!paymentInfo.matches("^\\d{13}$")) {
                    Toast.makeText(this, "Số tài khoản Vietcombank phải là 13 chữ số!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "MB Bank":
                // Kiểm tra số tài khoản MB Bank: 12 chữ số
                if (!paymentInfo.matches("^\\d{12}$")) {
                    Toast.makeText(this, "Số tài khoản MB Bank phải là 12 chữ số!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "VietinBank":
                // Kiểm tra số tài khoản VietinBank: 12 chữ số
                if (!paymentInfo.matches("^\\d{12}$")) {
                    Toast.makeText(this, "Số tài khoản VietinBank phải là 12 chữ số!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
        }
        return true;
    }

    private void savePaymentInfo(String columnName, String paymentInfo) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            int userId = databaseHelper.getUserId(user.getUid());
            databaseHelper.updatePaymentInfo(userId,
                    columnName.equals("momo_number") ? paymentInfo : "",
                    columnName.equals("zalopay_number") ? paymentInfo : "",
                    columnName.equals("vietcombank_account") ? paymentInfo : "",
                    columnName.equals("mbbank_account") ? paymentInfo : "",
                    columnName.equals("vietinbank_account") ? paymentInfo : "");
        }
    }
}