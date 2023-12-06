package anhpvph37030.fpoly.duan_nhom8.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import anhpvph37030.fpoly.duan_nhom8.R;

public class ChiTietHoaDonAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don_admin);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String maHoaDon = intent.getStringExtra("maHoaDon");
        String imageUrl = intent.getStringExtra("imageUrl");
        String tenSanPham = intent.getStringExtra("tenSanPham");
        String tongtien = intent.getStringExtra("giaSanPham");
        // Thêm các trường khác tương ứng

        // Hiển thị dữ liệu trên giao diện người dùng
        ImageView imgHoaDon = findViewById(R.id.imgHoaDon);
        TextView txtMaHoaDon = findViewById(R.id.txtMaHoaDon);
        TextView txtTenSanPham = findViewById(R.id.txtTenSanPham);
        // Thêm các trường khác tương ứng

        // Sử dụng Picasso để hiển thị ảnh từ URL
        Picasso.get().load(imageUrl).into(imgHoaDon);

        // Hiển thị dữ liệu khác trên giao diện người dùng
        txtMaHoaDon.setText("Mã Hóa Đơn: " + maHoaDon);
        txtTenSanPham.setText("Tên Sản Phẩm: " + tenSanPham);
        // Thêm các dòng khác tương ứng
    }
}