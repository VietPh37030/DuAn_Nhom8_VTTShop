package anhpvph37030.fpoly.duan_nhom8.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
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

public class HoaDonActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listViewHoaDon;
    private DatabaseReference hoaDonRef;
    private List<HoaDon> hoaDonList;

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
        toolbar.setNavigationIcon(R.drawable.ic_previous);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitsActi();
            }
        });
        // Gọi hàm để hiển thị dữ liệu từ Firebase
        displayDataFromFirebase();
    }

    private void ExitsActi() {
        finish();
    }

    private void displayDataFromFirebase() {
        // Sử dụng custom adapter HoaDonAdapter để hiển thị dữ liệu từ Firebase
        HoaDonAdapter hoaDonAdapter = new HoaDonAdapter(
                this,
                R.layout.item_hoadon,
                new ArrayList<>()
        );

        // Liên kết Adapter với ListView
        listViewHoaDon.setAdapter(hoaDonAdapter);

        // Thực hiện lắng nghe sự thay đổi dữ liệu trên Firebase và cập nhật Adapter
        hoaDonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hoaDonList = new ArrayList<>();

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
                // Hiện tại chỉ log lỗi, bạn có thể xử lý thêm theo ý của mình
                Log.e("HoaDonActivity", "Lỗi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
            }
        });
    }
}
