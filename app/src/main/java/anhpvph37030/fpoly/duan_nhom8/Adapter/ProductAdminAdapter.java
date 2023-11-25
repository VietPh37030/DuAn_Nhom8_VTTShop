package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    public ProductAdminAdapter(Context context, List<Product> productList) {
        super(context, 0, productList);
        this.context = context;
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
        }

        return listItemView;
    }
}
