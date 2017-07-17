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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.data.ScoreCardDetailData;
import realmstudy.data.SessionSave;
import realmstudy.databaseFunctions.RealmDB;
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
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.match_detail_main, container, false);
        System.out.println("ScoreDataaaaonc");
        try {
            match_id = getArguments().getInt("match_id", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabLayout1);

//        View bottomSheet = v.findViewById(R.id.bot);
//        behavior = BottomSheetBehavior.from(bottomSheet);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.info)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.overs)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.score)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        //viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        adapter = new Pager(getChildFragmentManager(), tabLayout.getTabCount(), match_id);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(this);
        boolean forSecInnings = false;


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("1498913437");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("valueee", "Value is: " + value);
//                detailedScoreData = CommanData.fromJson(value, DetailedScoreData.class);
//
//                if (detailedScoreData != null) {
//                    Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
//                    if (fragment instanceof ScorecardDetailFragment ) {
//                        ScorecardDetailFragment scorecardDetailFragment = (ScorecardDetailFragment) fragment;
//                        ArrayList<ScoreCardDetailData> datas = new ArrayList<ScoreCardDetailData>();
//                        datas.add(detailedScoreData.getScoreCardDetailData());
//
//                        if (detailedScoreData.getSecscoreCardDetailData().getTeamName() != null) {
//                            System.out.println("not_nulll__" + detailedScoreData.getScoreCardDetailData().getTeamName() + "__" + detailedScoreData.getSecscoreCardDetailData().getTeamName());
//                            datas.add(detailedScoreData.getSecscoreCardDetailData());
//                        }
//                        scorecardDetailFragment.setDatas(datas);
//                    } else if (fragment instanceof OversFragment ) {
//                        OversFragment oversFragment = (OversFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
//                        oversFragment.setData(detailedScoreData.getOverAdapterData(), detailedScoreData.getScoreBoardData());
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("valueee", "Failed to read value.", error.toException());
//            }
//        });


        try {
            MatchDetails md = RealmDB.getMatchById(getActivity(), realm, match_id);
            InningsData d = realm.where(InningsData.class).equalTo("match_id", match_id).equalTo("firstInnings", !md.isFirstInningsCompleted()).findAllSorted("delivery", Sort.ASCENDING).last();
            detailedScoreData = CommanData.fromJson(d.getDetailedScoreBoardData(), DetailedScoreData.class);

            System.out.println("ScoreDataaaa" + d.getDetailedScoreBoardData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabLayout.addOnTabSelectedListener(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.match_detail_main);


    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        System.out.println("Selected__rrta");
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
        //System.out.println("Selected__vvv" + position+"__"+positionOffset+"__"+positionOffsetPixels);
        if (detailedScoreData != null) {
            Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
            if (fragment instanceof ScorecardDetailFragment && positionOffsetPixels == 0) {
                ScorecardDetailFragment scorecardDetailFragment = (ScorecardDetailFragment) fragment;
                ArrayList<ScoreCardDetailData> datas = new ArrayList<ScoreCardDetailData>();
                datas.add(detailedScoreData.getScoreCardDetailData());

                if (detailedScoreData.getSecscoreCardDetailData().getTeamName() != null) {
                    System.out.println("not_nulll__" + detailedScoreData.getScoreCardDetailData().getTeamName() + "__" + detailedScoreData.getSecscoreCardDetailData().getTeamName());
                    datas.add(detailedScoreData.getSecscoreCardDetailData());
                }
                scorecardDetailFragment.setDatas(datas);
            } else if (fragment instanceof OversFragment && positionOffsetPixels == 0) {
                OversFragment oversFragment = (OversFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                oversFragment.setData(detailedScoreData.getOverAdapterData(), detailedScoreData.getScoreBoardData());
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

        System.out.println("Selected__" + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        System.out.println("Selected__ccc");
    }


}

// Load data on background


