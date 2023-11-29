package anhpvph37030.fpoly.duan_nhom8.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import anhpvph37030.fpoly.duan_nhom8.Adapter.GioHangThanhToanAdapter;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.Cart;

public class GioHangThanhToanActi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang_thanh_toan);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("cartItems") && intent.hasExtra("totalAmount")) {
            List<Cart> cartItems = intent.getParcelableArrayListExtra("cartItems");
            int totalAmount = intent.getIntExtra("totalAmount", 0);

            // Hiển thị tổng tiền trong GioHangThanhToanActi
            TextView txtTotalAmount = findViewById(R.id.txtTongtiengh);
            txtTotalAmount.setText(totalAmount + " VND");

            // Hiển thị danh sách sản phẩm trong ListView
            ListView listView = findViewById(R.id.lstthanhtoagio);
            GioHangThanhToanAdapter adapter = new GioHangThanhToanAdapter(this, cartItems);
            listView.setAdapter(adapter);
        }
    }
}
