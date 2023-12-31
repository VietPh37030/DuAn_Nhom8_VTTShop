package anhpvph37030.fpoly.duan_nhom8.fragment.fragmentHoaDon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.Ql_HoaDonAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.HoaDon;


public class ThanhCongFrg extends Fragment {
    private Ql_HoaDonAdapter hoaDonAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ql_hoadon_trangthai, container, false);
        ListView listView = view.findViewById(R.id.lvQuanLyHoaDon);
        hoaDonAdapter = new Ql_HoaDonAdapter(getContext(), R.layout.item_ql_hoadon, new ArrayList<>());
        listView.setAdapter(hoaDonAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("HoaDonThanhToan");

        // Thêm ValueEventListener để lắng nghe sự thay đổi dữ liệu
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HoaDon> hoaDonList = new ArrayList<>();

                // Lặp qua từng người dùng
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Lặp qua từng hóa đơn của người dùng
                    for (DataSnapshot hoaDonSnapshot : userSnapshot.getChildren()) {
                        // Lấy mã hóa đơn (key) và tất cả thông tin của hóa đơn (value)
                        String maHoaDon = hoaDonSnapshot.getKey();

                        // Lấy thông tin chi tiết của hóa đơn
                        String diaChi = hoaDonSnapshot.child("diaChi").getValue(String.class);
                        String imageUrl = hoaDonSnapshot.child("imageUrl").getValue(String.class);
                        String ngayDat = hoaDonSnapshot.child("ngayDat").getValue(String.class);
                        String nguoiNhan = hoaDonSnapshot.child("nguoiNhan").getValue(String.class);
                        String sdt = hoaDonSnapshot.child("sdt").getValue(String.class);
                        String tenSanPham = hoaDonSnapshot.child("tenSanPham").getValue(String.class);
                        int soLuong = hoaDonSnapshot.child("soLuong").getValue(Integer.class);
                        int tongTien = hoaDonSnapshot.child("tongTien").getValue(Integer.class);
                        int trangThai = hoaDonSnapshot.child("trangThai").getValue(Integer.class);
                        Log.d("ThanhCongFrg", "maHoaDon: " + maHoaDon);
                        Log.d("ThanhCongFrg", "diaChi: " + diaChi);
                        Log.d("ThanhCongFrg", "imageUrl: " + imageUrl);
                        Log.d("ThanhCongFrg", "ngayDat: " + ngayDat);
                        Log.d("ThanhCongFrg", "nguoiNhan: " + nguoiNhan);
                        Log.d("ThanhCongFrg", "sdt: " + sdt);
                        Log.d("ThanhCongFrg", "tenSanPham: " + tenSanPham);
                        Log.d("ThanhCongFrg", "soLuong: " + soLuong);
                        Log.d("ThanhCongFrg", "tongTien: " + tongTien);
                        Log.d("ThanhCongFrg", "trangThai: " + trangThai);

                        // Filter only items with trangThai = 3
                        if (trangThai == 3) {
                            // Tạo đối tượng HoaDon và thêm vào danh sách
                            HoaDon hoaDon = new HoaDon(maHoaDon, imageUrl, tenSanPham, soLuong, tongTien, nguoiNhan, sdt, diaChi, ngayDat, trangThai);
                            hoaDonList.add(hoaDon);

                            // Chuyển đơn hàng thành công vào bảng GiaoHangThanhCong
                            moveHoaDonToGiaoHangThanhCong(userSnapshot.getKey(), maHoaDon, hoaDon);
                        }
                    }
                }

                // Cập nhật dữ liệu của adapter và thông báo sự thay đổi
                hoaDonAdapter.clear();
                hoaDonAdapter.addAll(hoaDonList);
                hoaDonAdapter.notifyDataSetChanged();

                Log.d("ThanhCongFrg", "Data pushed to ListView: " + hoaDonList.size() + " items");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ThanhCongFrg", "Error reading data from Firebase: " + error.getMessage());
            }
        });

        return view;
    }

    // Phương thức chuyển đơn hàng vào bảng GiaoHangThanhCong
    private void moveHoaDonToGiaoHangThanhCong(String userId, String maHoaDon, HoaDon hoaDon) {
        DatabaseReference giaoHangThanhCongRef = FirebaseDatabase.getInstance().getReference().child("GiaoHangThanhCong");
        DatabaseReference newHoaDonRef = giaoHangThanhCongRef.child(userId).child(maHoaDon);

        // Set dữ liệu của đơn hàng mới trong bảng GiaoHangThanhCong
        newHoaDonRef.setValue(hoaDon);
    }
}
