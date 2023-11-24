package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import anhpvph37030.fpoly.duan_nhom8.Adapter.ProductAdapter;
import anhpvph37030.fpoly.duan_nhom8.DAO.DanhMucDAO;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class DanhMucFrg extends Fragment {

    private ImageButton btnThemHang;
    private ListView lstDanhMuc;
    private DanhMucDAO danhMucDAO;
    private List<DanhMuc> danhMucList;
    private DanhMucAdapter danhMucAdapter;

    public DanhMucFrg() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        danhMucDAO = new DanhMucDAO();
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

        // Lắng nghe sự kiện khi có dữ liệu mới từ Firebase
        danhMucDAO.getDanhMucRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Xóa danh sách hiện tại
                danhMucList.clear();

                // Lấy dữ liệu từ dataSnapshot và thêm vào danh sách
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                    danhMucList.add(danhMuc);
                }

                // Cập nhật giao diện
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

        // Xử lý sự kiện khi nhấn nút "Thêm" trong layout thêm hãng sản phẩm
        Button btnThemHang = dialogView.findViewById(R.id.btnAddhang);
        btnThemHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện thêm hãng sản phẩm vào Firebase
                themHangSanPham(alertDialog);

                // Đóng dialog
                alertDialog.dismiss();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Hủy"
        Button btnHuy = dialogView.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng dialog
                alertDialog.dismiss();
            }
        });
    }

    private void themHangSanPham(AlertDialog alertDialog) {
        // Lấy dữ liệu từ EditText
        EditText edURL = alertDialog.findViewById(R.id.ed_URL_SanPham);
        EditText edTenHang = alertDialog.findViewById(R.id.ed_TenHang);

        String urlSanPham = edURL.getText().toString().trim();
        String tenHang = edTenHang.getText().toString().trim();

        // Generate ID
        danhMucDAO.getMaxMaDanhMuc(new DanhMucDAO.MaxMaDanhMucCallback() {
            @Override
            public void onCallback(int maxMaDanhMuc) {
                int maDanhMuc = maxMaDanhMuc + 1;

                if (!TextUtils.isEmpty(urlSanPham) && !TextUtils.isEmpty(tenHang)) {
                    DanhMuc danhMuc = new DanhMuc(maDanhMuc, urlSanPham, tenHang);
                    danhMucDAO.addDanhMuc(danhMuc);

                    // Hiển thị thông báo hoặc cập nhật giao diện nếu cần
                    Toast.makeText(getContext(), "Đã thêm hãng sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
