package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Activities.ProductDeltaActivity;
import anhpvph37030.fpoly.duan_nhom8.Adapter.ProductAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Product;


public class MainFrg extends Fragment {
    private DatabaseReference productsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_frg, container, false);

        // Khởi tạo DatabaseReference để truy cập nút "products" trong Firebase Realtime Database
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        // Liên kết Adapter với GridView
        GridView gridView = v.findViewById(R.id.gvDT);

        // Lắng nghe sự kiện khi có thay đổi trong dữ liệu
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }

                // Tạo Adapter và setAdapter cho GridView
                ProductAdapter adapter = new ProductAdapter(getContext(), productList);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi truy cập cơ sở dữ liệu
                Toast.makeText(getContext(), "Lỗi khi truy cập cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi người dùng chọn một sản phẩm
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy sản phẩm được chọn từ danh sách
                Product selectedProduct = (Product) parent.getItemAtPosition(position);

                // Tạo Intent để chuyển từ MainFrg sang ProductDetailActivity
                Intent intent = new Intent(getActivity(), ProductDeltaActivity.class);

                // Đặt thông tin sản phẩm vào Intent
                intent.putExtra("PRODUCT_ID", selectedProduct.getId());

                // Chuyển sang ProductDetailActivity
                startActivity(intent);
            }
        });

        return v;
    }
}
