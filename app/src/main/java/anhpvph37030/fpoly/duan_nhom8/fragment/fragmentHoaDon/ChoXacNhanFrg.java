package anhpvph37030.fpoly.duan_nhom8.fragment.fragmentHoaDon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.Ql_HoaDonAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.Util.FirebaseCallback;
import anhpvph37030.fpoly.duan_nhom8.Util.FirebaseDBManager;
import anhpvph37030.fpoly.duan_nhom8.model.HoaDon;


public class ChoXacNhanFrg extends Fragment {
    private Ql_HoaDonAdapter hoaDonAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ql_hoadon_trangthai ,container, false);
        // Khởi tạo ListView và Adapter
        ListView listView = view.findViewById(R.id.lvQuanLyHoaDon);
        hoaDonAdapter = new Ql_HoaDonAdapter(getContext(), R.layout.item_ql_hoadon, new ArrayList<>());
        listView.setAdapter(hoaDonAdapter);

        // Gọi FirebaseDBManager để lấy dữ liệu
        // Thực hiện truy vấn Firebase bằng DatabaseReference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Hoadonthanhtoan");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HoaDon> hoaDonList = new ArrayList<>();

                // Lặp qua mỗi child trong dataSnapshot để lấy dữ liệu
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                    hoaDonList.add(hoaDon);
                }
                Log.d("ChoXacNhanFrg", "DataSnapshot count: " + dataSnapshot.getChildrenCount());


                // Cập nhật dữ liệu của adapter và thông báo sự thay đổi
                hoaDonAdapter.clear();
                hoaDonAdapter.addAll(hoaDonList);
                hoaDonAdapter.notifyDataSetChanged();

                // Log để kiểm tra dữ liệu đã được đẩy lên ListView
                Log.d("ChoXacNhanFrg", "Data pushed to ListView: " + hoaDonList.size() + " items");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
                Log.e("ChoXacNhanFrg", "Error reading data from Firebase: " + error.getMessage());
                error.toException().printStackTrace(); // In lỗi chi tiết ra Logcat
            }
        });


        return view;
    }

    }
