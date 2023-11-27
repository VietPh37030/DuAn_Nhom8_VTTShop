package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.ThongTinDiaChi;

public class DiaChiAdapter extends ArrayAdapter<ThongTinDiaChi> {

    private Context context;
    private EditClickListener editClickListener;
    private DeleteClickListener deleteClickListener;
    private LayoutInflater inflater;

    private ThongTinDiaChi selectedDiaChi;
    public ThongTinDiaChi getSelectedDiaChi() {
        return selectedDiaChi;
    }
    private int selectedPosition = -1; // Biến để lưu trạng thái của địa chỉ được chọn

    public DiaChiAdapter(@NonNull Context context, @NonNull List<ThongTinDiaChi> diaChiList) {
        super(context, 0, diaChiList);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setEditClickListener(EditClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }

    public void setDeleteClickListener(DeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public interface EditClickListener {
        void onEditClick(int position);
    }

    public interface DeleteClickListener {
        void onDeleteClick(int position);
    }

    // Phương thức để đặt địa chỉ được chọn
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_diachi, parent, false);

            holder = new ViewHolder();
            holder.txtId = convertView.findViewById(R.id.txtMa);
            holder.txtHoTen = convertView.findViewById(R.id.txtHovaten);
            holder.txtSoDienThoai = convertView.findViewById(R.id.txtsodienthoai);
            holder.txtDiaChi = convertView.findViewById(R.id.txtDiachinha);
            holder.imgSua = convertView.findViewById(R.id.txtsuadiachi);
            holder.imgXoa = convertView.findViewById(R.id.btnDelete);
            holder.chkluachon = convertView.findViewById(R.id.chkluachon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ThongTinDiaChi thongTinDiaChi = getItem(position);

        if (thongTinDiaChi != null) {
            holder.txtId.setText(thongTinDiaChi.getId());
            holder.txtHoTen.setText(thongTinDiaChi.getHoTen());
            holder.txtSoDienThoai.setText(thongTinDiaChi.getSoDienThoai());
            holder.txtDiaChi.setText(thongTinDiaChi.getDiaChi());

            // Đặt trạng thái của RadioButton dựa trên việc địa chỉ có được chọn hay không
            holder.chkluachon.setChecked(position == selectedPosition);
        }
        // Trong phương thức getView của Adapter
        holder.chkluachon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (selectedDiaChi != null) {
                        selectedDiaChi.setSelected(false);
                    }

                    selectedDiaChi = getItem(position);
                    if (selectedDiaChi != null) {
                        selectedDiaChi.setSelected(true);
                    }

                    notifyDataSetChanged();
                }
            }
        });

// Bỏ dòng này nếu không cần CheckBox được chọn từ danh sách
        holder.chkluachon.setChecked(position == selectedPosition);


        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickListener != null) {
                    editClickListener.onEditClick(position);
                }
            }
        });

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(position);
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView txtId;
        TextView txtHoTen;
        TextView txtSoDienThoai;
        TextView txtDiaChi;
        ImageView imgSua;
        ImageView imgXoa;
        CheckBox chkluachon;
    }
}
