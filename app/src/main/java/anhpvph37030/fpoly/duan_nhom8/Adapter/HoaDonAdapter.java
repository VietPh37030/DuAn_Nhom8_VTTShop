package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Activities.ChiTietHoaDon;
import anhpvph37030.fpoly.duan_nhom8.Activities.HoaDonActivity;
import anhpvph37030.fpoly.duan_nhom8.model.HoaDon;
import anhpvph37030.fpoly.duan_nhom8.R;

public class HoaDonAdapter extends ArrayAdapter<HoaDon> {

    private List<HoaDon> hoaDonList;
    private Context context;


    public HoaDonAdapter(@NonNull Context context, int resource, @NonNull List<HoaDon> objects) {
        super(context, resource, objects);
        this.context = context;
        this.hoaDonList = objects;
    }
    // Hàm chuyển đổi giá trị trạng thái số sang chuỗi



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hoadon, parent, false);
        }

        // Ánh xạ các thành phần UI từ item_hoadon.xml
        TextView txtMaHoaDon = convertView.findViewById(R.id.txtmahoadon);
        TextView txtSdt = convertView.findViewById(R.id.txtSdt);
        TextView txtSoLuong = convertView.findViewById(R.id.txtSoLuongDH);
        TextView txtDiachinhanhang = convertView.findViewById(R.id.txtDiaChi);
        TextView txtTongTien = convertView.findViewById(R.id.txttongtien);
        TextView txtNgayDat = convertView.findViewById(R.id.txtthoigian);
        TextView txtTrangThai = convertView.findViewById(R.id.txtTrangThai);
        Button buttonsemchitiet = convertView.findViewById(R.id.buttonsemchitiet);
        // ... Ánh xạ thêm các thành phần khác

        // Lấy đối tượng HoaDon tại vị trí hiện tại
        HoaDon hoaDon = hoaDonList.get(position);

        // Gán dữ liệu từ đối tượng HoaDon vào các thành phần UI
        txtMaHoaDon.setText(hoaDon.getMaHoaDon());
        txtSdt.setText(hoaDon.getSdt());
        txtSoLuong.setText(String.valueOf(hoaDon.getSoLuong()));
        txtDiachinhanhang.setText(hoaDon.getDiaChi());
        txtTongTien.setText(String.valueOf(hoaDon.getTongTien()));
        txtNgayDat.setText(hoaDon.getNgayDat());
        // Gọi hàm chuyển đổi để lấy chuỗi trạng thái tương ứng
        txtTrangThai.setText(String.valueOf(hoaDon.getTrangThai()));
        if (hoaDon.getTrangThai() == 0) {
            txtTrangThai.setText("Trạng thái: Chờ xác nhân");
        } else if (hoaDon.getTrangThai() == 1) {
            txtTrangThai.setText("Trạng thái: Đã xác nhân");
        } else if (hoaDon.getTrangThai() == 2) {
            txtTrangThai.setText("Trạng thái: Đang giao");
//            txtTrangThai.setVisibility(View.GONE);
        } else if (hoaDon.getTrangThai() == 3) {
            txtTrangThai.setText("Trạng thái: Giao hàng thành công");
//            txtTrangThai.setVisibility(View.GONE);
        } else if (hoaDon.getTrangThai() == 4) {
            txtTrangThai.setText("Trạng thái: Đã hủy");
//            holder.btnhuy.setVisibility(View.GONE);
        }
        // ... Gán dữ liệu cho các thành phần khác
        buttonsemchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm dữ liệu vào Intent
                Intent intent = new Intent(context, ChiTietHoaDon.class);
                intent.putExtra("ORDER_ID", hoaDon.getMaHoaDon());
                intent.putExtra("ORDER_QUANTITY", String.valueOf(hoaDon.getSoLuong()));
                intent.putExtra("ORDER_SUM", String.valueOf(hoaDon.getTongTien()));
                intent.putExtra("ORDER_NAME", hoaDon.getTenSanPham());
                intent.putExtra("ORDER_IMAGE", hoaDon.getImageUrl());
                // Log để kiểm tra dữ liệu
                Log.d("HoaDonAdapter", "ORDER_ID: " + hoaDon.getMaHoaDon());
                Log.d("HoaDonAdapter", "ORDER_QUANTITY: " + hoaDon.getSoLuong());
                Log.d("HoaDonAdapter", "ORDER_SUM: " + hoaDon.getTongTien());
                Log.d("HoaDonAdapter", "ORDER_NAME: " + hoaDon.getTenSanPham());
                Log.d("HoaDonAdapter", "ORDER_IMAGE: " + hoaDon.getImageUrl());

                context.startActivity(intent);

            }
        });
        return convertView;
    }
}
