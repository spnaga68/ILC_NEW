package realmstudy.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.adapter.OverRvAdapter;
import realmstudy.data.CommanData;
import realmstudy.data.OverAdapterData;
import realmstudy.data.RealmObjectData.InningsData;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.data.ScoreBoardData;
import realmstudy.data.SessionSave;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.lib.Util;

import static android.R.attr.id;

/**
 * Created by developer on 17/4/17.
 */
public class OversFragment extends Fragment {
    //    private TextView
//            overs_home_team, over_home_score, overs_away, overs_away_score, overs_match_quote;
    private android.support.v7.widget.RecyclerView overs_rv;
    @Inject
    Realm realm;
    private MatchDetails md;
    int id;
    // private LinearLayout score_layy;
    private ScoreBoardFragment scoreBoardFragment;
    private ScoreBoardData current_score_data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.match_over_fragment_lay, container, false);
//        overs_home_team = (TextView) v.findViewById(R.id.overs_home_team);
//        over_home_score = (TextView) v.findViewById(R.id.over_home_score);
//        overs_away = (TextView) v.findViewById(R.id.overs_away);
//        overs_away_score = (TextView) v.findViewById(R.id.overs_away_score);
//        overs_match_quote = (TextView) v.findViewById(R.id.overs_match_quote);

        // score_layy=(LinearLayout)v.findViewById(R.id.score_layy);
        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
        scoreBoardFragment = new ScoreBoardFragment(getActivity());
        scoreBoardFragment.initialize(v);
        id = getArguments().getInt("match_id");
        md = RealmDB.getMatchById(getActivity(), realm, id);

        // current_score_data = CommanData.fromJson(d.getScoreBoardData(), ScoreBoardData.class);
        overs_rv = (android.support.v7.widget.RecyclerView) v.findViewById(R.id.overs_rv);
        overs_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        return v;
    }

    public void setData(List<OverAdapterData> datas,ScoreBoardData current_score_data ) {


        System.out.println("________sss" + datas.size());
        if(overs_rv!=null){
        OverRvAdapter adapter = new OverRvAdapter(getActivity(), datas);
        overs_rv.setAdapter(adapter);

        scoreBoardFragment.updateUI(current_score_data);}
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onResume() {
        super.onResume();


        scoreBoardFragment.showPreviousDelivery(false);
//        scoreBoardFragment.updateUI(current_score_data);

//        if (md != null) {
//            String firstInningsScore = RealmDB.getFirstInningsTotal(realm, md) + "/" + RealmDB.noOfWicket(getActivity(), realm, md.getMatch_id(), true) + "  (" + RealmDB.getFirstInningsOver(realm, md) + ")";
//            if (md.isFirstInningsCompleted()) {
//                String secInningsScore = RealmDB.getSecInningsTotal(realm, md) + "/" + RealmDB.noOfWicket(getActivity(), realm, md.getMatch_id(), false) + "  (" + RealmDB.getsecInningsOver(realm, md) + ")";
//                // overs_away_score.setText(secInningsScore);
//            }
////            overs_home_team.setText(md.getHomeTeam().nick_name);
////            over_home_score.setText(firstInningsScore);
////            overs_away.setText(md.getAwayTeam().nick_name);
//
//           // overs_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//
//            //  overs_match_quote.setText(md.);
//
//
//        }
    }


//    List<OverAdapterData> getData(int id, boolean isFirstInnings) {
//        //  float recOver = 0;
//        List<OverAdapterData> datas = new ArrayList<>();
//        RealmResults<InningsData> data = realm.where(InningsData.class).equalTo("match_id", id)
//                .equalTo("firstInnings", isFirstInnings).notEqualTo("delivery", 0).findAllSorted("delivery", Sort.DESCENDING);
//        // recOver = data.get(0).getOver();
//        Set<String> batsmans = new TreeSet<>();
//        Set<String> bowlers = new TreeSet<>();
//        int total_run = 0;
//        float epsilon = (float) 0.00000001;
//        ArrayList<String> deliveries = new ArrayList<>();
//
//        for (int i = 0; i < data.size(); i++) {
//            InningsData currentdata = data.get(i);
//            boolean isOverCompleted = false;
//            if (i != (data.size() - 1))
//                isOverCompleted = data.get(i + 1).isOversCompleted();
//            batsmans.add(RealmDB.getPlayer(realm, currentdata.getStriker()).getName());
//            batsmans.add(RealmDB.getPlayer(realm, currentdata.getNonStriker()).getName());
//            bowlers.add(RealmDB.getPlayer(realm, currentdata.getCurrentBowler()).getName());
//            deliveries.add(Util.get_delivery_result(currentdata.getRun()
//                    , currentdata.getWicket(), currentdata.isLegal(), currentdata.getBallType()));
//            total_run += currentdata.getRun();
//
//            System.out.println("_______****ssvv" + datas.size());
//
//            if (isOverCompleted || i == (data.size() - 1)) {
//
//                String batsmansString = "";
//                String bowlersString = "";
//
//                Iterator batsmansIterator = batsmans.iterator();
//                Iterator bowlersIterator = bowlers.iterator();
//                System.out.println("_______****ssve" + (currentdata.getOver() + 1));
//                System.out.println("_______****ssvd" + batsmansIterator.hasNext());
//                if (batsmans.size() > 0) {
//                    System.out.println("_______****ssvg" + batsmansIterator.hasNext());
//                    do {
//                        batsmansString += batsmansIterator.next() + " & ";
//                    } while (batsmansIterator.hasNext());
//                    do {
//                        bowlersString += bowlersIterator.next() + " & ";
//                    } while (bowlersIterator.hasNext());
//                    OverAdapterData overData = new OverAdapterData();
//                    overData.setBatsmans(batsmansString.substring(0, batsmansString.length() - 3));
//                    overData.setBolwers(bowlersString.substring(0, bowlersString.length() - 3));
//                    overData.setTotal_run(total_run);
//                    int curOver = currentdata.getOver() < 1 ? 1 : (int) Math.floor(currentdata.getOver() + 1);
//                    overData.setOver(curOver);
//                    ArrayList<String> dd = (ArrayList<String>) deliveries.clone();
//                    Collections.reverse(dd);
//                    overData.setDeliveries(dd);
//                    datas.add(overData);
//                    batsmans.clear();
//                    bowlers.clear();
//                    deliveries.clear();
//                    total_run = 0;
//                    System.out.println("_______****ssv" + datas.size());
//                }
//            }
//
//        }
//
//
//        return datas;
//    }
}
