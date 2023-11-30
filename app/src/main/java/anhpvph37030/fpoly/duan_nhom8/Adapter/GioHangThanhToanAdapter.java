package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Cart;

public class GioHangThanhToanAdapter extends ArrayAdapter<Cart> {
    private Context context;
    private List<Cart> cartItems;
    public GioHangThanhToanAdapter(Context context, List<Cart> cartItems) {
        super(context, 0, cartItems);
        this.context = context;
        this.cartItems = cartItems;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        ViewHolder holder;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.item_navthanhtoan, parent, false);

            holder = new ViewHolder();
            holder.productImageView = listItemView.findViewById(R.id.imgnavsp);
            holder.productNameTextView = listItemView.findViewById(R.id.txtTenSanphamgh);
            holder.productPriceTextView = listItemView.findViewById(R.id.txtnavGia);
            holder.quantityTextView = listItemView.findViewById(R.id.navsoluong);
            holder.quantityChoiceTextView = listItemView.findViewById(R.id.navChonsoluong);

            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        Cart cartItem = getItem(position);

        if (cartItem != null && cartItem.getProduct() != null) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_cart)
                    .error(R.drawable.item_phone);

            Glide.with(context)
                    .load(cartItem.getProduct().getImage())
                    .apply(options)
                    .into(holder.productImageView);

            holder.productNameTextView.setText(cartItem.getProduct().getName());
            holder.productPriceTextView.setText(cartItem.getProduct().getPrice());
            holder.quantityTextView.setText(String.valueOf(cartItem.getProduct().getQuantity1()));
            holder.quantityChoiceTextView.setText(String.valueOf(cartItem.getQuantity()));
        }

        return listItemView;
    }

    private static class ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView quantityTextView;
        TextView quantityChoiceTextView;

    }
}
