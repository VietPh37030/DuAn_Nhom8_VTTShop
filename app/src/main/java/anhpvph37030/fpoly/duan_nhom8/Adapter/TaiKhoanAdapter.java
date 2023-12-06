package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.TaiKhoan;

public class TaiKhoanAdapter extends ArrayAdapter<TaiKhoan> {

    public TaiKhoanAdapter(Context context, List<TaiKhoan> taiKhoanList) {
        super(context, 0, taiKhoanList);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_taikhoan_qlkh, parent, false);
        }

        final TaiKhoan taiKhoan = getItem(position);

        // Ánh xạ các thành phần trong layout item_taikhoan_qlkh.xml và đặt giá trị cho chúng
        ImageView imgQltk = convertView.findViewById(R.id.imgavt_qlkh);
        TextView txttendn_qltk = convertView.findViewById(R.id.txttendn_qlkh);
        TextView txtmk_qltk = convertView.findViewById(R.id.txtmk_qlkh);
        TextView txtTrangThai = convertView.findViewById(R.id.txt_trang_thai);

        // Đặt giá trị cho các thành phần
        txttendn_qltk.setText(taiKhoan.getEmail());
        txtmk_qltk.setText(taiKhoan.getPassword());

        // Kiểm tra và thực hiện các hành động tương ứng với trạng thái khóa
        if (taiKhoan.isLocked()) {
            txtTrangThai.setText("Tài khoản Bị Khóa");
            txtTrangThai.setTextColor(Color.RED);
        } else {
            txtTrangThai.setText("Tài khoản Đã Mở");
            txtTrangThai.setTextColor(Color.GREEN);
        }

        // Xử lý sự kiện khi người dùng giữ vào item
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Hiển thị thông báo hoặc thực hiện các hành động khác khi giữ vào item
                Toast.makeText(getContext(), "Item " + position + " được giữ", Toast.LENGTH_SHORT).show();

                // Nếu muốn khóa tài khoản, bạn có thể thực hiện một hành động nào đó ở đây
                // Ví dụ: gửi yêu cầu khóa tài khoản lên server
                banTaiKhoan(position);
                return true;
            }
        });

        return convertView;
    }
    public void banTaiKhoan(int position) {
        final TaiKhoan taiKhoan = getItem(position);
        if (taiKhoan != null && !taiKhoan.isLocked()) {
            // Hiển thị Dialog để xác nhận hành động
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Xác nhận");
            builder.setMessage("Bạn có chắc muốn ban tài khoản này?");
            builder.setPositiveButton("Ban", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Thay đổi trạng thái của tài khoản thành bị khóa (ban)
                    taiKhoan.setLocked(true);
                    // Cập nhật lại giao diện
                    notifyDataSetChanged();
                    // Thực hiện các hành động khác, ví dụ: gửi yêu cầu ban tài khoản lên server
                    // ...
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Đóng Dialog nếu người dùng chọn "Hủy"
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

}
