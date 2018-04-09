package realmstudy.tournament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import realmstudy.extras.AppConstants;
import realmstudy.matchList.view.MatchListPage;

public class TournamentPagerAdapter extends FragmentStatePagerAdapter {
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray();
    private final Bundle bundle;
    int tabCount;
    String id, aboutData;

    public TournamentPagerAdapter(FragmentManager fm, int tabCount, String tournamentID) {
        super(fm);
        this.tabCount = tabCount;
        id = tournamentID;
        bundle = new Bundle();
        this.aboutData = aboutData;
        bundle.putString(AppConstants.TourID, id);
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                MatchListPage frag = new MatchListPage();
                Bundle b = new Bundle();
                b.putInt("type", 4);
                b.putString(AppConstants.TourID,id);
                b.putBoolean("is_online", true);
                frag.setArguments(b);
                return frag;
            case 2:
                TournamentCommentary tournamentCommentary = new TournamentCommentary();
                tournamentCommentary.setArguments(bundle);
                return tournamentCommentary;
            case 3:
                LeaderBoardFragment leaderBoardFragment = new LeaderBoardFragment();
                leaderBoardFragment.setArguments(bundle);
                return leaderBoardFragment;
            case 4:
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                statisticsFragment.setArguments(bundle);
                return statisticsFragment;
//            case 3:
//                return new HeroesFragment();
//            case 4:
//                return new SponsorsFragment();
            case 5:
                TournamentGalleryFragment tournamentGalleryFragment = new TournamentGalleryFragment();

                Bundle bundles = (Bundle) bundle.clone();
                bundles.putInt("type", 0);
                tournamentGalleryFragment.setArguments(bundles);
                return tournamentGalleryFragment;
            case 6:
                TournamentVideoFragment tournamentGalleryFragment1 = new TournamentVideoFragment();
                Bundle bundls = (Bundle) bundle.clone();
                bundls.putInt("type", 1);
                tournamentGalleryFragment1.setArguments(bundls);
                return tournamentGalleryFragment1;
            case 7:
                TournamentSponsors tournamentGalleryFragment2 = new TournamentSponsors();
                Bundle bunls = (Bundle) bundle.clone();
                bunls.putInt("type", 2);
                tournamentGalleryFragment2.setArguments(bunls);
                return tournamentGalleryFragment2;
            case 0:
                InfoFragment infoFragment = new InfoFragment();
                infoFragment.setArguments(bundle);
                return infoFragment;
//            case 7:
//                return new TournamentMediaFragment();
//            case 8:
//                return new TournamentAboutUsFragment();
            default:
                return new InfoFragment();
            // return null;
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
