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
    private boolean cardViewClicked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        // Khởi tạo giỏ hàng và Adapter
        cartItems = new ArrayList<>();
        cartDAO = new CartDAO();
        txtTotalAmount = view.findViewById(R.id.txtTotalAmount);

        // Lấy userId của người dùng hiện tại
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Khởi tạo DatabaseReference để truy cập nút giỏ hàng của người dùng trong Firebase Realtime Database
            cartRef = FirebaseDatabase.getInstance().getReference().child("carts").child(userId);
            // Liên kết Adapter với ListView
            cartListView = view.findViewById(R.id.lstmycart);
            cartAdapter = new CartAdapter(getContext(), cartItems, cartDAO, this);
            cartListView.setAdapter(cartAdapter);

            // Lắng nghe sự kiện khi có thay đổi trong dữ liệu của giỏ hàng trên Firebase
            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Xóa toàn bộ items trong giỏ hàng local
                    cartItems.clear();

                    // Duyệt qua danh sách items trong giỏ hàng trên Firebase và thêm vào giỏ hàng local
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot cartItemSnapshot : userSnapshot.getChildren()) {
                            // Sử dụng Cart.class trực tiếp để Firebase tự chuyển đổi dữ liệu
                            Cart cartItem = cartItemSnapshot.getValue(Cart.class);
                            if (cartItem != null) {
                                cartItems.add(cartItem);
                            }
                        }
                    }
                    // Cập nhật dữ liệu cho Adapter và tính tổng giá trị
                    cartAdapter.notifyDataSetChanged();
                    // Nếu có CardView được click, tính tổng giá trị
                    if (cardViewClicked) {
                        calculateAndDisplayTotalPrice();
                        cardViewClicked = false;  // Reset the flag
                    } else {
                        // Nếu không có CardView được click, reset giá trị về 0
                        txtTotalAmount.setText("Tổng tiền: 0 VND");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý khi có lỗi truy cập cơ sở dữ liệu
                    Toast.makeText(getContext(), "Lỗi khi truy cập giỏ hàng trên Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    private void calculateAndDisplayTotalPrice() {
        int totalPrice = 0;

        for (Cart cartItem : cartItems) {
            if (cartItem != null && cartItem.getProduct() != null) {
                String priceString = cartItem.getProduct().getPrice().replaceAll("[^0-9]", "");
                try {
                    int productPrice = Integer.parseInt(priceString);
                    totalPrice += productPrice * cartItem.getQuantity();
                } catch (NumberFormatException e) {
                    Log.e("MyCartFrg", "Error parsing price: " + cartItem.getProduct().getPrice());
                }
            }
        }

        // Hiển thị tổng giá trị trên giao diện người dùng
        txtTotalAmount.setText("Tổng tiền: " + totalPrice + " VND");
    }

    public void updateTotalPrice() {
        cardViewClicked = true;
    }
}
