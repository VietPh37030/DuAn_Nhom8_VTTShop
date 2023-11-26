package anhpvph37030.fpoly.duan_nhom8.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.HangFrgDanhMucAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class HangFrg_DanhMucActivity extends AppCompatActivity {

    private ListView listViewHang;
    private List<Product> productList;
    private HangFrgDanhMucAdapter hangFrgDanhMucAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hang_danh_muc);

        listViewHang = findViewById(R.id.lsthangdanhmuc);
        productList = new ArrayList<>();
        hangFrgDanhMucAdapter = new HangFrgDanhMucAdapter(this, R.layout.item_sanpham, productList);

        // Gắn Adapter vào ListView
        listViewHang.setAdapter(hangFrgDanhMucAdapter);

        // Lấy thông tin của danh mục từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            DanhMuc selectedDanhMuc = (DanhMuc) intent.getSerializableExtra("selectedDanhMuc");
            if (selectedDanhMuc != null) {
                // Gọi hàm để lấy dữ liệu từ Firebase và đổ vào ListView, lọc theo danh mục
                loadDataFromFirebase(selectedDanhMuc.getMaDanhMuc());
            }
        }
    }

    private void loadDataFromFirebase(int maDanhMuc) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products");

        productRef.orderByChild("maDanhMuc").equalTo(maDanhMuc).addValueEventListener(new ValueEventListener() {
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
