package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.widget.SearchView;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_frg, container, false);

        // Khởi tạo DatabaseReference để truy cập nút "products" trong Firebase Realtime Database
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        // Liên kết Adapter với GridView
        gridView = v.findViewById(R.id.gvDT);

        // Liên kết ViewPager
        viewPager = v.findViewById(R.id.viewPager);
        androidx.appcompat.widget.SearchView searchView = v.findViewById(R.id.searchView);
        // Lắng nghe sự kiện khi có thay đổi trong dữ liệu
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }

                // Tạo Adapter và setAdapter cho GridView
                ProductAdapter adapter = new ProductAdapter(getContext(), productList);
                gridView.setAdapter(adapter);

                // Tạo Adapter cho ViewPager
                BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(getContext(), bannerImages);
                viewPager.setAdapter(bannerPagerAdapter);

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
                // Lấy sản phẩm được chọn từ danh sách
                Product selectedProduct = productList.get(position);

                // Tạo Intent để chuyển từ MainFrg sang ProductDetailActivity
                Intent intent = new Intent(getActivity(), ProductDeltaActivity.class);

                // Đặt thông tin sản phẩm vào Intent
                intent.putExtra("PRODUCT_ID", selectedProduct.getId());
                intent.putExtra("PRODUCT_NAME", selectedProduct.getName());
                intent.putExtra("PRODUCT_PRICE", selectedProduct.getPrice());
                intent.putExtra("PRODUCT_IMAGE_URL", selectedProduct.getImage());

                // Chuyển sang ProductDetailActivity
                startActivity(intent);
            }
        });

        return v;
    }

    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        // Tạo Adapter và setAdapter cho GridView với danh sách sản phẩm đã lọc
        ProductAdapter adapter = new ProductAdapter(getContext(), filteredList);
        gridView.setAdapter(adapter);
    }

    // TimerTask để tự động chuyển đổi ảnh trong ViewPager
    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
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
