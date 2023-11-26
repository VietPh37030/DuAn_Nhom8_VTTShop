package anhpvph37030.fpoly.duan_nhom8.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

        ImageButton btnThem = findViewById(R.id.btnThem);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressDialog();
            }
        });

        diaChiAdapter.setEditClickListener(new DiaChiAdapter.EditClickListener() {
            @Override
            public void onEditClick(int position) {
                ThongTinDiaChi selectedDiaChi = diaChiList.get(position);
                showEditAddressDialog(selectedDiaChi);
            }
        });

        diaChiAdapter.setDeleteClickListener(new DiaChiAdapter.DeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                ThongTinDiaChi selectedDiaChi = diaChiList.get(position);
                showDeleteConfirmationDialog(selectedDiaChi);
            }
        });

        updateListView();
    }

    private void showEditAddressDialog(ThongTinDiaChi selectedDiaChi) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Chỉnh sửa địa chỉ");

        View view = getLayoutInflater().inflate(R.layout.dialog_update_diachi, null);
        alertDialogBuilder.setView(view);

        EditText edtHoTen = view.findViewById(R.id.up_hovaten);
        EditText edtSoDienThoai = view.findViewById(R.id.up_sodienthoai);
        EditText edtDiaChi = view.findViewById(R.id.up_diachi);

        edtHoTen.setText(selectedDiaChi.getHoTen());
        edtSoDienThoai.setText(selectedDiaChi.getSoDienThoai());
        edtDiaChi.setText(selectedDiaChi.getDiaChi());

        alertDialogBuilder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String hoTen = edtHoTen.getText().toString();
                String soDienThoai = edtSoDienThoai.getText().toString();
                String diaChi = edtDiaChi.getText().toString();

                if (hoTen.isEmpty() || soDienThoai.isEmpty() || diaChi.isEmpty()) {
                    Toast.makeText(DiaChiActi.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference selectedDiaChiRef = diaChiRef.child(selectedDiaChi.getId());
                ThongTinDiaChi updatedDiaChi = new ThongTinDiaChi(selectedDiaChi.getId(), hoTen, soDienThoai, diaChi);
                selectedDiaChiRef.setValue(updatedDiaChi);

                Toast.makeText(DiaChiActi.this, "Cập nhật địa chỉ thành công", Toast.LENGTH_SHORT).show();

                updateListView();
            }
        });

        alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.create().show();
    }

    private void showAddAddressDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Thêm địa chỉ");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_diachi, null);
        alertDialogBuilder.setView(view);

        EditText edtid = view.findViewById(R.id.ed_id);
        EditText edtHoTen = view.findViewById(R.id.ed_hovaten);
        EditText edtSoDienThoai = view.findViewById(R.id.ed_Gia);
        EditText edtDiaChi = view.findViewById(R.id.ed_soLuong);

        alertDialogBuilder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = edtid.getText().toString();
                String hoTen = edtHoTen.getText().toString();
                String soDienThoai = edtSoDienThoai.getText().toString();
                String diaChi = edtDiaChi.getText().toString();

                if (hoTen.isEmpty() || soDienThoai.isEmpty() || diaChi.isEmpty()) {
                    Toast.makeText(DiaChiActi.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                ThongTinDiaChi thongTinDiaChi = new ThongTinDiaChi(id, hoTen, soDienThoai, diaChi);

                diaChiRef.child(id).setValue(thongTinDiaChi);

                Toast.makeText(DiaChiActi.this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();

                updateListView();
            }
        });

        alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.create().show();
    }

    private void showDeleteConfirmationDialog(ThongTinDiaChi selectedDiaChi) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Xác nhận xóa");
        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xóa địa chỉ này?");
        alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                diaChiRef.child(selectedDiaChi.getId()).removeValue();
                Toast.makeText(DiaChiActi.this, "Xóa địa chỉ thành công", Toast.LENGTH_SHORT).show();
                updateListView();
            }
        });
        alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.create().show();
    }

    private void updateListView() {
        diaChiList.clear();

        diaChiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ThongTinDiaChi thongTinDiaChi = dataSnapshot.getValue(ThongTinDiaChi.class);
                    diaChiList.add(thongTinDiaChi);
                }

                diaChiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
}
