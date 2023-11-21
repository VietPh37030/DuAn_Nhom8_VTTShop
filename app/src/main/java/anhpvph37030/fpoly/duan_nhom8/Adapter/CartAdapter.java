// CartAdapter.java
package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.DAO.CartDAO;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Cart;

public class CartAdapter extends ArrayAdapter<Cart> {
    private Context context;
    private List<Cart> cartItemList;
    private CartDAO cartDAO;

    public CartAdapter(Context context, List<Cart> cartItemList, CartDAO cartDAO) {
        super(context, 0, cartItemList);
        this.context = context;
        this.cartItemList = cartItemList;
        this.cartDAO = cartDAO;
    }

    @Override
    public int getCount() {
        return cartItemList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        ViewHolder holder;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.item_giohang, parent, false);

            holder = new ViewHolder();
            holder.productImageView = listItemView.findViewById(R.id.img_phone);
            holder.productNameTextView = listItemView.findViewById(R.id.txt_phone);
            holder.productPriceTextView = listItemView.findViewById(R.id.txt_gia);
            holder.quantityTextView = listItemView.findViewById(R.id.txt_soluong2);
            holder.btnCancle = listItemView.findViewById(R.id.btn_cancle);

            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        final Cart cartItem = getItem(position);

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
            holder.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));

            holder.btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeFromCart(position);
                }
            });
        } else {
            Log.e("CartAdapter", "cartItem or cartItem.getProduct() is null");
        }

        return listItemView;
    }

    private static class ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView quantityTextView;
        Button btnCancle;
    }

    private void removeFromCart(final int position) {
        final Cart removedItem = cartItemList.get(position);

        cartDAO.removeFromCart(removedItem.getProduct());

        cartItemList.remove(position);
        notifyDataSetChanged();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userCartRef = cartDAO.getCartRef().child(userId);

            userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot cartItemSnapshot : userSnapshot.getChildren()) {
                            Cart cartItem = cartItemSnapshot.getValue(Cart.class);
                            if (cartItem != null && cartItem.getProduct().getId().equals(removedItem.getProduct().getId())) {
                                cartItemSnapshot.getRef().removeValue();
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "Lỗi khi xóa sản phẩm trên Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
