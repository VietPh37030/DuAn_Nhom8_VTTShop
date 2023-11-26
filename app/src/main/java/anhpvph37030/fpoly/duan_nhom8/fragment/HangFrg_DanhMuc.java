package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import anhpvph37030.fpoly.duan_nhom8.Adapter.HangFrgDanhMucAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Product;


public class HangFrg_DanhMuc extends Fragment {


    private ListView listViewHang;
    private List<Product> productList;
    private HangFrgDanhMucAdapter hangFrgDanhMucAdapter; // Đổi tên ở đây

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hang_frg__danh_muc, container, false);

        listViewHang = v.findViewById(R.id.lsthangdanhmuc);
        productList = new ArrayList<>();
        hangFrgDanhMucAdapter = new HangFrgDanhMucAdapter(getContext(), R.layout.item_sanpham, productList); // Đổi tên ở đây

        // Gắn Adapter vào ListView
        listViewHang.setAdapter(hangFrgDanhMucAdapter);

        // Gọi hàm để lấy dữ liệu từ Firebase và đổ vào ListView
        loadDataFromFirebase();

        return v;
    }

    // ... phần code khác
    private void loadDataFromFirebase() {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    productList.add(product);
                }

                // Cập nhật Adapter khi có dữ liệu mới
                hangFrgDanhMucAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

}