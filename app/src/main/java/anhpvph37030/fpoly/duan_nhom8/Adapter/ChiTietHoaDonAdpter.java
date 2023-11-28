package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;

public class ChiTietHoaDonAdpter  extends ArrayAdapter<String> {
    private List<String> hoaDonChiTietList;
    private Context context;
    public ChiTietHoaDonAdpter(@NonNull Context context, int resource, List<String> hoaDonChiTietList) {
        super(context, resource);
        this.context = context;
        this.hoaDonChiTietList = hoaDonChiTietList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_chitiethoadon, parent, false);

            holder = new ViewHolder();
            holder.txtMaHoaDon = convertView.findViewById(R.id.txtmahoadonct);
            holder.txtSoLuong = convertView.findViewById(R.id.txtSoLuonghd1);
            holder.txtTongTien = convertView.findViewById(R.id.txttongtienhd);
            holder.txtTenSanPham = convertView.findViewById(R.id.txttensphd);
            holder.imageView = convertView.findViewById(R.id.imageView3);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy dữ liệu từ List và gán vào các thành phần UI trong item_chitiethoadon.xml
        holder.txtMaHoaDon.setText(hoaDonChiTietList.get(0));
        holder.txtSoLuong.setText( hoaDonChiTietList.get(1));
        holder.txtTongTien.setText( hoaDonChiTietList.get(2));
        holder.txtTenSanPham.setText( hoaDonChiTietList.get(3));

        // Load hình ảnh sử dụng Picasso
        Picasso.get().load(hoaDonChiTietList.get(4)).into(holder.imageView);

        return convertView;
    }

    static class ViewHolder {
        TextView txtMaHoaDon;
        TextView txtSoLuong;
        TextView txtTongTien;
        TextView txtTenSanPham;
        ImageView imageView;
    }
}
