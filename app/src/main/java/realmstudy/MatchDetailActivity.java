package realmstudy;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.Sort;
import realmstudy.adapter.Pager;
import realmstudy.adapter.ScorecardDetailAdapter;
import realmstudy.data.CommanData;
import realmstudy.data.DetailedScoreData;
import realmstudy.data.OverAdapterData;
import realmstudy.data.RealmObjectData.InningsData;
import realmstudy.data.ScoreCardDetailData;
import realmstudy.data.SessionSave;
import realmstudy.extras.AnimatedExpandableListView;
import realmstudy.extras.ZoomOutPageTransformer;
import realmstudy.fragments.OversFragment;
import realmstudy.fragments.ScorecardDetailFragment;

/**
 * Created by developer on 2/5/17.
 */

public class MatchDetailActivity extends Fragment implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
//   BottomSheetBehavior behavior;

    private ViewPager viewPager;
    private int match_id;
    private Toolbar tool_bar;
    @Inject
    Realm realm;
    private DetailedScoreData detailedScoreData;
    private Pager adapter;
    private View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.match_detail_main, container, false);
        match_id = getArguments().getInt("match_id", 0);

        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabLayout1);

//        View bottomSheet = v.findViewById(R.id.bot);
//        behavior = BottomSheetBehavior.from(bottomSheet);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.info)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.overs)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.score)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        adapter = new Pager(getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), match_id);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(this);
        InningsData d = realm.where(InningsData.class).equalTo("match_id", match_id).findAllSorted("delivery", Sort.ASCENDING).last();
        detailedScoreData = CommanData.fromJson(d.getDetailedScoreBoardData(), DetailedScoreData.class);
        SessionSave.saveSession("checjjj",d.getDetailedScoreBoardData(),getActivity());


//        tool_bar = (android.support.v7.widget.Toolbar) v.findViewById(realmstudy.R.id.tool_bar);
//        tool_bar.setTitle(getString(R.string.score_board));

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.match_detail_main);


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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        System.out.println("Selected__" + position);
        if (position == 2) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    ArrayList<ScoreCardDetailData> datas = new ArrayList<ScoreCardDetailData>();
//                    datas.add(detailedScoreData.getScoreCardDetailData());
//                    ((ScorecardDetailFragment) adapter.getItem(2)).setDatas(datas,getActivity());
//                }
//            },1000);

            ArrayList<ScoreCardDetailData> datas = new ArrayList<ScoreCardDetailData>();
            datas.add(detailedScoreData.getScoreCardDetailData());
            AnimatedExpandableListView listView = (AnimatedExpandableListView) v.findViewById(R.id.listView);
            ScorecardDetailAdapter scorecardDetailAdapter = new ScorecardDetailAdapter(getActivity(), datas);
            listView.setAdapter(scorecardDetailAdapter);
          //  ((ScorecardDetailFragment) adapter.getItem(2)).setDatas();


        } else {
            ((OversFragment) adapter.getItem(1)).setData(detailedScoreData.getOverAdapterData());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.option_menu, menu); //your file name
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
////            case menu_one:
////                Intent ii=new Intent(MatchDetails.this,second.class);
////                startActivity(ii);
//
//            case R.id.menu_two:
//
//                return true;
//            case R.id.menu_three:
//
//                return true;
//            case R.id.menu_four:
//
//                return true;
//
//        }
//        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
//        View sheetView = this.getLayoutInflater().inflate(R.layout.activity_main, null);
//        mBottomSheetDialog.setContentView(sheetView);
//        mBottomSheetDialog.show();
////        LinearLayout edit = (LinearLayout) sheetView.v.findViewById(R.id.fragment_history_bottom_sheet_edit);
////        LinearLayout delete = (LinearLayout) sheetView.v.findViewById(R.id.fragment_history_bottom_sheet_delete);
////        edit.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();
////
////            }
////        });
////
////        delete.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Toast.makeText(MainActivity.this,"welcome",Toast.LENGTH_LONG).show();
////            }
////        });
//
//        return super.onOptionsItemSelected(item);
//
//    }


}

// Load data on background


