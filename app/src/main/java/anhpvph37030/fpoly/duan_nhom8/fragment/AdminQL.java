package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import anhpvph37030.fpoly.duan_nhom8.R;


public class AdminQL extends Fragment {

    ListView lstadminql;
    ImageButton btnThem;
    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_admin, container, false);
        searchView = view.findViewById(R.id.search_view);
        btnThem = view.findViewById(R.id.btnThem);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện ở đây
            }
        });

        // Xử lý sự kiện khi thanh tìm kiếm thay đổi
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng nhấn Enter trên bàn phím
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý khi người dùng thay đổi nội dung trong thanh tìm kiếm
                return false;
            }
        });
        return view;
    }
}