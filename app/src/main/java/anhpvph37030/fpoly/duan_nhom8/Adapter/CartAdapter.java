package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.DAO.CartDAO;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.fragment.MyCartFrg;
import anhpvph37030.fpoly.duan_nhom8.model.Cart;

public class CartAdapter extends ArrayAdapter<Cart> {
    private Context context;
    private List<Cart> cartItemList;
    private CartDAO cartDAO;
    private MyCartFrg myCartFragment;

    public CartAdapter(Context context, List<Cart> cartItemList, CartDAO cartDAO, MyCartFrg myCartFragment) {
        super(context, 0, cartItemList);
        this.context = context;
        this.cartItemList = cartItemList;
        this.cartDAO = cartDAO;
        this.myCartFragment = myCartFragment;
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
            holder.imgGiam = listItemView.findViewById(R.id.img_giam);
            holder.imgTang = listItemView.findViewById(R.id.img_tang);
            holder.productNameTextView = listItemView.findViewById(R.id.txt_phone);
            holder.productPriceTextView = listItemView.findViewById(R.id.txt_gia);
            holder.quantityTextView = listItemView.findViewById(R.id.txt_soluong2);
            holder.btnCancle = listItemView.findViewById(R.id.btn_cancle);
            holder.cardView = listItemView.findViewById(R.id.cardView);

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

            holder.imgGiam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reduceQuantity(position);
                }
            });

            holder.imgTang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseQuantity(position);
                }
            });

            holder.btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeFromCart(position);
                }
            });

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle the selection state
                    holder.isSelected = !holder.isSelected;

                    // Update the UI based on the selection state
                    if (holder.isSelected) {
                        holder.cardView.setBackground(new ColorDrawable(Color.parseColor("#A9B388")));
                    } else {
                        holder.cardView.setBackground(new ColorDrawable(Color.parseColor("#FFFFFF")));
                    }

                    // Recalculate the total price whenever the selection state changes
                    myCartFragment.updateTotalPrice();
                }
            });
        } else {
            Log.e("CartAdapter", "cartItem or cartItem.getProduct() is null");
        }

        return listItemView;
    }

    private static class ViewHolder {
        ImageView productImageView, imgGiam, imgTang;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView quantityTextView;
        Button btnCancle;
        LinearLayout cardView;
        boolean isSelected;
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
                                cartItemSnapshot.getRef().removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Xử lý khi xóa thành công
                                                    Toast.makeText(context, "Sản phẩm đã được xóa khỏi Firebase", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Xử lý khi xóa thất bại
                                                    Toast.makeText(context, "Không thể xóa sản phẩm khỏi Firebase", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "Lỗi khi xóa sản phẩm khỏi Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Gọi phương thức tính tổng giá trị từ MyCartFrg
        myCartFragment.updateTotalPrice();
    }

    private void reduceQuantity(int position) {
        final Cart cartItem = getItem(position);

        if (cartItem != null && cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            notifyDataSetChanged();
            updateQuantityOnFirebase(cartItem);
        }

        // Gọi phương thức tính tổng giá trị từ MyCartFrg
        myCartFragment.updateTotalPrice();
    }

    private void increaseQuantity(int position) {
        final Cart cartItem = getItem(position);

        if (cartItem != null && cartItem.getQuantity() < 10) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            notifyDataSetChanged();
            updateQuantityOnFirebase(cartItem);
        }

        // Gọi phương thức tính tổng giá trị từ MyCartFrg
        myCartFragment.updateTotalPrice();
    }

    private void updateQuantityOnFirebase(final Cart cartItem) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userCartRef = cartDAO.getCartRef().child(userId);

            userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot cartItemSnapshot : userSnapshot.getChildren()) {
                            Cart existingCartItem = cartItemSnapshot.getValue(Cart.class);
                            if (existingCartItem != null && existingCartItem.getProduct().getId().equals(cartItem.getProduct().getId())) {
                                cartItemSnapshot.getRef().child("quantity").setValue(cartItem.getQuantity())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Xử lý khi cập nhật số lượng thành công
                                                    Toast.makeText(context, "Số lượng đã được cập nhật trên Firebase", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Xử lý khi cập nhật số lượng thất bại
                                                    Toast.makeText(context, "Không thể cập nhật số lượng trên Firebase", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi cơ sở dữ liệu
                    Toast.makeText(context, "Lỗi cơ sở dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Gọi phương thức tính tổng giá trị từ MyCartFrg
        myCartFragment.updateTotalPrice();
    }
}
