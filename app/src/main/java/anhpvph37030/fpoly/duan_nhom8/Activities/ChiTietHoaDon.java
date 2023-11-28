package anhpvph37030.fpoly.duan_nhom8.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.ChiTietHoaDonAdpter;
import anhpvph37030.fpoly.duan_nhom8.R;

public class ChiTietHoaDon extends AppCompatActivity {
    private ListView lstChiTietHoaDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don);
        // Khởi tạo ListView
        // Khởi tạo ListView
        lstChiTietHoaDon = findViewById(R.id.lstchitiethd1);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String orderId = intent.getStringExtra("ORDER_ID");
        String orderQuantity = intent.getStringExtra("ORDER_QUANTITY");
        String orderSum = intent.getStringExtra("ORDER_SUM");
        String orderName = intent.getStringExtra("ORDER_NAME");
        String orderImage = intent.getStringExtra("ORDER_IMAGE");

        // Tạo danh sách dữ liệu
        List<String> hoaDonChiTietList = new ArrayList<>();
        hoaDonChiTietList.add(orderId);
        hoaDonChiTietList.add(orderQuantity);
        hoaDonChiTietList.add(orderSum);
        hoaDonChiTietList.add(orderName);
        hoaDonChiTietList.add(orderImage);

        // Log để kiểm tra dữ liệu
        Log.d("ChiTietHoaDon", "ORDER_ID: " + orderId);
        Log.d("ChiTietHoaDon", "ORDER_QUANTITY: " + orderQuantity);
        Log.d("ChiTietHoaDon", "ORDER_SUM: " + orderSum);
        Log.d("ChiTietHoaDon", "ORDER_NAME: " + orderName);
        Log.d("ChiTietHoaDon", "ORDER_IMAGE: " + orderImage);
        // Tạo Adapter và liên kết với ListView
        ChiTietHoaDonAdpter chiTietHoaDonAdapter = new ChiTietHoaDonAdpter(this, R.layout.item_chitiethoadon, hoaDonChiTietList);
        lstChiTietHoaDon.setAdapter(chiTietHoaDonAdapter);
        chiTietHoaDonAdapter.notifyDataSetChanged();
    }
}