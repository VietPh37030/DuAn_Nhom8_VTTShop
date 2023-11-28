package anhpvph37030.fpoly.duan_nhom8.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import anhpvph37030.fpoly.duan_nhom8.R;

public class GioHangThanhToanActi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang_thanh_toan);
        // Nhận tổng tiền từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("totalAmount")) {
            int totalAmount = intent.getIntExtra("totalAmount", 0);

            // Hiển thị tổng tiền trong GioHangThanhToanActi
            TextView txtTotalAmount = findViewById(R.id.txtTongtiengh);
            txtTotalAmount.setText(totalAmount + " VND");
        }
    }
}