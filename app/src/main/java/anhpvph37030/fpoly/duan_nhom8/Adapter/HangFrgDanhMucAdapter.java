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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class HangFrgDanhMucAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;

    public HangFrgDanhMucAdapter(Context context, int resource, List<Product> productList) {
        super(context, resource, productList);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Product product = getItem(position);

        if (product != null) {
            // Thực hiện ánh xạ view và đổ dữ liệu từ product vào các view tương ứng
            ImageView imgAnhSp = convertView.findViewById(R.id.imgAnhSp);
            TextView txtTenSp = convertView.findViewById(R.id.txtTenSp);
            TextView txtGiaSp = convertView.findViewById(R.id.txtGiasp);
            TextView txtHangSp = convertView.findViewById(R.id.txtHangSp);
            TextView txtSoLuong = convertView.findViewById(R.id.txtsoluongsp);

            // Load hình ảnh sử dụng Picasso
            String urlSanPham = product.getImage();
            if (urlSanPham != null && !urlSanPham.isEmpty()) {
                Picasso.get().load(urlSanPham).into(imgAnhSp);
            } else {
                imgAnhSp.setImageResource(R.drawable.avt); // Hình ảnh mặc định nếu không có URL
            }

            txtTenSp.setText(product.getName());
            txtGiaSp.setText(product.getPrice());
            txtSoLuong.setText(String.valueOf(product.getQuantity1()));
            txtHangSp.setText(product.getHang());

            int maDanhMuc = product.getMaDanhMuc();
            DatabaseReference danhMucRef = FirebaseDatabase.getInstance().getReference().child("danhmuc").child(String.valueOf(maDanhMuc));

            danhMucRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DanhMuc danhMuc = dataSnapshot.getValue(DanhMuc.class);
                        if (danhMuc != null) {
                            txtHangSp.setText(danhMuc.getTenHang());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu cần
                }
            });
        }

        return convertView;
    }
}

