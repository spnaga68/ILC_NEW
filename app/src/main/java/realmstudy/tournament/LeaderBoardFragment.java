package realmstudy.tournament;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;

import realmstudy.R;
import realmstudy.extras.AppConstants;


public class LeaderBoardFragment extends Fragment implements OnClickListener {
    LeaderBoardListFragment frgBatsmanList;
    LeaderBoardListFragment frgBowlerList;
    LeaderPagerAdapter leaderPagerAdapter;
    View rootView;
    int tournamentId;

    android.support.v7.widget.CardView
            card_top;
    realmstudy.view.TextView
            txt_fielder1, txt_fielder2, txt_error;
    Spinner
            spinnerFilter;
    android.support.v7.widget.RecyclerView
            rvMatches;
    android.support.v4.view.ViewPager
            leader_pager;
    ProgressBar
            progressBar;

    class C13391 implements OnPageChangeListener {
        C13391() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    LeaderBoardFragment.this.setBatsMenView();
                    return;
                case 1:
                    LeaderBoardFragment.this.setBowlerView();
                    return;
                default:
                    return;
            }
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_player_matches, container, false);
        //ButterKnife.bind((Object) this, this.rootView);
        this.tournamentId = getActivity().getIntent().getIntExtra(AppConstants.EXTRA_TOURNAMENTID, 0);
        card_top = (android.support.v7.widget.CardView
                ) rootView.findViewById(R.id.card_top);
        txt_fielder1 = (realmstudy.view.TextView
                ) rootView.findViewById(R.id.txt_fielder1);
        txt_fielder2 = (realmstudy.view.TextView
                ) rootView.findViewById(R.id.txt_fielder2);
        spinnerFilter = (Spinner
                ) rootView.findViewById(R.id.spinnerFilter);
        rvMatches = (android.support.v7.widget.RecyclerView
                ) rootView.findViewById(R.id.rvMatches);
        leader_pager = (android.support.v4.view.ViewPager
                ) rootView.findViewById(R.id.leader_pager);
        txt_error = (realmstudy.view.TextView
                ) rootView.findViewById(R.id.txt_error);
        progressBar = (ProgressBar
                ) rootView.findViewById(R.id.progressBar);
        this.leader_pager.setVisibility(View.VISIBLE);
        this.card_top.setVisibility(View.VISIBLE);
        this.rvMatches.setVisibility(View.GONE);
        this.txt_fielder1.setOnClickListener(this);
        this.txt_fielder2.setOnClickListener(this);
        return this.rootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.leaderPagerAdapter = new LeaderPagerAdapter(getChildFragmentManager(), 2);
        this.leader_pager.setOffscreenPageLimit(2);
        this.leader_pager.addOnPageChangeListener(new C13391());
        this.leader_pager.setAdapter(this.leaderPagerAdapter);
    }

    public void setData() {
        if (this.frgBatsmanList == null) {
            this.frgBatsmanList = (LeaderBoardListFragment) this.leaderPagerAdapter.getFragment(0);
            if (this.frgBatsmanList != null) {
                this.frgBatsmanList.setData();
            }
        }
        if (this.frgBowlerList == null) {
            this.frgBowlerList = (LeaderBoardListFragment) this.leaderPagerAdapter.getFragment(1);
            if (this.frgBowlerList != null) {
                this.frgBowlerList.setData();
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_fielder1:
                this.leader_pager.setCurrentItem(0);
                setBatsMenView();
                return;
            case R.id.txt_fielder2:
                this.leader_pager.setCurrentItem(1);
                setBowlerView();
                return;
            default:
                return;
        }
    }

    private void setBatsMenView() {
        this.txt_fielder1.setTextColor(-1);
        this.txt_fielder2.setTextColor(Color.parseColor("#2A373F"));
        this.txt_fielder1.setBackgroundColor(Color.parseColor("#2A373F"));
        this.txt_fielder2.setBackgroundColor(Color.parseColor("#E7E8EA"));
    }

    private void setBowlerView() {
        this.txt_fielder1.setTextColor(Color.parseColor("#2A373F"));
        this.txt_fielder2.setTextColor(-1);
        this.txt_fielder1.setBackgroundColor(Color.parseColor("#E7E8EA"));
        this.txt_fielder2.setBackgroundColor(Color.parseColor("#2A373F"));
    }

    public Bitmap shareStats() {
        try {
            Bitmap bitmap = Bitmap.createBitmap(this.rootView.getWidth(), this.rootView.getHeight(), Config.ARGB_8888);
            this.rootView.draw(new Canvas(bitmap));
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
