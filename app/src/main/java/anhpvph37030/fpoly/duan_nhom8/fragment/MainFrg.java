package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import anhpvph37030.fpoly.duan_nhom8.Adapter.ProductAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class MainFrg extends Fragment {

    private DatabaseReference productsRef;
    private ViewPager viewPager;
    private int[] bannerImages = {R.drawable.banner14, R.drawable.banner15, R.drawable.banner12};
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private GridView gridView;
    private List<Product> originalProductList;
    private List<Product> filteredProductList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo DatabaseReference để truy cập nút "products" trong Firebase Realtime Database
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_frg, container, false);

        // Liên kết Adapter với GridView
        gridView = v.findViewById(R.id.gvDT);

        // Liên kết ViewPager
        viewPager = v.findViewById(R.id.viewPager);
        androidx.appcompat.widget.SearchView searchView = v.findViewById(R.id.searchView);

        // Lắng nghe sự kiện khi có thay đổi trong dữ liệu
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                originalProductList = new ArrayList<>();
                filteredProductList = new ArrayList<>();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    originalProductList.add(product);
                    filteredProductList.add(product);
                }

                // Kiểm tra xem productList có null hay không trước khi sao chép
                if (productList != null) {
                    productList.clear(); // Xóa dữ liệu cũ nếu có
                    productList.addAll(originalProductList);
                } else {
                    productList = new ArrayList<>(originalProductList);
                }
            

                // Tạo Adapter cho ViewPager
                BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(getContext(), bannerImages);
                viewPager.setAdapter(bannerPagerAdapter);

                // Tạo Adapter và setAdapter cho GridView
                productAdapter = new ProductAdapter(getContext(), productList);
                gridView.setAdapter(productAdapter);

                // Tự động chuyển đổi giữa các ảnh sau một khoảng thời gian
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000); // Chuyển đổi ảnh mỗi 4 giây
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi truy cập cơ sở dữ liệu
                Toast.makeText(getContext(), "Lỗi khi truy cập cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi người dùng thay đổi nội dung tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Không cần xử lý submit ở đây
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Lọc danh sách sản phẩm dựa trên nội dung tìm kiếm
                filterProducts(newText);
                return true;
            }
        });

        // Xử lý sự kiện khi người dùng chọn một sản phẩm
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (productList != null && position < productList.size()) {
                    Product selectedProduct = productList.get(position);

                    // Kiểm tra xem selectedProduct có null hay không
                    if (selectedProduct != null) {
                        // Tạo Intent để chuyển từ MainFrg sang ProductDeltaActivity
                        Intent intent = new Intent(getActivity(), ProductDeltaActivity.class);
                        // Log để kiểm tra xem dữ liệu số lượng có đúng không
                        Log.d("MainFrg", "Selected Product Quantity: " + selectedProduct.getQuantity1());
                        // Đặt thông tin sản phẩm vào Intent
                        intent.putExtra("PRODUCT_ID", selectedProduct.getId());
                        intent.putExtra("PRODUCT_NAME", selectedProduct.getName());
                        intent.putExtra("PRODUCT_PRICE", selectedProduct.getPrice());
                        intent.putExtra("PRODUCT_IMAGE_URL", selectedProduct.getImage());
                        intent.putExtra("PRODUCT_description", selectedProduct.getDescription());
                        intent.putExtra("PRODUCT_QUANTITY", String.valueOf(selectedProduct.getQuantity1()));
                        // Chuyển sang ProductDetailActivity
                        // Log để kiểm tra xem Intent đã chứa đúng dữ liệu không
                        Log.d("MainFrg", "Sending Intent with Quantity: " + selectedProduct.getQuantity1());
                        startActivity(intent);
                    }
                }
            }
        });

        return v;
    }

    // Thêm phương thức để cập nhật GridView với danh sách sản phẩm đã lọc
    private void updateGridView(List<Product> filteredProducts) {
        productAdapter.updateData(filteredProducts);
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
