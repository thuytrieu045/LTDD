package com.sinhvien.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Category1Activity extends AppCompatActivity {
    private RecyclerView rvListC;
    private ProductAdapter productAdapter;
    private ArrayList<Product> lstProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category1);

        // Xử lý nút "Back"
        Button button = findViewById(R.id.btnback);
        button.setOnClickListener(v -> finish()); // Trả về MainActivity

        // Ánh xạ RecyclerView
        rvListC = findViewById(R.id.rvList);
        lstProduct = new ArrayList<>();

        // Load dữ liệu sản phẩm
        LoadData();

        // Thiết lập Adapter và LayoutManager cho RecyclerView
        productAdapter = new ProductAdapter(lstProduct);
        rvListC.setAdapter(productAdapter);
        rvListC.setLayoutManager(new LinearLayoutManager(this));

        // Xử lý hiển thị tự động căn lề
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rvList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void LoadData() {
        lstProduct.add(new Product("1", "Choco", "Bánh Choco thường là bánh ngọt có hương vị chocolate đậm đà, kết hợp với các nguyên liệu như hạnh nhân, dừa, hoặc caramel.", "choco.jpg", 5.99, R.drawable.choco));
        lstProduct.add(new Product("2", "Tiramisu", "Tiramisu là món tráng miệng Ý gồm lớp bánh ladyfinger thấm cà phê, xen kẽ kem mascarpone béo mịn, phủ cacao đắng nhẹ, tạo hương vị thơm ngon, tinh tế. ", "tiraminsu.jpg", 3.49, R.drawable.tiraminsu));
        lstProduct.add(new Product("3", "Rocher", "Phủ lớp kem hoặc ganache, có hương vị liên quan đến chocolate và hạt phỉ, lấy cảm hứng từ Ferrero Rocher.", "rocher.jpg", 6.99, R.drawable.rocher));
        lstProduct.add(new Product("4", "Flan", "Bánh flan là món tráng miệng mềm mịn, làm từ trứng, sữa và caramel, có vị béo ngậy, ngọt dịu, thường ăn kèm với cà phê hoặc đá lạnh. ", "flan.jpg", 5.99, R.drawable.flan));

    }
}
