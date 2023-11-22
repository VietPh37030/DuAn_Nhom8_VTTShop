package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import anhpvph37030.fpoly.duan_nhom8.R;

public class Ad_TaiKhoanFrg extends Fragment {
    TextView txtdskh;
    ListView lst_taikhoan_adm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad__tai_khoan_frg, container, false);
        txtdskh = view.findViewById(R.id.txtdskh);

        return view;
    }
}