package realmstudy.fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.common.StringUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import realmstudy.R;
import realmstudy.data.CommanData;
import realmstudy.data.RealmObjectData.InningsData;
import realmstudy.data.RealmObjectData.Wicket;
import realmstudy.data.ScoreBoardData;
import realmstudy.interfaces.ScoreBoardViewClickListner;
import realmstudy.lib.customViews.AutoResizeTextView;

/**
 * Created by developer on 8/3/17.
 */
public class ScoreBoardFragment extends View {


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
    ScoreBoardViewClickListner listner;
    private TextView current_bowler_name, current_bowler_overs, current_bowler_runs,
            next_bowler_name, next_bowler_overs, next_bowler_runs,
            current_bowler_maiden, current_bowler_wicket, current_bowler_er,
            next_bowler_maiden, next_bowler_wicket, next_bowler_er;
    private LinearLayout last_twelve_balls;
    private TextView score, crr, wicket_home, overs, reqRR;

    private TextView away_wicket_u, wicket_away, match_status_quote;
    private TextView shot;
    Context context;
    private TextView non_striker_fours, non_striker_sixes, non_striker_sr, striker_fours, striker_sixes, striker_sr;
    LinearLayout rrr_lay;

    public ScoreBoardFragment(Context context) {
        super(context);
        this.context = context;
    }

    public ScoreBoardFragment(Context context, ScoreBoardViewClickListner listner) {
        super(context);
        this.context = context;
        this.listner = listner;
    }
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        final View v = inflater.inflate(R.layout.activity_main, container, false);
//        initialize(v);
//        return v;
//    }


    public void initialize(View v) {

        score = (TextView) v.findViewById(realmstudy.R.id.score);
        crr = (TextView) v.findViewById(realmstudy.R.id.crr);
        reqRR = (TextView) v.findViewById(realmstudy.R.id.rrr);
        wicket_home = (TextView) v.findViewById(realmstudy.R.id.wicket_home);
        overs = (TextView) v.findViewById(realmstudy.R.id.overs);
        non_striker_fours = (TextView) v.findViewById(realmstudy.R.id.non_striker_fours);
        non_striker_sixes = (TextView) v.findViewById(realmstudy.R.id.non_striker_sixes);
        non_striker_sr = (TextView) v.findViewById(realmstudy.R.id.non_striker_sr);
        striker_fours = (TextView) v.findViewById(realmstudy.R.id.striker_fours);
        striker_sixes = (TextView) v.findViewById(realmstudy.R.id.striker_sixes);
        striker_sr = (TextView) v.findViewById(realmstudy.R.id.striker_sr);
        away_wicket_u = (TextView) v.findViewById(realmstudy.R.id.away_wicket_u);
        wicket_away = (TextView) v.findViewById(realmstudy.R.id.wicket_away);
        // over_away = (TextView) v.findViewById(realmstudy.R.id.over_away);
        rrr_lay = (LinearLayout) v.findViewById(realmstudy.R.id.rrr_lay);

        current_bowler_maiden = (TextView) v.findViewById(realmstudy.R.id.current_bowler_maiden);
        current_bowler_wicket = (TextView) v.findViewById(realmstudy.R.id.current_bowler_wicket);
        current_bowler_er = (TextView) v.findViewById(realmstudy.R.id.current_bowler_er);
        next_bowler_maiden = (TextView) v.findViewById(realmstudy.R.id.next_bowler_maiden);
        next_bowler_wicket = (TextView) v.findViewById(realmstudy.R.id.next_bowler_wicket);
        next_bowler_er = (TextView) v.findViewById(realmstudy.R.id.next_bowler_er);

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

        striker_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null)
                    listner.strikerClicked();
            }
        });
        non_striker_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null)
                    listner.nonStrikerClicked();
            }
        });
        current_bowler_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null)
                    listner.bowlerClicked();
            }
        });

    }


    /**
     * update UI to the current database using current_score_data
     * current_score_data-> object of ScoreboardData
     */
    public void updateUI(ScoreBoardData current_score_data) {
        System.out.println("___________updateUI");
        System.out.println("nagacheckkk" + runs % 2 + "____" + current_score_data.isBatsmanSwitched() + current_score_data.striker.getName());
        setPreviousDelivery(current_score_data.getLastThreeOvers());


        striker_score.setText(String.valueOf(current_score_data.striker.getRuns()));
        striker_balls.setText(String.valueOf(current_score_data.striker.getBalls()));
        striker_name.setText(current_score_data.striker.getName() + "*");
        striker_fours.setText("" + current_score_data.striker.getFours());
        striker_sixes.setText("" + current_score_data.striker.getSixes());
        striker_sr.setText(CommanData.getStrikeRate(current_score_data.striker.getBalls(), current_score_data.striker.getRuns()));


        non_striker_score.setText(String.valueOf(current_score_data.nonStriker.getRuns()));
        non_striker_balls.setText(String.valueOf(current_score_data.nonStriker.getBalls()));
        non_striker_name.setText(current_score_data.nonStriker.getName());
        non_striker_fours.setText("" + current_score_data.nonStriker.getFours());
        non_striker_sixes.setText("" + current_score_data.nonStriker.getSixes());
        non_striker_sr.setText(CommanData.getStrikeRate(current_score_data.nonStriker.getBalls(), current_score_data.nonStriker.getRuns()));

        team_name.setText(current_score_data.isHomeTeamBatting() ? current_score_data.getHomeTeam() : current_score_data.getAwayTeam());
        if (current_score_data.curr_bowlers.getName() != null) {
            current_bowler_name.setText(current_score_data.curr_bowlers.getName());
            current_bowler_overs.setText(current_score_data.curr_bowlers.getOvers());
            current_bowler_runs.setText(String.valueOf(current_score_data.curr_bowlers.getRuns()));
            current_bowler_maiden.setText(String.valueOf(current_score_data.curr_bowlers.getMaiden()));
            current_bowler_wicket.setText(String.valueOf(current_score_data.curr_bowlers.getWicket()));
            current_bowler_er.setText(CommanData.getER(current_score_data.curr_bowlers.getRuns(), current_score_data.curr_bowlers.getOvers()));
            current_bowler_lay.setVisibility(View.VISIBLE);
        } else {
            current_bowler_lay.setVisibility(View.GONE);
        }
        if (current_score_data.next_bowlers.getName() != null) {
            next_bowler_name.setText(current_score_data.next_bowlers.getName());
            next_bowler_overs.setText("" + current_score_data.next_bowlers.getOvers());
            next_bowler_runs.setText(String.valueOf(current_score_data.next_bowlers.getRuns()));
            next_bowler_maiden.setText(String.valueOf(current_score_data.next_bowlers.getMaiden()));
            next_bowler_wicket.setText(String.valueOf(current_score_data.next_bowlers.getWicket()));
            next_bowler_er.setText(CommanData.getER(current_score_data.next_bowlers.getRuns(), current_score_data.next_bowlers.getOvers()));
            next_bowler_lay.setVisibility(View.VISIBLE);
        } else {
            next_bowler_lay.setVisibility(View.GONE);
        }

        if (current_score_data.isBatsmanSwitched()) {
            //SwaponlyText
            swapStrikerText(current_score_data, true, false);

//            striker_score.setText(String.valueOf(current_score_data.nonStriker.getRuns()));
//            striker_balls.setText(String.valueOf(current_score_data.nonStriker.getBalls()));
//            striker_fours.setText(String.valueOf(current_score_data.nonStriker.getFours()));
//            striker_sixes.setText(String.valueOf(current_score_data.nonStriker.getSixes()));
//            non_striker_sr.setText(CommanData.getStrikeRate(current_score_data.striker.getBalls(), current_score_data.striker.getRuns()));
//
//
//
//
//            non_striker_fours.setText(String.valueOf(current_score_data.striker.getFours()));
//            non_striker_sixes.setText(String.valueOf(current_score_data.striker.getSixes()));
//            non_striker_score.setText(String.valueOf(current_score_data.striker.getRuns()));
//            non_striker_balls.setText(String.valueOf(current_score_data.striker.getBalls()));
//            striker_name.setText(current_score_data.nonStriker.getName() + "*");
//            non_striker_name.setText(current_score_data.striker.getName());
//            non_striker_sr.setText(CommanData.getStrikeRate(current_score_data.striker.getBalls(), current_score_data.striker.getRuns()));
            // Toast.makeText(getActivity(), "switching1", Toast.LENGTH_SHORT).show();
        }
        System.out.println("_______________sss" + current_score_data.getWicket() + "__" + current_score_data.isBatsmanSwitched());

//        if (current_score_data.getWicket() != null) {
//            if (current_score_data.getNextBatsmanName() != null) {
//                Wicket wicket = CommanData.fromJson(current_score_data.getWicket(), Wicket.class);
//                if (wicket.isStrikerOut()) {
//                    //   BatingProfile bf = RealmDB.getBattingProfile(getActivity(), realm, current_score_data.getNextBatsmanName(), matchDetails.getMatch_id());
//                    System.out.println("____________" + current_score_data.getNextBatsmanRun());
//                    if (current_score_data.getNextBatsmanName() != null) {
//                        striker_score.setText(String.valueOf(current_score_data.getNextBatsmanRun()));
//                        striker_balls.setText(String.valueOf(current_score_data.getNextBatsmanBalls()));
//                        striker_name.setText(current_score_data.getNextBatsmanName());
//                    }
//
//                } else {
//                    non_striker_score.setText(String.valueOf(current_score_data.getNextBatsmanRun()));
//                    non_striker_balls.setText(String.valueOf(current_score_data.getNextBatsmanBalls()));
//                    non_striker_name.setText(String.valueOf(current_score_data.getNextBatsmanName()));
//                }
//
//            }
//        }
        int wicket = (current_score_data.getTotal_wicket());
        score.setText(String.valueOf(current_score_data.getTotalRuns()) + ("/" + wicket));
        overs.setText("(" + String.valueOf(current_score_data.getTotalOver()) + ")");
        // float balls=Float.parseFloat(current_score_data.getTotalOver());
        crr.setText(CommanData.currentRunRate(current_score_data.getTotalRuns(), current_score_data.getTotalOver()));

        if (current_score_data.isFirstInnings())
            rrr_lay.setVisibility(GONE);
        else {

            rrr_lay.setVisibility(VISIBLE);
            reqRR.setText(current_score_data.getReqRunRate());
            if (current_score_data.getReqRunRate() != null)
                if (!current_score_data.getReqRunRate().matches("^-?\\d+$"))
                    rrr_lay.setVisibility(GONE);
                else if (Float.parseFloat(current_score_data.getReqRunRate()) < 0)
            rrr_lay.setVisibility(GONE);
        }
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
//        shot.setText(current_score_data.getShotAt());


    }

    public void showPreviousDelivery(boolean show) {
        if (show)
            last_twelve_balls.setVisibility(View.VISIBLE);
        else
            last_twelve_balls.setVisibility(View.GONE);
    }

    private void setPreviousDelivery(ArrayList<String> lpbf) {
        List<String> lpb;
        if (lpbf != null) {
            if (lpbf.size() > 12)
                lpb = lpbf.subList(lpbf.size() - 12, lpbf.size());
            else
                lpb = lpbf;
            if (last_twelve_balls.getVisibility() == VISIBLE) {
                last_twelve_balls.removeAllViews();
                if (lpb != null)
                    for (String s : lpb) {
                        TextView tv;
                        if (s.trim().equals("|"))
                            tv = new TextView(context);
                        else {
                            tv = new AutoResizeTextView(context);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
//            layoutParams.width=20;
//            layoutParams.height=20;
                            layoutParams.setMargins(5, 5, 5, 5);
                            tv.setLayoutParams(layoutParams);
                            ((AutoResizeTextView) tv).setSolidColor("#BCAAA4");
                            //   tv.setStrokeColor("#43A047");
                            tv.setGravity(Gravity.CENTER);
                            // tv.setPadding(8, 8, 8, 8);
                        }
                        tv.setText(s);
                        tv.setPadding(5, 5, 5, 5);
                        // Log.d("ball added", s != null ? s : "");
                        last_twelve_balls.addView(tv);
                    }
            }
        }
    }

    public void swapStrikerText(ScoreBoardData current_score_data, boolean withText, boolean redo) {
        System.out.println("score_data_swap____" + current_score_data.striker.getName() + "___" + withText + "__" + redo + "__" + current_score_data.getTotalBalls());
        if (withText) {
            if (redo) {
                striker_score.setText(String.valueOf(current_score_data.striker.getRuns()));
                striker_balls.setText(String.valueOf(current_score_data.striker.getBalls()));
                non_striker_score.setText(String.valueOf(current_score_data.nonStriker.getRuns()));
                non_striker_balls.setText(String.valueOf(current_score_data.nonStriker.getBalls()));
                non_striker_fours.setText("" + current_score_data.nonStriker.getFours());
                non_striker_sixes.setText("" + current_score_data.nonStriker.getSixes());
                non_striker_sr.setText(CommanData.getStrikeRate(current_score_data.nonStriker.getBalls(), current_score_data.nonStriker.getRuns()));
                striker_fours.setText("" + current_score_data.striker.getFours());
                striker_sixes.setText("" + current_score_data.striker.getSixes());
                striker_sr.setText(CommanData.getStrikeRate(current_score_data.striker.getBalls(), current_score_data.striker.getRuns()));
                striker_name.setText(current_score_data.striker.getName() + "*");
                non_striker_name.setText(current_score_data.nonStriker.getName());
            } else {
                striker_score.setText(String.valueOf(current_score_data.nonStriker.getRuns()));
                striker_balls.setText(String.valueOf(current_score_data.nonStriker.getBalls()));
                non_striker_score.setText(String.valueOf(current_score_data.striker.getRuns()));
                non_striker_balls.setText(String.valueOf(current_score_data.striker.getBalls()));
                striker_name.setText(current_score_data.nonStriker.getName() + "*");
                non_striker_name.setText(current_score_data.striker.getName());
                striker_fours.setText("" + current_score_data.nonStriker.getFours());
                striker_sixes.setText("" + current_score_data.nonStriker.getSixes());
                striker_sr.setText(CommanData.getStrikeRate(current_score_data.nonStriker.getBalls(), current_score_data.nonStriker.getRuns()));
                non_striker_fours.setText("" + current_score_data.striker.getFours());
                non_striker_sixes.setText("" + current_score_data.striker.getSixes());
                non_striker_sr.setText(CommanData.getStrikeRate(current_score_data.striker.getBalls(), current_score_data.striker.getRuns()));

            }
        }

    }
}
