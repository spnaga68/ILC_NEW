package realmstudy.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import realmstudy.MainFragmentActivity;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.data.CommanData;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.data.RealmObjectData.Player;
import realmstudy.data.RealmObjectData.Team;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.interfaces.MsgFromDialog;

/**
 * Created by developer on 6/3/17.
 */
public class ScheduleNewGame extends Fragment implements MsgFromDialog {

    TextView home_team, away_team, venue, desc, players, time;
    Button share;

    DatePicker datePicker;
    TimePicker timePicker;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
    private Button set1;
    private Button set;
    private boolean askByHome;
    private Team homeTeam;
    private Team awayTeam;
    @Inject
    Realm realm;
    RealmList<Player> players_array;
    private long match_time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_list, container, false);
        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);

        players_array = new RealmList<>();
        home_team = (TextView) v.findViewById(R.id.home_team);
        away_team = (TextView) v.findViewById(R.id.away_team);
        venue = (TextView) v.findViewById(R.id.venue);
        desc = (TextView) v.findViewById(R.id.descc);
        players = (TextView) v.findViewById(R.id.players);
        time = (TextView) v.findViewById(R.id.times);
        share = (Button) v.findViewById(R.id.share);

        players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("______________cccc1");
                ((MainFragmentActivity) getActivity()).showMultiPlayerSelect(-1, false, -1);
            }
        });
        away_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askByHome = false;
                ((MainFragmentActivity) getActivity()).showMultiTeamSelect(-1, false, -1);
            }
        });
        home_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askByHome = true;
                ((MainFragmentActivity) getActivity()).showMultiTeamSelect(-1, false, -1);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calDatePicker();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(homeTeam!=null){
                MatchDetails md = RealmDB.createNewMatch(getActivity(), realm, homeTeam, awayTeam, "", null, 0, venue.getText().toString(), 0,match_time);
                RealmDB.addPlayerToMatch(players_array, getActivity(), realm, md);}else
                    Toast.makeText(getActivity(), "sfksklf", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    void calDatePicker() {
        final View dialogView = View.inflate(getActivity(), R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

        set = (Button) dialogView.findViewById(R.id.date_time_set);
        set1 = (Button) dialogView.findViewById(R.id.date_time_set1);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        dialogView.findViewById(R.id.date_time_set1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.GONE);
                set.setVisibility(View.VISIBLE);
                set1.setVisibility(View.GONE);

            }
        });
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            public Date sdate;

            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                match_time=calendar.getTimeInMillis()/1000;
                time.setText(dateFormat.format(calendar.getTime()));
                alertDialog.dismiss();
            }
        });


        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    @Override
    public void messageFromDialog(int dialogType, boolean success, String data, String message) {
        System.out.println("______vv" + data + "___" + dialogType);
        if (dialogType == CommanData.DIALOG_SELECT_TEAM) {
            System.out.println("______vv" + data);
            int id = Integer.parseInt(data);
            if (askByHome) {

                if (away_team != null && id == away_team.getId()) {
                    awayTeam = null;
                    away_team.setText("");
                }
                homeTeam = RealmDB.getTeam(realm, id);
                home_team.setText(homeTeam.nick_name);


            } else {
                if (homeTeam != null && id == home_team.getId()) {
                    homeTeam = null;
                    home_team.setText("");
                }
                awayTeam = RealmDB.getTeam(realm, id);
                away_team.setText(awayTeam.nick_name);
            }
        }
    }

    @Override
    public void messageFromDialog(int dialogType, boolean success, ArrayList<Integer> data, String message) {
        //System.out.println("_________sss" + data);
        if (dialogType == CommanData.DIALOG_SELECT_MULTI_PLAYER) {
            String ss = "";
            players_array.clear();
            for (int i = 0; i < data.size(); i++) {
                Player p = RealmDB.getPlayer( realm, data.get(i));
                ss = ss + p.getName() + (i == (data.size() - 1) ? "" : ",");

                players_array.add(p);
                players.setText(ss);
            }

        }
    }

    @Override
    public void messageFromDialog(int dialogType, boolean success, String data, String message, int assignTo) {

    }
}
