package anhpvph37030.fpoly.duan_nhom8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import anhpvph37030.fpoly.duan_nhom8.Adapter.MenuAdapter;
import anhpvph37030.fpoly.duan_nhom8.fragment.AdminQL;
import anhpvph37030.fpoly.duan_nhom8.fragment.CaNhanFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.DanhMucFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.MainFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.MyCartFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.Ql_KhachHangFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.ThongKeFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.TrangThaiFrg;

public class MainactivityAdmin extends AppCompatActivity {
    BottomNavigationView bottomNavigationView_adm;
    MenuAdapter adapter;
    ViewPager2 pagerMain_adm;
    ArrayList<Fragment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainadmin);
        initView_adm();
    }

    private void initView_adm() {
        pagerMain_adm = findViewById(R.id.pagerMain_adm);
        bottomNavigationView_adm = findViewById(R.id.menu_nav_adm);
        list.add(new DanhMucFrg());
        list.add(new AdminQL());
        list.add(new TrangThaiFrg());
        list.add(new ThongKeFrg());
        list.add(new Ql_KhachHangFrg());


        adapter = new MenuAdapter(this, list);
        pagerMain_adm.setAdapter(adapter);
        pagerMain_adm.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView_adm.setSelectedItemId(R.id.nav_trangtru);
                        break;
                    case 1:
                        bottomNavigationView_adm.setSelectedItemId(R.id.nav_sanpham);
                        break;
                    case 2:
                        bottomNavigationView_adm.setSelectedItemId(R.id.nav_trangthai);
                        break;
                    case 3:
                        bottomNavigationView_adm.setSelectedItemId(R.id.nav_thongke);
                        break;
                    case 4:
                        bottomNavigationView_adm.setSelectedItemId(R.id.nav_tai_khoan);
                        break;
                }
                super.onPageSelected(position);
            }
        });
        bottomNavigationView_adm.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_trangtru) {
                    pagerMain_adm.setCurrentItem(0);
                }
                if (item.getItemId() == R.id.nav_sanpham) {
                    pagerMain_adm.setCurrentItem(1);
                }
                if (item.getItemId() == R.id.nav_trangthai) {
                    pagerMain_adm.setCurrentItem(2);
                }
                if (item.getItemId() == R.id.nav_thongke) {
                    pagerMain_adm.setCurrentItem(3);
                }
                if (item.getItemId() == R.id.nav_tai_khoan) {
                    pagerMain_adm.setCurrentItem(4);
                }
                return true;
            }
        });
    }
}