package realmstudy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.data.CommanData;
import realmstudy.data.MatchShortSummaryData;
import realmstudy.data.OverAdapterData;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.data.ScoreBoardData;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.extras.Utils;

/**
 * Created by developer on 12/4/17.
 */
public class InfoFragment extends Fragment {

    private TextView
            squad_home_team, squad_away_team, info_match_id, info_series, info_date, info_time, info_toss, info_venue, info_umpires, info_third_umpire, info_refree;
    MatchShortSummaryData md;
    LinearLayout series_lay, umpire_lay, third_umpire_lay, refree_lay;
//    @Inject
//    Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String id = getArguments().getString("mss");
       // ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
       md= CommanData.fromJson(id, MatchShortSummaryData.class);
        System.out.println("____"+md+"__"+id);
        if (md != null) {
            squad_home_team.setText(md.getHomeTeam());
            squad_away_team.setText(md.getAwayTeam());
            info_match_id.setText(String.valueOf(md.getQuotes()));
            info_series.setText("-");
            info_date.setText(Utils.getDateOnly(md.getTime()));
            info_time.setText(Utils.getTimeOnly(md.getTime()));
            info_toss.setText(md.getToss());
            info_venue.setText(md.getLocation());
            refree_lay.setVisibility(View.GONE);
            third_umpire_lay.setVisibility(View.GONE);
            umpire_lay.setVisibility(View.GONE);
            series_lay.setVisibility(View.GONE);
            //  squad_home_team.setText(md.getHomeTeam().name);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.match_info_layout, container, false);
        squad_home_team = (TextView) v.findViewById(R.id.squad_home_team);
        squad_away_team = (TextView) v.findViewById(R.id.squad_away_team);
        info_match_id = (TextView) v.findViewById(R.id.info_match_id);
        info_series = (TextView) v.findViewById(R.id.info_series);
        info_date = (TextView) v.findViewById(R.id.info_date);
        info_time = (TextView) v.findViewById(R.id.info_time);
        info_toss = (TextView) v.findViewById(R.id.info_toss);
        info_venue = (TextView) v.findViewById(R.id.info_venue);
        info_umpires = (TextView) v.findViewById(R.id.info_umpires);
        info_third_umpire = (TextView) v.findViewById(R.id.info_third_umpire);
        info_refree = (TextView) v.findViewById(R.id.info_refree);
        series_lay = (LinearLayout) v.findViewById(R.id.series_lay);
        umpire_lay = (LinearLayout) v.findViewById(R.id.umpire_lay);
        third_umpire_lay = (LinearLayout) v.findViewById(R.id.third_umpire_lay);
        refree_lay = (LinearLayout) v.findViewById(R.id.refree_lay);
        squad_home_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.comming_soon), Toast.LENGTH_SHORT).show();
            }
        });
        squad_away_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.comming_soon), Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    public void setData(List<OverAdapterData> overAdapterData, ScoreBoardData scoreBoardData) {

    }
}
