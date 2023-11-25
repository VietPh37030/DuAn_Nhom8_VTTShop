package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import anhpvph37030.fpoly.duan_nhom8.Adapter.ProductAdminAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class AdminQL extends Fragment {

    private DatabaseReference productsRef;
    private ProductAdminAdapter productAdminAdapter;
    private SearchView searchView;
    private ImageButton btnThem;
    private ListView lstadminql;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_admin, container, false);

        searchView = view.findViewById(R.id.search_view);
        btnThem = view.findViewById(R.id.btnThem);
        lstadminql = view.findViewById(R.id.lstadminql);

        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        // Initialize Adapter
        productAdminAdapter = new ProductAdminAdapter(getContext(), new ArrayList<>());
        lstadminql.setAdapter(productAdminAdapter);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event here
            }
        });

        // Listen for changes in the data
        // ...

// Listen for changes in the data
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);

                    int maDanhMuc = product.getMaDanhMuc();
                    DatabaseReference danhMucRef = FirebaseDatabase.getInstance().getReference().child("danhmuc").child(String.valueOf(maDanhMuc));
                    Log.d("ProductAdminAdapter", "Ma danh muc: " + String.valueOf(maDanhMuc));
                    danhMucRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                DanhMuc danhMuc = dataSnapshot.getValue(DanhMuc.class);
                                Log.d("ProductAdminAdapter", "DataSnapshot exists");
                                if (danhMuc != null) {
                                    // Use danhMuc information here
                                    // For example, danhMuc.getTenHang() to get the brand name
                                    Log.d("ProductAdminAdapter", "Ten hang: " + danhMuc.getTenHang());
                                    String tenHang = danhMuc.getTenHang();
                                    Log.d("AdminQL", "Tên hãng sản phẩm: " + tenHang);

                                    // Perform any additional actions with the tenHang information
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors if needed
                            Log.e("ProductAdminAdapter", "onCancelled: " + databaseError.getMessage());
                            // Xử lý lỗi nếu cần
                        }
                    });
                }

                productAdminAdapter.clear();
                productAdminAdapter.addAll(productList);
                productAdminAdapter.notifyDataSetChanged();
                Log.d("AdminQL", "Dữ liệu sản phẩm từ Firebase: " + productList.size() + " sản phẩm");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
                Toast.makeText(getContext(), "Error accessing the database", Toast.LENGTH_SHORT).show();
            }
        });

// ...


        // Handle item click event
        lstadminql.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected product from the adapter
                Product selectedProduct = productAdminAdapter.getItem(position);

                // Handle the event when a product is clicked
                // ...
            }
        });

        // Handle search view text change event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the event when the user submits a query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the event when the text in the search view changes
                return false;
            }
        });

        return view;
    }
}
