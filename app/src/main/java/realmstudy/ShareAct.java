package realmstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import realmstudy.extras.Utils;
import realmstudy.tournament.Media;

public class ShareAct extends AppCompatActivity {
    ViewPager mViewPager;
    ViewPagerAdapter adap;
    Media[] media;
    int position;
    TextView left_icon,right_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mViewPager = (ViewPager) findViewById(R.id.image_view_pager);
        ImageButton backImg = (ImageButton) findViewById(R.id.slideImg);
        ImageButton shareImg= (ImageButton) findViewById(R.id.shareImg);
        left_icon=(TextView)findViewById(R.id.left_icon);
        right_icon = (TextView)findViewById(R.id.right_icon);
        backImg.setVisibility(View.VISIBLE);
        shareImg.setVisibility(View.VISIBLE);
        backImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                System.out.println("backCalled");
                finish();
                return true;
            }
        });
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    startActivity(new Intent(ShareAct.this, GallerySlideAdapter.class));

            }
        });

        Intent get = getIntent();
        media = Utils.fromJson(get.getStringExtra("image_data"),Media[].class);
        position = get.getIntExtra("pos", 0);
        adap = new ViewPagerAdapter(ShareAct.this, media, position);
        mViewPager.setAdapter(adap);
        mViewPager.setCurrentItem(position);
        left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewPager.getCurrentItem()!=0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                }

            }
        });
        right_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        });
    }
}
