package realmstudy.matchList.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by developer on 29/5/17.
 */


//Extending FragmentStatePagerAdapter
public class MatchListPager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public MatchListPager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:

                MatchListPage frag = new MatchListPage();
                Bundle b = new Bundle();
                b.putInt("type", 0);
                frag.setArguments(b);
                return frag;
            case 1:
                MatchListPage frags = new MatchListPage();
                Bundle bs = new Bundle();
                bs.putInt("type", 1);
                //  bs.putInt("scoreData", match_id);
                frags.setArguments(bs);
                return frags;
            case 2:
                MatchListPage Sfrag = new MatchListPage();
                Bundle bd = new Bundle();
                bd.putInt("type", 2);
                Sfrag.setArguments(bd);

                return Sfrag;
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