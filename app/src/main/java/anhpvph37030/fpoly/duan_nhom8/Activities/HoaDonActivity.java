package anhpvph37030.fpoly.duan_nhom8.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.HoaDonAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.HoaDon;
import anhpvph37030.fpoly.duan_nhom8.model.Product;


public class HoaDonActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listViewHoaDon;
    private DatabaseReference hoaDonRef;
    private List<HoaDon> hoadonlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadon);
        toolbar = findViewById(R.id.toolbar);
        listViewHoaDon = findViewById(R.id.lsthoadon);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // Người dùng đã đăng nhập, thực hiện các hành động tiếp theo
            String userID = currentUser.getUid();
            hoaDonRef = FirebaseDatabase.getInstance().getReference().child("HoaDonThanhToan").child(userID);
        } else {
            // Người dùng chưa đăng nhập, thực hiện các hành động phù hợp với ứng dụng của bạn
        }
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExitsActi();
                }
            });
        // Gọi hàm để hiển thị dữ liệu từ Firebase
        displayDataFromFirebase();
//        listViewHoaDon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (hoadonlist != null && position < hoadonlist.size()) {
//                    HoaDon selectedHoadon = hoadonlist.get(position);
//                    if (selectedHoadon != null) {
//                        // Thêm dữ liệu vào Intent
//                        Intent intent = new Intent(HoaDonActivity.this, ChiTietHoaDon.class);
//                        intent.putExtra("ORDER_ID", selectedHoadon.getMaHoaDon());
//                        intent.putExtra("ORDER_QUANTITY", String.valueOf(selectedHoadon.getSoLuong()));
//                        intent.putExtra("ORDER_SUM", String.valueOf(selectedHoadon.getTongTien()));
//                        intent.putExtra("ORDER_NAME", selectedHoadon.getTenSanPham());
//                        intent.putExtra("ORDER_IMAGE", selectedHoadon.getImageUrl());
//
//                        // Chuyển sang ChiTietHoaDonActivity
//                        startActivity(intent);
//                    }
//                }
//            }
//        });
    }

    private void ExitsActi() {
        finish();
    }

    private void displayDataFromFirebase() {
        // Sử dụng custom adapter HoaDonAdapter để hiển thị dữ liệu từ Firebase
        HoaDonAdapter hoaDonAdapter = new HoaDonAdapter(
                this,
                R.layout.item_hoadon,
                new ArrayList<HoaDon>()
        );

        // Liên kết Adapter với ListView
        listViewHoaDon.setAdapter(hoaDonAdapter);

        // Thực hiện lắng nghe sự thay đổi dữ liệu trên Firebase và cập nhật Adapter
        hoaDonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HoaDon> hoaDonList = new ArrayList<>();

                // Lặp qua dataSnapshot để lấy dữ liệu từ Firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                    hoaDonList.add(hoaDon);
                }

                // Cập nhật dữ liệu mới cho Adapter
                hoaDonAdapter.clear();
                hoaDonAdapter.addAll(hoaDonList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Log.e("HoaDonActivity", "Lỗi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
            }
        });

    }

}
