package anhpvph37030.fpoly.duan_nhom8.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.DAO.CartDAO;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Cart;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class ProductDeltaActivity extends AppCompatActivity {
    private CartDAO cartDAO; // Khai báo đối tượng CartDAO

    private String productId;
    private String productName;
    private String productPrice;
    private String productImageUrl;
    private  String productDescription;
    private  String productQuantity;
    private Cart cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_delta);

        Intent intent = getIntent();
        productId = intent.getStringExtra("PRODUCT_ID");
        Log.d("ProductDeltaActivity", "Received Product ID: " + productId);
        productName = intent.getStringExtra("PRODUCT_NAME");
        productPrice = intent.getStringExtra("PRODUCT_PRICE");
        productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL");
        productDescription = intent.getStringExtra("PRODUCT_description");
        productQuantity = intent.getStringExtra("PRODUCT_QUANTITY");
        Log.d("ProductDeltaActivity", "Received Product Quantity: " + productQuantity);
        TextView productNameTextView = findViewById(R.id.txtnamedeita);
        TextView productPriceTextView = findViewById(R.id.txtgiadeita);
        ImageView productImageView = findViewById(R.id.imgdeilta);
        TextView productQantityTextView = findViewById(R.id.txtsoluongsanpham);
        TextView productDescription1 = findViewById(R.id.txtmota);
        Button btnThemGioHang = findViewById(R.id.btnthemvaogio);
        Log.d("ProductDeltaActivity", "Received Quantity: " + productQuantity);
        cartDAO = CartDAO.getInstance();
        cart = new Cart();
        productNameTextView.setText(productName);
        productPriceTextView.setText(productPrice);
        productQantityTextView.setText(productQuantity);
        productDescription1.setText(productDescription);

        Picasso.get().load(productImageUrl).into(productImageView);
        Log.d("ImageLoad", "Image URL: " + productImageUrl);
        Picasso.get().load(productImageUrl).into(productImageView);

        btnThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
                if (!cartDAO.isProductAlreadyInCart(productId)) {
                    // Nếu chưa có, thêm sản phẩm vào giỏ hàng và hiển thị thông báo
                    addToCart();
                } else {
                    // Nếu đã có, hiển thị thông báo
                    showProductAlreadyInCartDialog();
                }
            }
        });
    }

    private void addToCart() {
        // Tạo đối tượng Product từ thông tin chi tiết của sản phẩm
        Product product = new Product(productId, productImageUrl, productName, productPrice, Integer.parseInt(productQuantity), 0, "");
        // Thêm sản phẩm vào giỏ hàng
        cartDAO.addToCart(product, 1); // 1 là số lượng mặc định

        // Lấy danh sách sản phẩm trong giỏ hàng sau khi thêm mới
        List<Cart> updatedCartItems = cartDAO.getCartItems();

        // Hiển thị dialog thông báo thêm vào giỏ hàng thành công
        showAddToCartSuccessDialog(updatedCartItems);
    }

    private void showProductAlreadyInCartDialog() {
        // Hiển thị dialog thông báo sản phẩm đã có trong giỏ hàng
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sản phẩm đã có trong giỏ hàng.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Đóng dialog
                    dialog.dismiss();
                })
                .show();
    }

    private void showAddToCartSuccessDialog(List<Cart> updatedCartItems) {
        // Hiển thị dialog thông báo thêm vào giỏ hàng thành công
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sản phẩm đã được thêm vào giỏ hàng.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Đóng dialog
                    dialog.dismiss();
                })
                .show();
    }

}
