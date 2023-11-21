package anhpvph37030.fpoly.duan_nhom8.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.model.Cart;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class CartDAO {
    private List<Cart> cartItems;
    private DatabaseReference cartRef;

    public CartDAO() {
        this.cartItems = new ArrayList<>();
        this.cartRef = FirebaseDatabase.getInstance().getReference().child("carts");
    }

    public List<Cart> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Cart> cartItems) {
        this.cartItems = cartItems;
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addToCart(Product product, int quantity) {
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        for (Cart item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                // Nếu có rồi, tăng số lượng
                item.setQuantity(item.getQuantity() + quantity);
                updateCartOnFirebase(cartItems); // Cập nhật toàn bộ giỏ hàng
                return;
            }
        }

        // Nếu chưa có, thêm sản phẩm mới vào giỏ hàng
        Cart newItem = new Cart(product, quantity);
        cartItems.add(newItem);
        updateCartOnFirebase(cartItems); // Cập nhật toàn bộ giỏ hàng
    }



    public void removeFromCart(Product product) {
        if (cartItems == null) {
            return;
        }

        // Duyệt qua danh sách sản phẩm trong giỏ hàng
        Iterator<Cart> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            Cart item = iterator.next();
            if (item.getProduct().getId().equals(product.getId())) {
                // Nếu tìm thấy sản phẩm, xóa nó khỏi giỏ hàng
                iterator.remove();
                updateCartOnFirebase(cartItems); // Cập nhật giỏ hàng trên Firebase
                return;
            }
        }
    }

    public void clearCart() {
        if (cartItems != null) {
            cartItems.clear();
            updateCartOnFirebase(new ArrayList<>()); // Truyền danh sách trống để xóa giỏ hàng trên Firebase
        }
    }
    // Cập nhật giỏ hàng lên Firebase
    public void updateCartOnFirebase(List<Cart> cartItems) {
        // Lưu giỏ hàng lên Firebase dựa trên userId
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userCartRef = cartRef.child(userId);

            // Sử dụng push() để tạo một nút mới với khóa ngẫu nhiên
            DatabaseReference newCartItemRef = userCartRef.push();

            // Sét giá trị cho nút mới
            newCartItemRef.setValue(cartItems);
        }
    }



}
