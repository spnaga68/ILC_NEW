package realmstudy.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import io.realm.Realm;
import realmstudy.GroundPickerActivity;
import realmstudy.MainActivity;
import realmstudy.MainFragmentActivity;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.TeamPickerActivity;
import realmstudy.data.RealmObjectData.Ground;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.data.RealmObjectData.Team;
import realmstudy.databaseFunctions.RealmDB;

/**
 * Created by developer on 1/7/17.
 */

public class MatchInfo extends Fragment {
    private TextView
            home_team_select, away_team_select, venue, Noofplayers;
    private Spinner
            no_of_players, no_of_overs;
    private EditText
            total_overs;
    private android.support.v7.widget.AppCompatButton
            continue_toss;
    @Inject
    Realm realm;
    private Team homeTeam;
    private Team awayTeam;
    int match_id = -1;
    MatchDetails matchDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.match_info, container, false);
        ((MyApplication) (getActivity()).getApplication()).getComponent().inject(this);


        home_team_select = (TextView) v.findViewById(R.id.home_team_select);
        away_team_select = (TextView) v.findViewById(R.id.away_team_select);
        venue = (TextView) v.findViewById(R.id.venue);
        Noofplayers = (TextView) v.findViewById(R.id.Noofplayers);
        no_of_players = (Spinner) v.findViewById(R.id.no_of_players);
        no_of_overs = (Spinner) v.findViewById(R.id.no_of_overs);
        total_overs = (EditText) v.findViewById(R.id.total_overs);
        no_of_overs.setSelection(9);
        no_of_players.setSelection(9);
        continue_toss = (android.support.v7.widget.AppCompatButton) v.findViewById(R.id.continue_toss);
        venue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), GroundPickerActivity.class);
                getActivity().startActivityForResult(i, 30);
            }
        });
        home_team_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TeamPickerActivity.class);
                Bundle b = new Bundle();
                i.putExtra("for", "home");
                getActivity().startActivityForResult(i, 20);
            }
        });
        away_team_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TeamPickerActivity.class);
                Bundle b = new Bundle();
                i.putExtra("for", "away");
                getActivity().startActivityForResult(i, 20);
            }
        });

        if (getArguments() != null) {
            match_id = getArguments().getInt("match_id");
            matchDetails = RealmDB.getMatchById(getActivity(), realm, match_id);
            homeTeam = matchDetails.getHomeTeam();
            awayTeam = matchDetails.getAwayTeam();
            home_team_select.setText(homeTeam.nick_name);
            away_team_select.setText(awayTeam.nick_name);
        }
        continue_toss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveMatchInfo();
            }
        });

        return v;
    }

    void saveMatchInfo() {
        if (homeTeam != null) {
            if (awayTeam != null) {
                if (venue.getText().toString().length() != 0) {
                    matchDetails = RealmDB.UpdateorCreateMatchDetail(getActivity(), realm, homeTeam, awayTeam, "", null, no_of_overs.getSelectedItemPosition() + 1, venue.getText().toString(), no_of_players.getSelectedItemPosition() + 2, 0, match_id);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bundle b = new Bundle();
                            b.putString("teamIDs", homeTeam.team_id + "__" + awayTeam.team_id);
                            b.putInt("match_id", matchDetails.getMatch_id());
                            TossFragment mf = new TossFragment();
                            mf.setArguments(b);
                            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainFrag, mf).commit();
                        }
                    }, 100);
                } else
                    Toast.makeText(getActivity(), getString(R.string.select_valid_ground), Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), getString(R.string.select_valid_home_away_team), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getActivity(), getString(R.string.select_valid_home_away_team), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.match_info));
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((MainFragmentActivity)getActivity()).setNaviHome();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 20) {
                final int teamID = data.getIntExtra("id", 0);
                home_team_select.post(new Runnable() {
                    @Override
                    public void run() {
                        if (data.getStringExtra("for").equals("home")) {
                            if (awayTeam != null && teamID == awayTeam.team_id) {
                                awayTeam = null;
                                away_team_select.setText("");
                            }
                            homeTeam = RealmDB.getTeam(realm, teamID);
                            home_team_select.setText(homeTeam.nick_name);
                        } else {
                            if (homeTeam != null && teamID == homeTeam.team_id) {
                                homeTeam = null;
                                home_team_select.setText("");
                            }
                            awayTeam = RealmDB.getTeam(realm, teamID);
                            away_team_select.setText(awayTeam.nick_name);
                        }
                    }
                });
            } else {
                final int groundId = data.getIntExtra("id", 0);
                Ground ground = RealmDB.getGround(realm, groundId);
                if (ground != null)
                    venue.setText(ground.getGroundName());
            }
        }
    }

}
