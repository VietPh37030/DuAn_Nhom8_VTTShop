package anhpvph37030.fpoly.duan_nhom8.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    private int selectedFilterStatus = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadon);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        listViewHoaDon = (ListView) findViewById(R.id.lsthoadon);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        // Thêm lựa chọn lọc vào toolbar
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.loc) {
                    // Hiển thị dialog để chọn trạng thái
                    showFilterDialog();
                    return true;
                }
                return false;
            }
        });

        // Gọi hàm để hiển thị dữ liệu từ Firebase
        displayDataFromFirebase(selectedFilterStatus);
    }

    private void ExitsActi() {
        finish();
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn trạng thái");

        String[] statusArray = {"Chờ xác nhận", "Đã xác nhận", "Đang giao", "Giao hàng thành công", "Đã hủy"};

        builder.setSingleChoiceItems(statusArray, selectedFilterStatus, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                selectedFilterStatus = selectedIndex;
                dialogInterface.dismiss();
                // Gọi hàm để hiển thị dữ liệu từ Firebase với trạng thái đã chọn
                displayDataFromFirebase(selectedFilterStatus);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void displayDataFromFirebase(int selectedFilterStatus) {
        // Sử dụng custom adapter HoaDonAdapter để hiển thị dữ liệu từ Firebase
        HoaDonAdapter hoaDonAdapter = new HoaDonAdapter(
                this,
                R.layout.item_hoadon,
                new ArrayList<>(),
                this.selectedFilterStatus
        );

        // Liên kết Adapter với ListView
        listViewHoaDon.setAdapter(hoaDonAdapter);

        // Thực hiện lắng nghe sự thay đổi dữ liệu trên Firebase và cập nhật Adapter
        hoaDonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hoaDonList = new ArrayList<>();

                // Lặp qua dataSnapshot để lấy dữ liệu từ Firebase và lọc theo trạng thái
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                    if (hoaDon != null && (HoaDonActivity.this.selectedFilterStatus == -1 || hoaDon.getTrangThai() == HoaDonActivity.this.selectedFilterStatus)) {
                        hoaDonList.add(hoaDon);
                    }
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
