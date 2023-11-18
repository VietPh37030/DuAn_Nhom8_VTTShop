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
import anhpvph37030.fpoly.duan_nhom8.fragment.CaNhanFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.MainFrg;
import anhpvph37030.fpoly.duan_nhom8.fragment.MyCartFrg;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    MenuAdapter adapter;
    ViewPager2 pagerMain;
    ArrayList<Fragment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        pagerMain = findViewById(R.id.pagerMain);
        bottomNavigationView = findViewById(R.id.menu_nav);
        list.add(new MainFrg());
        list.add(new MyCartFrg());
        list.add(new CaNhanFrg());
        adapter = new MenuAdapter(this, list);
        pagerMain.setAdapter(adapter);
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.mnu_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.mnu_giohang);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.mnu_info);
                        break;
                }
                super.onPageSelected(position);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.mnu_home) {
                    pagerMain.setCurrentItem(0);
                }
                if (item.getItemId() == R.id.mnu_giohang) {
                    pagerMain.setCurrentItem(1);
                }
                if (item.getItemId() == R.id.mnu_info) {
                    pagerMain.setCurrentItem(2);
                }
                return true;
            }
        });
    }
}