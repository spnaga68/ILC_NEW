package realmstudy.tournament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public class LeaderPagerAdapter extends FragmentStatePagerAdapter {
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray();
    int tabCount;

    public LeaderPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                bundle.putBoolean("batsman", true);
                Fragment leaderBoardListFragment = new LeaderBoardListFragment();
                leaderBoardListFragment.setArguments(bundle);
                return leaderBoardListFragment;
            case 1:
                bundle.putBoolean("batsman", false);
                Fragment leaderBoardListFragment1 = new LeaderBoardListFragment();
                leaderBoardListFragment1.setArguments(bundle);
                return leaderBoardListFragment1;
            default:
                return null;
        }
    }

    public int getCount() {
        return this.tabCount;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.instantiatedFragments.put(position, new WeakReference(fragment));
        return fragment;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        this.instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(int position) {
        WeakReference<Fragment> wr = (WeakReference) this.instantiatedFragments.get(position);
        if (wr != null) {
            return (Fragment) wr.get();
        }
        return null;
    }
}
