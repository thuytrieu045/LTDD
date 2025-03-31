package com.sinhvien.doan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.app.Dialog;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

public class RecipePostActivity extends AppCompatActivity {
    private ImageView imgRecipe;
    private TextView txtRecipeName, txtDifficulty, txtTime, txtIngredients, txtSteps;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_product_details);

        databaseHelper = new DatabaseHelper(this);

        imgRecipe = findViewById(R.id.imgRecipe);
        txtRecipeName = findViewById(R.id.txtRecipeTitle);
        txtDifficulty = findViewById(R.id.txtRecipeDifficulty);
        txtTime = findViewById(R.id.txtRecipeTime);
        txtIngredients = findViewById(R.id.txtIngredients);
        txtSteps = findViewById(R.id.txtSteps);
        Button btnDonate = findViewById(R.id.btnDonate);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnFav = findViewById(R.id.btnFav);



        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("title");
            String difficulty = intent.getStringExtra("difficulty");
            int time = intent.getIntExtra("time", -1);
            String ingredients = intent.getStringExtra("ingredients");
            String steps = intent.getStringExtra("steps");
            String imageUrl = intent.getStringExtra("imageUrl");

            txtRecipeName.setText(name);
            txtDifficulty.setText("Độ khó: " + difficulty);
            if (time > 60) {
                txtTime.setText("Thời gian: " + time / 60 + " giờ " + time % 60 + " phút");
            } else {
                txtTime.setText("Thời gian: " + time + " phút");
            }
            txtIngredients.setText(ingredients);
            txtSteps.setText(steps);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this).load(imageUrl).into(imgRecipe);
            } else {
                imgRecipe.setImageResource(R.drawable.dessert);
            }

            // Kiểm tra trạng yêu thích
            if (databaseHelper.isFavorite(databaseHelper.getUserId(FirebaseAuth.getInstance().getCurrentUser().getUid()), databaseHelper.getRecipeId(name))) {
                btnFav.setText("Bỏ yêu thích");
            }
            else
                btnFav.setText("Yêu thích");




            btnDonate.setOnClickListener(v -> handleDonate(name));
            btnFav.setOnClickListener(v -> {
                int userId = databaseHelper.getUserId(FirebaseAuth.getInstance().getUid());
                int recipeId = databaseHelper.getRecipeId(name);
                if (databaseHelper.isFavorite(userId, recipeId)) {
                    databaseHelper.removeFavorite(userId, recipeId);
                    btnFav.setText("Yêu thích");
                    Toast.makeText(this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.addFavorite(userId, recipeId);
                    btnFav.setText("Bỏ yêu thích");
                    Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void handleDonate(String recipeName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_id FROM recipes WHERE recipe_name = ?", new String[]{recipeName});
        String defaultPaymentLink = "https://mbbank.com.vn/0906780284"; // Tài khoản admin mặc định

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            Cursor paymentCursor = db.rawQuery(
                    "SELECT momo_number, zalopay_number, vietcombank_account, mbbank_account, vietinbank_account FROM users WHERE user_id = ?",
                    new String[]{String.valueOf(userId)}
            );

            if (paymentCursor.moveToFirst()) {
                List<String> paymentOptions = new ArrayList<>();
                List<String> paymentLinks = new ArrayList<>();

                // Kiểm tra từng phương thức thanh toán
                String momo = paymentCursor.getString(0);
                if (momo != null && !momo.isEmpty()) {
                    paymentOptions.add("MoMo (" + momo + ")");
                    paymentLinks.add("https://momo.vn/" + momo);
                }
                String zalopay = paymentCursor.getString(1);
                if (zalopay != null && !zalopay.isEmpty()) {
                    paymentOptions.add("ZaloPay (" + zalopay + ")");
                    paymentLinks.add("https://zalopay.vn/" + zalopay);
                }
                String vietcombank = paymentCursor.getString(2);
                if (vietcombank != null && !vietcombank.isEmpty()) {
                    paymentOptions.add("Vietcombank (" + vietcombank + ")");
                    paymentLinks.add("https://vietcombank.com.vn/" + vietcombank);
                }
                String mbbank = paymentCursor.getString(3);
                if (mbbank != null && !mbbank.isEmpty()) {
                    paymentOptions.add("MB Bank (" + mbbank + ")");
                    paymentLinks.add("https://mbbank.com.vn/" + mbbank);
                }
                String vietinbank = paymentCursor.getString(4);
                if (vietinbank != null && !vietinbank.isEmpty()) {
                    paymentOptions.add("VietinBank (" + vietinbank + ")");
                    paymentLinks.add("https://vietinbank.vn/" + vietinbank);
                }

                paymentCursor.close();

                if (paymentOptions.isEmpty()) {
                    openPaymentLink(defaultPaymentLink);
                } else if (paymentOptions.size() == 1) {
                    openPaymentLink(paymentLinks.get(0));
                } else {
                    showPaymentOptionsDialog(paymentOptions, paymentLinks);
                }
                cursor.close();
                return;
            }
            paymentCursor.close();
        }
        cursor.close();
        openPaymentLink(defaultPaymentLink);
    }

    private void openPaymentLink(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    private void showPaymentOptionsDialog(List<String> options, List<String> links) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_payment_options);

        ListView lvPaymentOptions = dialog.findViewById(R.id.lvPaymentOptions);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        lvPaymentOptions.setAdapter(adapter);

        lvPaymentOptions.setOnItemClickListener((parent, view, position, id) -> {
            openPaymentLink(links.get(position));
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}