package anhpvph37030.fpoly.duan_nhom8.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.ChiTietHoaDonAdpter;
import anhpvph37030.fpoly.duan_nhom8.MainActivity;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.ChiTietHoaDon;

public class ChiTietHoaDonActivity extends AppCompatActivity {

    private ListView lstChiTietHoaDon;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don);
        toolbar = findViewById(R.id.toolbarcthd);
        toolbar.setNavigationIcon(R.drawable.ic_previous);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết hoá đơn");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitcthd();
            }
        });

        lstChiTietHoaDon = findViewById(R.id.lstchitiethd1);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("ORDER_ID");
        String orderQuantity = intent.getStringExtra("ORDER_QUANTITY");
        String orderSum = intent.getStringExtra("ORDER_SUM");
        String orderName = intent.getStringExtra("ORDER_NAME");
        String orderImage = intent.getStringExtra("ORDER_IMAGE");

        // Create a ChiTietHoaDonItem object
        ChiTietHoaDon chiTietHoaDonItem = new ChiTietHoaDon(orderId, orderQuantity, orderSum, orderName, orderImage);

        // Create a list and add the ChiTietHoaDonItem to the list
        List<ChiTietHoaDon> hoaDonChiTietList = new ArrayList<>();
        hoaDonChiTietList.add(chiTietHoaDonItem);

        ChiTietHoaDonAdpter chiTietHoaDonAdapter = new ChiTietHoaDonAdpter(this, R.layout.item_chitiethoadon, hoaDonChiTietList);
        lstChiTietHoaDon.setAdapter(chiTietHoaDonAdapter);
    }

    private void exitcthd() {
        finish();
    }
}
