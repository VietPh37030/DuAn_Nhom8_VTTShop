package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.tabs.TabLayout;
import anhpvph37030.fpoly.duan_nhom8.Adapter.MyAdapterPager;
import anhpvph37030.fpoly.duan_nhom8.R;

public class QL_HoaDonFrg extends Fragment {
    MyAdapterPager adapterPager;
    ViewPager2 pager2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_q_l__hoa_don, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //tạo pager
        adapterPager = new MyAdapterPager(this);
        pager2 = view.findViewById(R.id.pager);
        pager2.setAdapter(adapterPager);

        //tạo tab
        TabLayout tabLayout = view.findViewById(R.id.tab_01);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, pager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Chờ xác nhân");
                        break;
                    case 1:
                        tab.setText("Đã Xác Nhân");
                        break;
                    case 2:
                        tab.setText("Đang Giao");
                        break;
                    case 3:
                        tab.setText("Thành Công");
                        break;
                    case 4:
                        tab.setText("Đã Hủy");
                }
            }
        });
        mediator.attach();
    }
}