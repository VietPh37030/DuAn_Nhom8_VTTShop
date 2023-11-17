package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_dienthoai, parent, false);
        }

        Product currentProduct = getItem(position);

        ImageView productImageView = listItemView.findViewById(R.id.ivAnhSP);
        TextView productNameTextView = listItemView.findViewById(R.id.tvTenDT);
        TextView productPriceTextView = listItemView.findViewById(R.id.tvGia);

        if (currentProduct != null) {
            // Sử dụng thư viện Glide để hiển thị ảnh từ URL hoặc resource
            Glide.with(getContext())
                    .load(currentProduct.getProductImage())
                    .into(productImageView);


            productNameTextView.setText(currentProduct.getProductName());
            productPriceTextView.setText(currentProduct.getProductPrice());
        }

        return listItemView;
    }
}
