package anhpvph37030.fpoly.duan_nhom8.DAO;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;

public class DanhMucDAO {
    private DatabaseReference danhMucRef;

    public DanhMucDAO() {
        // Thay đổi đường dẫn tới "danhmuc" nếu cần
        danhMucRef = FirebaseDatabase.getInstance().getReference().child("danhmuc");
    }

    public void addDanhMuc(DanhMuc danhMuc) {
        // Implement your logic to get the maximum maDanhMuc from Firebase
        getMaxMaDanhMuc(new MaxMaDanhMucCallback() {
            @Override
            public void onCallback(int maxMaDanhMuc) {
                int newMaDanhMuc = maxMaDanhMuc + 1;

                // Set the newMaDanhMuc to the danhMuc object
                danhMuc.setMaDanhMuc(newMaDanhMuc);

                // Thực hiện thêm hãng sản phẩm vào Firebase
                DatabaseReference newHangRef = danhMucRef.child(String.valueOf(newMaDanhMuc));
                newHangRef.setValue(danhMuc);
            }
        });
    }

    // Callback interface to handle the result of getMaxMaDanhMuc
    public void getMaxMaDanhMuc(MaxMaDanhMucCallback callback) {
        // Implement your logic here to get the maximum maDanhMuc from Firebase
        // This method should invoke the callback with the result when available
        // For example, if you are querying Firebase asynchronously, invoke the callback in the completion listener
        // Replace the placeholder logic below with your actual implementation
        danhMucRef.orderByChild("maDanhMuc").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int maxMaDanhMuc = 0;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        maxMaDanhMuc = snapshot.child("maDanhMuc").getValue(Integer.class);
                    }
                }

                callback.onCallback(maxMaDanhMuc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }

    // Define the callback interface
    public interface MaxMaDanhMucCallback {
        void onCallback(int maxMaDanhMuc);
    }

    public DatabaseReference getDanhMucRef() {
        return danhMucRef;
    }
}
