package realmstudy.tournament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import realmstudy.extras.AppConstants;
import realmstudy.extras.Utils;
import realmstudy.tournament.StaisticsData;


/**
 * Created by developer on 8/11/17.
 */


public class StatisticsFragment extends Fragment {

    private final String android_version_names[] = {
            "Donut",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Ice Cream Sandwich",
            "Jelly Bean",
            "KitKat",
            "Lollipop",
            "Marshmallow"
    };

    private final String android_image_urls[] = {
            "13", "20", "33", "453", "132", "86",
            "978", "55",
            "1", "88"
    };
    private StatisticsAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<StaisticsData> itemArrayList = new ArrayList<>();
    private DataSnapshot baseResponse;
    private String tourID;
    private ProgressBar progressBar;
    private ViewGroup viewEmpty;
    private TextView tvTitle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statistics_list, container, false);
        tourID = getArguments().getString(AppConstants.TourID);

        initViews(v);
        return v;
    }

    private void initViews(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
     tvTitle= (TextView) v.findViewById(R.id.tvTitle);
        viewEmpty = (ViewGroup) v.findViewById(R.id.viewEmpty);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        getBattingLeaderboard();
//        ArrayList<StaisticsData> androidVersions = prepareData();
//         adapter = new StatisticsAdapter(getActivity(), androidVersions);
//        recyclerView.setAdapter(adapter);

    }

    private ArrayList<StaisticsData> prepareData() {

        ArrayList<StaisticsData> android_version = new ArrayList<>();
//        for (int i = 0; i < android_version_names.length; i++) {
//            StaisticsData androidVersion = new StaisticsData();
//            androidVersion.setTitle(android_version_names[i]);
//            androidVersion.setCount(android_image_urls[i]);
//            android_version.add(androidVersion);
//        }
        return android_version;

    }

    public void getBattingLeaderboard() {

        // emptyViewVisibility(false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tournament_new/statistics/" + tourID);
        System.out.println("statisticstour" + tourID);
        final String TAG = "Statistics itemArrayList";

        Query queryRef;
//if(page==null)
//        queryRef = myRef
//                .orderByKey()
//                .limitToFirst(10);
        queryRef = myRef
                .orderByKey()
                .limitToFirst(25);
//else
//    queryRef=myRef.orderByChild("position").endAt("197").limitToFirst(10);

        ValueEventListener valueEventListener = (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever itemArrayList at this location is updated.
                progressBar.setVisibility(View.GONE);
//                Log.d(TAG, "Value is: Leader" + dataSnapshot.getValue().toString());
                if (getActivity() != null) {
                    if (!dataSnapshot.exists()) {
                        //      StatisticsFragment.this.loadmore = true;
                        if (StatisticsFragment.this.adapter != null) {
                            //  StatisticsFragment.this.adapter.loadMoreFail();
                        }
                        if (StatisticsFragment.this.itemArrayList.size() <= 0) {
                            System.out.println("StatisticsDAtant");
                            StatisticsFragment.this.emptyViewVisibility(true);
                            StatisticsFragment.this.recyclerView.setVisibility(View.GONE);
                            return;
                        }
                        return;
                    }
                    StatisticsFragment.this.baseResponse = dataSnapshot;
                    ArrayList<StaisticsData> arrayList = new ArrayList<>();

                    for (DataSnapshot md : dataSnapshot.getChildren()) {
                        System.out.println("data came" + md.getValue().toString());

                        if (md.getValue() != null && !md.getValue().equals("")) {
                            StaisticsData StaisticsData = md.getValue(StaisticsData.class);
                            if (itemArrayList.size() > 0 && itemArrayList.get(itemArrayList.size() - 1).getTitle() == StaisticsData.getTitle()) {

                                System.out.println("datatacccc");
                            } else {

                                itemArrayList.add(StaisticsData);
                                arrayList.add(StaisticsData);
                            }
                            //   itemArrayList.add(arrayList.get(i));
                        }
                    }
                    //   itemArrayList.addAll(Arrays.asList(Utils.fromJson(Utils.toString(arrayList),StaisticsData[].class)));
                    //    System.out.println("datavvv  "+ (pasu.vadivasal.android.Utils.toString(datav)));
                    // ArrayList<StaisticsData> datav = new ArrayList<StaisticsData>(sh.values());
                    System.out.println("datavvvf  " + TAG + (Utils.toString(arrayList.get(0))));

                    if (StatisticsFragment.this.adapter == null) {
                        System.out.println("NEW ADAPTER SETbat");
                        //StatisticsFragment.this.itemArrayList.addAll(arrayList);
                        StatisticsFragment.this.adapter = new StatisticsAdapter(StatisticsFragment.this.getActivity(), StatisticsFragment.this.itemArrayList);
                        StatisticsFragment.this.recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                        StatisticsFragment.this.recyclerView.setLayoutManager(layoutManager);

                        StatisticsFragment.this.recyclerView.setAdapter(StatisticsFragment.this.adapter);
                        // StatisticsFragment.this.recyclerView.addOnItemTouchListener(new C13401());

                        System.out.println("datavvv  ss" + adapter.getItemCount() + "__" + (arrayList.size() % 10 != 0));
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

    private void emptyViewVisibility(boolean b) {
        if (b) {
            viewEmpty.setVisibility(View.VISIBLE);
            tvTitle.setText(getString(R.string.statistics_empty));
        } else {
            viewEmpty.setVisibility(View.GONE);
        }

    }
}
    