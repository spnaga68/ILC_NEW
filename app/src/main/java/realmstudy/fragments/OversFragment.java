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
import android.widget.TextView;

import java.util.ArrayList;
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
import realmstudy.data.OverAdapterData;
import realmstudy.data.RealmObjectData.InningsData;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.lib.Util;

/**
 * Created by developer on 17/4/17.
 */
public class OversFragment extends Fragment {
    private TextView
            overs_home_team, over_home_score, overs_away, overs_away_score, overs_match_quote;
    private android.support.v7.widget.RecyclerView overs_rv;
    @Inject
    Realm realm;
    private MatchDetails md;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.match_over_fragment_lay, container, false);
        overs_home_team = (TextView) v.findViewById(R.id.overs_home_team);
        over_home_score = (TextView) v.findViewById(R.id.over_home_score);
        overs_away = (TextView) v.findViewById(R.id.overs_away);
        overs_away_score = (TextView) v.findViewById(R.id.overs_away_score);
        overs_match_quote = (TextView) v.findViewById(R.id.overs_match_quote);
        overs_rv = (android.support.v7.widget.RecyclerView) v.findViewById(R.id.overs_rv);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onResume() {
        super.onResume();
       final int id = getArguments().getInt("match_id");
        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
        md = RealmDB.getMatchById(getActivity(), realm, id);
        overs_home_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<OverAdapterData> datas = getData(id, true);

//                List<OverAdapterData> sdatas = getData(id, false);
//                datas.addAll(sdatas);

                OverRvAdapter adapter = new OverRvAdapter(getActivity(), datas);
                overs_rv.setAdapter(adapter);
                overs_rv.getAdapter().notifyDataSetChanged();
            }
        });
        overs_away.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<OverAdapterData> datas = getData(id, false);

//                List<OverAdapterData> sdatas = getData(id, false);
//                datas.addAll(sdatas);

                OverRvAdapter adapter = new OverRvAdapter(getActivity(), datas);
                overs_rv.setAdapter(adapter);
                overs_rv.getAdapter().notifyDataSetChanged();
            }
        });
        if (md != null) {
            String firstInningsScore = RealmDB.getFirstInningsTotal(realm, md)+"/"+RealmDB.noOfWicket(getActivity(),realm,md.getMatch_id(),true) + "  (" + RealmDB.getFirstInningsOver(realm, md) + ")";
            String secInningsScore = RealmDB.getSecInningsTotal(realm, md) +"/"+RealmDB.noOfWicket(getActivity(),realm,md.getMatch_id(),false) +  "  (" + RealmDB.getsecInningsOver(realm, md) + ")";
            overs_home_team.setText(md.getHomeTeam().nick_name);
            over_home_score.setText(firstInningsScore);
            overs_away.setText(md.getAwayTeam().nick_name);
            overs_away_score.setText(secInningsScore);
            overs_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            //  overs_match_quote.setText(md.);

            List<OverAdapterData> datas = getData(id, true);


            OverRvAdapter adapter = new OverRvAdapter(getActivity(), datas);
            overs_rv.setAdapter(adapter);
        }
    }


    List<OverAdapterData> getData(int id, boolean isFirstInnings) {
        float recOver = 0;
        List<OverAdapterData> datas = new ArrayList<>();
        RealmResults<InningsData> data = realm.where(InningsData.class).equalTo("match_id", id)
                .equalTo("firstInnings", isFirstInnings).notEqualTo("delivery", 0).findAllSorted("delivery", Sort.DESCENDING);
        recOver = data.get(0).getOver();
        Set<String> batsmans = new TreeSet<>();
        Set<String> bowlers = new TreeSet<>();
        int total_run = 0;
        float epsilon = (float) 0.00000001;
        ArrayList<String> deliveries = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            InningsData currentdata = data.get(i);
            System.out.println("_______****" + currentdata.getOver() + "^^^" + Math.floor(currentdata.getOver()));
            if ((!(currentdata.getOver() > Math.floor(currentdata.getOver()))) || Math.abs(currentdata.getOver() - 0.1) < epsilon) {

                String batsmansString = "";
                String bowlersString = "";
                System.out.println("_______****ss"+currentdata.getOver()+"__"+ Math.ceil(currentdata.getOver())+"__"+ (int)Math.ceil(currentdata.getOver()));
                Iterator batsmansIterator = batsmans.iterator();
                Iterator bowlersIterator = bowlers.iterator();
                if (batsmansIterator.hasNext()) {
                    while (batsmansIterator.hasNext()) {
                        batsmansString += batsmansIterator.next() + " & ";
                    }
                    while (bowlersIterator.hasNext()) {
                        bowlersString += bowlersIterator.next() + " & ";
                    }
                    OverAdapterData overData = new OverAdapterData();
                    overData.setBatsmans(batsmansString.substring(0, batsmansString.length() - 3));
                    overData.setBolwers(bowlersString.substring(0, bowlersString.length() - 3));
                    overData.setTotal_run(total_run);
                    int curOver = currentdata.getOver() < 1 ? 1 : (int) Math.ceil(currentdata.getOver()) + 1;
                    overData.setOver(curOver);
                    overData.setDeliveries((List<String>) deliveries.clone());
                    datas.add(overData);
                    batsmans.clear();
                    bowlers.clear();
                    deliveries.clear();
                    total_run = 0;
                }

            }
            batsmans.add(RealmDB.getPlayer(realm, currentdata.getStriker()).getName());
            batsmans.add(RealmDB.getPlayer(realm, currentdata.getNonStriker()).getName());
            bowlers.add(RealmDB.getPlayer(realm, currentdata.getCurrentBowler()).getName());
            deliveries.add(Util.get_delivery_result(currentdata.getRun()
                    , currentdata.getWicket(), currentdata.isLegal(), currentdata.getBallType()));
            total_run += currentdata.getRun();


        }
        return datas;
    }
}
