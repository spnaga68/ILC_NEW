package realmstudy.tournament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import realmstudy.MainFragmentActivity;
import realmstudy.R;
import realmstudy.extras.NetworkStatus;


/**
 * Created by developer on 29/5/17.
 */

public class MatchListMainFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    ViewPager viewPager;
    Toolbar tool_bar;
    private int match_id;
    private boolean isOnline;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.match_detail_main, container, false);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabLayout1);
        if (getArguments() != null)
            isOnline = getArguments().getBoolean("is_online", false);
        // match_id =  getArguments().getInt("match_id", 0);
//        View bottomSheet = v.findViewById(R.id.bot);
//        behavior = BottomSheetBehavior.from(bottomSheet);
        if (!NetworkStatus.isOnline(getActivity()))
            ((MainFragmentActivity) getActivity()).showNetWorkAlert();
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.upcoming)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.on_going)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.recent)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        // viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

//        if (!NetworkStatus.isOnline(getActivity()) && isOnline)
//            ((MainFragmentActivity) getActivity()).showNetWorkAlert();
        TournamentListPager adapter = new TournamentListPager(getChildFragmentManager(), tabLayout.getTabCount(),isOnline);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        //      tool_bar = (android.support.v7.widget.Toolbar) v.findViewById(realmstudy.R.id.tool_bar);
//        tool_bar.setTitle(getString(R.string.score_board));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("Selected__" + position);

                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
        return v;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onResume() {
        super.onResume();
//        if(isOnline)
//            getActivity().setTitle(getString(R.string.match_center));
//        else
//            getActivity().setTitle(getString(R.string.scorer));
//
//        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
//            ((MainFragmentActivity)getActivity()).setNaviHome();
//
//        }
    }
}
