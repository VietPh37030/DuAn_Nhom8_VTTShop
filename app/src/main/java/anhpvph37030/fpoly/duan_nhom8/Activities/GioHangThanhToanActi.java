package anhpvph37030.fpoly.duan_nhom8.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import anhpvph37030.fpoly.duan_nhom8.Adapter.GioHangThanhToanAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Cart;
import anhpvph37030.fpoly.duan_nhom8.model.HoaDon;
import anhpvph37030.fpoly.duan_nhom8.model.ThongTinDiaChi;

public class GioHangThanhToanActi extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference diaChiRef;
    Button btnThanhToanGio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang_thanh_toan);
        btnThanhToanGio = findViewById(R.id.btnthanhtoangh);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            diaChiRef = FirebaseDatabase.getInstance().getReference().child("thongtinnhanhang").child(currentUser.getUid());
        }

        // Fetch and display address
        fetchAndDisplayAddress();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("cartItems") && intent.hasExtra("totalAmount")) {
            List<Cart> cartItems = intent.getParcelableArrayListExtra("cartItems");
            int totalAmount = intent.getIntExtra("totalAmount", 0);

            // Display total amount in GioHangThanhToanActi
            TextView txtTotalAmount = findViewById(R.id.txtTongtiengh);
            txtTotalAmount.setText(totalAmount + " VND");

            // Display cart items in ListView
            updateUIWithProducts(cartItems);

            // Set the cart items as tag to the button for retrieval later
            btnThanhToanGio.setTag(cartItems);
        }
        btnThanhToanGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLickThanhToanGio();
            }
        });
    }

    private void onCLickThanhToanGio() {
        List<Cart> cartItems = (List<Cart>) btnThanhToanGio.getTag();
        if (cartItems != null && !cartItems.isEmpty()) {
            fetchAndSetNguoiNhan(new OnNguoiNhanFetchedListener1() {
                @Override
                public void onNguoiNhanFetched(ThongTinDiaChi diaChi) {
                    // Tạo mã hóa đơn chung cho toàn bộ đơn hàng
                    String maHoaDonChung = generateMaHoaDon();
                    int trangThai = 0;

                    HoaDon hoaDonChung = new HoaDon();
                    hoaDonChung.setMaHoaDon(maHoaDonChung);
                    hoaDonChung.setNgayDat(getCurrentDate());
                    hoaDonChung.setTrangThai(trangThai);
                    hoaDonChung.setNguoiNhan(diaChi.getHoTen());
                    hoaDonChung.setSdt(diaChi.getSoDienThoai());
                    hoaDonChung.setDiaChi(diaChi.getDiaChi());

                    // Thêm thông tin sản phẩm vào hóa đơn chung
                    StringBuilder tenSanPham = new StringBuilder();
                    StringBuilder imageUrl = new StringBuilder();

                    int tongSoLuong = 0;
                    int tongTien = 0;

                    for (Cart cartItem : cartItems) {
                        tongSoLuong += cartItem.getQuantity();

                        String productPriceString = cartItem.getProduct().getPrice();
                        String numericPriceString = productPriceString.replaceAll("[^0-9]", "");
                        int productPrice = Integer.parseInt(numericPriceString);
                        tongTien += productPrice * cartItem.getQuantity();

                        // Thêm thông tin sản phẩm vào StringBuilder
                        tenSanPham.append(cartItem.getProduct().getName()).append(", ");
                        imageUrl.append(cartItem.getProduct().getImage()).append(", ");
                    }

                    // Cắt bỏ dấu "," cuối cùng nếu có
                    if (tenSanPham.length() > 0) {
                        tenSanPham.deleteCharAt(tenSanPham.length() - 2);
                        imageUrl.deleteCharAt(imageUrl.length() - 2);
                    }

                    hoaDonChung.setTenSanPham(tenSanPham.toString());
                    hoaDonChung.setImageUrl(imageUrl.toString());
                    hoaDonChung.setSoLuong(tongSoLuong);
                    hoaDonChung.setTongTien(tongTien);

                    // Đẩy thông tin lên Firebase
                    DatabaseReference hoaDonThanhToanRef = FirebaseDatabase.getInstance().getReference().child("HoaDonThanhToan").child(currentUser.getUid());
                    hoaDonThanhToanRef.push().setValue(hoaDonChung);

                    // Chuyển sang màn hình hóa đơn sau khi thanh toán tất cả sản phẩm
                    Intent intent = new Intent(GioHangThanhToanActi.this, HoaDonActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private interface OnNguoiNhanFetchedListener {
        void onNguoiNhanFetched(ThongTinDiaChi diaChi);
    }

    private void fetchAndSetNguoiNhan(OnNguoiNhanFetchedListener1 listener) {
        diaChiRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Assuming there's only one address for simplicity, you might need to handle multiple addresses differently
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ThongTinDiaChi diaChi = dataSnapshot.getValue(ThongTinDiaChi.class);
                    // Log thông tin người nhận từ Firebase
                    Log.d("FirebaseData", "HoTen: " + diaChi.getHoTen());
                    Log.d("FirebaseData", "SoDienThoai: " + diaChi.getSoDienThoai());
                    Log.d("FirebaseData", "DiaChi: " + diaChi.getDiaChi());

                    listener.onNguoiNhanFetched(diaChi);
                    break; // Stop after the first address, modify as needed
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Log.e("FirebaseError", "Error fetching data: " + error.getMessage());
            }
        });
    }

    private void fetchAndDisplayAddress() {
        diaChiRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Assuming there's only one address for simplicity, you might need to handle multiple addresses differently
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ThongTinDiaChi diaChi = dataSnapshot.getValue(ThongTinDiaChi.class);
                    updateUIWithAddress(diaChi);
                    break; // Stop after the first address, modify as needed
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void updateUIWithAddress(ThongTinDiaChi diaChi) {
        // Update the UI elements with the fetched address data
        TextView txtHoTen = findViewById(R.id.txtnguoinhangh);
        TextView txtSoDienThoai = findViewById(R.id.txtSdtgh);
        TextView txtDiaChi = findViewById(R.id.txtDiaChigh);

        txtHoTen.setText(diaChi.getHoTen());
        txtSoDienThoai.setText(diaChi.getSoDienThoai());
        txtDiaChi.setText(diaChi.getDiaChi());
    }

    private void updateUIWithProducts(List<Cart> cartItems) {
        // Cập nhật giao diện người dùng với danh sách sản phẩm
        ListView listView = findViewById(R.id.lstthanhtoagio);
        GioHangThanhToanAdapter adapter = new GioHangThanhToanAdapter(this, cartItems);
        listView.setAdapter(adapter);
    }

    private String generateMaHoaDon() {
        // Sử dụng một số thông tin chung để tạo mã hóa đơn cho toàn bộ đơn hàng
        long timestamp = System.currentTimeMillis();
        int randomNumber = (int) (Math.random() * 1000); // Điều chỉnh phạm vi nếu cần
        return "HD" + timestamp + randomNumber;
    }

    private String getCurrentDate() {
        // Xử lý và trả về ngày hiện tại dưới dạng chuỗi
        // (Bạn có thể sử dụng thư viện SimpleDateFormat để định dạng ngày)
        // Ví dụ:
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    private interface OnNguoiNhanFetchedListener1 {
        void onNguoiNhanFetched(ThongTinDiaChi diaChi);
    }
}
