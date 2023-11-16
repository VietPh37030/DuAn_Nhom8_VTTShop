package anhpvph37030.fpoly.duan_nhom8.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class MenuAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> list;

    public MenuAdapter(@NonNull FragmentActivity fragmentActivity,ArrayList<Fragment> list) {
        super(fragmentActivity);
        this.list = list;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
