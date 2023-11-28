package anhpvph37030.fpoly.duan_nhom8.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import anhpvph37030.fpoly.duan_nhom8.fragment.QL_HoaDonFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.fragmentHoaDon.ChoXacNhanFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.fragmentHoaDon.DaXacNhanFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.fragmentHoaDon.DangGiaoFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.fragmentHoaDon.HuyFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.fragmentHoaDon.ThanhCongFrg;


public class MyAdapterPager extends FragmentStateAdapter {

    int soluongpage = 5;

    public MyAdapterPager(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new ChoXacNhanFrg();
            case 1:
                return new DaXacNhanFrg();
            case 2:
                return new DangGiaoFrg();
            case 3:
                return new ThanhCongFrg();
            case 4:
                return new HuyFrg();
            default:
                return new ChoXacNhanFrg();
        }

    }

    @Override
    public int getItemCount() {
        return soluongpage;
    }
}