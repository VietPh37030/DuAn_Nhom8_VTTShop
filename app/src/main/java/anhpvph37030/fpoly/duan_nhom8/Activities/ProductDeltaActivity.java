package anhpvph37030.fpoly.duan_nhom8.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private CartDAO cartDAO;
    private String productId;
    private String productName;
    private String productPrice;
    private String productImageUrl;
    private String productDescription;
    private String productQuantity;
    private Cart cart;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_delta);
        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("PRODUCT_ID");
            Log.d("ProductDeltaActivity", "Received Product ID: " + productId);
            productName = intent.getStringExtra("PRODUCT_NAME");
            productPrice = intent.getStringExtra("PRODUCT_PRICE");
            productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL");
            productDescription = intent.getStringExtra("PRODUCT_description");
            productQuantity = intent.getStringExtra("PRODUCT_QUANTITY");
            Log.d("ProductDeltaActivity", "Received Product Quantity: " + productQuantity);
        } else {
            Log.e("ProductDeltaActivity", "Intent is null");
        }
        Log.d("ProductDeltaActivity", "Received Product Quantity: " + productQuantity);
        TextView productNameTextView = findViewById(R.id.txtnamedeita);
        TextView productPriceTextView = findViewById(R.id.txtgiadeita);
        ImageView productImageView = findViewById(R.id.imgdeilta);
        TextView productQantityTextView = findViewById(R.id.txtsoluongsanpham);
        TextView productDescription1 = findViewById(R.id.txtmota);
        Button btnThemGioHang = findViewById(R.id.btnthemvaogio);
        Button btnThanhToan = findViewById(R.id.btnmuangay);
        Log.d("ProductDeltaActivity", "Received Quantity: " + productQuantity);
        cartDAO = CartDAO.getInstance();
        cart = new Cart();
        productNameTextView.setText(productName);
        productPriceTextView.setText(productPrice);
        productQantityTextView.setText(productQuantity);
        productDescription1.setText(productDescription);

        Picasso.get().load(productImageUrl).into(productImageView);
        Log.d("ImageLoad", "Image URL: " + productImageUrl);

        btnThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(productQuantity);
                if (quantity > 0) {
                    if (!cartDAO.isProductAlreadyInCart(productId)) {
                        addToCart();
                    } else {
                        showProductAlreadyInCartDialog();
                    }
                } else {
                    showInvalidQuantityDialog();
                }
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Create an Intent to start the ThanhToanActi activity
                Intent thanhToanIntent = new Intent(ProductDeltaActivity.this, ThanhToanActi.class);
                thanhToanIntent.putExtra("PRODUCT_ID",productId);
                // Put extra data to pass to the ThanhToanActi activity
                thanhToanIntent.putExtra("PRODUCT_NAME", productName);
                thanhToanIntent.putExtra("PRODUCT_PRICE", productPrice);
                thanhToanIntent.putExtra("PRODUCT_IMAGE_URL", productImageUrl);
//                int quantity = Integer.parseInt(productQuantity);
//                thanhToanIntent.putExtra("PRODUCT_QUANTITY", quantity);
                thanhToanIntent.putExtra("PRODUCT_QUANTITY", productQuantity);

                // Start the ThanhToanActi activity
                startActivity(thanhToanIntent);
            }
        });
    }

    private void addToCart() {
        Product product = new Product(productId, productImageUrl, productName, productPrice, Integer.parseInt(productQuantity), 0, "");
        cartDAO.addToCart(product, 1);

        List<Cart> updatedCartItems = cartDAO.getCartItems();
        showAddToCartSuccessDialog(updatedCartItems);
    }

    private void showProductAlreadyInCartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sản phẩm đã có trong giỏ hàng.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void showAddToCartSuccessDialog(List<Cart> updatedCartItems) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sản phẩm đã được thêm vào giỏ hàng.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void showInvalidQuantityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Số lượng sản phẩm không hợp lệ.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }


}
