package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.ProductAdminAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Product;


public class AdminQL extends Fragment {

    ListView lstadminql;
    ImageButton btnThem;
    SearchView searchView;
    private DatabaseReference productsRef;
    private ProductAdminAdapter productAdminAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_admin, container, false);
        searchView = view.findViewById(R.id.search_view);
        btnThem = view.findViewById(R.id.btnThem);
        // Xử lý sự kiện khi thanh tìm kiếm thay đổi
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng nhấn Enter trên bàn phím
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý khi người dùng thay đổi nội dung trong thanh tìm kiếm
                return false;
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện ở đây
            }
        });
        ListView listView = view.findViewById(R.id.lstadminql);
        ImageButton btnThem = view.findViewById(R.id.btnThem);

        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        // Khởi tạo Adapter
        productAdminAdapter = new ProductAdminAdapter(getContext(), new ArrayList<>());
        listView.setAdapter(productAdminAdapter);

        // Lắng nghe sự kiện khi có thay đổi trong dữ liệu
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }

                // Cập nhật dữ liệu mới cho Adapter
                productAdminAdapter.clear();
                productAdminAdapter.addAll(productList);
                productAdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi truy cập cơ sở dữ liệu
                Toast.makeText(getContext(), "Lỗi khi truy cập cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi người dùng chọn một sản phẩm
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy sản phẩm được chọn từ danh sách
                Product selectedProduct = productAdminAdapter.getItem(position);

                // Xử lý khi người dùng chọn một sản phẩm
                // ...

            }
        });






        return view;
    }
}