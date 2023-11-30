package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.TaiKhoanAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.TaiKhoan;

public class Ql_KhachHangFrg extends Fragment {

    private DatabaseReference databaseRef;
    private List<TaiKhoan> taiKhoanList;
    private TaiKhoanAdapter taiKhoanAdapter;

    public Ql_KhachHangFrg() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ql__khach_hang_frg, container, false);

        // Ánh xạ ListView từ layout
        ListView listView = view.findViewById(R.id.lst_taikhoan_adm);
        taiKhoanList = new ArrayList<>();
        taiKhoanAdapter = new TaiKhoanAdapter(getContext(), taiKhoanList);
        listView.setAdapter(taiKhoanAdapter);
        // Kết nối Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Lắng nghe sự kiện khi có thay đổi trong dữ liệu Firebase
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Xóa danh sách người dùng cũ
                taiKhoanList.clear();

                // Lặp qua các snapshot để lấy thông tin người dùng
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    TaiKhoan taiKhoan = userSnapshot.getValue(TaiKhoan.class);
                    taiKhoanList.add(taiKhoan);
                }

                // Cập nhật ListView
                taiKhoanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi truy cập cơ sở dữ liệu
            }
        });

        return view;
    }
}
