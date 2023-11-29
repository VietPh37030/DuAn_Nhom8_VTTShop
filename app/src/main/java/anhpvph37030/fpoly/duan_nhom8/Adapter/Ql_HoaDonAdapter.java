package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.HoaDon;

public class Ql_HoaDonAdapter extends ArrayAdapter<HoaDon> {
    int trangThai;
    public Ql_HoaDonAdapter(@NonNull Context context, int resource, @NonNull List<HoaDon> objects) {
        super(context, resource, objects);
    }

    // ...

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            Log.d("Ql_HoaDonAdapter", "convertView is null. Creating new view.");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ql_hoadon, parent, false);
        }

        // Ánh xạ các thành phần UI từ item_ql_hoadon.xml
        TextView txtMaHoaDon = convertView.findViewById(R.id.maHoaDon);
        TextView txtSoLuong = convertView.findViewById(R.id.hdSoLuong);
        TextView txtTongTien = convertView.findViewById(R.id.hdTongTien);
        TextView txtNgayDat = convertView.findViewById(R.id.ngayHoaDon);
        TextView txtNguoiNhan = convertView.findViewById(R.id.tvNguoiNhan);
        TextView txtDiaChi = convertView.findViewById(R.id.tvDiachiNhan);
        TextView txtSdt = convertView.findViewById(R.id.tvsdt);
        TextView txtTenSanPham = convertView.findViewById(R.id.hdtensp); // Thêm TextView mới
        ImageView txtImageUrl = convertView.findViewById(R.id.imgHoaDon); // Thêm TextView mới
        Button btnHuyDon = convertView.findViewById(R.id.btnHuyDon);
        Button btnXN = convertView.findViewById(R.id.btnXN);

        // Lấy đối tượng HoaDon tại vị trí hiện tại
        HoaDon hoaDon = getItem(position);
        Log.d("Ql_HoaDonAdapter", "Processing item at position " + position + ": " + hoaDon.toString());
        // Gán dữ liệu từ đối tượng HoaDon vào các thành phần UI
        if (hoaDon != null) {
            Log.d("Ql_HoaDonAdapter", "Processing item at position " + position + ": " + hoaDon.toString());
            txtMaHoaDon.setText("Mã Hóa Đơn: " + hoaDon.getMaHoaDon());
            txtSoLuong.setText("Số Lượng: " + hoaDon.getSoLuong());
            txtTongTien.setText("Tổng Tiền: " + hoaDon.getTongTien());
            txtNgayDat.setText("Ngày Đặt: " + hoaDon.getNgayDat());
            txtNguoiNhan.setText("Người Nhận: " + hoaDon.getNguoiNhan());
            txtDiaChi.setText("Địa Chỉ: " + hoaDon.getDiaChi());
            txtSdt.setText("Số Điện Thoại: " + hoaDon.getSdt());
            txtTenSanPham.setText("Tên Sản Phẩm: " + hoaDon.getTenSanPham()); // Thêm thông tin chi tiết
//            txtImageUrl.setText("Hình Ảnh: " + hoaDon.getImageUrl()); // Thêm thông tin chi tiết
//            txtTrangThai.setText("Trạng Thái: " + hoaDon.getTrangThai()); // Thêm thông tin chi tiết
            Picasso.get().load(hoaDon.getImageUrl()).into(txtImageUrl);

// ...

            if (hoaDon != null) {
                Log.d("Ql_HoaDonAdapter", "Processing item at position " + position + ": " + hoaDon.toString());
                // ... (các dòng code khác)

                trangThai = hoaDon.getTrangThai();

                if (trangThai == 0) {
                    btnXN.setText("Xác Nhận");
                } else if (trangThai == 1) {
                    btnXN.setText("Giao Hàng");
                } else if (trangThai == 2) {
                    btnXN.setText("Hoàn Thành");
                    btnHuyDon.setVisibility(View.GONE);
                } else if (trangThai == 3) {
                    btnXN.setVisibility(View.GONE);
                    btnHuyDon.setVisibility(View.GONE);
                } else if (trangThai == 4) {
                    btnXN.setText("Khôi Phục");
                    btnHuyDon.setVisibility(View.GONE);
                }

                // Xử lý sự kiện cho Button nếu cần
                btnHuyDon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Xử lý khi Button Hủy Đơn được nhấn
                    }
                });

                btnXN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Xử lý khi Button Xác Nhận được nhấn
                    }
                });
            } else {
                Log.e("Ql_HoaDonAdapter", "getItem returns null for position " + position);
            }
        }

            return convertView;
    }

}
