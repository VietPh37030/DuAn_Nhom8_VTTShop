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
import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;

public class DanhMucAdapter extends ArrayAdapter<DanhMuc> {
    private Context context;
    private int resource;

    public DanhMucAdapter(Context context, int resource, List<DanhMuc> danhMucList) {
        super(context, resource, danhMucList);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        DanhMuc danhMuc = getItem(position);

        if (danhMuc != null) {
            ImageView imgAnhSp = convertView.findViewById(R.id.imgAnhSp);
            TextView txthang = convertView.findViewById(R.id.txthang);

            // Đặt dữ liệu từ danhMuc vào các thành phần của layout
            if (danhMuc.getUrlSanPham() != null && !danhMuc.getUrlSanPham().isEmpty()) {
                Picasso.get().load(danhMuc.getUrlSanPham()).into(imgAnhSp);
            }

            if (danhMuc.getTenHang() != null) {
                txthang.setText(danhMuc.getTenHang());
            }
        }

        return convertView;
    }

}
