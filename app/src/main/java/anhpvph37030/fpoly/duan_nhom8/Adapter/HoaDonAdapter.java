package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

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
        txtTrangThai.setText(hoaDon.getTrangThai());
        // ... Gán dữ liệu cho các thành phần khác
        buttonsemchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return convertView;
    }
}
