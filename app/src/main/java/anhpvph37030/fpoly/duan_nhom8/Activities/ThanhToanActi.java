package anhpvph37030.fpoly.duan_nhom8.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import anhpvph37030.fpoly.duan_nhom8.R;

public class ThanhToanActi extends AppCompatActivity {
    private ImageView ImageSanPham;
    private TextView txttttensp, txttgiasp, txttsoluong, txtsoluonghienthi, txtTongtien;
    private ImageButton giamsoluong, tangsoluong;
    private Button btnthanhtoan;
    private int soLuong = 1;  // Số lượng mặc định
    private int giaSanPham;  // Giá sản phẩm
    private String productId;  // ID của sản phẩm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        productId = intent.getStringExtra("PRODUCT_ID");  // Cập nhật giá trị của productId
        String productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL");
        String productName = intent.getStringExtra("PRODUCT_NAME");
        String productPrice = intent.getStringExtra("PRODUCT_PRICE");
        String productQuantity = intent.getStringExtra("PRODUCT_QUANTITY");


        // Áp dụng các giá trị này để điền vào giao diện hoặc thực hiện bất kỳ thao tác nào khác
        ImageSanPham = findViewById(R.id.imgttsp);
        txttttensp = findViewById(R.id.txttttensp);
        txttgiasp = findViewById(R.id.txttgia);
        txttsoluong = findViewById(R.id.txtttsoluong);
        giamsoluong = findViewById(R.id.img_giamnav);
        tangsoluong = findViewById(R.id.img_tangnav);
        txtsoluonghienthi = findViewById(R.id.txt_navsoluong);
        txtTongtien = findViewById(R.id.txtTongtien);
        Picasso.get().load(productImageUrl).into(ImageSanPham);
        Log.d("ImageLoad", "URL Ảnh: " + productImageUrl);
        txttttensp.setText(productName);

        // Xử lý giá sản phẩm
        giaSanPham = layGiaSanPham(productPrice);
        txttgiasp.setText(String.valueOf(giaSanPham));
        txtTongtien.setText(productPrice);
        txttsoluong.setText(productQuantity);

        // Xử lý sự kiện tăng và giảm số lượng
        tangsoluong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productId != null) {
                    layQuantity1TuFirebase();  // Lấy giá trị quantity1 từ Firebase
                } else {
                    Log.e("ThanhToanActi", "ID Sản phẩm là null");
                }
            }
        });

        giamsoluong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soLuong > 1) {
                    soLuong--;
                    txtsoluonghienthi.setText(String.valueOf(soLuong));
                    capNhatTongTien();
                }
            }
        });
    }

    // Hàm cập nhật tổng tiền
    private void capNhatTongTien() {
        int tongTien = giaSanPham * soLuong;
        String tongTienString = String.format("%,d", tongTien);  // Định dạng tổng tiền với dấu phẩy ngăn cách hàng nghìn
        txtTongtien.setText(tongTienString);
    }

    // Hàm lấy giá sản phẩm từ chuỗi giá
    private int layGiaSanPham(String productPrice) {
        String priceString = productPrice.replaceAll("[^0-9]", "");
        try {
            return Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            Log.e("ThanhToanActi", "Lỗi chuyển đổi giá: " + productPrice);
            return 0;  // Giá mặc định nếu có lỗi
        }
    }

    // Hàm lấy giá trị quantity1 từ Firebase
    private void layQuantity1TuFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("products").child(productId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int quantity1 = dataSnapshot.child("quantity1").getValue(Integer.class);
                    kiemTraSoLuong(quantity1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ThanhToanActi", "Lỗi đọc quantity1 từ Firebase: " + databaseError.getMessage());
            }
        });
    }

    // Hàm kiểm tra số lượng và thực hiện tăng số lượng nếu hợp lệ
    private void kiemTraSoLuong(int quantity1) {
        if (soLuong < quantity1) {
            soLuong++;
            txtsoluonghienthi.setText(String.valueOf(soLuong));
            capNhatTongTien();
        } else {
            // Hiển thị thông báo nếu số lượng vượt quá quantity1
            Toast.makeText(ThanhToanActi.this, "Sản phẩm không đủ để bạn mua", Toast.LENGTH_SHORT).show();
        }
    }
}
