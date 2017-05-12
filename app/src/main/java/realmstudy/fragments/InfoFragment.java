package realmstudy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import io.realm.Realm;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.data.CommanData;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.databaseFunctions.RealmDB;

/**
 * Created by developer on 12/4/17.
 */
public class InfoFragment extends Fragment {

    private TextView
            squad_home_team, squad_away_team, info_match_id, info_series, info_date, info_time, info_toss, info_venue, info_umpires, info_third_umpire, info_refree;
MatchDetails md;
    @Inject
    Realm realm;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int id= getArguments().getInt("match_id");
        ((MyApplication)getActivity().getApplication()).getComponent().inject(this);
        md= RealmDB.getMatchById(getActivity(),realm,id);
        if(md!=null){
            squad_home_team.setText(md.getHomeTeam().name);
            squad_away_team.setText(md.getAwayTeam().name);
            info_match_id.setText(String.valueOf(md.getMatchStatus()));
            info_series.setText("-");
            info_date.setText(CommanData.getDate(md.getTime()));
            info_time.setText(CommanData.getTime(md.getTime()));
            info_toss.setText(md.getToss().nick_name);
            info_venue.setText(md.getLocation());
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

        return v;
    }
}
