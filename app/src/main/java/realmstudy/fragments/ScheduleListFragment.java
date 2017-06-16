package realmstudy.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.realm.Realm;
import realmstudy.MainFragmentActivity;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.adapter.SavedGameListAdapter;
import realmstudy.adapter.ScheduleListAdapter;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.lib.customViews.SlideDeleteRecyclcerView;

/**
 * Created by developer on 7/3/17.
 */
public class ScheduleListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SavedGameListAdapter adapter;
    @Inject
     Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.player_list_view, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list_view);
        v.findViewById(R.id.add).setVisibility(View.GONE);
        v.findViewById(R.id.add_from_contacts).setVisibility(View.GONE);
        ((MyApplication)getActivity().getApplication()).getComponent().inject(this);
        setUpRecyclerView();
//


        return v;
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ScheduleListAdapter(getActivity(),realm.where(MatchDetails.class).findAll()));
        mRecyclerView.setHasFixedSize(true);
    }


}