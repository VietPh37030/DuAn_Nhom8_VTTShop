package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.DanhMucAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;

public class DanhMucFrg extends Fragment {

    private ImageButton btnThemHang;
    private ListView lstDanhMuc;
    private DatabaseReference danhMucRef;
    private List<DanhMuc> danhMucList;
    private DanhMucAdapter danhMucAdapter;

    public DanhMucFrg() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        danhMucRef = FirebaseDatabase.getInstance().getReference().child("danhmuc");
        danhMucList = new ArrayList<>();
        danhMucAdapter = new DanhMucAdapter(getContext(), R.layout.item_danhmuc, danhMucList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_danh_muc_frg, container, false);

        btnThemHang = v.findViewById(R.id.btnThem);
        lstDanhMuc = v.findViewById(R.id.lstdanhmuc);
        lstDanhMuc.setAdapter(danhMucAdapter);

        btnThemHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThemHangLayout();
            }
        });
        danhMucAdapter.setEditDanhMucListener(new DanhMucAdapter.EditDanhMucListener() {
            @Override
            public void onEditDanhMuc(DanhMuc danhMuc) {
                openEditDanhMucDialog(danhMuc);
            }
        });

        danhMucRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                danhMucList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                    danhMucList.add(danhMuc);
                }

                danhMucAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        return v;
    }



    private void openThemHangLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_adddanhmuc, null);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnThemHang = dialogView.findViewById(R.id.btnAddhang);
        btnThemHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themHangSanPham(alertDialog);
                alertDialog.dismiss();
            }
        });


        Button btnHuy = dialogView.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void themHangSanPham(AlertDialog alertDialog) {
        EditText editText = alertDialog.findViewById(R.id.ed_maDanhMuc);
        editText.setKeyListener(null);
        EditText edURL = alertDialog.findViewById(R.id.ed_URL_SanPham);
        EditText edTenHang = alertDialog.findViewById(R.id.ed_TenHang);

        String urlSanPham = edURL.getText().toString().trim();
        String tenHang = edTenHang.getText().toString().trim();

        getMaxMaDanhMuc(new MaxMaDanhMucCallback() {
            @Override
            public void onCallback(int maxMaDanhMuc) {
                int maDanhMuc = maxMaDanhMuc + 1;

                if (!TextUtils.isEmpty(urlSanPham) && !TextUtils.isEmpty(tenHang)) {
                    DanhMuc danhMuc = new DanhMuc(maDanhMuc, urlSanPham, tenHang);
                    danhMucRef.child(String.valueOf(maDanhMuc)).setValue(danhMuc);

                    Toast.makeText(getContext(), "Đã thêm hãng sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getMaxMaDanhMuc(MaxMaDanhMucCallback callback) {
        danhMucRef.orderByChild("maDanhMuc").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int maMaDanhMuc = 0;
                Log.d("ProductAdminAdapter", "Ma danh muc: " + maMaDanhMuc);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        maMaDanhMuc = snapshot.child("maDanhMuc").getValue(Integer.class);
                        DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                        danhMucList.add(danhMuc);
                    }
                    danhMucAdapter.notifyDataSetChanged();
                    Log.d("DanhMucFrg", "Dữ liệu danh mục từ Firebase: " + danhMucList.size() + " danh mục");
                }

                callback.onCallback(maMaDanhMuc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }

    public interface MaxMaDanhMucCallback {
        void onCallback(int maxMaDanhMuc);
    }
    private void openEditDanhMucDialog(DanhMuc danhMuc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_updatedanhmuc, null);

        // Khởi tạo các trường EditText trong dialogView và thiết lập giá trị ban đầu
        EditText edURL = dialogView.findViewById(R.id.ep_URL_UpdateSp);
        EditText edTenHang = dialogView.findViewById(R.id.ed_Updatesp);
        edURL.setText(danhMuc.getUrlSanPham());
        edTenHang.setText(danhMuc.getTenHang());

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnEditDanhMuc = dialogView.findViewById(R.id.btnUpdatehang);
        btnEditDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các EditText
                String editedURL = edURL.getText().toString().trim();
                String editedTenHang = edTenHang.getText().toString().trim();

                // Kiểm tra xem có thay đổi không trước khi cập nhật
                if (!danhMuc.getUrlSanPham().equals(editedURL) || !danhMuc.getTenHang().equals(editedTenHang)) {
                    // Nếu có sự thay đổi, cập nhật vào Firebase và cập nhật vào danh sách
                    danhMuc.setUrlSanPham(editedURL);
                    danhMuc.setTenHang(editedTenHang);

                    // Cập nhật lên Firebase
                    danhMucRef.child(String.valueOf(danhMuc.getMaDanhMuc())).setValue(danhMuc);

                    // Đóng dialog
                    alertDialog.dismiss();
                } else {
                    // Nếu không có sự thay đổi, có thể thông báo hoặc không làm gì cả
                    Toast.makeText(getContext(), "Không có thay đổi để cập nhật.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnHuy = dialogView.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


}
