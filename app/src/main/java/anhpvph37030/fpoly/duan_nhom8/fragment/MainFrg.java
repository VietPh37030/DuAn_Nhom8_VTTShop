package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import anhpvph37030.fpoly.duan_nhom8.Activities.ProductDeltaActivity;
import anhpvph37030.fpoly.duan_nhom8.Adapter.BannerPagerAdapter;
import anhpvph37030.fpoly.duan_nhom8.Adapter.DanhMucUserAdapter;
import anhpvph37030.fpoly.duan_nhom8.Adapter.ProductAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class MainFrg extends Fragment {
private RecyclerView rcvTheLoai;
    private DatabaseReference productsRef,danhMucRef;
    private ImageView imgAll;
    private ViewPager viewPager;
    private int[] bannerImages = {R.drawable.banner14, R.drawable.banner15, R.drawable.banner12};
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private GridView gridView;
    private List<Product> originalProductList;
    private List<Product> filteredProductList;
    private DanhMucUserAdapter danhMucUserAdapter;
    private List<DanhMuc> danhMucList; // Khởi tạo và điền dữ liệu vào danh sách này

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        danhMucRef = FirebaseDatabase.getInstance().getReference().child("danhmuc");
        danhMucList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_frg, container, false);

        gridView = v.findViewById(R.id.gvDT);
        viewPager = v.findViewById(R.id.viewPager);
        rcvTheLoai = v.findViewById(R.id.rcvTheLoai);
        SearchView searchView = v.findViewById(R.id.searchView);
        imgAll = v.findViewById(R.id.chonAll);
        rcvTheLoai.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        danhMucUserAdapter = new DanhMucUserAdapter(getContext(), danhMucList);
        danhMucUserAdapter.setOnDanhMucClickListener(new DanhMucUserAdapter.OnDanhMucClickListener() {
            @Override
            public void onDanhMucClick(DanhMuc danhMuc) {
                // Xử lý sự kiện khi một danh mục được chọn, ví dụ: hiển thị sản phẩm của hãng
                // Gọi phương thức để lấy sản phẩm của hãng từ cơ sở dữ liệu và cập nhật GridView
                loadProductsByDanhMuc(danhMuc);
            }
        });
        rcvTheLoai.setAdapter(danhMucUserAdapter);
        danhMucRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                danhMucList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                    danhMucList.add(danhMuc);
                }

                danhMucUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
        imgAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị tất cả sản phẩm
                showAllProducts();
            }
        });
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                originalProductList = new ArrayList<>();
                filteredProductList = new ArrayList<>();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    originalProductList.add(product);
                    filteredProductList.add(product);
                }

                if (productList != null) {
                    productList.clear();
                    productList.addAll(originalProductList);
                } else {
                    productList = new ArrayList<>(originalProductList);
                }

                BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(getContext(), bannerImages);
                viewPager.setAdapter(bannerPagerAdapter);

                productAdapter = new ProductAdapter(getContext(), productList);
                gridView.setAdapter(productAdapter);

                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi khi truy cập cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (productList != null && position < productList.size()) {
                    Product selectedProduct = productList.get(position);

                    if (selectedProduct != null) {
                        Intent intent = new Intent(getActivity(), ProductDeltaActivity.class);
                        Log.d("MainFrg", "Số lượng Sản phẩm đã chọn: " + selectedProduct.getQuantity1());
                        intent.putExtra("PRODUCT_ID", selectedProduct.getId());
                        intent.putExtra("PRODUCT_NAME", selectedProduct.getName());
                        intent.putExtra("PRODUCT_PRICE", selectedProduct.getPrice());
                        intent.putExtra("PRODUCT_IMAGE_URL", selectedProduct.getImage());
                        intent.putExtra("PRODUCT_description", selectedProduct.getDescription());
                        intent.putExtra("PRODUCT_QUANTITY", String.valueOf(selectedProduct.getQuantity1()));
                        Log.d("MainFrg", "Gửi Intent với Số lượng: " + selectedProduct.getQuantity1());
                        startActivity(intent);
                    }
                }
            }
        });

        return v;
    }
    private void showAllProducts() {
        // Cập nhật GridView với toàn bộ danh sách sản phẩm
        updateGridView(originalProductList);
    }
//    private void updateDanhMucAdapter(List<DanhMuc> updatedList) {
//        danhMucUserAdapter.clear();
//        danhMucUserAdapter.addAll(updatedList);
//        danhMucUserAdapter.notifyDataSetChanged();
//    }

    // Thêm phương thức để cập nhật GridView với danh sách sản phẩm đã lọc
    private void updateGridView(List<Product> filteredProducts) {
        productAdapter.updateData(filteredProducts);
    }
    private void loadProductsByDanhMuc(DanhMuc danhMuc) {
        // Lấy mã danh mục của danhMuc
        int maDanhMuc = danhMuc.getMaDanhMuc();

        // Thực hiện truy vấn cơ sở dữ liệu Firebase để lấy sản phẩm của hãng
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        productsRef.orderByChild("maDanhMuc").equalTo(maDanhMuc).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> products = new ArrayList<>();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    products.add(product);
                }

                // Cập nhật GridView với danh sách sản phẩm của hãng
                updateGridView(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    // Thêm phương thức tìm kiếm theo hãng
    private List<Product> searchByBrand(List<Product> products, String brand) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            // Kiểm tra nếu sản phẩm thuộc hãng
            if (isProductByBrand(product, brand)) {
                result.add(product);
            }
        }
        return result;
    }

    // Thêm phương thức để kiểm tra sản phẩm có thuộc hãng không
    private boolean isProductByBrand(Product product, String brand) {
        return product.getHang() != null && product.getHang().equalsIgnoreCase(brand);
    }

    // Thêm phương thức để lọc danh sách sản phẩm dựa trên nội dung tìm kiếm
    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : originalProductList) {
            // Kiểm tra nếu tên, giá hoặc hãng của sản phẩm chứa nội dung tìm kiếm
            if ((product.getName() != null && product.getName().toLowerCase().contains(query.toLowerCase())) ||
                    (product.getPrice() != null && product.getPrice().toLowerCase().contains(query.toLowerCase())) ||
                    (product.getHang() != null && product.getHang().toLowerCase().contains(query.toLowerCase()))) {
                filteredList.add(product);
            }
        }
        // Cập nhật GridView với danh sách sản phẩm đã lọc
        updateGridView(filteredList);
    }

    // TimerTask để tự động chuyển đổi ảnh trong ViewPager
    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < bannerImages.length - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }
}
