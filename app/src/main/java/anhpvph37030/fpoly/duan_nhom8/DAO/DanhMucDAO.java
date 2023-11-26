package anhpvph37030.fpoly.duan_nhom8.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;

public class DanhMucDAO {
    private DatabaseReference danhMucRef;

    // Tạo một biến DatabaseReference để sử dụng chung
    public DanhMucDAO() {
        danhMucRef = FirebaseDatabase.getInstance().getReference().child("danhmuc");
    }

    public void addDanhMuc(DanhMuc danhMuc) {
        // Sử dụng biến DatabaseReference đã tạo
        getMaxMaDanhMuc(new MaxMaDanhMucCallback() {
            @Override
            public void onCallback(int maxMaDanhMuc) {
                int newMaDanhMuc = maxMaDanhMuc + 1;
                danhMuc.setMaDanhMuc(newMaDanhMuc);

                // Thực hiện thêm hãng sản phẩm vào Firebase
                DatabaseReference newHangRef = danhMucRef.child(String.valueOf(newMaDanhMuc));
                newHangRef.setValue(danhMuc);
                Log.d("DanhMucDAO", "Đã thêm danh mục mới: " + danhMuc.getTenHang() + " vào Firebase");
            }
        });
    }

    public void getMaxMaDanhMuc(MaxMaDanhMucCallback callback) {
        // Sử dụng biến DatabaseReference đã tạo
        danhMucRef.orderByChild("maDanhMuc").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int maxMaDanhMuc = 0;

                if (dataSnapshot.exists()) {
                    DataSnapshot lastChild = dataSnapshot.getChildren().iterator().next();
                    maxMaDanhMuc = lastChild.child("maDanhMuc").getValue(Integer.class);
                }

                callback.onCallback(maxMaDanhMuc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi theo nhu cầu của ứng dụng
                // Ví dụ: Log lỗi
                // Log.e("DanhMucDAO", "Error fetching max maDanhMuc", databaseError.toException());
            }
        });
    }

    public interface MaxMaDanhMucCallback {
        void onCallback(int maxMaDanhMuc);
    }

    public DatabaseReference getDanhMucRef() {
        return danhMucRef;
    }
}
