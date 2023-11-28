package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.CartAdapter;
import anhpvph37030.fpoly.duan_nhom8.DAO.CartDAO;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Cart;

public class MyCartFrg extends Fragment {

    private DatabaseReference cartRef;
    private List<Cart> cartItems;
    private CartAdapter cartAdapter;
    private ListView cartListView;
    private CartDAO cartDAO;
    private TextView txtTotalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        cartItems = new ArrayList<>();
        cartDAO = new CartDAO();
        txtTotalAmount = view.findViewById(R.id.txtTotalAmount);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            cartRef = FirebaseDatabase.getInstance().getReference().child("carts").child(userId);
            cartListView = view.findViewById(R.id.lstmycart);
            cartAdapter = new CartAdapter(getContext(), cartItems, cartDAO, this);
            cartListView.setAdapter(cartAdapter);

            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cartItems.clear();

                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot cartItemSnapshot : userSnapshot.getChildren()) {
                            Cart cartItem = cartItemSnapshot.getValue(Cart.class);
                            if (cartItem != null) {
                                cartItems.add(cartItem);
                            }
                        }
                    }

                    cartAdapter.notifyDataSetChanged();
                    calculateAndDisplayTotalPrice();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Lỗi khi truy cập giỏ hàng trên Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    private void calculateAndDisplayTotalPrice() {
        int totalPrice = 0;

        for (Cart cartItem : cartItems) {
            if (cartItem.getProduct() != null) {
                String priceString = cartItem.getProduct().getPrice().replaceAll("[^0-9]", "");
                try {
                    int productPrice = Integer.parseInt(priceString);
                    totalPrice += productPrice * cartItem.getQuantity();
                } catch (NumberFormatException e) {
                    Log.e("MyCartFrg", "Error parsing price: " + cartItem.getProduct().getPrice());
                }
            }
        }

        txtTotalAmount.setText("Tổng tiền: " + totalPrice + " VND");
    }

    public void updateTotalPrice() {
        calculateAndDisplayTotalPrice();
    }
}
