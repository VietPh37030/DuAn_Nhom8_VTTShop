package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_taikhoan_qlkh, parent, false);
        }

        TaiKhoan taiKhoan = getItem(position);

        // Ánh xạ các thành phần trong layout card_view_tai_khoan.xml và đặt giá trị cho chúng
        ImageView imgQltk = convertView.findViewById(R.id.imgavt_qlkh);
        TextView txttendn_qltk = convertView.findViewById(R.id.txttendn_qlkh);
        TextView txtmk_qltk = convertView.findViewById(R.id.txtmk_qlkh);

        // Đặt giá trị cho các thành phần
        txttendn_qltk.setText(taiKhoan.getEmail());
        txtmk_qltk.setText(taiKhoan.getPassword());

        return convertView;
    }
}
