package realmstudy.matchList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import realmstudy.R;
import realmstudy.adapter.Pager;
import realmstudy.extras.ZoomOutPageTransformer;
import realmstudy.matchList.view.MatchListPager;

/**
 * Created by developer on 29/5/17.
 */

public class MatchListMainFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    ViewPager viewPager;
    Toolbar tool_bar;
    private int match_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.match_detail_main, container, false);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabLayout1);

        // match_id =  getArguments().getInt("match_id", 0);
//        View bottomSheet = v.findViewById(R.id.bot);
//        behavior = BottomSheetBehavior.from(bottomSheet);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.upcoming)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.on_going)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.recent)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        MatchListPager adapter = new MatchListPager(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding adapter to pager
          viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tool_bar = (android.support.v7.widget.Toolbar) v.findViewById(realmstudy.R.id.tool_bar);
        tool_bar.setTitle(getString(R.string.score_board));

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
        return v;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
