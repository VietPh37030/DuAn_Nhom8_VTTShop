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
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        super(context, 0, productList);
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
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
        TextView productHangTextView = listItemView.findViewById(R.id.tvHang);// them truong du lieu moi

        if (currentProduct != null) {
            // Sử dụng thư viện Glide để hiển thị ảnh từ URL hoặc resource
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_cart) // Ảnh mặc định khi tải
                    .error(R.drawable.item_phone); // Ảnh mặc định khi có lỗi

            Glide.with(context)
                    .load(currentProduct.getImage())
                    .apply(options)
                    .into(productImageView);

            productNameTextView.setText(currentProduct.getName());
            productPriceTextView.setText(currentProduct.getPrice());
        }


        return listItemView;
    }
}

