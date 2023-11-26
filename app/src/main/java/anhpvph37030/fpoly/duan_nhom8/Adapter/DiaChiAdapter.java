package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.ThongTinDiaChi;

public class DiaChiAdapter extends ArrayAdapter<ThongTinDiaChi> {

    private Context context;

    public DiaChiAdapter(@NonNull Context context, @NonNull List<ThongTinDiaChi> diaChiList) {
        super(context, 0, diaChiList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_diachi, parent, false);
        }

        ThongTinDiaChi thongTinDiaChi = getItem(position);

        TextView txtHoTen = convertView.findViewById(R.id.txtHovaten);
        TextView txtSoDienThoai = convertView.findViewById(R.id.txtsodienthoai);
        TextView txtDiaChi = convertView.findViewById(R.id.txtDiachinha);

        if (thongTinDiaChi != null) {
            txtHoTen.setText(thongTinDiaChi.getHoTen());
            txtSoDienThoai.setText(thongTinDiaChi.getSoDienThoai());
            txtDiaChi.setText(thongTinDiaChi.getDiaChi());
        }

        return convertView;
    }
}

