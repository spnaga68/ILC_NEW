package realmstudy.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import realmstudy.MainActivity;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.adapter.ScorecardDetailAdapter;
import realmstudy.data.CommanData;
import realmstudy.data.DetailedScoreData;
import realmstudy.data.RealmObjectData.BatingProfile;
import realmstudy.data.RealmObjectData.BowlingProfile;
import realmstudy.data.RealmObjectData.InningsData;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.data.RealmObjectData.Wicket;
import realmstudy.data.ScoreBoardData;
import realmstudy.data.ScoreCardDetailData;
import realmstudy.data.SessionSave;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.extras.AnimatedExpandableListView;

import static realmstudy.data.CommanData.wicketIdToString;

/**
 * Created by developer on 2/5/17.
 */

public class ScorecardDetailFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    AnimatedExpandableListView listView;
    ScorecardDetailAdapter adapter;
    SwipeRefreshLayout swipeLayout;
    int addOnOddRefresh;
    ScoreCardDetailData homeData, awayData;
    ArrayList<ScoreCardDetailData> datas = new ArrayList<>();

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View convertview = inflater.inflate(R.layout.scorecard_detail, container, false);

        float recOver = 0;
//        if (md != null) {
//            if (md.isFirstInningsCompleted()) {
//                homeData = createScoreDetailData(true);
//                awayData = createScoreDetailData(false);
//                datas.add(homeData);
//                datas.add(awayData);
//            } else {
//                if (md.isHomeTeamBattingFirst()) {
//                    homeData = createScoreDetailData(true);
//                    datas.add(homeData);
//                } else {
//                    awayData = createScoreDetailData(false);
//                    datas.add(awayData);
//                }
//            }
//
//        }
        FloatingActionButton bb = (FloatingActionButton) convertview.findViewById(R.id.butt);
        swipeLayout = (SwipeRefreshLayout) convertview.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.RED);


//
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {


                if (addOnOddRefresh % 2 == 0) {
//
//

                    adapter.notifyDataSetChanged();
                }
                addOnOddRefresh += 1;
                swipeLayout.setRefreshing(false);
                swipeLayout.setTop(5000);
            }


        });


        //adapter.setData(items);
        listView = (AnimatedExpandableListView) convertview.findViewById(R.id.listView);


        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                int otherPosition = 0;
                if (groupPosition == 0)
                    otherPosition = 1;

                if (parent.isGroupExpanded(groupPosition)) {
                    ((AnimatedExpandableListView) parent).collapseGroupWithAnimation(groupPosition);

                } else {
                    ((AnimatedExpandableListView) parent).expandGroupWithAnimation(groupPosition);
                    if (parent.isGroupExpanded(otherPosition))
                        ((AnimatedExpandableListView) parent).collapseGroupWithAnimation(otherPosition);

                }
                return true;
            }

        });
        String d = SessionSave.getSession("checjjj", getActivity());
        datas = new ArrayList<>();
        datas.add(CommanData.fromJson(d, DetailedScoreData.class).getScoreCardDetailData());
        setDatas(datas);
        return convertview;
    }

    public void setDatas(ArrayList<ScoreCardDetailData> datas) {

        this.datas = datas;
        adapter = new ScorecardDetailAdapter(getActivity(), datas);
        listView.setAdapter(adapter);
    }
//    public String wicketToString(Wicket wicket) {
//        String s = "";
//        String as = "";
//        if (wicket.getType() == CommanData.W_CAUGHT)
//            as = RealmDB.getPlayer(realm, wicket.getCaughtBy()).getName();
//        else if (wicket.getType() == CommanData.W_RUNOUT)
//            as = RealmDB.getPlayer(realm, wicket.getRunoutBy()).getName();
//        else if(wicket.getType()!=CommanData.W_STUMPED)
//            as = RealmDB.getPlayer(realm, wicket.getBowler()).getName();
//        s += wicketIdToString(wicket.getType()) + " b " + as;
//        return s;
//    }

//    public ScoreCardDetailData createScoreDetailData(boolean forHomeTeam) {
//        ScoreCardDetailData scoreCardDetailData = new ScoreCardDetailData();
//
//        InningsData InningsData = null;
//        RealmResults<BatingProfile> battingProfiles = null;
//        RealmResults<BowlingProfile> bowlingProfiles = null;
//        RealmResults<InningsData> fow = null;
//        RealmResults<InningsData> extraTypes = null;
//        ScoreBoardData scData = null;
//        if (forHomeTeam) {
//            scoreCardDetailData.setTeamName(md.getHomeTeam().nick_name);
//            InningsData = realm.where(InningsData.class).equalTo("match_id", md.getMatch_id()).equalTo("firstInnings", md.isHomeTeamBattingFirst()).findAllSorted("delivery", Sort.DESCENDING).first();
//            battingProfiles = realm.where(BatingProfile.class).equalTo("match_id", md.getMatch_id()).equalTo("inFirstinnings", md.isHomeTeamBattingFirst())
//                    .notEqualTo("currentStatus", CommanData.StatusFree).notEqualTo("currentStatus", CommanData.StatusInMatch).findAllSorted("battedAt", Sort.ASCENDING);
//            bowlingProfiles = realm.where(BowlingProfile.class).equalTo("currentBowlerStatus",CommanData.StatusBowling).equalTo("match_id", md.getMatch_id()).equalTo("inFirstinnings", md.isHomeTeamBattingFirst()).findAll();
//            fow = realm.where(InningsData.class).equalTo("match_id", md.getMatch_id()).equalTo("firstInnings", md.isHomeTeamBattingFirst()).isNotNull("wicket").findAllSorted("delivery", Sort.ASCENDING);
//            extraTypes = realm.where(InningsData.class).equalTo("match_id", md.getMatch_id()).equalTo("firstInnings", md.isHomeTeamBattingFirst()).notEqualTo("ballType", 0).findAll();
//        } else {
//            System.out.println("naaaaaaa" + !md.isHomeTeamBattingFirst());
//            scoreCardDetailData.setTeamName(md.getAwayTeam().nick_name);
//            InningsData = realm.where(InningsData.class).equalTo("match_id", md.getMatch_id()).equalTo("firstInnings", !md.isHomeTeamBattingFirst()).findAllSorted("delivery", Sort.DESCENDING).first();
//            battingProfiles = realm.where(BatingProfile.class).equalTo("match_id", md.getMatch_id()).equalTo("inFirstinnings", !md.isHomeTeamBattingFirst())
//                    .notEqualTo("currentStatus", CommanData.StatusFree).notEqualTo("currentStatus", CommanData.StatusInMatch).findAllSorted("battedAt", Sort.ASCENDING);
//            bowlingProfiles = realm.where(BowlingProfile.class).equalTo("currentBowlerStatus",CommanData.StatusBowling).equalTo("match_id", md.getMatch_id()).equalTo("inFirstinnings", !md.isHomeTeamBattingFirst()).findAll();
//            System.out.println("naaaaaaa" + !md.isHomeTeamBattingFirst() + "__" + bowlingProfiles.size());
//            fow = realm.where(InningsData.class).equalTo("match_id", md.getMatch_id()).equalTo("firstInnings", !md.isHomeTeamBattingFirst()).isNotNull("wicket").findAllSorted("delivery", Sort.ASCENDING);
//            extraTypes = realm.where(InningsData.class).equalTo("match_id", md.getMatch_id()).equalTo("firstInnings", !md.isHomeTeamBattingFirst()).notEqualTo("ballType", 0).findAll();
//        }
//       // scData = CommanData.fromJson(InningsData.getScoreBoardData(), ScoreBoardData.class);
//        scoreCardDetailData.setTeamRun_over(scData.getTotalRuns() + "-" + scData.getTotal_wicket() + "(" + InningsData.getOver() + ")");
//        for (int i = 0; i < battingProfiles.size(); i++) {
//            ScoreCardDetailData.BatsmanDetail data = new ScoreCardDetailData.BatsmanDetail();
//            data.balls = battingProfiles.get(i).getBallFaced();
//            data.name = RealmDB.getPlayer(realm, battingProfiles.get(i).getPlayerID()).getName();
//            if (battingProfiles.get(i).getWicket() != null)
//                data.outAs = wicketToString(battingProfiles.get(i).getWicket());
//            else
//                data.outAs = getString(R.string.batting);
//            data.runs = battingProfiles.get(i).getRuns();
//            data.fours = battingProfiles.get(i).getFours();
//            data.sixes = battingProfiles.get(i).getSixes();
//            if (battingProfiles.get(i).getBallFaced() != 0) {
//                data.strike_rate = CommanData.getStrikeRate(battingProfiles.get(i).getBallFaced(), battingProfiles.get(i).getRuns());
//                System.out.println("_____hiii" + data.strike_rate);
//            }
//            scoreCardDetailData.addBatsmanDetails(data);
//
//        }
//
//
//        for (int i = 0; i < bowlingProfiles.size(); i++) {
//            ScoreCardDetailData.BowlersDetail data = new ScoreCardDetailData.BowlersDetail();
//            // System.out.println("eeee"+bowlingProfiles.get(i).getPlayerID());
//            data.name = RealmDB.getPlayer(realm, bowlingProfiles.get(i).getPlayerID()).getName();
//            data.outAs = String.valueOf(bowlingProfiles.get(i).getWickets().size());
//            data.runs = bowlingProfiles.get(i).getRunsGranted();
//            data.overs = bowlingProfiles.get(i).OversBowled();
//            data.maiden = bowlingProfiles.get(i).getMaiden();
//            data.ecnomic_rate = CommanData.getER(data.runs, data.overs);
//            scoreCardDetailData.addBowlersDetails(data);
//
//        }
//
//        for (int i = 0; i < fow.size(); i++) {
//            ScoreCardDetailData.FOW data = new ScoreCardDetailData.FOW();
//            data.name = RealmDB.getPlayer(realm, fow.get(i).getWicket().getBatsman()).getName();
//            data.score = (fow.get(i).getRun());
//            data.overs = fow.get(i).getDelivery();
//            scoreCardDetailData.addFow(data);
//
//        }
//        int wide = 0, lb = 0, b = 0, noball = 0;
//        for (int i = 0; i < extraTypes.size(); i++) {
//            if (extraTypes.get(i).getBallType() == CommanData.BALL_WIDE) {
//                wide += MainActivity.legalRun + extraTypes.get(i).getRun();
//            }
//            if (extraTypes.get(i).getBallType() == CommanData.BALL_NO_BALL || extraTypes.get(i).getBallType() == CommanData.BALL_NO_OVER_STEP)
//                noball += MainActivity.legalRun + extraTypes.get(i).getRun();
//            if (extraTypes.get(i).getBallType() == CommanData.BALL_LEGAL_BYES)
//                b += extraTypes.get(i).getRun();
//            if (extraTypes.get(i).getBallType() == CommanData.BALL_LB)
//                lb += extraTypes.get(i).getRun();
//
//
//        }
//        scoreCardDetailData.setTotal_extras(wide + lb + b + noball);
//        scoreCardDetailData.setExtras_detail("wd " + wide + ",b " + b + ",lb " + lb + ",nb " + noball);
//        if (InningsData.getDelivery() != 0)
//            scoreCardDetailData.setCurrent_run_rate(InningsData.getRun() / InningsData.getDelivery());
//        return scoreCardDetailData;
    // }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                swipeLayout.setRefreshing(false);

            }
        }, 5000);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

}