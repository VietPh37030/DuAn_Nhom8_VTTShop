package anhpvph37030.fpoly.duan_nhom8.Util;

// FirebaseDBManager.java


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.model.HoaDon;

public class FirebaseDBManager {

    private DatabaseReference databaseReference;

    public FirebaseDBManager() {
        // Khởi tạo DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Hoadonthanhtoan");
    }

    public void getHoaDonData(final FirebaseCallback callback) {
        // Lắng nghe sự thay đổi trong dữ liệu Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HoaDon> hoaDonList = new ArrayList<>();

                // Lặp qua mỗi child trong dataSnapshot để lấy dữ liệu
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                    hoaDonList.add(hoaDon);
                }

                // Gọi callback để truyền dữ liệu về Fragment
                callback.onDataReceived(hoaDonList);
                Log.d("FirebaseDBManager", "Received data from Firebase: " + hoaDonList.size() + " items");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
                Log.e("FirebaseDBManager", "Error reading data from Firebase: " + error.getMessage());
            }
        });
    }
}
