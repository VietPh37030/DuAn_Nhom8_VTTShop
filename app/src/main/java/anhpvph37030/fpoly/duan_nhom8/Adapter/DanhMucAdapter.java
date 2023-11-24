package anhpvph37030.fpoly.duan_nhom8.Adapter;// DanhMucAdapter.java
// Import statements...

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
            TextView txtMaDanhMuc = convertView.findViewById(R.id.txtMaHang);
            ImageView imgAnhSp = convertView.findViewById(R.id.imgAnhSp);
            TextView txthang = convertView.findViewById(R.id.txthang);

            // Set the ID to the TextView
            txtMaDanhMuc.setText("ID: " + danhMuc.getMaDanhMuc());

            // Load image using Picasso or your preferred image loading library
            if (danhMuc.getUrlSanPham() != null && !danhMuc.getUrlSanPham().isEmpty()) {
                Picasso.get().load(danhMuc.getUrlSanPham()).into(imgAnhSp);
            }

            // Set other data to the views
            if (danhMuc.getTenHang() != null) {
                txthang.setText(danhMuc.getTenHang());
            }
        }

        return convertView;
    }
}
