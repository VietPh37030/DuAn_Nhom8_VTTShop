package anhpvph37030.fpoly.duan_nhom8.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import anhpvph37030.fpoly.duan_nhom8.R;

public class ProductDeltaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_delta);
        Intent intent = getIntent();
        String productId = intent.getStringExtra("PRODUCT_ID");
        String productName = intent.getStringExtra("PRODUCT_NAME");
        String productPrice = intent.getStringExtra("PRODUCT_PRICE");
        String productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL");
        TextView productNameTextView = findViewById(R.id.txtnamedeita);
        TextView productPriceTextView = findViewById(R.id.txtgiadeita);
        ImageView productImageView = findViewById(R.id.imgdeilta);
        productNameTextView.setText(productName);
        productPriceTextView.setText(productPrice);


        Picasso.get().load(productImageUrl).into(productImageView);
        Log.d("ImageLoad", "Image URL: " + productImageUrl);
        Picasso.get().load(productImageUrl).into(productImageView);

    }
}