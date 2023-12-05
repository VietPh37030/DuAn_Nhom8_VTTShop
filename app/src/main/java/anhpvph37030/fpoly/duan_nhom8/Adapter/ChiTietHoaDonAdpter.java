package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.ChiTietHoaDon;

public class ChiTietHoaDonAdpter extends ArrayAdapter<ChiTietHoaDon> {
    private List<ChiTietHoaDon> hoaDonChiTietList;
    private Context context;

    public ChiTietHoaDonAdpter(@NonNull Context context, int resource, List<ChiTietHoaDon> hoaDonChiTietList) {
        super(context, resource, hoaDonChiTietList);
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

        ChiTietHoaDon chiTietHoaDonItem = getItem(position);
        if (chiTietHoaDonItem != null) {
            holder.txtMaHoaDon.setText(chiTietHoaDonItem.getOrderId());
            holder.txtSoLuong.setText(chiTietHoaDonItem.getOrderQuantity());
            // Định dạng tổng tiền thành tiền Việt Nam và hiển thị
            String formattedTotalAmount = formatCurrency(chiTietHoaDonItem.getOrderSum());
            holder.txtTongTien.setText(formattedTotalAmount);
            holder.txtTenSanPham.setText(chiTietHoaDonItem.getOrderName());

            // Load hình ảnh sử dụng Picasso
            Picasso.get().load(chiTietHoaDonItem.getOrderImage()).into(holder.imageView);
        }

        return convertView;
    }
    private String formatCurrency(String amount) {
        // Kiểm tra xem amount có chứa ký tự số hay không
        if (!TextUtils.isEmpty(amount) && TextUtils.isDigitsOnly(amount)) {
            int intAmount = Integer.parseInt(amount);

            // Sử dụng NumberFormat để định dạng số thành chuỗi tiền Việt Nam
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);

            // Đặt đơn vị tiền tệ là VND
            currencyFormatter.setCurrency(Currency.getInstance("VND"));

            // Định dạng số thành chuỗi tiền tệ
            return currencyFormatter.format(intAmount);
        } else {
            return amount; // Trả về nguyên bản nếu không thể chuyển đổi thành số
        }
    }

    static class ViewHolder {
        TextView txtMaHoaDon;
        TextView txtSoLuong;
        TextView txtTongTien;
        TextView txtTenSanPham;
        ImageView imageView;
    }
}
