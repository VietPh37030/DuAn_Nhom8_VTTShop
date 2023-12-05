package anhpvph37030.fpoly.duan_nhom8.Adapter;

// DanhMucUserAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;

public class DanhMucUserAdapter extends RecyclerView.Adapter<DanhMucUserAdapter.ViewHolder> {

    private Context context;
    private List<DanhMuc> danhMucList;
    private OnDanhMucClickListener onDanhMucClickListener;


    public DanhMucUserAdapter(Context context, List<DanhMuc> danhMucList) {
        this.context = context;
        this.danhMucList = danhMucList;
    }

    // Thêm phương thức clear để xóa tất cả dữ liệu trong danh sách
    public void clear() {
        danhMucList.clear();
        notifyDataSetChanged();
    }

    // Thêm phương thức addAll để thêm một danh sách mới vào danh sách hiện tại
    public void addAll(List<DanhMuc> danhMucList) {
        this.danhMucList.addAll(danhMucList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danhmuc_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhMuc danhMuc = danhMucList.get(position);

        // Bind data to the ViewHolder components
        holder.tvTenDanhMuc.setText(danhMuc.getTenHang());

        // Load image using Picasso or your preferred image loading library
        String urlDanhMuc = danhMuc.getUrlSanPham();
        if (urlDanhMuc != null && !urlDanhMuc.isEmpty()) {
            Picasso.get().load(urlDanhMuc).into(holder.imgDanhMuc);
        } else {
            // Set a default image if the URL is not available
            holder.imgDanhMuc.setImageResource(R.drawable.avt);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDanhMucClickListener != null) {
                    onDanhMucClickListener.onDanhMucClick(danhMuc);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhMucList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDanhMuc;
        TextView tvTenDanhMuc;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize your ViewHolder components here
            imgDanhMuc = itemView.findViewById(R.id.imgDanhMuc);
            tvTenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
    public interface OnDanhMucClickListener {
        void onDanhMucClick(DanhMuc danhMuc);
    }
    public void setOnDanhMucClickListener(OnDanhMucClickListener listener) {
        this.onDanhMucClickListener = listener;
    }

}

