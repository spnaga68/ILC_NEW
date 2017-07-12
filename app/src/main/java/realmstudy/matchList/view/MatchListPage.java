package realmstudy.matchList.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.adapter.SavedGameListAdapter;
import realmstudy.data.CommanData;
import realmstudy.data.RealmObjectData.MatchDetails;

/**
 * Created by developer on 29/5/17.
 */


public class MatchListPage extends Fragment {
    private RecyclerView mRecyclerView;
    private SavedGameListAdapter adapter;
    ViewGroup no_data_lay;
    @Inject
     Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.player_list_view, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list_view);
        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
        v.findViewById(R.id.add).setVisibility(View.GONE);
        v.findViewById(R.id.add_from_contacts).setVisibility(View.GONE);
        no_data_lay= (ViewGroup) v.findViewById(R.id.no_data_lay);
        if (getArguments() != null)
            setUpRecyclerView(getArguments().getInt("type"));
        else
            System.out.println("______setttitt"+null);
//


        return v;
    }

    private void setUpRecyclerView(int type) {
        System.out.println("______setttitt"+type);
        RealmResults data = null;
        if (type == 0)
            data = realm.where(MatchDetails.class).equalTo("matchStatus", CommanData.MATCH_NOT_YET_STARTED).findAll();
        else if (type == 2)
            data = realm.where(MatchDetails.class).equalTo("matchStatus", CommanData.MATCH_COMPLETED).findAll();
        else
            data = realm.where(MatchDetails.class).notEqualTo("matchStatus", CommanData.MATCH_COMPLETED).notEqualTo("matchStatus", CommanData.MATCH_NOT_YET_STARTED).findAll();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new SavedGameListAdapter(getActivity(),data ));
        mRecyclerView.setHasFixedSize(true);
        if(data.size()==0)
            no_data_lay.setVisibility(View.VISIBLE);
    }
}
