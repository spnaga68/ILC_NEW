package realmstudy.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import realmstudy.fragments.ChartFrag;
import realmstudy.fragments.InfoFragment;
import realmstudy.fragments.OversFragment;
import realmstudy.fragments.ScoreBoardFragment;
import realmstudy.fragments.ScorecardDetailFragment;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    int match_id;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount, int match_id) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.match_id = match_id;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:

                InfoFragment frag = new InfoFragment();
                Bundle b = new Bundle();
                b.putInt("match_id", match_id);
                frag.setArguments(b);
                return frag;
            case 1:
                OversFragment frags = new OversFragment();
                Bundle bs = new Bundle();
                bs.putInt("match_id", match_id);
              //  bs.putInt("scoreData", match_id);
                frags.setArguments(bs);
                return frags;
            case 2:
                ScorecardDetailFragment Sfrag = new ScorecardDetailFragment();
                Bundle bd = new Bundle();
                bd.putInt("match_id", match_id);
                Sfrag.setArguments(bd);

                return Sfrag;
            case 3:
                ChartFrag Sfrags = new ChartFrag();
                Bundle bds = new Bundle();
                bds.putInt("match_id", match_id);
                Sfrags.setArguments(bds);

                return Sfrags;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}