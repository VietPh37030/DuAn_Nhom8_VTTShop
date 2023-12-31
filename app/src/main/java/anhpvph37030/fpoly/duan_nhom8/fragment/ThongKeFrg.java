package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import anhpvph37030.fpoly.duan_nhom8.R;

public class ThongKeFrg extends Fragment {

    private EditText edtNgayBatDau;
    private EditText edtNgayKetThuc;
    private TextView txtTongDoanhThu, txtTop1, txtSoLuongDonHangBiHuy;
    private Button btnTinhDoanhThu;
    private int totalRevenue = 0;

    public ThongKeFrg() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_ke_frg, container, false);

        edtNgayBatDau = view.findViewById(R.id.edtNgayBatDau);
        edtNgayKetThuc = view.findViewById(R.id.edtNgayKetThuc);
        txtTongDoanhThu = view.findViewById(R.id.txtTongDoanhThu);
        txtTop1 = view.findViewById(R.id.txtTop1);
        txtSoLuongDonHangBiHuy = view.findViewById(R.id.txtSoluonghangkhachhuy);
        btnTinhDoanhThu = view.findViewById(R.id.btnTinhDoanhThu);

        btnTinhDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ngayBatDauStr = edtNgayBatDau.getText().toString();
                String ngayKetThucStr = edtNgayKetThuc.getText().toString();

                if (!TextUtils.isEmpty(ngayBatDauStr) && !TextUtils.isEmpty(ngayKetThucStr)) {
                    calculateRevenueForDateRange(ngayBatDauStr, ngayKetThucStr);
                }
            }
        });

        // Code để hiển thị DatePickerDialog khi EditText được chọn
        edtNgayBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(edtNgayBatDau);
            }
        });
        edtNgayKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(edtNgayKetThuc);
            }
        });

        return view;
    }

    // Phương thức hiển thị DatePickerDialog
    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth1) -> {
                    month1 += 1; // DatePickerDialog sử dụng index từ 0 đến 11 cho tháng
                    String selectedDate = (dayOfMonth1 < 10 ? "0" + dayOfMonth1 : dayOfMonth1) + "-" +
                            (month1 < 10 ? "0" + month1 : month1) + "-" + year1;

                    // Hiển thị ngày đã chọn trong EditText
                    editText.setText(selectedDate);
                },
                year,
                month,
                dayOfMonth);

        datePickerDialog.show();
    }

    // Phương thức tính doanh thu dựa trên khoảng thời gian đã chọn
    private void calculateRevenueForDateRange(String ngayBatDau, String ngayKetThuc) {
        DatabaseReference giaoHangThanhCongRef = FirebaseDatabase.getInstance().getReference().child("GiaoHangThanhCong");

        giaoHangThanhCongRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalRevenue = 0;
                String topProduct = "";
                int maxQuantity = 0;

                // Create a map to store the total quantity sold for each product
                Map<String, Integer> productQuantities = new HashMap<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot hoaDonSnapshot : userSnapshot.getChildren()) {
                        if (hoaDonSnapshot.hasChild("tongTien")) {
                            int tongTien = hoaDonSnapshot.child("tongTien").getValue(Integer.class);
                            String ngayDat = hoaDonSnapshot.child("ngayDat").getValue(String.class);
                            String tenSanPham = hoaDonSnapshot.child("tenSanPham").getValue(String.class);
                            int soLuong = hoaDonSnapshot.child("soLuong").getValue(Integer.class);

                            // Log dữ liệu để theo dõi
                            Log.d("MyTag", "ngayDat: " + ngayDat);
                            Log.d("MyTag", "ngayBatDau: " + ngayBatDau);
                            Log.d("MyTag", "ngayKetThuc: " + ngayKetThuc);
                            Log.d("MyTag", "Tong Tien: " + tongTien);

                            if (isDateInRange(ngayDat, ngayBatDau, ngayKetThuc)) {
                                totalRevenue += tongTien;

                                // Update the quantity for the current product
                                int currentQuantity = productQuantities.getOrDefault(tenSanPham, 0);
                                int updatedQuantity = currentQuantity + soLuong;
                                productQuantities.put(tenSanPham, updatedQuantity);

                                // Check if the current product has the highest quantity
                                if (updatedQuantity > maxQuantity) {
                                    maxQuantity = updatedQuantity;
                                    topProduct = tenSanPham;
                                }

                                Log.d("MyTag", "Hóa đơn nằm trong khoảng thời gian đã chọn. Tổng doanh thu tạm thời: " + totalRevenue);
                            } else {
                                Log.d("MyTag", "Hóa đơn không nằm trong khoảng thời gian đã chọn.");
                            }
                        }
                    }
                }

                // Hiển thị tổng doanh thu trong TextView
                txtTongDoanhThu.setText(String.valueOf(totalRevenue));
                txtTop1.setText(topProduct + " - " + maxQuantity + " sản phẩm");

                // Gọi phương thức tính số đơn hàng bị hủy
                calculateCancelledOrders(ngayBatDau, ngayKetThuc);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
                Log.e("MyTag", "Error: " + error.getMessage());
            }
        });
    }

    // Phương thức tính số lượng đơn hàng bị hủy
    private void calculateCancelledOrders(String ngayBatDau, String ngayKetThuc) {
        DatabaseReference donHangKhachHuyRef = FirebaseDatabase.getInstance().getReference("DonHangKhachHuy");

        donHangKhachHuyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int cancelledOrders = 0;

                for (DataSnapshot donHangSnapshot : dataSnapshot.getChildren()) {
                    String ngayHuy = donHangSnapshot.child("ngayDat").getValue(String.class);

                    // Log dữ liệu để theo dõi
                    Log.d("MyTag", "ngayHuy: " + ngayHuy);
                    Log.d("MyTag", "ngayBatDau: " + ngayBatDau);
                    Log.d("MyTag", "ngayKetThuc: " + ngayKetThuc);

                    if (isDateInRange(ngayHuy, ngayBatDau, ngayKetThuc)) {
                        cancelledOrders++;
                        Log.d("MyTag", "Đơn hàng đã bị hủy nằm trong khoảng thời gian đã chọn. Số đơn bị hủy tạm thời: " + cancelledOrders);
                    } else {
                        Log.d("MyTag", "Đơn hàng đã bị hủy không nằm trong khoảng thời gian đã chọn.");
                    }
                }

                // Hiển thị số lượng đơn hàng bị hủy trong TextView
                txtSoLuongDonHangBiHuy.setText(String.valueOf(cancelledOrders));
                Log.d("MyTag", "Tổng số đơn hàng bị hủy sau khi tính toán: " + cancelledOrders);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
                Log.e("MyTag", "Error: " + error.getMessage());
            }
        });
    }



    // Phương thức kiểm tra xem một ngày có nằm trong khoảng thời gian đã chọn không
    private boolean isDateInRange(String date, String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date dateObj = sdf.parse(date);
            Date startDateObj = sdf.parse(startDate);
            Date endDateObj = sdf.parse(endDate);

            return dateObj != null && !dateObj.before(startDateObj) && !dateObj.after(endDateObj);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
