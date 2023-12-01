package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Activities.ChiTietHoaDon;
import anhpvph37030.fpoly.duan_nhom8.model.HoaDon;
import anhpvph37030.fpoly.duan_nhom8.R;

public class HoaDonAdapter extends ArrayAdapter<HoaDon> {

    private List<HoaDon> hoaDonList;
    private Context context;
    private DatabaseReference hoaDonRef;

    public HoaDonAdapter(@NonNull Context context, int resource, @NonNull List<HoaDon> objects) {
        super(context, resource, objects);
        this.context = context;
        this.hoaDonList = objects;
        hoaDonRef = FirebaseDatabase.getInstance().getReference().child("HoaDonThanhToan");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hoadon, parent, false);
        }

        TextView txtMaHoaDon = convertView.findViewById(R.id.txtmahoadon);
        TextView txtSdt = convertView.findViewById(R.id.txtSdt);
        TextView txtSoLuong = convertView.findViewById(R.id.txtSoLuongDH);
        TextView txtDiachinhanhang = convertView.findViewById(R.id.txtDiaChi);
        TextView txtTongTien = convertView.findViewById(R.id.txttongtien);
        TextView txtNgayDat = convertView.findViewById(R.id.txtthoigian);
        TextView txtTrangThai = convertView.findViewById(R.id.txtTrangThai);
        Button buttonsemchitiet = convertView.findViewById(R.id.buttonsemchitiet);
        ImageView btnHuyDonHang = convertView.findViewById(R.id.btnhuydonhang);

        HoaDon hoaDon = hoaDonList.get(position);

        txtMaHoaDon.setText(hoaDon.getMaHoaDon());
        txtSdt.setText(hoaDon.getSdt());
        txtSoLuong.setText(String.valueOf(hoaDon.getSoLuong()));
        txtDiachinhanhang.setText(hoaDon.getDiaChi());
        txtTongTien.setText(String.valueOf(hoaDon.getTongTien()));
        txtNgayDat.setText(hoaDon.getNgayDat());

        txtTrangThai.setText(String.valueOf(hoaDon.getTrangThai()));
        if (hoaDon.getTrangThai() == 0) {
            txtTrangThai.setText("Trạng thái: Chờ xác nhận");
        } else if (hoaDon.getTrangThai() == 1) {
            txtTrangThai.setText("Trạng thái: Đã xác nhận");
        } else if (hoaDon.getTrangThai() == 2) {
            txtTrangThai.setText("Trạng thái: Đang giao");
        } else if (hoaDon.getTrangThai() == 3) {
            txtTrangThai.setText("Trạng thái: Giao hàng thành công");
        } else if (hoaDon.getTrangThai() == 4) {
            txtTrangThai.setText("Trạng thái: Đã hủy");
        }

        buttonsemchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietHoaDon.class);
                intent.putExtra("ORDER_ID", hoaDon.getMaHoaDon());
                intent.putExtra("ORDER_QUANTITY", String.valueOf(hoaDon.getSoLuong()));
                intent.putExtra("ORDER_SUM", String.valueOf(hoaDon.getTongTien()));
                intent.putExtra("ORDER_NAME", hoaDon.getTenSanPham());
                intent.putExtra("ORDER_IMAGE", hoaDon.getImageUrl());
                Log.d("HoaDonAdapter", "ORDER_ID: " + hoaDon.getMaHoaDon());
                Log.d("HoaDonAdapter", "ORDER_QUANTITY: " + hoaDon.getSoLuong());
                Log.d("HoaDonAdapter", "ORDER_SUM: " + hoaDon.getTongTien());
                Log.d("HoaDonAdapter", "ORDER_NAME: " + hoaDon.getTenSanPham());
                Log.d("HoaDonAdapter", "ORDER_IMAGE: " + hoaDon.getImageUrl());

                context.startActivity(intent);
            }
        });

        btnHuyDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyenDonHangSangDanhSachDaHuy(hoaDon);
            }
        });

        return convertView;
    }

    private void chuyenDonHangSangDanhSachDaHuy(HoaDon hoaDon) {
        DatabaseReference donHangDaHuyRef = FirebaseDatabase.getInstance().getReference().child("DonHangKhachHuy");
        donHangDaHuyRef.child(hoaDon.getMaHoaDon()).setValue(hoaDon);

        hoaDonRef.child(hoaDon.getMaHoaDon()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hoaDonList.remove(hoaDon);
                notifyDataSetChanged();
                hienThiThongBao("Thông báo đơn hàng", "Cảm ơn quý khách đã sử dụng dịch vụ. Đơn hàng của quý khách đã được hủy.");
            } else {
                Log.e("HoaDonAdapter", "Lỗi xóa dữ liệu từ Firebase: " + task.getException().getMessage());
                hienThiThongBao("Lỗi", "Có lỗi xảy ra khi xóa đơn hàng. Vui lòng thử lại.");
            }
        });
    }

    private void hienThiThongBao(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            String channelName = "Channel Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder
                .setSmallIcon(R.drawable.logodong)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(soundUri);

        notificationManager.notify(0, builder.build());
    }
}
