package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import anhpvph37030.fpoly.duan_nhom8.R;

public class Ad_TrangThaiFrg extends Fragment {

    TextView txttrangthai;
    ListView lst_trangthai_adm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad__trang_thai_frg, container, false);
        txttrangthai = view.findViewById(R.id.txttrangthai);


        return view;
    }
}