package anhpvph37030.fpoly.duan_nhom8.Activities;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Locale;

import anhpvph37030.fpoly.duan_nhom8.model.HoaDon;
import anhpvph37030.fpoly.duan_nhom8.model.ThongTinDiaChi;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.taikhoan.Login;

public class ThanhToanActi extends AppCompatActivity {
    private ImageView ImageSanPham;
    private TextView txttttensp, txttgiasp, txttsoluong, txtsoluonghienthi, txtTongtien, txtnguoinhan, txtSdt, txtDiaChi;
    private ImageButton giamsoluong, tangsoluong;
    private Button btnthanhtoan;
    private int soLuong = 1;  // Số lượng mặc định
    private int giaSanPham;  // Giá sản phẩm
    private String productId;  // ID của sản phẩm
    private DatabaseReference productRef; // Khai báo DatabaseReference để tham chiếu đến sản phẩm cần cập nhật


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

        // Kiểm tra đăng nhập và lấy thông tin địa chỉ
        kiemTraDangNhap();

        // Áp dụng các giá trị này để điền vào giao diện hoặc thực hiện bất kỳ thao tác nào khác
        ImageSanPham = findViewById(R.id.imgttsp);
        txttttensp = findViewById(R.id.txttttensp);
        txttgiasp = findViewById(R.id.txttgia);
        txttsoluong = findViewById(R.id.txtttsoluong);
        giamsoluong = findViewById(R.id.img_giamnav);
        tangsoluong = findViewById(R.id.img_tangnav);
        txtsoluonghienthi = findViewById(R.id.txt_navsoluong);
        txtTongtien = findViewById(R.id.txtTongtien);
        txtnguoinhan = findViewById(R.id.txtnguoinhan);
        txtSdt = findViewById(R.id.txtSdt);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        btnthanhtoan = findViewById(R.id.btnthanhtoan);
        Picasso.get().load(productImageUrl).into(ImageSanPham);
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
                    // Xử lý khi ID Sản phẩm là null
                    // Log.e("ThanhToanActi", "ID Sản phẩm là null");
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
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// Tạo một đối tượng HoaDon từ thông tin đã nhập
                HoaDon hoaDon = new HoaDon();
                hoaDon.setImageUrl(productImageUrl);
                hoaDon.setTenSanPham(productName);
                hoaDon.setSoLuong(soLuong);
                hoaDon.setTongTien(giaSanPham * soLuong);
                hoaDon.setNguoiNhan(txtnguoinhan.getText().toString());
                hoaDon.setSdt(txtSdt.getText().toString());
                hoaDon.setDiaChi(txtDiaChi.getText().toString());
                // Ngày đặt và mã hóa đơn có thể được xử lý ở đây, ví dụ:
                hoaDon.setNgayDat(getCurrentDate());
                hoaDon.setMaHoaDon(generateMaHoaDon());
                hoaDon.setTrangThai("Chờ xác nhận");

                // Đẩy dữ liệu lên Firebase
                DatabaseReference hoaDonRef = FirebaseDatabase.getInstance().getReference().child("HoaDonThanhToan");
                String key = hoaDonRef.push().getKey();
                hoaDonRef.child(key).setValue(hoaDon);

                // Hiển thị thông báo hoặc chuyển hướng đến màn hình hóa đơn nếu cần
                // Ví dụ:
                // Cập nhật số lượng trên Firebase (quantity1)
                capNhatSoLuongSauThanhToan(productId);
                Intent intent = new Intent(ThanhToanActi.this, HoaDonActivity.class);
                startActivity(intent);
            }
        });
    }

    private void capNhatSoLuongSauThanhToan(String productId) {
        if (productId != null) {
            productRef = FirebaseDatabase.getInstance().getReference().child("products").child(productId);

            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int quantity1 = dataSnapshot.child("quantity1").getValue(Integer.class);
                        // Kiểm tra nếu có đủ số lượng để trừ
                        if (soLuong <= quantity1) {
                            int newQuantity1 = quantity1 - soLuong;
                            // Cập nhật lại quantity1 trên Firebase
                            productRef.child("quantity1").setValue(newQuantity1);
                        } else {
                            // Hiển thị thông báo nếu số lượng vượt quá quantity1
                            Toast.makeText(ThanhToanActi.this, "Sản phẩm không đủ để bạn mua", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý khi có lỗi đọc quantity1 từ Firebase
                    // Log.e("ThanhToanActi", "Lỗi đọc quantity1 từ Firebase: " + databaseError.getMessage());
                }
            });
        } else {
            // Xử lý khi ID Sản phẩm là null
            // Log.e("ThanhToanActi", "ID Sản phẩm là null");
        }
    }

    private String getCurrentDate() {
        // Xử lý và trả về ngày hiện tại dưới dạng chuỗi
        // (Bạn có thể sử dụng thư viện SimpleDateFormat để định dạng ngày)
        // Ví dụ:
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
    private String generateMaHoaDon() {
        // Sử dụng timestamp để tạo mã hóa đơn
        long timestamp = System.currentTimeMillis();
        return String.valueOf(timestamp);
    }
    private void kiemTraDangNhap() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // Người dùng đã đăng nhập
            // Gọi hàm lấy thông tin địa chỉ từ Firebase
            layThongTinDiaChi(currentUser.getUid());
        } else {
            // Người dùng chưa đăng nhập, chuyển hướng đến màn hình đăng nhập
            // Hoặc thực hiện các hành động khác phù hợp với ứng dụng của bạn
            // Ví dụ:
            Intent intent = new Intent(ThanhToanActi.this, Login.class);
            startActivity(intent);
            finish();  // Đóng activity hiện tại nếu cần
        }
    }

    private void layThongTinDiaChi(String userId) {
        DatabaseReference diaChiRef = FirebaseDatabase.getInstance().getReference().child("thongtinnhanhang").child(userId);

        diaChiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lấy thông tin địa chỉ đầu tiên (hoặc theo logic của bạn)
                    DataSnapshot firstData = snapshot.getChildren().iterator().next();
                    ThongTinDiaChi thongTinDiaChi = firstData.getValue(ThongTinDiaChi.class);

                    if (thongTinDiaChi != null) {
                        txtnguoinhan.setText(thongTinDiaChi.getHoTen());
                        txtSdt.setText(thongTinDiaChi.getSoDienThoai());
                        txtDiaChi.setText(thongTinDiaChi.getDiaChi());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }

    private void capNhatTongTien() {
        int tongTien = giaSanPham * soLuong;
        String tongTienString = String.format("%,d", tongTien);  // Định dạng tổng tiền với dấu phẩy ngăn cách hàng nghìn
        txtTongtien.setText(tongTienString);
    }

    private int layGiaSanPham(String productPrice) {
        String priceString = productPrice.replaceAll("[^0-9]", "");
        try {
            return Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            // Xử lý khi có lỗi chuyển đổi giá
            // Log.e("ThanhToanActi", "Lỗi chuyển đổi giá: " + productPrice);
            return 0;  // Giá mặc định nếu có lỗi
        }
    }

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
                // Xử lý khi có lỗi đọc quantity1 từ Firebase
                // Log.e("ThanhToanActi", "Lỗi đọc quantity1 từ Firebase: " + databaseError.getMessage());
            }
        });
    }

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
