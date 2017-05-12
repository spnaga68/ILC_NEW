package realmstudy.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import realmstudy.R;
import realmstudy.data.CommanData;
import realmstudy.data.RealmObjectData.InningsData;
import realmstudy.data.RealmObjectData.Wicket;
import realmstudy.data.ScoreBoardData;

/**
 * Created by developer on 8/3/17.
 */
public class ScoreBoardFragment extends Fragment {


    private static final int COLOR_SELECT = Color.RED;
    //    private static final int COLOR_UNSELECT = Color.BLACK;

    private TextView team_name;
    private TextView dot_txt, one_run_txt,
            two_run_txt, three_run_txt, four_run_txt, bfour_txt, bSix_txt;
    private TextView wide_txt, no_ball_txt, byes_txt, leg_byes_txt, granted_txt, legal_ball_txt;

    LinearLayout next_bowler_lay, current_bowler_lay;
    private int runs = 0;
    private boolean legal = true;
    private AppCompatButton submit, out;
    private ScoreBoardData r1;
    private InningsData lastInningsDataItem;
    private TextView striker_score, non_striker_score, striker_balls, non_striker_balls;
    private TextView striker_name, non_striker_name;
    public static int legalRun = 1;

    private TextView current_bowler_name, current_bowler_overs, current_bowler_runs, next_bowler_name, next_bowler_overs, next_bowler_runs;
    private LinearLayout last_twelve_balls;
    private TextView score, home_wicket_u, wicket_home, overs;

    private TextView  away_wicket_u, wicket_away,  match_status_quote;
    private TextView shot;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        final View v = inflater.inflate(R.layout.activity_main, container, false);
//        initialize(v);
//        return v;
//    }


    public void initialize(View v) {

        score = (TextView) v.findViewById(realmstudy.R.id.score);
        home_wicket_u = (TextView) v.findViewById(realmstudy.R.id.home_wicket_u);
        wicket_home = (TextView) v.findViewById(realmstudy.R.id.wicket_home);
        overs = (TextView) v.findViewById(realmstudy.R.id.overs);

        away_wicket_u = (TextView) v.findViewById(realmstudy.R.id.away_wicket_u);
        wicket_away = (TextView) v.findViewById(realmstudy.R.id.wicket_away);
       // over_away = (TextView) v.findViewById(realmstudy.R.id.over_away);

        match_status_quote = (TextView) v.findViewById(realmstudy.R.id.match_status_quote);
        striker_score = (TextView) v.findViewById(realmstudy.R.id.striker_score);
        non_striker_score = (TextView) v.findViewById(realmstudy.R.id.non_striker_score);
        striker_balls = (TextView) v.findViewById(realmstudy.R.id.striker_balls);
        non_striker_balls = (TextView) v.findViewById(realmstudy.R.id.non_striker_balls);
        striker_name = (TextView) v.findViewById(realmstudy.R.id.striker_name);
        non_striker_name = (TextView) v.findViewById(realmstudy.R.id.non_striker_name);

        current_bowler_name = (TextView) v.findViewById(realmstudy.R.id.current_bowler_name);
        current_bowler_overs = (TextView) v.findViewById(realmstudy.R.id.current_bowler_overs);
        current_bowler_runs = (TextView) v.findViewById(realmstudy.R.id.current_bowler_runs);
        next_bowler_name = (TextView) v.findViewById(realmstudy.R.id.next_bowler_name);
        next_bowler_overs = (TextView) v.findViewById(realmstudy.R.id.next_bowler_overs);
        next_bowler_runs = (TextView) v.findViewById(realmstudy.R.id.next_bowler_runs);

        next_bowler_lay = (LinearLayout) v.findViewById(realmstudy.R.id.next_bowler_lay);
        current_bowler_lay = (LinearLayout) v.findViewById(realmstudy.R.id.current_bowler_lay);
        team_name = (TextView) v.findViewById(realmstudy.R.id.team_name);

        last_twelve_balls = (LinearLayout) v.findViewById(R.id.last_twelve_balls);

        shot = (TextView) v.findViewById(realmstudy.R.id.shot);

    }


    /**
     * update UI to the current database using current_score_data
     * current_score_data-> object of ScoreboardData
     */
    public void updateUI(ScoreBoardData current_score_data) {
        System.out.println("___________updateUI");
        System.out.println("nagacheckkk" + runs % 2 + "____" + current_score_data.getTotalBalls() + current_score_data.getCurrentBowlerName());
        setPreviousDelivery(current_score_data.getLastThreeOvers());

        striker_score.setText(String.valueOf(current_score_data.getStrikerRun()));
        striker_balls.setText(String.valueOf(current_score_data.getStrikerBalls()));
        non_striker_score.setText(String.valueOf(current_score_data.getNonStrikerRun()));
        non_striker_balls.setText(String.valueOf(current_score_data.getNonStrikerBalls()));
        striker_name.setText(current_score_data.getStrikerName() + "*");
        non_striker_name.setText(current_score_data.getNonStrikerName());
        team_name.setText(current_score_data.getHomeTeam());
        if (current_score_data.getCurrentBowlerName() != null) {
            current_bowler_name.setText(current_score_data.getCurrentBowlerName());
            current_bowler_overs.setText(current_score_data.getCurrentBowlerOver());
            current_bowler_runs.setText(String.valueOf(current_score_data.getCurrentBowlerRuns()));
            current_bowler_lay.setVisibility(View.VISIBLE);
        } else {
            current_bowler_lay.setVisibility(View.GONE);
        }
        if (current_score_data.getNextBowlerName() != null) {
            next_bowler_name.setText(current_score_data.getNextBowlerName());
            next_bowler_overs.setText(current_score_data.getNextBowlerOver());
            next_bowler_runs.setText(String.valueOf(current_score_data.getNextBowlerRuns()));
            next_bowler_lay.setVisibility(View.VISIBLE);
        } else {
            next_bowler_lay.setVisibility(View.GONE);
        }

        if (current_score_data.isBatsmanSwitched()) {
            //SwaponlyText

            striker_score.setText(String.valueOf(current_score_data.getNonStrikerRun()));
            striker_balls.setText(String.valueOf(current_score_data.getNonStrikerBalls()));
            non_striker_score.setText(String.valueOf(current_score_data.getStrikerRun()));
            non_striker_balls.setText(String.valueOf(current_score_data.getStrikerBalls()));
            striker_name.setText(current_score_data.getNonStrikerName() + "*");
            non_striker_name.setText(current_score_data.getStrikerName());
            // Toast.makeText(getActivity(), "switching1", Toast.LENGTH_SHORT).show();
        }
        System.out.println("_______________" + current_score_data.getWicket());

        if (current_score_data.getWicket() != null) {
            if (current_score_data.getNextBatsmanName() != null) {
                Wicket wicket = CommanData.fromJson(current_score_data.getWicket(), Wicket.class);
                if (wicket.isStrikerOut()) {
                    //   BatingProfile bf = RealmDB.getBattingProfile(getActivity(), realm, current_score_data.getNextBatsmanName(), matchDetails.getMatch_id());
                    System.out.println("____________" + current_score_data.getNextBatsmanRun());
                    if (current_score_data.getNextBatsmanName() != null) {
                        striker_score.setText(String.valueOf(current_score_data.getNextBatsmanRun()));
                        striker_balls.setText(String.valueOf(current_score_data.getNextBatsmanBalls()));
                        striker_name.setText(current_score_data.getNextBatsmanName());
                    }

                } else {
                    non_striker_score.setText(String.valueOf(current_score_data.getNextBatsmanRun()));
                    non_striker_balls.setText(String.valueOf(current_score_data.getNextBatsmanBalls()));
                    non_striker_name.setText(String.valueOf(current_score_data.getNextBatsmanName()));
                }

            }
        }
        int wicket=(current_score_data.getTotal_wicket());
        score.setText(String.valueOf(current_score_data.getTotalRuns())+(wicket!=0?"/"+wicket:""));
        overs.setText("(" + String.valueOf(current_score_data.getTotalOver()) + ")");

        wicket_away.setText(String.valueOf(current_score_data.getfirstIinningsWicket()));
//        if (current_score_data.isHomeTeamBatting()) {
//            //home_total.setText(current_score_data.getTotalOver());
//
////            over_away.setText("(" + String.valueOf(current_score_data.getFirstInningsOver()) + ")");
////
////
////            home_team_name.setText(home_team_name.getText() + "*");
//
//        } else {
////            away_total.setText(String.valueOf(current_score_data.getTotalRuns()));
////            wicket_away.setText(String.valueOf(current_score_data.getTotal_wicket()));
////            over_away.setText("(" + String.valueOf(current_score_data.getTotalOver()) + ")");
////
////
////            home_total.setText(String.valueOf(current_score_data.getFirstInningsTotal()));
////            wicket_home.setText(String.valueOf(current_score_data.getfirstIinningsWicket()));
////            over_home.setText("(" + String.valueOf(current_score_data.getFirstInningsOver()) + ")");
////
////            away_team_name.setText(away_team_name.getText() + "*");
//        }

        match_status_quote.setText(current_score_data.getMatchQuote());
        shot.setText(current_score_data.getShotAt());


    }

    private void setPreviousDelivery(ArrayList<String> lpb) {
        last_twelve_balls.removeAllViews();
        if (lpb != null)
            for (String s : lpb) {
                TextView tv = new TextView(getActivity());
                tv.setText(s);
                tv.setPadding(5, 5, 5, 5);
                Log.d("ball added", s != null ? s : "");
                last_twelve_balls.addView(tv);
            }
    }

    protected void swapStrikerText(ScoreBoardData current_score_data, boolean withText, boolean redo) {

        if (withText) {
            if (redo) {
                striker_score.setText(String.valueOf(current_score_data.getStrikerRun()));
                striker_balls.setText(String.valueOf(current_score_data.getStrikerBalls()));
                non_striker_score.setText(String.valueOf(current_score_data.getNonStrikerRun()));
                non_striker_balls.setText(String.valueOf(current_score_data.getNonStrikerBalls()));
                striker_name.setText(current_score_data.getStrikerName() + "*");
                non_striker_name.setText(current_score_data.getNonStrikerName());
            } else {
                striker_score.setText(String.valueOf(current_score_data.getNonStrikerRun()));
                striker_balls.setText(String.valueOf(current_score_data.getNonStrikerBalls()));
                non_striker_score.setText(String.valueOf(current_score_data.getStrikerRun()));
                non_striker_balls.setText(String.valueOf(current_score_data.getStrikerBalls()));
                striker_name.setText(current_score_data.getNonStrikerName() + "*");
                non_striker_name.setText(current_score_data.getStrikerName());
            }
        }

    }
}
