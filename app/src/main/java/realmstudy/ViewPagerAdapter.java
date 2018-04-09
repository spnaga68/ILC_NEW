package realmstudy;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import realmstudy.tournament.Media;


public class ViewPagerAdapter extends PagerAdapter {
    Context mContext;
    Media[] items;
    int position;

    ViewPagerAdapter(Context context, Media[] items, int position) {
        this.items = items;
        this.mContext = context;
        this.position = position;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == (obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        ImageView mImageView = new ImageView(mContext);
        System.out.println("showing image of"+items[i].getMediaUrl());
        Picasso.with(mContext)
                .load(items[i].getMediaUrl())
                .into(mImageView);
        container.addView(mImageView);//(container).addView(mImageView,0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        (container).removeView((ImageView) obj);
    }
}
