package realmstudy.tournament;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import realmstudy.R;
import realmstudy.base.BaseQuickAdapter;
import realmstudy.extras.AppConstants;
import realmstudy.extras.Utils;
import realmstudy.view.TextView;

public class TournamentCommentary extends Fragment implements BaseQuickAdapter.RequestLoadMoreListener, OnItemSelectedListener {
    private CommentaryRecyclerAdapter adapter;
    int ballType = -1;
    private boolean batsman = true;
    private List<String> filterList;
    private List<String> filterListCode;
    private boolean initSpinListener = true;
    private ArrayList<CommentaryData> itemArrayList;
    private boolean loadmore;
    int tournamentId;
    private RecyclerView rvMatches;
    ViewGroup viewEmpty;
    TextView tvDetail;
    private ProgressBar progressBar;
    private DataSnapshot baseResponse;
    private String TOUR_ID = "tour1";
    private android.widget.TextView tvTitle;
    private ChildEventListener childAddListner;
    private long lastItemId=0;

    class C13422 implements Runnable {
        C13422() {
        }

        public void run() {
            TournamentCommentary.this.adapter.loadMoreEnd();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.commentary_layout, container, false);
        this.TOUR_ID = getActivity().getIntent().getStringExtra(AppConstants.TourID);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        viewEmpty = (ViewGroup) rootView.findViewById(R.id.viewEmpty);
        tvDetail = (TextView) rootView.findViewById(R.id.tvDetail);
        tvTitle = (android.widget.TextView) rootView.findViewById(R.id.tvTitle);
        itemArrayList = new ArrayList<>();
        rvMatches = (RecyclerView) rootView.findViewById(R.id.rvMatches);
        this.rvMatches.setLayoutManager(new LinearLayoutManager(getActivity()));
        setData();

        return rootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.itemArrayList = new ArrayList();
    }

    public void setData() {
        this.progressBar.setVisibility(View.VISIBLE);
        System.out.println("Setdata");
        getBattingLeaderboard(null, null, false);
    }

    public void onStop() {
//        ApiCallManager.cancelCall("get_bat_leader_board");
//        ApiCallManager.cancelCall("get_bowl_leader_board");
        super.onStop();
    }

    public void getBattingLeaderboard(String page, Long datetime, final boolean refresh) {
        if (!this.loadmore) {
            this.progressBar.setVisibility(View.VISIBLE);
        }
        this.loadmore = false;
        emptyViewVisibility(false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("tournament_new/newsfeed/" + TOUR_ID);
        final String TAG = "Commentary itemArrayList";

        Query queryRef;
        System.out.println("postionnnncomm" + page + TOUR_ID);
        if (page == null)
            queryRef = myRef
                    .orderByChild("time")
                    .limitToLast(10);
        else
            queryRef = myRef.orderByChild("time").endAt(datetime).limitToFirst(10);

        ValueEventListener valueEventListener = (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever itemArrayList at this location is updated.
                progressBar.setVisibility(View.GONE);
//                Log.d(TAG, "Value is: Leader" + dataSnapshot.getValue().toString());
                if (getActivity() != null) {
                    if (!dataSnapshot.exists()) {
                        TournamentCommentary.this.loadmore = true;
                        if (TournamentCommentary.this.adapter != null) {
                            TournamentCommentary.this.adapter.loadMoreFail();
                        }
                        if (TournamentCommentary.this.itemArrayList.size() <= 0) {
                            TournamentCommentary.this.emptyViewVisibility(true);
                            TournamentCommentary.this.rvMatches.setVisibility(View.GONE);
                            return;
                        }
                        return;
                    }
                    TournamentCommentary.this.baseResponse = dataSnapshot;
                    ArrayList<CommentaryData> arrayList = new ArrayList<>();
                    for (DataSnapshot md : dataSnapshot.getChildren()) {
                        if (md.getValue() != null && !md.getValue().equals("")) {
                            CommentaryData matchDetails = md.getValue(CommentaryData.class);
                            if(arrayList.size()>0)
                            System.out.println("datatacccc" + arrayList.size()+"__"+lastItemId+"___"+matchDetails.getTime());
                            if (arrayList.size() > 0 && lastItemId == matchDetails.getTime()) {

                                System.out.println("datatacccc");
                            } else {

//                                    matchDetails.setMatch_id(Integer.parseInt(md.getTournament_key()));
//                                    matchDetails.setmatchShortSummary(CommanData.toString(md.getValue()));

                                arrayList.add(matchDetails);
                            }
                        }
                    }
//                    ArrayList<CommentaryData> arrayList = new ArrayList<>(Arrays.asList(Utils.fromJson(Utils.toString(arrayLists), CommentaryData[].class)));
//                    for (int i = 0; i < arrayList.size(); i++) {
//                        System.out.println("cameaddbat" + i + "____" + arrayList.get(i).getTime());
//                        itemArrayList.add(arrayList.get(i));
//                    }
                    Collections.reverse(arrayList);
                    lastItemId=arrayList.get(arrayList.size()-1).getTime();
                    if(itemArrayList.size()==0)
                    itemArrayList.addAll(arrayList);
                    System.out.println("datataccccitem"+itemArrayList.size());
                    //   itemArrayList.addAll(Arrays.asList(Utils.fromJson(Utils.toString(arrayList),CommentaryData[].class)));
                    //    System.out.println("datavvv  "+ (pasu.vadivasal.android.Utils.toString(datav)));
                    // ArrayList<CommentaryData> datav = new ArrayList<CommentaryData>(sh.values());
                    System.out.println("datavvvf  " + TAG + (Utils.toString(arrayList.get(0))));

                    if (TournamentCommentary.this.adapter == null) {
                        System.out.println("NEW ADAPTER SETbat");
                        //TournamentCommentary .this.itemArrayList.addAll(arrayList);
                        TournamentCommentary.this.adapter = new CommentaryRecyclerAdapter(TournamentCommentary.this.getActivity(), R.layout.commentary_video, TournamentCommentary.this.itemArrayList);
                        TournamentCommentary.this.adapter.setEnableLoadMore(true);
                        TournamentCommentary.this.rvMatches.setAdapter(TournamentCommentary.this.adapter);
                        // TournamentCommentary .this.rvMatches.addOnItemTouchListener(new C13401());
                        TournamentCommentary.this.adapter.setOnLoadMoreListener(TournamentCommentary.this, TournamentCommentary.this.rvMatches);

                        if (arrayList.size() % 10 != 0) {
                            TournamentCommentary.this.adapter.loadMoreEnd();
                        }
                        final DatabaseReference myRefs = database.getReference("tournament_new/newsfeed/" + TOUR_ID);
                        try {
                            // String firstKey = (String) ((HashMap<String, CommentaryData>) dataSnapshot.getValue()).keySet().toArray()[0];
                            System.out.println("datavvv  ssfirst key" + adapter.getData().size() + "__" + itemArrayList.get(0).getTime());
                            myRefs.orderByChild("time").startAt(itemArrayList.get(0).getTime()).addChildEventListener(childAddListner);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (refresh) {
                            TournamentCommentary.this.adapter.getData().clear();
                            TournamentCommentary.this.itemArrayList.clear();
                            TournamentCommentary.this.itemArrayList.addAll(arrayList);
                            TournamentCommentary.this.adapter.setNewData(arrayList);
                            TournamentCommentary.this.adapter.setEnableLoadMore(true);
                            TournamentCommentary.this.rvMatches.scrollToPosition(0);
                        } else {
                            System.out.println("datataccccitemaddd"+arrayList.size()+   TournamentCommentary.this.adapter.getItemCount());
                            TournamentCommentary.this.adapter.addData((Collection) arrayList);
                            TournamentCommentary.this.adapter.loadMoreComplete();
                            System.out.println("datataccccitemafter"+arrayList.size()+   TournamentCommentary.this.adapter.getItemCount());
                        }

                    }
                    if (TournamentCommentary.this.baseResponse != null && arrayList.size() % 10 != 0) {
                        TournamentCommentary.this.adapter.loadMoreEnd();
                    }else
                    TournamentCommentary.this.loadmore = true;
                    if (TournamentCommentary.this.itemArrayList.size() == 0) {
                        TournamentCommentary.this.emptyViewVisibility(true);
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });


        childAddListner = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("commentary added" + dataSnapshot.getValue());

                if (dataSnapshot != null && itemArrayList != null) {

                    CommentaryData arrayLists = dataSnapshot.getValue(CommentaryData.class);
                    if (itemArrayList.size() > 0 && itemArrayList.get(0).getTime() != (arrayLists).getTime()) {
                        System.out.println("commentary adding data");
                        TournamentCommentary.this.adapter.addData(0, arrayLists);
                        TournamentCommentary.this.adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        queryRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private String getHighlightedText(String text, boolean isHighlight) {
        if (isHighlight) {
            return "<b>" + text + "</b>";
        }
        return text;
    }

//    private View getEmptyView() {
//        if (getActivity() != null) {
//            this.viewEmpty = getActivity().getLayoutInflater().inflate(R.layout.raw_empty_view, null);
//            TextView tvTitle = (TextView) this.viewEmpty.findViewById(R.id.tvTitle);
//            TextView tvDetail = (TextView) this.viewEmpty.findViewById(R.id.tvDetail);
//            ((ImageView) this.viewEmpty.findViewById(R.id.ivImage)).setImageResource(R.drawable.about);
//            //tvTitle.setText(R.string.leaderbord_blank_stat);
//            tvDetail.setVisibility(View.GONE);
//        }
//        return this.viewEmpty;
//    }

    public void onLoadMoreRequested() {
        System.out.println("onLoadMoreRequested" + this.loadmore + "___" + (itemArrayList.size() % 10 != 0));
        if (!this.loadmore || (itemArrayList.size() % 10 != 0)) {
            new Handler().postDelayed(new C13422(), 1500);
        } else if (this.batsman) {
            System.out.println("Load more" + itemArrayList.get(itemArrayList.size() - 1).getTime());
            getBattingLeaderboard(String.valueOf(itemArrayList.get(itemArrayList.size() - 1).getTime()), (itemArrayList.get(itemArrayList.size() - 1).getTime()), false);
        } else {
            // getBowlingLeaderboard(Long.valueOf(this.baseResponse.getPage().getNextPage()), Long.valueOf(this.baseResponse.getPage().getDatetime()), false);
        }
    }

    private void emptyViewVisibility(boolean b) {
        if (b) {
            this.viewEmpty.setVisibility(View.VISIBLE);
//            this.ivImage.setImageResource(R.drawable.leaderboard_blankstate);
            this.tvTitle.setText(R.string.commentary_empty);
            this.tvDetail.setVisibility(View.GONE);
            return;
        }
        this.viewEmpty.setVisibility(View.GONE);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (this.initSpinListener) {
            this.initSpinListener = false;
        } else if (this.batsman) {
            System.out.println("On iTem select");
            getBattingLeaderboard(null, null, true);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
