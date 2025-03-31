package com.sinhvien.doan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> lstProduct;
    private List<Product> lstProductFull; // Danh sách đầy đủ (không lọc)
    private Context context;
    private MyDataBase myDataBase;

    public ProductAdapter(Context context, List<Product> lstProduct) {
        this.context = context;
        this.lstProduct = new ArrayList<>(lstProduct); // Copy danh sách ban đầu
        this.lstProductFull = new ArrayList<>(lstProduct); // Lưu danh sách đầy đủ để phục hồi khi xóa bộ lọc
        this.myDataBase = new MyDataBase(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product item = lstProduct.get(position);
        holder.tvRecipeName.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());
        holder.ivAvatar.setImageResource(item.getImageResource());

        // Kiểm tra trạng thái yêu thích
        boolean isFavorite = myDataBase.isProductFavorite(item.getId());
        holder.btnFavorite.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);

        // Xử lý sự kiện click vào biểu tượng yêu thích
        holder.btnFavorite.setOnClickListener(v -> {
            boolean newFavoriteState = !isFavorite;
            if (newFavoriteState) {
                myDataBase.addToFavorites(item.getId(), item.getName(), item.getDescription(), item.getImageResource());
                Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                myDataBase.removeFromFavorites(item.getId());
                Toast.makeText(context, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
            }
            holder.btnFavorite.setImageResource(newFavoriteState ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        });

        // Khi nhấn vào ảnh, mở ProductDetailActivity và truyền product_id
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_id", item.getId()); // Truyền ID sản phẩm
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lstProduct.size();
    }

    // 🔥 Thêm phương thức filter() để tìm kiếm sản phẩm
    public void filter(String query) {
        lstProduct.clear();
        if (query.isEmpty()) {
            lstProduct.addAll(lstProductFull); // Nếu chuỗi tìm kiếm rỗng, khôi phục danh sách ban đầu
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Product product : lstProductFull) {
                if (product.getName().toLowerCase().contains(lowerCaseQuery)) {
                    lstProduct.add(product);
                }
            }
        }
        notifyDataSetChanged(); // Cập nhật RecyclerView sau khi lọc
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvRecipeName, tvDescription;
        ImageButton btnFavorite;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}
