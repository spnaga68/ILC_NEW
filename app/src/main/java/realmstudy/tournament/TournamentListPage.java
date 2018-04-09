package realmstudy.tournament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import realmstudy.R;
import realmstudy.adapter.recycler.Paginate;


/**
 * Created by developer on 29/5/17.
 */


public class TournamentListPage extends Fragment implements Paginate.Callbacks {
    private RecyclerView mRecyclerView;
    ViewGroup no_data_lay;

    private int type;
    String typeString = "";
    boolean isOnline = true;
    private ArrayList<TournamentData> datas = new ArrayList<>();
    private DatabaseReference myRef;
    private ValueEventListener valueEventListener;
    private ProgressBar progress_bar;
    private Paginate paginate;
    private int page;
    private boolean loading;
    private boolean allItemLoaded;
    private SavedGameListAdapter savedGameListAdapter;
    private ArrayList<TournamentData> dummuy_datas = new ArrayList<>();
    private TextView error_long_description;

    @Override
    public void onStop() {
        if (valueEventListener != null)
            myRef.removeEventListener(valueEventListener);
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.player_list_view, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list_view);
        //  ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
        v.findViewById(R.id.add).setVisibility(View.GONE);
        progress_bar = (ProgressBar) v.findViewById(R.id.progress_bar);
        v.findViewById(R.id.add_from_contacts).setVisibility(View.GONE);
        no_data_lay = (ViewGroup) v.findViewById(R.id.no_data_lay);

        error_long_description = (TextView) v.findViewById(R.id.error_long_description);
        error_long_description.setText(getString(R.string.no_tournament));
        v.findViewById(R.id.error_action_button).setVisibility(View.GONE);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
            // isOnline = getArguments().getBoolean("is_online", true);
            setUpRecyclerView(type);
        } else
            System.out.println("______setttitt" + null);
        v.findViewById(R.id.error_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = null;
//                if (type == 0)
//                    f = new ScheduleNewGame();
//                else
//                    f = new MatchInfo();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, f).commit();
            }
        });

//


        return v;
    }


    private void setUpRecyclerView(final int type) {
        System.out.println("______setttitt" + type + isOnline);
        //RealmResults data = null;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (isOnline) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            if (getActivity() != null) {
                savedGameListAdapter = new SavedGameListAdapter(getActivity(), datas);
                mRecyclerView.setAdapter(savedGameListAdapter);

                mRecyclerView.setHasFixedSize(true);

            }
            if (type == 0) {
                typeString = "upcoming";

            } else if (type == 1) {
                typeString = "ongoing";
            } else {
                typeString = "recent";
            }
            myRef = database.getReference("tournament_new/about/");
            Query queryRef;
            //  System.out.println("orderByChild(\"status\").equalTo(type)"+type);
            queryRef = myRef.orderByChild("status").equalTo(type)
                    .limitToLast(10);

            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progress_bar.setVisibility(View.GONE);
                    dummuy_datas.clear();
                    setupPagination(mRecyclerView);
                    if (dataSnapshot.exists()) {
                        System.out.println("md.getValue()*" + dataSnapshot.getChildrenCount() + "__" + savedGameListAdapter.getItemCount());
                        for (DataSnapshot md : dataSnapshot.getChildren()) {
                            if (md.getValue() != null && !md.getValue().equals("")) {
                                if (datas.size() > 0 && datas.get(datas.size() - 1).getKey().equals(md.getKey())) {

                                    System.out.println("datatacccc");
                                } else {
                                    TournamentData matchDetails = md.getValue(TournamentData.class);
//                                    matchDetails.setMatch_id(Integer.parseInt(md.getTournament_key()));
//                                    matchDetails.setmatchShortSummary(CommanData.toString(md.getValue()));

                                    dummuy_datas.add(matchDetails);
                                }

                            }


                        }
                        if (dummuy_datas.size() > 0)
                            for (int i = dummuy_datas.size() - 1; i >= 0; i--) {

                                datas.add(dummuy_datas.get(i));
                            }

                        if (datas == null || datas.size() == 0)
                            no_data_lay.setVisibility(View.VISIBLE);
                        else
                            no_data_lay.setVisibility(View.GONE);
                        page += 1;
                        if (datas.size() % 10 != 0)
                            allItemLoaded = true;
                        loading = false;

                        System.out.println("md.getValue()" + dataSnapshot.getChildrenCount() + "__" + datas.size());
                        savedGameListAdapter.addData(datas);

                    } else {
//                        System.out.println("databaseerrorss" );
                        if (datas.size() == 0)
                            no_data_lay.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("databaseerror" + databaseError.getMessage());
                    progress_bar.setVisibility(View.GONE);
                    if (datas == null || datas.size() == 0)
                        no_data_lay.setVisibility(View.VISIBLE);
                    else
                        no_data_lay.setVisibility(View.GONE);
                }
            };
            queryRef.addListenerForSingleValueEvent(valueEventListener);
            //   myRef.addValueEventListener(valueEventListener);
        } else {
//            if (type == 0)
//                data = realm.where(MatchDetails.class).equalTo("matchStatus", CommanData.MATCH_NOT_YET_STARTED).findAll();
//            else if (type == 2)
//                data = realm.where(MatchDetails.class).equalTo("matchStatus", CommanData.MATCH_COMPLETED).findAll();
//            else
//                data = realm.where(MatchDetails.class).notEqualTo("matchStatus", CommanData.MATCH_COMPLETED).notEqualTo("matchStatus", CommanData.MATCH_NOT_YET_STARTED).findAll();
//
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            mRecyclerView.setAdapter(new SavedGameListAdapter(getActivity(), data));
//            mRecyclerView.setHasFixedSize(true);
//            if (data == null || data.size() == 0)
//                no_data_lay.setVisibility(View.VISIBLE);
//            else
//                no_data_lay.setVisibility(View.GONE);
//
//            progress_bar.setVisibility(View.GONE);
        }

    }

    protected void setupPagination(RecyclerView recyclerView) {
        // If RecyclerView was recently bound, unbind
        if (paginate != null) {
            paginate.unbind();
        }

        paginate = Paginate.with(recyclerView, this)
                .setLoadingTriggerThreshold(5)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(null)
                .build();
    }

    @Override
    public void onLoadMore() {

        if (datas.size() > 0 && page > 0) {
            loading = true;
            System.out.println("Nan__" + datas.size() + "__" + datas.get(datas.size() - 1).getKey());
            Query queryRef = myRef.orderByKey()
                    .endAt(String.valueOf(datas.get(datas.size() - 1).getKey())).limitToFirst(10);
            queryRef.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    @Override
    public void viewScrolled() {

    }

    @Override
    public void inStart() {

    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return allItemLoaded;
    }
}
