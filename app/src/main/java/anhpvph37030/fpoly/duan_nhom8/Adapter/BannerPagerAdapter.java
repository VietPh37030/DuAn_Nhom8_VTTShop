package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import anhpvph37030.fpoly.duan_nhom8.R;

public class BannerPagerAdapter extends PagerAdapter {
    private Context context;
    private int[] bannerImages = {R.drawable.banner14, R.drawable.banner15, R.drawable.banner12};

    public BannerPagerAdapter(Context context, int[] bannerImages) {
        this.context = context;
        this.bannerImages = bannerImages;
    }

    @Override
    public int getCount() {
        return bannerImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.banner_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        imageView.setImageResource(bannerImages[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
