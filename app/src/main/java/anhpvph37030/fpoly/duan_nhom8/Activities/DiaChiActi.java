package anhpvph37030.fpoly.duan_nhom8.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import anhpvph37030.fpoly.duan_nhom8.Adapter.DiaChiAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.ThongTinDiaChi;

public class DiaChiActi extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference diaChiRef;

    private ListView listView;
    private ArrayList<ThongTinDiaChi> diaChiList;
    private DiaChiAdapter diaChiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_chi);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            diaChiRef = FirebaseDatabase.getInstance().getReference().child("thongtinnhanhang").child(currentUser.getUid());
        }

        listView = findViewById(R.id.lstdiachi);
        diaChiList = new ArrayList<>();
        diaChiAdapter = new DiaChiAdapter(this, diaChiList);
        listView.setAdapter(diaChiAdapter);

        // Thêm sự kiện click vào button để hiển thị dialog thêm địa chỉ
        ImageButton btnThem = findViewById(R.id.btnThem);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressDialog();
            }
        });

        // Hiển thị danh sách địa chỉ
        updateListView();
    }

    private void showAddAddressDialog() {
        // Tạo dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_diachi);

        EditText edtHoTen = dialog.findViewById(R.id.ed_hovaten);
        EditText edtSoDienThoai = dialog.findViewById(R.id.ed_Gia);
        EditText edtDiaChi = dialog.findViewById(R.id.ed_soLuong);

        Button btnThemDiaChi = dialog.findViewById(R.id.btnthemdiachi);
        Button btnHuy = dialog.findViewById(R.id.btnHuydiachi);

        // Thêm sự kiện click vào button "Thêm Địa Chỉ"
        btnThemDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ EditText
                String hoTen = edtHoTen.getText().toString();
                String soDienThoai = edtSoDienThoai.getText().toString();
                String diaChi = edtDiaChi.getText().toString();

                // Kiểm tra xem các trường có trống không
                if (hoTen.isEmpty() || soDienThoai.isEmpty() || diaChi.isEmpty()) {
                    Toast.makeText(DiaChiActi.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo đối tượng ThongTinDiaChi
                ThongTinDiaChi thongTinDiaChi = new ThongTinDiaChi(hoTen, soDienThoai, diaChi);

                // Thêm thông tin địa chỉ vào Firebase
                diaChiRef.push().setValue(thongTinDiaChi, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            Toast.makeText(DiaChiActi.this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
                            // Cập nhật danh sách địa chỉ
                            updateListView();
                            // Đóng dialog
                            dialog.dismiss();
                        } else {
                            Toast.makeText(DiaChiActi.this, "Lỗi khi thêm địa chỉ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Thêm sự kiện click vào button "Hủy"
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng dialog
                dialog.dismiss();
            }
        });

        // Hiển thị dialog
        dialog.show();
    }

    private void updateListView() {
        // Xóa danh sách hiện tại
        diaChiList.clear();

        // Lắng nghe sự kiện thay đổi trên Firebase và cập nhật danh sách
        diaChiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ThongTinDiaChi thongTinDiaChi = dataSnapshot.getValue(ThongTinDiaChi.class);
                    diaChiList.add(thongTinDiaChi);
                }

                // Thông báo Adapter cập nhật lại ListView
                diaChiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
}
