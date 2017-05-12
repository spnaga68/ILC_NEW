package realmstudy.fragments.DialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import realmstudy.MainFragmentActivity;
import realmstudy.R;
import realmstudy.adapter.SelectTeamListAdapter;
import realmstudy.data.CommanData;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.data.RealmObjectData.Team;
import realmstudy.databaseFunctions.RealmDB;

/**
 * Created by developer on 6/3/17.
 */

public class SelectTeamDialog extends DialogFragment {
    private boolean ishomeTeam;
    private Realm realm;
    private MatchDetails matchDetails;
    private Button butConfirmTime;
    private SelectTeamListAdapter multiTeamListAdapter;
    TextView header_txt;
    private String ss;
    private int match_id;

    public static SelectTeamDialog newInstance(int match_id, boolean ishomeTeam) {
        SelectTeamDialog f = new SelectTeamDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("match_id", match_id);
        args.putBoolean("ishomeTeam", ishomeTeam);
        //args.putInt("current_bowler_id", current_bowler_id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        match_id = getArguments().getInt("match_id");
        ishomeTeam = getArguments().getBoolean("ishomeTeam");
        Realm.init(getActivity());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .build();
        realm = Realm.getInstance(config);
        if (match_id != -1)
            matchDetails = RealmDB.getMatchById(getActivity(), realm, match_id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_listview_dialog, null);
        ListView lv = (ListView) v.findViewById(R.id.list_view);
        butConfirmTime = (Button) v.findViewById(R.id.butConfirmTime);
        header_txt = (TextView) v.findViewById(R.id.header_txt);
        header_txt.setText(getString(R.string.add_player));
        RealmResults<Team> playerList = RealmDB.getAllTeam(realm);
        multiTeamListAdapter = new SelectTeamListAdapter(getActivity(), playerList);
        lv.setAdapter(multiTeamListAdapter);
//        butConfirmTime.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int is, long l) {
//                ArrayList<Integer> data = multiTeamListAdapter.selectedPlayersID();
//                if (data.size() > 0)
//                    System.out.println("____________" + data.toString());
//                for (int i = 0; i < data.size(); i++) {
//                    ss = ss+RealmDB.getTeam(getActivity(), realm, data.get(i)).nick_name ;
//                    if (match_id != -1)
//                        RealmDB.addPlayerToMatch(data.get(i), getActivity(), realm, matchDetails, ishomeTeam);
//
//                }
                ss = String.valueOf(multiTeamListAdapter.getItem(is).team_id);
                //((MainFragmentActivity) getActivity()).msg("Success");
                ((MainFragmentActivity) getActivity()).messageFromDialog(CommanData.DIALOG_SELECT_TEAM, true, ss, "name");
                dismiss();
            }
        });
        return v;
    }
}
