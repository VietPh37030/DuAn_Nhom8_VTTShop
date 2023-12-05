package anhpvph37030.fpoly.duan_nhom8.fragment.fragmentHoaDon;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Activities.ChiTietHoaDonAdmin;
import anhpvph37030.fpoly.duan_nhom8.Adapter.Ql_HoaDonAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.HoaDon;

public class ChoXacNhanFrg extends Fragment {
    private Ql_HoaDonAdapter hoaDonAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ql_hoadon_trangthai, container, false);
        ListView listView = view.findViewById(R.id.lvQuanLyHoaDon);
        hoaDonAdapter = new Ql_HoaDonAdapter(getContext(), R.layout.item_ql_hoadon, new ArrayList<>());
        listView.setAdapter(hoaDonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HoaDon selectedHoaDon = hoaDonAdapter.getItem(position);
                if (selectedHoaDon != null) {
                    // Chuyển sang màn hình chi tiết và chuyển dữ liệu hóa đơn
                    navigateToChiTietHoaDon(selectedHoaDon);
                }
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HoaDonThanhToan");

        // Thêm ValueEventListener để lắng nghe sự thay đổi dữ liệu
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HoaDon> hoaDonList = new ArrayList<>();

                // Lặp qua từng người dùng
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey(); // Lấy userId từ key của mỗi user

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

                        Log.d("ChoXacNhanFrg", "userId: " + userId); // Sử dụng userId ở đây nếu cần
                        Log.d("ChoXacNhanFrg", "maHoaDon: " + maHoaDon);
                        Log.d("ChoXacNhanFrg", "diaChi: " + diaChi);
                        Log.d("ChoXacNhanFrg", "imageUrl: " + imageUrl);
                        Log.d("ChoXacNhanFrg", "ngayDat: " + ngayDat);
                        Log.d("ChoXacNhanFrg", "nguoiNhan: " + nguoiNhan);
                        Log.d("ChoXacNhanFrg", "sdt: " + sdt);
                        Log.d("ChoXacNhanFrg", "tenSanPham: " + tenSanPham);
                        Log.d("ChoXacNhanFrg", "soLuong: " + soLuong);
                        Log.d("ChoXacNhanFrg", "tongTien: " + tongTien);
                        Log.d("ChoXacNhanFrg", "trangThai: " + trangThai);

                        // Filter only items with trangThai = 0
                        if (trangThai == 0) {
                            // Tạo đối tượng HoaDon và thêm vào danh sách
                            HoaDon hoaDon = new HoaDon(maHoaDon, imageUrl, tenSanPham, soLuong, tongTien, nguoiNhan, sdt, diaChi, ngayDat, trangThai);
                            hoaDonList.add(hoaDon);
                        }
                    }
                }

                // Cập nhật dữ liệu của adapter và thông báo sự thay đổi
                hoaDonAdapter.clear();
                hoaDonAdapter.addAll(hoaDonList);
                hoaDonAdapter.notifyDataSetChanged();

                Log.d("ChoXacNhanFrg", "Data pushed to ListView: " + hoaDonList.size() + " items");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ChoXacNhanFrg", "Error reading data from Firebase: " + error.getMessage());
            }
        });

        return view;
    }

    private void navigateToChiTietHoaDon(HoaDon selectedHoaDon) {
        // Tạo một Intent để chuyển sang màn hình chi tiết
        Intent intent = new Intent(getContext(), ChiTietHoaDonAdmin.class);

        // Đính kèm dữ liệu hóa đơn vào Intent
        intent.putExtra("maHoaDon", selectedHoaDon.getMaHoaDon());
        intent.putExtra("imageUrl", selectedHoaDon.getImageUrl());
        intent.putExtra("tenSanPham", selectedHoaDon.getTenSanPham());
        intent.putExtra("soLuong", selectedHoaDon.getSoLuong());
        intent.putExtra("tongTien", selectedHoaDon.getTongTien());
        intent.putExtra("nguoiNhan", selectedHoaDon.getNguoiNhan());
        intent.putExtra("sdt", selectedHoaDon.getSdt());
        intent.putExtra("diaChi", selectedHoaDon.getDiaChi());
        intent.putExtra("ngayDat", selectedHoaDon.getNgayDat());
        intent.putExtra("trangThai", selectedHoaDon.getTrangThai());

        // Chuyển sang màn hình chi tiết
        startActivity(intent);
    }

}
