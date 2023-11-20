package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Cart;

public class CartAdapter extends ArrayAdapter<Cart> {
    private Context context;
    private List<Cart> cartItemList;

    public CartAdapter(Context context, List<Cart> cartItemList) {
        super(context, 0, cartItemList);
        this.context = context;
        this.cartItemList = cartItemList;
    }
    @Override
    public int getCount() {
        return cartItemList.size();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.item_giohang, parent, false);
        }

        Cart cartItem = getItem(position);

        ImageView productImageView = listItemView.findViewById(R.id.img_phone);
        TextView productNameTextView = listItemView.findViewById(R.id.txt_phone);
        TextView productPriceTextView = listItemView.findViewById(R.id.txt_gia);
        TextView quantityTextView = listItemView.findViewById(R.id.txt_soluong2);
        if (cartItem != null && cartItem.getProduct() != null) {
            // Hiển thị thông tin sản phẩm trong giỏ hàng
            Log.d("CartAdapter", "Product Name: " + cartItem.getProduct().getName());
            Log.d("CartAdapter", "Product Price: " + cartItem.getProduct().getPrice());

            Glide.with(context)
                    .load(cartItem.getProduct().getImage())
                    .into(productImageView);

            productNameTextView.setText(cartItem.getProduct().getName());
            productPriceTextView.setText(cartItem.getProduct().getPrice());
            quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        } else {
            Log.e("CartAdapter", "cartItem or cartItem.getProduct() is null");
        }


        return listItemView;
    }

}
