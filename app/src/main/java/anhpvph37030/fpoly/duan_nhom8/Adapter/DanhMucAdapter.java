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
    private EditDanhMucListener editDanhMucListener;
    public DanhMucAdapter(Context context, int resource, List<DanhMuc> danhMucList) {
        super(context, resource, danhMucList);
        this.context = context;
        this.resource = resource;
    }
    public interface EditDanhMucListener {
        void onEditDanhMuc(DanhMuc danhMuc);
    }
    public void setEditDanhMucListener(EditDanhMucListener listener) {
        this.editDanhMucListener = listener;
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
            ImageView btnedit = convertView.findViewById(R.id.btnEdit);

            // Set the ID to the TextView
            txtMaDanhMuc.setText("ID: " + danhMuc.getMaDanhMuc());

            // Load image using Picasso or your preferred image loading library
            String urlSanPham = danhMuc.getUrlSanPham();
            if (urlSanPham != null && !urlSanPham.isEmpty()) {
                Picasso.get().load(urlSanPham).into(imgAnhSp);
            } else {
                // Nếu không có URL, có thể hiển thị một ảnh mặc định
                imgAnhSp.setImageResource(R.drawable.avt);
            }

            // Set other data to the views
            String tenHang = danhMuc.getTenHang();
            if (tenHang != null) {
                txthang.setText(tenHang);
            }
            btnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editDanhMucListener != null) {
                        editDanhMucListener.onEditDanhMuc(danhMuc);
                    }
                }
            });
        }

        return convertView;
    }

    private void OnClickUpdate() {

    }
}
