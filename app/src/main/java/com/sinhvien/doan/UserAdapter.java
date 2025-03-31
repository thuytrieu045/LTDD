package com.sinhvien.doan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public UserAdapter(Context context, List<User> userList) {
        this.userList = userList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.text2.setText("Email: " + user.getEmail());
        holder.text1.setText(user.getUsername());
        holder.text3.setText("Role: " + user.getRole());

        // Xử lý nút "Xóa"
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa người dùng")
                    .setMessage("Bạn có chắc chắn muốn xóa người dùng này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        db.collection("users").document(mAuth.getCurrentUser().getUid())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Đã xóa người dùng!", Toast.LENGTH_SHORT).show();
                                    userList.remove(position);
                                    notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Lỗi khi xóa!", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // Xử lý nút "Sửa"
        holder.btnEdit.setOnClickListener(v -> showEditDialog(user));
    }

    private void showEditDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sửa thông tin người dùng");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_user, null);
        EditText editEmail = view.findViewById(R.id.editUserEmail);
        EditText editRole = view.findViewById(R.id.editUserRole);
        EditText editName = view.findViewById(R.id.editUserName);

        editEmail.setText(user.getEmail());
        editRole.setText(user.getRole());
        editName.setText(user.getUsername());

        builder.setView(view);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newEmail = editEmail.getText().toString();
            String newRole = editRole.getText().toString();
            String newName = editName.getText().toString();

            db.collection("users").document(mAuth.getCurrentUser().getUid())
                    .update("email", newEmail, "role", newRole, "username", newName)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Đã cập nhật!", Toast.LENGTH_SHORT).show();
                        user.setEmail(newEmail);
                        user.setRole(newRole);
                        user.setUsername(newName);
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2, text3;
        Button btnEdit, btnDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
            text3 = itemView.findViewById(R.id.text3);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}