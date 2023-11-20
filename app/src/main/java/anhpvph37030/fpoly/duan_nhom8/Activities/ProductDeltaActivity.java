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

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.DAO.CartDAO;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.fragment.MyCartFrg;
import anhpvph37030.fpoly.duan_nhom8.model.Cart;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class ProductDeltaActivity extends AppCompatActivity {
    private CartDAO cartDAO; // Khai báo đối tượng CartDAO
    private Cart cart; // Khai báo đối tượng Cart

    private String productId;
    private String productName;
    private String productPrice;
    private String productImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_delta);

        Intent intent = getIntent();
        productId = intent.getStringExtra("PRODUCT_ID");
        productName = intent.getStringExtra("PRODUCT_NAME");
        productPrice = intent.getStringExtra("PRODUCT_PRICE");
        productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL");

        TextView productNameTextView = findViewById(R.id.txtnamedeita);
        TextView productPriceTextView = findViewById(R.id.txtgiadeita);
        ImageView productImageView = findViewById(R.id.imgdeilta);
        Button btnThemGioHang = findViewById(R.id.btnthemvaogio);

        // Khởi tạo đối tượng CartDAO
        cartDAO = new CartDAO();
        cart = new Cart();

        productNameTextView.setText(productName);
        productPriceTextView.setText(productPrice);

        Picasso.get().load(productImageUrl).into(productImageView);
        Log.d("ImageLoad", "Image URL: " + productImageUrl);
        Picasso.get().load(productImageUrl).into(productImageView);

        btnThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thêm sản phẩm vào giỏ hàng và hiển thị thông báo
                addToCart();
            }
        });
    }

    private void addToCart() {
        // Tạo đối tượng Product từ thông tin chi tiết của sản phẩm
        Product product = new Product(productId, productImageUrl, productName, productPrice);
        // Thêm sản phẩm vào giỏ hàng
        cartDAO.addToCart(product, 1); // 1 là số lượng mặc định

        // Lấy danh sách sản phẩm trong giỏ hàng sau khi thêm mới
        List<Cart> updatedCartItems = cartDAO.getCartItems();

        // Hiển thị dialog thông báo thêm vào giỏ hàng thành công
        showAddToCartSuccessDialog(updatedCartItems);
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