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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import realmstudy.R;
import realmstudy.base.BaseQuickAdapter;
import realmstudy.extras.AppConstants;
import realmstudy.extras.Utils;
import realmstudy.view.TextView;

public class LeaderBoardListFragment extends Fragment implements BaseQuickAdapter.RequestLoadMoreListener, OnItemSelectedListener {
    private LeaderBoardAdapter adapter;
    int ballType = -1;
    private boolean batsman=true;
    private List<String> filterList;
    private List<String> filterListCode;
    private boolean initSpinListener = true;
    private ArrayList<LeaderBoardModel> itemArrayList;
    private boolean loadmore;
    String tournamentId;
    private RecyclerView rvMatches;
    ViewGroup viewEmpty;
    TextView tvDetail;
private ProgressBar progressBar;
    private DataSnapshot baseResponse;
    private String TOUR_ID="tour2";

    class C13422 implements Runnable {
        C13422() {
        }

        public void run() {
            LeaderBoardListFragment.this.adapter.loadMoreEnd();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player_matches, container, false);
        this.tournamentId =getActivity().getIntent().getStringExtra(AppConstants.TourID);
        this.batsman = getArguments().getBoolean("batsman", false);
        progressBar= (ProgressBar) rootView.findViewById(R.id.progressBar);
        viewEmpty= (ViewGroup) rootView.findViewById(R.id.viewEmpty);
        tvDetail= (TextView) rootView.findViewById(R.id.tvDetail);
        itemArrayList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            itemArrayList.add(new LeaderBoardModel());
//
//        }
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
//        this.spinnerFilter.setVisibility(View.VISIBLE);


 //       LeaderBoardListFragment.this.adapter = new LeaderBoardAdapter(LeaderBoardListFragment.this.getActivity(), R.layout.raw_leaderboard, LeaderBoardListFragment.this.itemArrayList, true);



   //     rvMatches.setAdapter(adapter);


//        this.spinnerFilter.setOnItemSelectedListener(this);
        if (this.batsman) {
//            this.filterList = Arrays.asList(getResources().getStringArray(R.array.arrayBattingFilter));
//            this.filterListCode = Arrays.asList(getResources().getStringArray(R.array.arrayBattingFilterCode));
//            setSpinAdapter();
            System.out.println("Setdata");
            getBattingLeaderboard(null, null, false);
            return;
        }
//        this.filterList = Arrays.asList(getResources().getStringArray(R.array.arrayBowlingFilter));
//        this.filterListCode = Arrays.asList(getResources().getStringArray(R.array.arrayBowlingFilterCode));
//        setSpinAdapter();
        getBowlingLeaderboard(null, null, false);
    }

//    private void setSpinAdapter() {
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter(getContext(), R.layout.raw_spinner_item_chart, this.filterList);
//        dataAdapter.setDropDownViewResource(R.layout.raw_simple_spinner_dropdown_item);
//        this.spinnerFilter.setAdapter(dataAdapter);
//    }

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
        DatabaseReference myRef = database.getReference("tournament_new/bull/"+tournamentId);
        final String TAG = "Leaderboard itemArrayList";

        Query queryRef;
        System.out.println(TAG+"postionnnn"+page+"__"+tournamentId);
if(page==null)
        queryRef = myRef
                .orderByChild("position")
                .limitToFirst(10);
else {
    System.out.println("ppppp  "+itemArrayList.get(itemArrayList.size()-1).getPosition());
    queryRef = myRef.orderByChild("position").startAt(itemArrayList.get(itemArrayList.size() - 1).getPosition()+1).limitToFirst(10);
}
        ValueEventListener valueEventListener=(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever itemArrayList at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
                progressBar.setVisibility(View.GONE);
//                Log.d(TAG, "Value is: Leader" + dataSnapshot.getValue().toString());


                //  CommentaryDataList datav = dataSnapshot.getValue(CommentaryDataList.class);
//                datav.setTournamentDatas( dataSnapshot.getRef());
//                datav.setBull(plarray);
//                datav.setPlayer(blarray);
//                datav.setLatestVideos(vlarray);

                if (getActivity() != null) {
//                    rvDashboard.setLayoutManager(new LinearLayoutManager(getContext()));
//                    rvDashboard.setAdapter(new CommentaryRecyclerAdapter(new ArrayList<CommentaryData>(Arrays.asList(Utils.fromJson(Utils.toString(datav),CommentaryData[].class))),getActivity()));
                    if (!dataSnapshot.exists()) {
                        LeaderBoardListFragment.this.loadmore = true;
                        if (LeaderBoardListFragment.this.adapter != null) {
                            LeaderBoardListFragment.this.adapter.loadMoreFail();
                        }
                        if (LeaderBoardListFragment.this.itemArrayList.size() <= 0) {
                            LeaderBoardListFragment.this.emptyViewVisibility(true);
                            LeaderBoardListFragment.this.rvMatches.setVisibility(View.GONE);
                            return;
                        }
                        return;
                    }
                    LeaderBoardListFragment.this.baseResponse = dataSnapshot;
               //     ArrayList<LeaderBoardModel> arrayLists = new ArrayList<>(((HashMap<String, LeaderBoardModel>) dataSnapshot.getValue()).values());
               ArrayList<LeaderBoardModel> arrayList=new ArrayList<>();

                    for (DataSnapshot md : dataSnapshot.getChildren()) {
                        System.out.println(TAG+"data came"+md.getValue().toString());

                        if (md.getValue() != null && !md.getValue().equals("")) {
                            LeaderBoardModel leaderBoardModel= md.getValue(LeaderBoardModel.class);
                            if (itemArrayList.size() > 0 && itemArrayList.get(itemArrayList.size() - 1).getPosition() == leaderBoardModel.getPosition()) {

                                System.out.println(TAG+"datatacccc"+leaderBoardModel.getPosition()+"__"+leaderBoardModel.getPlayerId());
                            } else {

                            itemArrayList.add(leaderBoardModel);
                            arrayList.add(leaderBoardModel);}
                         //   itemArrayList.add(arrayList.get(i));
                        }
                    }
//                for(int i=0;i<arrayList.size();i++){
//                    System.out.println("cameaddbat"+i+"____"+arrayList.get(i).getPosition());
//                    itemArrayList.add(arrayList.get(i));
//                }
                 //   itemArrayList.addAll(Arrays.asList(Utils.fromJson(Utils.toString(arrayList),LeaderBoardModel[].class)));
                    //    System.out.println("datavvv  "+ (pasu.vadivasal.android.Utils.toString(datav)));
                    // ArrayList<CommentaryData> datav = new ArrayList<CommentaryData>(sh.values());
//                    System.out.println("datavvvf  "+TAG + (pasu.vadivasal.android.Utils.toString(arrayList.get(0))));

                    if (LeaderBoardListFragment.this.adapter == null) {
                        System.out.println("NEW ADAPTER SETbat");
                        //LeaderBoardListFragment.this.itemArrayList.addAll(arrayList);
                        LeaderBoardListFragment.this.adapter = new LeaderBoardAdapter(LeaderBoardListFragment.this.getActivity(), R.layout.raw_leaderboard, LeaderBoardListFragment.this.itemArrayList, true);
                        LeaderBoardListFragment.this.adapter.setEnableLoadMore(true);
                        LeaderBoardListFragment.this.rvMatches.setAdapter(LeaderBoardListFragment.this.adapter);
                        // LeaderBoardListFragment.this.rvMatches.addOnItemTouchListener(new C13401());
                        LeaderBoardListFragment.this.adapter.setOnLoadMoreListener(LeaderBoardListFragment.this, LeaderBoardListFragment.this.rvMatches);

                        System.out.println("datavvv  ss" + adapter.getData().size()+"__"+(arrayList.size() % 10 != 0));
                        if (arrayList.size() % 10 != 0) {
                            LeaderBoardListFragment.this.adapter.loadMoreEnd();
                        }
                    } else {
                        if (refresh) {
                            LeaderBoardListFragment.this.adapter.getData().clear();
                            LeaderBoardListFragment.this.itemArrayList.clear();
                            LeaderBoardListFragment.this.itemArrayList.addAll(arrayList);
                            LeaderBoardListFragment.this.adapter.setNewData(arrayList);
                            LeaderBoardListFragment.this.adapter.setEnableLoadMore(true);
                            LeaderBoardListFragment.this.rvMatches.scrollToPosition(0);
                        } else {
                            LeaderBoardListFragment.this.adapter.addData((Collection) arrayList);
                            LeaderBoardListFragment.this.adapter.loadMoreComplete();
                        }
                        if (LeaderBoardListFragment.this.baseResponse != null &&   arrayList.size() % 10 != 0) {
                            LeaderBoardListFragment.this.adapter.loadMoreEnd();
                        }
                    }
                    LeaderBoardListFragment.this.loadmore = true;
                if (LeaderBoardListFragment.this.itemArrayList.size() == 0) {
                    LeaderBoardListFragment.this.emptyViewVisibility(true);
                }

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });



        queryRef.addListenerForSingleValueEvent(valueEventListener);

//        ApiCallManager.enqueue("get_bat_leader_board", CricHeroes.apiClient.getBattingLeaderboard(Utils.udid(getActivity()), CricHeroes.getApp().getAccessToken(), this.tournamentId, -1, this.ballType, -1, (String) this.filterListCode.get(this.spinnerFilter.getSelectedItemPosition()), page, datetime), new CallbackAdapter() {
//
//            class C13401 extends OnItemClickListener {
//                C13401() {
//                }
//
//                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int i) {
//                }
//            }
//
//            public void onApiResponse(ErrorResponse err, BaseResponse response) {
//                LeaderBoardListFragment.this.progressBar.setVisibility(View.GONE);
//                LeaderBoardListFragment.this.rvMatches.setVisibility(View.VISIBLE);
//                if (err != null) {
//                    LeaderBoardListFragment.this.loadmore = true;
//                    Logger.m176d("getBattingLeaderboard err " + err);
//                    if (LeaderBoardListFragment.this.adapter != null) {
//                        LeaderBoardListFragment.this.adapter.loadMoreFail();
//                    }
//                    if (LeaderBoardListFragment.this.itemArrayList.size() <= 0) {
//                        LeaderBoardListFragment.this.emptyViewVisibility(true);
//                        LeaderBoardListFragment.this.rvMatches.setVisibility(View.GONE);
//                        return;
//                    }
//                    return;
//                }
//                LeaderBoardListFragment.this.baseResponse = response;
//                ArrayList<LeaderBoardModel> arrayList = new ArrayList();
//                JsonArray json = (JsonArray) response.getData();
//                try {
//                    JSONArray jsonArray = new JSONArray(json.toString());
//                    Logger.m176d("getBattingLeaderboard " + json);
//                    String strDevider = "<font color='#cccccc'>&#160&#160 | &#160&#160</font>";
//                    if (jsonArray != null) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            String str;
//                            String strDetail = "";
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
//                            leaderBoardModel.setPlayerId(jsonObject.optInt("player_id"));
//                            leaderBoardModel.setName(jsonObject.optString("name"));
//                            leaderBoardModel.setProfilePhoto(jsonObject.optString(UpdateUserProfile.PROFILE_PHOTO));
//                            leaderBoardModel.setTotalMatch(jsonObject.optString("total_match"));
//                            leaderBoardModel.setTotalInnings(jsonObject.optString("innings"));
//                            leaderBoardModel.setTotalRuns(jsonObject.optString("total_runs"));
//                            leaderBoardModel.setHighestRun(jsonObject.optString("highest_run"));
//                            leaderBoardModel.setAverage(jsonObject.optString("average"));
//                            leaderBoardModel.setStrikeRate(jsonObject.optString("strike_rate"));
//                            leaderBoardModel.setNotOut(jsonObject.optString("not_out"));
//                            leaderBoardModel.setSixes(jsonObject.optString("6s"));
//                            leaderBoardModel.setFours(jsonObject.optString("4s"));
//                            leaderBoardModel.setFifties(jsonObject.optString("50s"));
//                            leaderBoardModel.setCenturies(jsonObject.optString("100s"));
//                            leaderBoardModel.setTeamName(jsonObject.optString(AppConstants.EXTRA_TEAM_NAME));
//                            String grayColor = "#72797F";
//                            String darkGrayColor = "#000000";
//                            StringBuilder append = new StringBuilder().append(("<font color='" + grayColor + "'> Mat: 0" + leaderBoardModel.getTotalMatch() + "</font>" + strDevider) + "<font color='" + grayColor + "'> Inn: 0" + leaderBoardModel.getTotalInnings() + "</font>" + strDevider).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 0) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            append = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("R: " + leaderBoardModel.getTotalRuns(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 0)).append("</font>").append(strDevider).toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 1) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            append = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("HS: " + leaderBoardModel.MediaghestRun(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 1)).append("</font><BR/>").toString() + "<font color='" + grayColor + "'> N/O: " + leaderBoardModel.getNotOut() + "</font>" + strDevider).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 3) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            append = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("Avg: " + leaderBoardModel.getAverage(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 3)).append("</font>").append(strDevider).toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 2) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            append = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("SR: " + leaderBoardModel.getStrikeRate(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 2)).append("</font>").append(strDevider).toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 4) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            append = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("6s: " + leaderBoardModel.getSixes(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 4)).append("</font><BR/>").toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 5) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            append = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("4s: " + leaderBoardModel.getFours(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 5)).append("</font>").append(strDevider).toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 6) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            StringBuilder append2 = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("50s: " + leaderBoardModel.getFifties(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 6)).append("</font>").append(strDevider).toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() != 7) {
//                                darkGrayColor = grayColor;
//                            }
//                            leaderBoardModel.setDetail(append2.append(darkGrayColor).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("100s: " + leaderBoardModel.getCenturies(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 7)).append("</font>").toString());
//                            arrayList.add(leaderBoardModel);
//                        }
//                        if (LeaderBoardListFragment.this.adapter == null) {
//                            System.out.println("NEW ADAPTER SET");
//                            LeaderBoardListFragment.this.itemArrayList.addAll(arrayList);
//                            LeaderBoardListFragment.this.adapter = new LeaderBoardAdapter(LeaderBoardListFragment.this.getActivity(), R.layout.raw_leaderboard, LeaderBoardListFragment.this.itemArrayList, true);
//                            LeaderBoardListFragment.this.adapter.setEnableLoadMore(true);
//                            LeaderBoardListFragment.this.rvMatches.setAdapter(LeaderBoardListFragment.this.adapter);
//                            LeaderBoardListFragment.this.rvMatches.addOnItemTouchListener(new C13401());
//                            LeaderBoardListFragment.this.adapter.setOnLoadMoreListener(LeaderBoardListFragment.this, LeaderBoardListFragment.this.rvMatches);
//                            if (!(LeaderBoardListFragment.this.baseResponse == null || LeaderBoardListFragment.this.baseResponse.hasPage())) {
//                                LeaderBoardListFragment.this.adapter.loadMoreEnd();
//                            }
//                        } else {
//                            if (refresh) {
//                                LeaderBoardListFragment.this.adapter.getData().clear();
//                                LeaderBoardListFragment.this.itemArrayList.clear();
//                                LeaderBoardListFragment.this.itemArrayList.addAll(arrayList);
//                                LeaderBoardListFragment.this.adapter.setNewData(arrayList);
//                                LeaderBoardListFragment.this.adapter.setEnableLoadMore(true);
//                                LeaderBoardListFragment.this.rvMatches.scrollToPosition(0);
//                            } else {
//                                LeaderBoardListFragment.this.adapter.addData((Collection) arrayList);
//                                LeaderBoardListFragment.this.adapter.loadMoreComplete();
//                            }
//                            if (LeaderBoardListFragment.this.baseResponse != null && LeaderBoardListFragment.this.baseResponse.hasPage() && LeaderBoardListFragment.this.baseResponse.getPage().getNextPage() == 0) {
//                                LeaderBoardListFragment.this.adapter.loadMoreEnd();
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                LeaderBoardListFragment.this.loadmore = true;
//                if (LeaderBoardListFragment.this.itemArrayList.size() == 0) {
//                    LeaderBoardListFragment.this.emptyViewVisibility(true);
//                }
//            }
//        });
    }

    private String MediaghlightedText(String text, boolean isHighlight) {
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
        System.out.println("onLoadMoreRequested"+this.loadmore +"___"+(itemArrayList.size() % 10 != 0));
        if (!this.loadmore  || (itemArrayList.size() % 10 != 0)) {
            new Handler().postDelayed(new C13422(), 1500);
        } else if (this.batsman) {
            System.out.println("Load more");
            getBattingLeaderboard(String.valueOf(itemArrayList.get(itemArrayList.size() - 1).getPlayerId()), null, false);
        } else {
           // getBowlingLeaderboard(Long.valueOf(this.baseResponse.getPage().getNextPage()), Long.valueOf(this.baseResponse.getPage().getDatetime()), false);
        }
    }

    public void getBowlingLeaderboard(Long page, Long datetime, final boolean refresh) {
      //  this.loadmore = false;
        if (!this.loadmore) {
            this.progressBar.setVisibility(View.VISIBLE);
        }
        this.loadmore = false;
        emptyViewVisibility(false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tournament_new/player/"+tournamentId);
        final String TAG = "Leaderboard itemArrayList";

        Query queryRef;

        queryRef = myRef.orderByChild("position")
                .limitToFirst(10);
        ValueEventListener valueEventListener=(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever itemArrayList at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
                progressBar.setVisibility(View.GONE);
//                Log.d(TAG, "Value is: Leader" + dataSnapshot.getValue().toString());


                //  CommentaryDataList datav = dataSnapshot.getValue(CommentaryDataList.class);
//                datav.setTournamentDatas( dataSnapshot.getRef());
//                datav.setBull(plarray);
//                datav.setPlayer(blarray);
//                datav.setLatestVideos(vlarray);

                if (getActivity() != null) {
//                    rvDashboard.setLayoutManager(new LinearLayoutManager(getContext()));
//                    rvDashboard.setAdapter(new CommentaryRecyclerAdapter(new ArrayList<CommentaryData>(Arrays.asList(Utils.fromJson(Utils.toString(datav),CommentaryData[].class))),getActivity()));
                    if (!dataSnapshot.exists()) {
                        LeaderBoardListFragment.this.loadmore = true;
                        if (LeaderBoardListFragment.this.adapter != null) {
                            LeaderBoardListFragment.this.adapter.loadMoreFail();
                        }
                        if (LeaderBoardListFragment.this.itemArrayList.size() <= 0) {
                            LeaderBoardListFragment.this.emptyViewVisibility(true);
                            LeaderBoardListFragment.this.rvMatches.setVisibility(View.GONE);
                            return;
                        }
                        return;
                    }
                    LeaderBoardListFragment.this.baseResponse = dataSnapshot;
                    ArrayList<LeaderBoardModel> arrayList= new ArrayList<>();
                    GenericTypeIndicator<ArrayList<LeaderBoardModel>> t = new GenericTypeIndicator<ArrayList<LeaderBoardModel>>() {};
                   // ArrayList<LeaderBoardModel> arrayList = dataSnapshot.getValue(t);
                    for (DataSnapshot md : dataSnapshot.getChildren()) {
                        System.out.println("data came"+md.getValue().toString());
                        if (md.getValue() != null && !md.getValue().equals("")) {
arrayList.add( md.getValue(LeaderBoardModel.class));
                        }
                    }

//                    ArrayList<LeaderBoardModel> arrayList = new ArrayList<>(((HashMap<String, LeaderBoardModel>) dataSnapshot.getValue()).values());
                   // ArrayList<LeaderBoardModel> arrayList=new ArrayList<>(Arrays.asList(Utils.fromJson(Utils.toString(arrayLists),LeaderBoardModel[].class)));
                    for(int i=0;i<arrayList.size();i++){
                        System.out.println("cameadd"+i);
                        itemArrayList.add(arrayList.get(i));
                    }
                    //   itemArrayList.addAll(Arrays.asList(Utils.fromJson(Utils.toString(arrayList),LeaderBoardModel[].class)));
                    //    System.out.println("datavvv  "+ (pasu.vadivasal.android.Utils.toString(datav)));
                    // ArrayList<CommentaryData> datav = new ArrayList<CommentaryData>(sh.values());
                    System.out.println("datavvv  "+TAG + (Utils.toString(arrayList.get(0))));

                    if (LeaderBoardListFragment.this.adapter == null) {
                        System.out.println("NEW ADAPTER SET");
                        //LeaderBoardListFragment.this.itemArrayList.addAll(arrayList);
                        LeaderBoardListFragment.this.adapter = new LeaderBoardAdapter(LeaderBoardListFragment.this.getActivity(), R.layout.raw_leaderboard, LeaderBoardListFragment.this.itemArrayList, true);
                        LeaderBoardListFragment.this.adapter.setEnableLoadMore(true);
                        LeaderBoardListFragment.this.rvMatches.setAdapter(LeaderBoardListFragment.this.adapter);
                        // LeaderBoardListFragment.this.rvMatches.addOnItemTouchListener(new C13401());
                        LeaderBoardListFragment.this.adapter.setOnLoadMoreListener(LeaderBoardListFragment.this, LeaderBoardListFragment.this.rvMatches);

                        System.out.println("datavvv  ss" + adapter.getData().size());
                        if (arrayList.size() % 10 != 0) {
                            LeaderBoardListFragment.this.adapter.loadMoreEnd();
                        }
                    } else {
                        if (refresh) {
                            LeaderBoardListFragment.this.adapter.getData().clear();
                            LeaderBoardListFragment.this.itemArrayList.clear();
                            LeaderBoardListFragment.this.itemArrayList.addAll(arrayList);
                            LeaderBoardListFragment.this.adapter.setNewData(arrayList);
                            LeaderBoardListFragment.this.adapter.setEnableLoadMore(true);
                            LeaderBoardListFragment.this.rvMatches.scrollToPosition(0);
                        } else {
                            LeaderBoardListFragment.this.adapter.addData((Collection) arrayList);
                            LeaderBoardListFragment.this.adapter.loadMoreComplete();
                        }
                        System.out.println("arraycomplete"+(arrayList.size() % 10));
                        if ( (arrayList.size() % 10) != 0) {

                            LeaderBoardListFragment.this.adapter.loadMoreEnd();
                        }
                    }
                    LeaderBoardListFragment.this.loadmore = false;
                    if (LeaderBoardListFragment.this.itemArrayList.size() == 0) {
                        LeaderBoardListFragment.this.emptyViewVisibility(true);
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });



        queryRef.addListenerForSingleValueEvent(valueEventListener);

    }
//        if (!this.loadmore) {
//            this.progressBar.setVisibility(View.VISIBLE);
//        }

    //  emptyViewVisibility(false);
//        ApiCallManager.enqueue("get_bowl_leader_board", CricHeroes.apiClient.getBowlingLeaderboard(Utils.udid(getActivity()), CricHeroes.getApp().getAccessToken(), this.tournamentId, -1, this.ballType, -1, (String) this.filterListCode.get(this.spinnerFilter.getSelectedItemPosition()), page, datetime), new CallbackAdapter() {
//
//            class C13431 extends OnItemClickListener {
//                C13431() {
//                }
//
//                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int i) {
//                }
//            }
//
//            public void onApiResponse(ErrorResponse err, BaseResponse response) {
//                LeaderBoardListFragment.this.progressBar.setVisibility(View.GONE);
//                LeaderBoardListFragment.this.rvMatches.setVisibility(View.VISIBLE);
//                if (err != null) {
//                    LeaderBoardListFragment.this.loadmore = true;
//                    Logger.m176d("getBowlingLeaderboard err " + err);
//                    if (LeaderBoardListFragment.this.adapter != null) {
//                        LeaderBoardListFragment.this.adapter.loadMoreFail();
//                    }
//                    if (LeaderBoardListFragment.this.itemArrayList.size() <= 0) {
//                        LeaderBoardListFragment.this.emptyViewVisibility(true);
//                        LeaderBoardListFragment.this.rvMatches.setVisibility(View.GONE);
//                        return;
//                    }
//                    return;
//                }
//                LeaderBoardListFragment.this.baseResponse = response;
//                ArrayList<LeaderBoardModel> arrayList = new ArrayList();
//                JsonArray json = (JsonArray) response.getData();
//                try {
//                    JSONArray jsonArray = new JSONArray(json.toString());
//                    Logger.m176d("getBowlingLeaderboard " + json);
//                    String strDevider = "<font color='#cccccc'>&#160&#160 | &#160&#160</font>";
//                    if (jsonArray != null) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            String str;
//                            String strDetail = "";
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
//                            leaderBoardModel.setPlayerId(jsonObject.optInt("player_id"));
//                            leaderBoardModel.setName(jsonObject.optString("name"));
//                            leaderBoardModel.setProfilePhoto(jsonObject.optString(UpdateUserProfile.PROFILE_PHOTO));
//                            leaderBoardModel.setTotalMatch(jsonObject.optString("total_match"));
//                            leaderBoardModel.setTotalInnings(jsonObject.optString("innings"));
//                            leaderBoardModel.setAverage(jsonObject.optString("avg"));
//                            leaderBoardModel.setMaiden(jsonObject.optString("maidens"));
//                            leaderBoardModel.setTotalWickets(jsonObject.optString("total_wickets"));
//                            leaderBoardModel.setBalls(jsonObject.optString("balls"));
//                            leaderBoardModel.setHighestWicket(jsonObject.optString("highest_wicket"));
//                            leaderBoardModel.setEconomy(jsonObject.optString("economy"));
//                            leaderBoardModel.setStrikeRate(jsonObject.optString("SR"));
//                            leaderBoardModel.setTeamName(jsonObject.optString(AppConstants.EXTRA_TEAM_NAME));
//                            String grayColor = "#72797F";
//                            String darkGrayColor = "#2A373F";
//                            StringBuilder append = new StringBuilder().append(("<font color='" + grayColor + "'>Mat: " + leaderBoardModel.getTotalMatch() + "</font>" + strDevider) + "<font color='" + grayColor + "'>Inn: " + leaderBoardModel.getTotalInnings() + "</font>" + strDevider).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 0) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            append = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("W: " + leaderBoardModel.getTotalWickets(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 0)).append("</font>").append(strDevider).toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 5) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            append = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("M: " + leaderBoardModel.getMaiden(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 5)).append("</font>").append(strDevider).toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 1) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            append = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("Avg: " + leaderBoardModel.getAverage(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 1)).append("</font><BR/>").toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 4) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            append = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("HW: " + leaderBoardModel.MediaghestWicket(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 4)).append("</font>").append(strDevider).toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 2) {
//                                str = darkGrayColor;
//                            } else {
//                                str = grayColor;
//                            }
//                            StringBuilder append2 = new StringBuilder().append(append.append(str).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("Eco: " + leaderBoardModel.getEconomy(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 2)).append("</font>").append(strDevider).toString()).append("<font color='");
//                            if (LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() != 3) {
//                                darkGrayColor = grayColor;
//                            }
//                            leaderBoardModel.setDetail(append2.append(darkGrayColor).append("'>").append(LeaderBoardListFragment.this.MediaghlightedText("SR: " + leaderBoardModel.getStrikeRate(), LeaderBoardListFragment.this.spinnerFilter.getSelectedItemPosition() == 3)).append("</font>").toString());
//                            arrayList.add(leaderBoardModel);
//                        }
//                        if (LeaderBoardListFragment.this.adapter == null) {
//                            LeaderBoardListFragment.this.itemArrayList.addAll(arrayList);
//                            LeaderBoardListFragment.this.adapter = new LeaderBoardAdapter(LeaderBoardListFragment.this.getActivity(), R.layout.raw_leaderboard, LeaderBoardListFragment.this.itemArrayList, true);
//                            LeaderBoardListFragment.this.adapter.setEnableLoadMore(true);
//                            LeaderBoardListFragment.this.rvMatches.setAdapter(LeaderBoardListFragment.this.adapter);
//                            LeaderBoardListFragment.this.rvMatches.addOnItemTouchListener(new C13431());
//                            LeaderBoardListFragment.this.adapter.setOnLoadMoreListener(LeaderBoardListFragment.this, LeaderBoardListFragment.this.rvMatches);
//                            if (!(LeaderBoardListFragment.this.baseResponse == null || LeaderBoardListFragment.this.baseResponse.hasPage())) {
//                                LeaderBoardListFragment.this.adapter.loadMoreEnd();
//                            }
//                        } else {
//                            if (refresh) {
//                                LeaderBoardListFragment.this.adapter.getData().clear();
//                                LeaderBoardListFragment.this.itemArrayList.clear();
//                                LeaderBoardListFragment.this.itemArrayList.addAll(arrayList);
//                                LeaderBoardListFragment.this.adapter.setNewData(arrayList);
//                                LeaderBoardListFragment.this.adapter.setEnableLoadMore(true);
//                                LeaderBoardListFragment.this.rvMatches.scrollToPosition(0);
//                            } else {
//                                LeaderBoardListFragment.this.adapter.addData((Collection) arrayList);
//                                LeaderBoardListFragment.this.adapter.notifyDataSetChanged();
//                                LeaderBoardListFragment.this.adapter.loadMoreComplete();
//                            }
//                            if (LeaderBoardListFragment.this.baseResponse != null && LeaderBoardListFragment.this.baseResponse.hasPage() && LeaderBoardListFragment.this.baseResponse.getPage().getNextPage() == 0) {
//                                LeaderBoardListFragment.this.adapter.loadMoreEnd();
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                LeaderBoardListFragment.this.loadmore = true;
//                if (LeaderBoardListFragment.this.itemArrayList.size() == 0) {
//                    LeaderBoardListFragment.this.emptyViewVisibility(true);
//                }
//            }
////        });
//    }
//
    private void emptyViewVisibility(boolean b) {
        if (b) {
            this.viewEmpty.setVisibility(View.VISIBLE);
//            this.ivImage.setImageResource(R.drawable.leaderboard_blankstate);
//            this.tvTitle.setText(R.string.leaderbord_blank_stat);
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
        } else {
            getBowlingLeaderboard(null, null, true);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
