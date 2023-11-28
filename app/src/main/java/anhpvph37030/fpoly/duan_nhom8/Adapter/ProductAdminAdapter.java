package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class ProductAdminAdapter extends ArrayAdapter<Product> {
    private Context context;
    private EditSanPhamListener editSanPhamListener;

    public ProductAdminAdapter(Context context, List<Product> productList) {
        super(context, 0, productList);
        this.context = context;
    }
    public interface EditSanPhamListener {
        void onEditSanPham(Product danhMuc);
    }
    public void setEditSanPhamListener(ProductAdminAdapter.EditSanPhamListener listener) {
        this.editSanPhamListener = listener;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_sanpham, parent, false);
        }

        Product currentProduct = getItem(position);

        ImageView productImageView = listItemView.findViewById(R.id.imgAnhSp);
        TextView productNameTextView = listItemView.findViewById(R.id.txtTenSp);
        TextView productPriceTextView = listItemView.findViewById(R.id.txtGiasp);
        TextView productQuantityTextView = listItemView.findViewById(R.id.txtsoluongsp);
        TextView productHangTextView = listItemView.findViewById(R.id.txtHangSp);
        ImageView btnedit = listItemView.findViewById(R.id.btnEditSp);
        ImageView btnXoa = listItemView.findViewById(R.id.btnDeleteSp);

        if (currentProduct != null) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.item_phone)
                    .error(R.drawable.item_phone);

            Glide.with(context)
                    .load(currentProduct.getImage())
                    .apply(options)
                    .into(productImageView);

            productNameTextView.setText(currentProduct.getName());
            productPriceTextView.setText(currentProduct.getPrice());
            productQuantityTextView.setText(String.valueOf(currentProduct.getQuantity1()));
            productHangTextView.setText(currentProduct.getHang());

            // Thêm đoạn code sau để hiển thị tên hãng
            int maDanhMuc = currentProduct.getMaDanhMuc();
            DatabaseReference danhMucRef = FirebaseDatabase.getInstance().getReference().child("danhmuc").child(String.valueOf(maDanhMuc));

            danhMucRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DanhMuc danhMuc = dataSnapshot.getValue(DanhMuc.class);
                        if (danhMuc != null) {
                            productHangTextView.setText(danhMuc.getTenHang());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu cần
                }
            });
            btnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editSanPhamListener != null) {
                        editSanPhamListener.onEditSanPham(currentProduct);
                    }
                }
            });
            btnXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmAndDeleteProduct(currentProduct);
                }
            });

        }

        return listItemView;
    }
    // Hàm xác nhận và xóa sản phẩm
    private void confirmAndDeleteProduct(Product selectedProduct) {
        // Hiển thị Dialog xác nhận xóa
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận xóa sản phẩm");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideProduct(selectedProduct);
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

//    // Hàm xóa sản phẩm
//    private void deleteProduct(Product selectedProduct) {
//        // Xóa sản phẩm từ Firebase ở đây
//        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products").child(selectedProduct.getId());
//        productRef.removeValue();
//
//        // Hiển thị thông báo sau khi xóa
//        Toast.makeText(getContext(), "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
//    }
    // Hàm ẩn sản phẩm
    private void hideProduct(Product selectedProduct) {
        // Ẩn sản phẩm trong Firebase bằng cách cập nhật trường isHidden
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products").child(selectedProduct.getId());
        productRef.child("visible").setValue(true);

        // Hiển thị thông báo sau khi ẩn sản phẩm
        Toast.makeText(getContext(), "Đã ẩn sản phẩm", Toast.LENGTH_SHORT).show();
    }

}
