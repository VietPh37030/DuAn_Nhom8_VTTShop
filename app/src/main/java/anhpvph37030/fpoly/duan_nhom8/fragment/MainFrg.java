package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.ProductAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Product;


public class MainFrg extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_frg, container, false);

        // Tạo danh sách sản phẩm
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(R.drawable.item_phone,"Product 1", "10.000.000 đ"));
        productList.add(new Product(R.drawable.item_phone,"Product 2", "10.000.000 đ"));
        productList.add(new Product(R.drawable.item_phone,"Product 3", "10.000.000 đ"));
        productList.add(new Product(R.drawable.item_phone,"Product 1", "10.000.000 đ"));
        productList.add(new Product(R.drawable.item_phone,"Product 2", "10.000.000 đ"));
        productList.add(new Product(R.drawable.item_phone,"Product 3", "10.000.000 đ"));
        // Thêm thông tin cho các sản phẩm khác

        // Tạo Adapter
        ProductAdapter adapter = new ProductAdapter(getActivity(), productList);

        // Liên kết Adapter với GridView
        GridView gridView = v.findViewById(R.id.gvDT);
        gridView.setAdapter(adapter);

        return v;

    }
}