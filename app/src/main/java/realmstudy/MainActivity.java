package realmstudy;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import realmstudy.data.CommanData;
import realmstudy.data.DetailedScoreData;
import realmstudy.data.InningsSummary;
import realmstudy.data.MatchShortSummaryData;
import realmstudy.data.OverAdapterData;
import realmstudy.data.RealmObjectData.BatingProfile;
import realmstudy.data.RealmObjectData.BowlingProfile;
import realmstudy.data.RealmObjectData.InningsData;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.data.RealmObjectData.Player;
import realmstudy.data.RealmObjectData.Wicket;
import realmstudy.data.ScoreBoardData;
import realmstudy.data.ScoreCardDetailData;
import realmstudy.data.SessionSave;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.fragments.ScoreBoardFragment;
import realmstudy.interfaces.DialogInterface;
import realmstudy.interfaces.MsgFromDialog;
import realmstudy.interfaces.MsgToFragment;
import realmstudy.interfaces.ScoreBoardViewClickListner;
import realmstudy.lib.Util;
import realmstudy.service.CoreClient;
import realmstudy.service.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;

import static realmstudy.data.CommanData.wicketIdToString;

public class MainActivity extends Fragment implements DialogInterface,
        MsgToFragment, MsgFromDialog, ScoreBoardViewClickListner, View.OnClickListener {

    private static final int COLOR_SELECT = Color.RED;
    private static final int COLOR_UNSELECT = Color.BLACK;

    private static final int SUBMIT_DELAY = 500;
    private Realm realm;
    private DetailedScoreData detailedScoreBoardData;
    private Player striker, non_striker;
    private Player current_bowler, next_bowler;
    private TextView dot_txt, one_run_txt,
            two_run_txt, three_run_txt, four_run_txt, bfour_txt, bSix_txt;
    private TextView wide_txt, no_ball_txt, byes_txt, leg_byes_txt, granted_txt, legal_ball_txt;

    private int runs = 0;
    private boolean normal_delivery = true;
    private AppCompatButton submit, out;
    private ScoreBoardData r1;
    private InningsData lastInningsDataItem;
    public static int legalRun = 1;
    //    ImageButton undo, redo;
    private int undoCount, redoCount;
    //temp variable holds address of player(i.e striker,bowler)
    /**
     * 0-->striker
     * 1-->non_striker
     * 2-->current_bowler
     * 3-->next_bowler
     */
    private int assignToPlayer;
    private Dialog selectPlayerDialog;
    private AlertDialog outDialog;
    // private CanvasStudy groundFragment;
    private int swapCount = 0;
    private int resumeInningsFrom = -1;
    private ScoreBoardFragment scoreBoardFragment;
    private ImageButton view_icon;
    private MenuItem redo, undo, swap_batsman, view_scoreCard;
    private int firstInningsRun;
    private int firstInningsWicket;
    private String firstInningsOver;

    LinearLayout extras_lay_L, submit_lay, run_lay_L;
    TextView blockEntry;
    private boolean removedFromUpcoming;
    /*variable use when two select player dialog opens*/
    private boolean askBowler;

    @Override
    public void msg(String s) {

    }


    private CommanData.typeExtraEnum extraType;
    MatchDetails matchDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_main, container, false);


        realm = ((MainFragmentActivity) (getActivity())).getRealm();

        try {
            Bundle b = getArguments();
            int match_id = b.getInt("match_id");
            matchDetails = RealmDB.getMatchById(getActivity(), realm, match_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scoreBoardFragment = new ScoreBoardFragment(getActivity(), this);
        scoreBoardFragment.initialize(v);
        init(v);
        resumeMatch(matchDetails);

        return v;
    }

    public void showEntry() {
        extras_lay_L.setVisibility(View.VISIBLE);
        submit_lay.setVisibility(View.VISIBLE);
        run_lay_L.setVisibility(View.VISIBLE);
        blockEntry.setVisibility(View.GONE);
    }

    public void hideEntry(String s) {
        extras_lay_L.setVisibility(View.GONE);
        submit_lay.setVisibility(View.GONE);
        run_lay_L.setVisibility(View.GONE);
        blockEntry.setVisibility(View.VISIBLE);
        blockEntry.setText(s);
    }

    //initialize views
    public void init(View v) {
        dot_txt = (TextView) v.findViewById(realmstudy.R.id.dot_txt);
        one_run_txt = (TextView) v.findViewById(realmstudy.R.id.one_run_txt);
        two_run_txt = (TextView) v.findViewById(realmstudy.R.id.two_run_txt);
        three_run_txt = (TextView) v.findViewById(realmstudy.R.id.three_run_txt);
        four_run_txt = (TextView) v.findViewById(realmstudy.R.id.four_run_txt);
        bfour_txt = (TextView) v.findViewById(realmstudy.R.id.bfour_txt);
        bSix_txt = (TextView) v.findViewById(realmstudy.R.id.bSix_txt);

        wide_txt = (TextView) v.findViewById(realmstudy.R.id.wide_txt);
        no_ball_txt = (TextView) v.findViewById(realmstudy.R.id.no_ball_txt);
        byes_txt = (TextView) v.findViewById(realmstudy.R.id.byes_txt);
        leg_byes_txt = (TextView) v.findViewById(realmstudy.R.id.leg_byes_txt);
        granted_txt = (TextView) v.findViewById(realmstudy.R.id.granted_txt);
        legal_ball_txt = (TextView) v.findViewById(realmstudy.R.id.legal_ball_txt);
        submit = (AppCompatButton) v.findViewById(realmstudy.R.id.submit);
        out = (AppCompatButton) v.findViewById(realmstudy.R.id.out);
        extras_lay_L = (LinearLayout) v.findViewById(realmstudy.R.id.extras_lay_L);
        submit_lay = (LinearLayout) v.findViewById(realmstudy.R.id.submit_lay);
        blockEntry = (TextView) v.findViewById(realmstudy.R.id.blockEntry);
        run_lay_L = (LinearLayout) v.findViewById(realmstudy.R.id.run_lay_L);
        setInputListner(true);

        // groundFragment = (CanvasStudy) getChildFragmentManager().findFragmentById(R.id.ground_frag);


        blockEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (askBowler)
                    current_bowler = null;
                checkPlayerNotNull();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // callData();
                //submitbuttonClicked(view);


            }
        });

        out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //outDialog(getString(realmstudy.R.string.wicket));
                if (checkPlayerNotNull())
                    ((MainFragmentActivity) getActivity()).showOutDialog(striker.getpID(), non_striker.getpID(), current_bowler.getpID(), matchDetails.getMatch_id(), lastInningsDataItem.getOver());
            }
        });
//
    }

    private void setInputListner(boolean set) {
        if (set) {
            dot_txt.setOnClickListener(this);
            one_run_txt.setOnClickListener(this);
            two_run_txt.setOnClickListener(this);
            three_run_txt.setOnClickListener(this);
            four_run_txt.setOnClickListener(this);
            bfour_txt.setOnClickListener(this);
            bSix_txt.setOnClickListener(this);
            legal_ball_txt.setOnClickListener(this);
            wide_txt.setOnClickListener(this);
            no_ball_txt.setOnClickListener(this);
            byes_txt.setOnClickListener(this);
            leg_byes_txt.setOnClickListener(this);
            granted_txt.setOnClickListener(this);
        } else {
            dot_txt.setOnClickListener(null);
            one_run_txt.setOnClickListener(null);
            two_run_txt.setOnClickListener(null);
            three_run_txt.setOnClickListener(null);
            four_run_txt.setOnClickListener(null);
            bfour_txt.setOnClickListener(null);
            bSix_txt.setOnClickListener(null);
            legal_ball_txt.setOnClickListener(null);
            wide_txt.setOnClickListener(null);
            no_ball_txt.setOnClickListener(null);
            byes_txt.setOnClickListener(null);
            leg_byes_txt.setOnClickListener(null);
            granted_txt.setOnClickListener(null);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        swap_batsman = menu.findItem(R.id.swap_batsman);
        undo = menu.findItem(R.id.undo);

        redo = menu.findItem(R.id.redo);
        view_scoreCard = menu.findItem(R.id.view_icon);


        swap_batsman.setVisible(true);
        undo.setVisible(true);
        redo.setVisible(true);
        view_scoreCard.setVisible(true);

        swap_batsman.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                swapStriker(true, swapCount % 2 != 0);
                swapCount += 1;
                return false;
            }
        });
        undo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                undoCount = 1;
                checkUnOrRedo();
                scoreBoardFragment.updateUI(detailedScoreBoardData.getScoreBoardData());
                return false;
            }
        });
        redo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                redoCount = 1;
                checkUnOrRedo();
                scoreBoardFragment.updateUI(detailedScoreBoardData.getScoreBoardData());
                return false;
            }
        });
        view_scoreCard.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SessionSave.saveSession("sdata", CommanData.toString(detailedScoreBoardData.getScoreBoardData()), getActivity());
                Bundle b = new Bundle();
                MatchDetailActivity fragment = new MatchDetailActivity();
                b.putInt("match_id", matchDetails.getMatch_id());
                b.putString("mss", matchDetails.getmatchShortSummary());
                // Toast.makeText(context,  String.valueOf(md.getMatch_id()), Toast.LENGTH_SHORT).show();
                fragment.setArguments(b);
                ((MainFragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainFrag, fragment).commit();
                return false;
            }
        });
        getActivity().invalidateOptionsMenu();
        super.onPrepareOptionsMenu(menu);
    }


    private void checkAndUpdateUI() {

        if (detailedScoreBoardData != null)
            scoreBoardFragment.updateUI(detailedScoreBoardData.getScoreBoardData());

        if (checkPlayerNotNull()) {


            checkUnOrRedo();
            if (askBowler) {

                assignToPlayer = 2;

                hideEntry(getString(R.string.select_current_bowler));
                boolean ishome = false;
                if (!matchDetails.isHomeTeamBatting())
                    ishome = true;
                System.out.println("______DDcallinit3" + ishome);
                ((MainFragmentActivity) getActivity()).showSelectplayer(matchDetails.getMatch_id(), ishome, current_bowler, getString(R.string.current_bowler), assignToPlayer);
            }

            normal_delivery = true;
            runs = 0;
            clearLegalSelection();
            clearRunSelection();
            legal_ball_txt.setTextColor(COLOR_SELECT);
            checkInningsGoingOn();
            System.out.println("batsman_switched_____" + detailedScoreBoardData.getScoreBoardData().isBatsmanSwitched());
//            if (detailedScoreBoardData.getScoreBoardData().isBatsmanSwitched())
//                scoreBoardFragment.swapStrikerText(detailedScoreBoardData.getScoreBoardData(), true, true);
            // dot_txt.setTextColor(COLOR_SELECT);
        }
    }

    private void resumeMatch(MatchDetails matchDetails) {

        if (matchDetails.getMatchStatus() == CommanData.MATCH_NOT_YET_STARTED) {
            if (checkPlayerNotNull())
                initialData();
        } else {
            System.out.println("_______Resuming");
            //to get last updated item from db
            RealmResults<InningsData> thisInningsData = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted())
                    .findAllSorted("delivery", Sort.ASCENDING);
            if (thisInningsData.size() > 0) {
                lastInningsDataItem = thisInningsData.last();
                detailedScoreBoardData = CommanData.fromJson(lastInningsDataItem.getDetailedScoreBoardData(), DetailedScoreData.class);

                if (detailedScoreBoardData != null) {

                    if (checkInningsGoingOn()) {

                        if (detailedScoreBoardData.getScoreBoardData().isBatsmanSwitched()) {
                            non_striker = RealmDB.getPlayer(realm, lastInningsDataItem.getStriker());
                            striker = RealmDB.getPlayer(realm, lastInningsDataItem.getNonStriker());
                        } else {
                            striker = RealmDB.getPlayer(realm, lastInningsDataItem.getStriker());
                            non_striker = RealmDB.getPlayer(realm, lastInningsDataItem.getNonStriker());
                        }
                        if (detailedScoreBoardData.getScoreBoardData().isBowlerSwitched()) {
                        } else {
                            current_bowler = RealmDB.getPlayer(realm, lastInningsDataItem.getCurrentBowler());
                            next_bowler = RealmDB.getPlayer(realm, lastInningsDataItem.getNextBowler());
                        }

                        //To check last ball is wicket
                        if (detailedScoreBoardData.getScoreBoardData().getWicket() != null) {
                            Wicket lwicket = CommanData.fromJson(detailedScoreBoardData.getScoreBoardData().getWicket(), Wicket.class);
                            if (lwicket.getBatsman() == striker.getpID())
                                striker = null;
                            else
                                non_striker = null;
                        }
                        checkAndUpdateUI();

                    } else {
                        scoreBoardFragment.updateUI(detailedScoreBoardData.getScoreBoardData());
                    }
                }
            }
        }
    }

    private void submitbuttonClicked(int delay) {
        if (checkInningsGoingOn()) {
            submitbuttonClicked(null);
            setInputListner(false);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                setInputListner(true);
            }
        }, delay);
    }

    private void submitbuttonClicked(Wicket wicket) {

        if (checkPlayerNotNull()) {
            int totalSizes = 0;
            if (realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id())
                    .equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).notEqualTo("delivery", 0).max("delivery") != null)
                totalSizes =
                        realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id())
                                .equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).notEqualTo("delivery", 0).max("delivery").intValue();
            final int totalSize = totalSizes;

            RealmResults<InningsData> result = realm.where(InningsData.class).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted())
                    .between("delivery", lastInningsDataItem.getDelivery() + 1, totalSize)
                    .notEqualTo("delivery", 0).findAll();
            System.out.println("nnnnnnnss" + totalSize + "-____" + result.size());

            //check undo or redo
            if (lastInningsDataItem != null && lastInningsDataItem.getDelivery() != totalSize) {
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
                // RealmResults<InningsData> result = realm.where(InningsData.class).between("delivery", lastInningsDataItem.getDelivery() + 1, totalSize).findAll();
                System.out.println("nnnnnnn" +
                        "" + lastInningsDataItem.getDelivery() + "___" + totalSize + "__" + result.size());
                // if (checkPlayerNotNull()) {
                System.out.println("nnnnnnnnnnz" + result.size());
                removeBallsFromProfile(totalSize);
                System.out.println("nnnnnnn" + totalSize + "-____" + (undoCount));
                //   lastInningsDataItem = realm.where(InningsData.class).findAll().get(realm.where(InningsData.class).findAll().size() - 1);
                lastInningsDataItem = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted())
                        .findAllSorted("delivery", Sort.ASCENDING).last();
                System.out.println("nnnnnnn" + totalSize + "-____" + lastInningsDataItem.getDelivery());

                detailedScoreBoardData = CommanData.fromJson(lastInningsDataItem.getDetailedScoreBoardData(), DetailedScoreData.class);

                striker = RealmDB.getPlayer(realm, lastInningsDataItem.getStriker());
                non_striker = RealmDB.getPlayer(realm, lastInningsDataItem.getNonStriker());
                if (lastInningsDataItem.getOver() % 1 != 0)
                    current_bowler = RealmDB.getPlayer(realm, lastInningsDataItem.getCurrentBowler());
                next_bowler = RealmDB.getPlayer(realm, lastInningsDataItem.getNextBowler());

                if (lastInningsDataItem.getWicket() != null) {
                    if (lastInningsDataItem.getWicket().isStrikerOut())
                        striker = null;
                    else
                        non_striker = null;
                }


                //  RealmDB.updateBattingBowlingProfile(realm, matchDetails);


                //   resumeMatch(matchDetails);

//
//                    }
//                });

            }
            redoCount = 0;
            undoCount = 0;
            if (checkPlayerNotNull())
                addScore(wicket);
        }
    }

    private void removeBallsFromProfile(int totalSize) {

        RealmResults<InningsData> result = realm.where(InningsData.class).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).equalTo("match_id", matchDetails.getMatch_id()).between("delivery", lastInningsDataItem.getDelivery() + 1, totalSize).findAll();
        System.out.println("nnnnnnnnnna" + result.size());
        ArrayList<Integer> bf = new ArrayList<>();
        ArrayList<Integer> bwf = new ArrayList<>();
        RealmResults<InningsData> strikers = result.distinct("striker");
        for (int i = 0; i < strikers.size(); i++)
            bf.add(strikers.get(i).getStriker());

        System.out.println("updating battt___" + bf.size());

        result = realm.where(InningsData.class).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).between("delivery", lastInningsDataItem.getDelivery() + 1, totalSize).findAll();
        RealmResults<InningsData> non_strikers = result.distinct("nonStriker");
        for (int i = 0; i < non_strikers.size(); i++)
            bf.add(non_strikers.get(i).getNonStriker());

        result = realm.where(InningsData.class).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).equalTo("match_id", matchDetails.getMatch_id()).between("delivery", lastInningsDataItem.getDelivery() + 1, totalSize).findAll();
        RealmResults<InningsData> currentBowlers = result.distinct("currentBowler");
        for (int i = 0; i < currentBowlers.size(); i++) {
            bwf.add(currentBowlers.get(i).getCurrentBowler());


        }

        result = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).between("delivery", lastInningsDataItem.getDelivery() + 1, totalSize).findAll();
        RealmResults<InningsData> nextBowlers = result.distinct("nextBowler");
        for (int i = 0; i < nextBowlers.size(); i++) {
            bwf.add(nextBowlers.get(i).getNextBowler());
            // System.out.println("updating bowler___next" + currentBowlers.get(i).getCurrentBowler());

        }
        System.out.println("nnnnnnnnnnb" + result.size());

        result = realm.where(InningsData.class).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).equalTo("match_id", matchDetails.getMatch_id()).between("delivery", lastInningsDataItem.getDelivery() + 1, totalSize).findAll();
        realm.beginTransaction();
        if (matchDetails.getMatchStatus() == CommanData.MATCH_NOT_YET_STARTED)
            matchDetails.setMatchStatus(CommanData.MATCH_STARTED_FI);
        result.deleteAllFromRealm();
        realm.commitTransaction();
        System.out.println("nnnnnnnnnnc" + result.size());

        for (Integer data : bf)
            RealmDB.updateBattingProfile(realm, matchDetails, data);
        for (Integer data : bwf)
            RealmDB.updateBowlingProfile(realm, matchDetails, data, legalRun);

        RealmDB.updateWicket(realm, matchDetails);
        System.out.println("Alllllllll" + "___" + bf.size() + "___" + bwf.size());
    }


    private void switchInnings() {
        striker = null;
        non_striker = null;
        current_bowler = null;
        next_bowler = null;
        runs = 0;
        swapCount = 0;
        realm.beginTransaction();
        matchDetails.setFirstInningsCompleted(true);
        matchDetails.setMatchStatus(CommanData.MATCH_BREAK_FI);
        matchDetails.setHomeTeamBatting(!matchDetails.isHomeTeamBatting());
        detailedScoreBoardData = null;
        realm.commitTransaction();
        checkPlayerNotNull();
    }

    public int getNextKey(RealmObject t, String key) {
        try {
            int s = realm.where(t.getClass()).equalTo("match_id", matchDetails.getMatch_id())
                    .equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).max(key).intValue() + 1;
            System.out.println("FFFFFFFFF___" + s + "___" + realm.where(t.getClass()).equalTo("match_id", matchDetails.getMatch_id())
                    .equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).max(key).intValue());
            return s;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("FFFFFFFFF___eeee");
            e.printStackTrace();
            return 0;
        }
    }

    public int getNextBatKey(RealmObject t, String key) {
        try {
            if (realm.where(t.getClass()).equalTo("match_id", matchDetails.getMatch_id()).equalTo("inFirstinnings", !matchDetails.isFirstInningsCompleted()).max(key) != null)
                return realm.where(t.getClass()).equalTo("match_id", matchDetails.getMatch_id()).equalTo("inFirstinnings", !matchDetails.isFirstInningsCompleted()).max(key).intValue() + 1;
            else
                return 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    private boolean checkInningsGoingOn() {

        if (lastInningsDataItem == null) {
            if (matchDetails.getMatchStatus() == CommanData.MATCH_BREAK_FI)
                switchInnings();
            else
                initialData();
            return false;
        }
        System.out.println("_________WWs" + lastInningsDataItem.getOver() + "__" + matchDetails.getOvers());
        boolean overCompleted = (lastInningsDataItem.getOver() >= (matchDetails.getOvers()));
        boolean wicketOver = (RealmDB.noOfWicket(getActivity(), realm, matchDetails.getMatch_id(), !matchDetails.isFirstInningsCompleted()) >= (matchDetails.getTotalPlayers() - 1));
        boolean runChased = false;

        if (matchDetails.isFirstInningsCompleted())
            runChased = RealmDB.getFirstInningsTotal(realm, matchDetails) - lastInningsDataItem.getTotal_score() < 0;
        if ((detailedScoreBoardData != null && (overCompleted || wicketOver || runChased))) {
            System.out.println("_________WWs");
            if (matchDetails.getMatchStatus() == CommanData.MATCH_STARTED_SI) {
                showGameCompleteDailog();

            } else if (matchDetails.getMatchStatus() == CommanData.MATCH_STARTED_FI) {
                System.out.println("_________WWd");
//                if (matchDetails.getMatchStatus() == CommanData.MATCH_BREAK_FI)
//                    return true;
//                else {

                showSwitchInningsDailog();
                return false;
                // }
            } else if (matchDetails.getMatchStatus() == CommanData.MATCH_BREAK_FI) {
                switchInnings();
            } else if (matchDetails.getMatchStatus() == CommanData.MATCH_NOT_YET_STARTED)
                return true;

            return false;
        } else
            return true;
    }

    private void showSwitchInningsDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.confirm));
        builder.setMessage(getString(R.string.first_innings_completed));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        // positive button logic
                        switchInnings();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new android.content.DialogInterface.OnShowListener() {
            @Override
            public void onShow(android.content.DialogInterface dialogs) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            }
        });
        // display dialog
        dialog.show();
    }

    private void showGameCompleteDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.confirm));
        builder.setMessage(getString(R.string.match_completed));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {

                        RealmDB.completeMatch(realm, matchDetails);
                        if (matchDetails.isOnlineMatch())
                            removeMatchFromOncoming(CommanData.fromJson(matchDetails.getmatchShortSummary(), MatchShortSummaryData.class));

                        // SessionSave.saveSession("sdata", CommanData.toString(detailedScoreBoardData.getScoreBoardData()), getActivity());
                        Bundle b = new Bundle();
                        MatchDetailActivity fragment = new MatchDetailActivity();
                        b.putInt("match_id", matchDetails.getMatch_id());
                        // Toast.makeText(context,  String.valueOf(md.getMatch_id()), Toast.LENGTH_SHORT).show();
                        fragment.setArguments(b);
                        ((MainFragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, fragment).commit();
                        // positive button logic

                        //   Toast.makeText(getActivity(), getString(R.string.game_over), Toast.LENGTH_SHORT).show();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new android.content.DialogInterface.OnShowListener() {
            @Override
            public void onShow(android.content.DialogInterface dialogs) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            }
        });
        // display dialog
        dialog.show();
    }

    private boolean checkPlayerNotNull() {
        Log.d("adding Score", "not null");
        boolean ishome = false;
        if (true) {
            if (striker == null) {
                assignToPlayer = 0;
                if (matchDetails.isHomeTeamBatting())
                    ishome = true;
                // selectPlayerDialog(getString(realmstudy.R.string.striker));
                System.out.println("______DDcallinit1" + ishome);
                hideEntry(getString(R.string.select_striker));
                ((MainFragmentActivity) getActivity()).showSelectplayer(matchDetails.getMatch_id(), ishome, current_bowler, getString(realmstudy.R.string.striker), assignToPlayer);
                return false;
            } else if (non_striker == null) {
                assignToPlayer = 1;
                if (matchDetails.isHomeTeamBatting())
                    ishome = true;
                System.out.println("______DDcallinit2" + ishome);
                hideEntry(getString(R.string.select_non_striker));
                ((MainFragmentActivity) getActivity()).showSelectplayer(matchDetails.getMatch_id(), ishome, current_bowler, getString(R.string.non_striker), assignToPlayer);
                // selectPlayerDialog(getString(realmstudy.R.string.non_striker));
                return false;
            } else if (current_bowler == null) {
                assignToPlayer = 2;

                hideEntry(getString(R.string.select_current_bowler));
                if (!matchDetails.isHomeTeamBatting())
                    ishome = true;
                System.out.println("______DDcallinit3" + ishome);
                ((MainFragmentActivity) getActivity()).showSelectplayer(matchDetails.getMatch_id(), ishome, current_bowler, getString(R.string.current_bowler), assignToPlayer);
                // selectPlayerDialog(getString(realmstudy.R.string.current_bowler));
                return false;
            }

//            else if (lastInningsDataItem != null && lastInningsDataItem.getWicket() != null && detailedScoreBoardData != null && detailedScoreBoardData.getNextBatsmanName() == null) {
//                assignToPlayer = 5;
//                ishome = false;
//                if (matchDetails.isHomeTeamBatting())
//                    ishome = true;
//                // selectPlayerDialog(getString(realmstudy.R.string.striker));
//                System.out.println("______DDcallinit1" + ishome);
//                ((MainFragmentActivity) getActivity()).showSelectplayer(matchDetails.getMatch_id(), ishome, current_bowler, getString(realmstudy.R.string.next_batsman), assignToPlayer);
//                return false;
//            }

            else if (detailedScoreBoardData == null) {
                System.out.println("______DDcallinit4" + ishome);
//            detailedScoreBoardData = new ScoreBoardData();
//            detailedScoreBoardData.setTotalRuns(0);
//            detailedScoreBoardData.setTotalBalls(0);
                initialData();
                return false;
            } else {
                showEntry();
                return true;
            }
        } else
            return false;
        //  return true;
    }

    @Override
    public void onStop() {
//        if (matchDetails.getMatchStatus() != CommanData.MATCH_NOT_YET_STARTED)
        if (striker != null && RealmDB.getBattingProfile(realm, striker.getpID(), matchDetails.getMatch_id()).getBallFaced() == 0) {
            BatingProfile bf = RealmDB.getBattingProfile(realm, striker.getpID(), matchDetails.getMatch_id());
            realm.beginTransaction();
            bf.setCurrentStatus(CommanData.StatusFree);
            realm.commitTransaction();
        }
        if (non_striker != null && RealmDB.getBattingProfile(realm, non_striker.getpID(), matchDetails.getMatch_id()).getBallFaced() == 0) {
            BatingProfile bf = RealmDB.getBattingProfile(realm, non_striker.getpID(), matchDetails.getMatch_id());
            realm.beginTransaction();
            bf.setCurrentStatus(CommanData.StatusFree);
            realm.commitTransaction();
        }
        if (current_bowler != null && RealmDB.getBowlingProfile(realm, current_bowler.getpID(), matchDetails.getMatch_id()).getBallsBowled() == 0) {
            BowlingProfile bf = RealmDB.getBowlingProfile(realm, current_bowler.getpID(), matchDetails.getMatch_id());
            realm.beginTransaction();
            bf.setCurrentBowlerStatus(CommanData.StatusFree);
            realm.commitTransaction();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (selectPlayerDialog != null)
            selectPlayerDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onSuccess(String result, boolean success) {

    }

    /**
     * Set onClick listner for views
     *
     * @param v --> view that get clicked
     */
    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case realmstudy.R.id.dot_txt:
                System.out.println("________dottt");
                runs = 0;
                clearRunSelection();
                submitbuttonClicked(SUBMIT_DELAY);
                break;
            case realmstudy.R.id.one_run_txt:
                runs = 1;
                clearRunSelection();
                submitbuttonClicked(SUBMIT_DELAY);
                break;
            case realmstudy.R.id.two_run_txt:
                runs = 2;
                clearRunSelection();
                submitbuttonClicked(SUBMIT_DELAY);
                break;
            case realmstudy.R.id.three_run_txt:
                runs = 3;
                clearRunSelection();
                submitbuttonClicked(SUBMIT_DELAY);
                break;
            case realmstudy.R.id.four_run_txt:
                runs = 4;
                clearRunSelection();
                submitbuttonClicked(SUBMIT_DELAY);
                break;
            case realmstudy.R.id.bfour_txt:
                runs = 4;
                clearRunSelection();
                submitbuttonClicked(SUBMIT_DELAY);
                break;
            case realmstudy.R.id.bSix_txt:
                runs = 6;
                clearRunSelection();

                submitbuttonClicked(SUBMIT_DELAY);
                break;
            case realmstudy.R.id.wide_txt:
                normal_delivery = false;
                extraType = CommanData.typeExtraEnum.WIDE;
                clearLegalSelection();
                break;
            case realmstudy.R.id.no_ball_txt:
                normal_delivery = false;
                extraType = CommanData.typeExtraEnum.NO_BALL;
                clearLegalSelection();
                break;
            case realmstudy.R.id.byes_txt:
                normal_delivery = false;
                extraType = CommanData.typeExtraEnum.L_BYES;
                clearLegalSelection();
                break;
            case realmstudy.R.id.leg_byes_txt:
                normal_delivery = false;
                extraType = CommanData.typeExtraEnum.LEG_BYES;
                clearLegalSelection();
                break;
            case realmstudy.R.id.granted_txt:
                normal_delivery = false;
                extraType = CommanData.typeExtraEnum.GRANTED;
                clearLegalSelection();
                break;
            case realmstudy.R.id.legal_ball_txt:
                normal_delivery = true;
                clearLegalSelection();
                break;

        }

        ((TextView) v).setTextColor(COLOR_SELECT);
    }

    /**
     * Set all text color of normal_delivery layout to unselect
     */
    private void clearLegalSelection() {
        wide_txt.setTextColor(COLOR_UNSELECT);
        no_ball_txt.setTextColor(COLOR_UNSELECT);
        byes_txt.setTextColor(COLOR_UNSELECT);
        leg_byes_txt.setTextColor(COLOR_UNSELECT);
        granted_txt.setTextColor(COLOR_UNSELECT);
        legal_ball_txt.setTextColor(COLOR_UNSELECT);

    }

    /**
     * Set all text color of Run layout to unselect
     */
    private void clearRunSelection() {
        one_run_txt.setTextColor(COLOR_UNSELECT);
        two_run_txt.setTextColor(COLOR_UNSELECT);
        three_run_txt.setTextColor(COLOR_UNSELECT);
        four_run_txt.setTextColor(COLOR_UNSELECT);
        bfour_txt.setTextColor(COLOR_UNSELECT);
        bSix_txt.setTextColor(COLOR_UNSELECT);
        dot_txt.setTextColor(COLOR_UNSELECT);


    }

    /**
     * Add score to database
     */
    private void addScore(Wicket wicket) {
        Log.d("adding Score", "1");
        InningsData inningsData = RealmDB.getInningsData(getActivity(), realm,
                lastInningsDataItem != null ? (lastInningsDataItem.getDelivery() + 1) : 0,
                matchDetails.getMatch_id(), matchDetails.isFirstInningsCompleted());
        ScoreBoardData score_data = null;
        List<OverAdapterData> overAdapterData = null;
        ScoreCardDetailData firstInningsscoreCardDetailData = null;
        ScoreCardDetailData secInningsscoreCardDetailData = null;
        realm.beginTransaction();
        swapCount = 0;
        if (matchDetails.getMatchStatus() == CommanData.MATCH_NOT_YET_STARTED)
            matchDetails.setMatchStatus(CommanData.MATCH_STARTED_FI);
        else if (matchDetails.getMatchStatus() == CommanData.MATCH_BREAK_FI)
            matchDetails.setMatchStatus(CommanData.MATCH_STARTED_SI);

        inningsData.setNormalDelivery(normal_delivery);
        if (normal_delivery)
            inningsData.setBallType(CommanData.BALL_LEGAL);
        else
            inningsData.setBallType(extraType);
        inningsData.setMatch_id(matchDetails.getMatch_id());
        inningsData.setRun(runs);
        // inningsData.setScoreBoardData(CommanData.toString(score_data));
        inningsData.setStriker(striker.getpID());
        inningsData.setFirstInnings(!matchDetails.isFirstInningsCompleted());
        inningsData.setNonStriker(non_striker.getpID());
        inningsData.setCurrentBowler(current_bowler.getpID());
        inningsData.setNextBowler(next_bowler == null ? -1 : next_bowler.getpID());
        inningsData.setWicket(wicket);
        inningsData.setDelivery(getNextKey(inningsData, "delivery"));
        if (!normal_delivery && (extraType == CommanData.typeExtraEnum.NO_BALL || extraType == CommanData.typeExtraEnum.WIDE)) {
            System.out.println("_______OVer" + lastInningsDataItem.getOver() + "__" + Math.floor(lastInningsDataItem.getOver()) + "__" + (lastInningsDataItem.getOver() - Math.floor(lastInningsDataItem.getOver())));
            inningsData.setOver(lastInningsDataItem.getOver());
        } else {

            float over = RealmDB.BallinWhichOver(realm, matchDetails);
            inningsData.setOver(over);
        }
        BowlingProfile current_bowler_bf = RealmDB.getBowlingProfile(realm, current_bowler.getpID(), matchDetails.getMatch_id());
        //setting status of players
        BatingProfile StrikerProf = RealmDB.getBattingProfile(realm, striker.getpID(), matchDetails.getMatch_id());
        BatingProfile nonStrikerProf = RealmDB.getBattingProfile(realm, non_striker.getpID(), matchDetails.getMatch_id());
        if (StrikerProf.getBattedAt() == 0) {


            StrikerProf.setBattedAt(getNextBatKey(StrikerProf, "battedAt") == 0 ? 1 : getNextBatKey(StrikerProf, "battedAt"));
        }
        if (nonStrikerProf.getBattedAt() == 0) {
            nonStrikerProf.setBattedAt(getNextBatKey(nonStrikerProf, "battedAt") == 0 ? 2 : getNextBatKey(nonStrikerProf, "battedAt"));
        }
        StrikerProf.setCurrentStatus(CommanData.StatusBatting);
        nonStrikerProf.setCurrentStatus(CommanData.StatusBatting);
        current_bowler_bf.setCurrentBowlerStatus(CommanData.StatusBowling);
        striker.setStatus(CommanData.StatusInMatch);
        non_striker.setStatus(CommanData.StatusInMatch);
        current_bowler.setStatus(CommanData.StatusInMatch);


        realm.commitTransaction();


        RealmDB.updateBowlingProfile(realm, matchDetails, current_bowler.getpID(), legalRun);
        RealmDB.updateBattingProfile(realm, matchDetails, striker.getpID());
        RealmDB.updateWicket(realm, matchDetails);
        if (next_bowler != null)
            RealmDB.updateBowlingProfile(realm, matchDetails, next_bowler.getpID(), legalRun);
        if (non_striker != null)
            RealmDB.updateBattingProfile(realm, matchDetails, non_striker.getpID());

        realm.beginTransaction();
        score_data = setScoreDataForDB(wicket, inningsData.getOver(), inningsData);

        if (!matchDetails.isFirstInningsCompleted()) {
            overAdapterData = setOverDataForDB(true);
            firstInningsscoreCardDetailData = createScoreDetailData(true, score_data);
        } else {
            firstInningsscoreCardDetailData = createScoreDetailData(false, score_data);
            secInningsscoreCardDetailData = createScoreDetailData(true, score_data);
            overAdapterData = setOverDataForDB(false);
            overAdapterData.addAll(setOverDataForDB(true));
        }
        DetailedScoreData detailedScoreData = new DetailedScoreData();
        detailedScoreData.setOverAdapterData(overAdapterData);
        detailedScoreData.setScoreCardDetailData(firstInningsscoreCardDetailData);
        if (secInningsscoreCardDetailData != null)
            detailedScoreData.setSecscoreCardDetailData(secInningsscoreCardDetailData);
        detailedScoreData.setScoreBoardData(score_data);
        inningsData.setTotal_score(score_data.getTotalRuns());
        inningsData.setDetailedScoreBoardData(CommanData.toString(detailedScoreData));
        realm.commitTransaction();
        lastInningsDataItem = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).findAll().last();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("InningsDetailData/" + String.valueOf(matchDetails.getMatch_id()));

        myRef.setValue(lastInningsDataItem.getDetailedScoreBoardData());

        detailedScoreBoardData = CommanData.fromJson(lastInningsDataItem.getDetailedScoreBoardData(), DetailedScoreData.class);
        checkAndUpdateUI();

    }

    public ScoreCardDetailData createScoreDetailData(boolean forCurrentBattingTeam, ScoreBoardData scData) {
        ScoreCardDetailData scoreCardDetailData = new ScoreCardDetailData();

        InningsData InningsData = null;
        RealmResults<BatingProfile> battingProfiles = null;
        RealmResults<BowlingProfile> bowlingProfiles = null;
        RealmResults<InningsData> fow = null;
        RealmResults<InningsData> extraTypes = null;
        if (forCurrentBattingTeam) {
            scoreCardDetailData.setTeamName(matchDetails.getCurrentBattingTeam().nick_name);
            InningsData = lastInningsDataItem;
            battingProfiles = realm.where(BatingProfile.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("inFirstinnings", !matchDetails.isFirstInningsCompleted())
                    .notEqualTo("currentStatus", CommanData.StatusFree)
                    .greaterThan("battedAt", 0).findAll();
            bowlingProfiles = realm.where(BowlingProfile.class)
                    .greaterThan("ballsBowled", 0)
//                    .equalTo("currentBowlerStatus", CommanData.StatusBowling)
                    .equalTo("match_id", matchDetails.getMatch_id()).equalTo("inFirstinnings", !matchDetails.isFirstInningsCompleted()).findAll();
            fow = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).isNotNull("wicket").findAllSorted("delivery", Sort.ASCENDING);
            extraTypes = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).notEqualTo("ballType", 0).findAll();
            scoreCardDetailData.setTeamRun_over(scData.getTotalRuns() + "-" + scData.getTotal_wicket() + "(" + scData.getTotalOver() + ")");
        } else {

            InningsData = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", true).findAllSorted("delivery", Sort.DESCENDING).first();
            battingProfiles = realm.where(BatingProfile.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("inFirstinnings", true)
                    .notEqualTo("currentStatus", CommanData.StatusFree).greaterThan("battedAt", 0).findAllSorted("battedAt", Sort.ASCENDING);
            bowlingProfiles = realm.where(BowlingProfile.class)
                    .greaterThan("ballsBowled", 0)
//                    .equalTo("currentBowlerStatus", CommanData.StatusBowling)
                    .equalTo("match_id", matchDetails.getMatch_id()).equalTo("inFirstinnings", matchDetails.isFirstInningsCompleted()).findAll();

            fow = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", !matchDetails.isHomeTeamBattingFirst()).isNotNull("wicket").findAllSorted("delivery", Sort.ASCENDING);
            extraTypes = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", !matchDetails.isHomeTeamBattingFirst()).notEqualTo("ballType", 0).findAll();
            if (firstInningsOver == null) {
                MatchShortSummaryData matchShortSummaryData = CommanData.fromJson(matchDetails.getmatchShortSummary(), MatchShortSummaryData.class);
                firstInningsRun = matchShortSummaryData.getFirstInningsSummary().run;
                firstInningsWicket = matchShortSummaryData.getFirstInningsSummary().wicket;
                firstInningsOver = matchShortSummaryData.getFirstInningsSummary().overs;
            }

            boolean ishomeBatFirst = matchDetails.isHomeTeamBattingFirst();

            String teamName;
//            if (ishomeBatFirst) {
//                if (!matchDetails.isFirstInningsCompleted())
//                    teamName = matchDetails.getHomeTeam().nick_name;
//                else
//                    teamName = matchDetails.getAwayTeam().nick_name;
//            } else {
//                if (!matchDetails.isFirstInningsCompleted())
//                    teamName = matchDetails.getAwayTeam().nick_name;
//                else
//                    teamName = matchDetails.getHomeTeam().nick_name;
//            }

            scoreCardDetailData.setTeamName(matchDetails.getCurrentBowlingTeam().nick_name);
            scoreCardDetailData.setTeamRun_over(firstInningsRun + "-" + firstInningsWicket + "(" + firstInningsOver + ")");
        }
        //scData = CommanData.fromJson(InningsData.getDetailedScoreBoardData(), ScoreBoardData.class);
        // if((matchDetails.isHomeTeamBatting() && forHomeTeam) && (!matchDetails.isHomeTeamBatting() && !forHomeTeam))

        // else

        System.out.println("naaaaaaa" + !matchDetails.isHomeTeamBattingFirst() + "__" + battingProfiles.size() + "__" + bowlingProfiles.size());
        for (int i = 0; i < battingProfiles.size(); i++) {
            ScoreCardDetailData.BatsmanDetail data = new ScoreCardDetailData.BatsmanDetail();
            data.balls = battingProfiles.get(i).getBallFaced();
            data.name = RealmDB.getPlayer(realm, battingProfiles.get(i).getPlayerID()).getName();
            if (battingProfiles.get(i).getWicket() != null)
                data.outAs = wicketToString(battingProfiles.get(i).getWicket());
            else
                data.outAs = getString(R.string.batting);
            data.runs = battingProfiles.get(i).getRuns();
            data.fours = battingProfiles.get(i).getFours();
            data.sixes = battingProfiles.get(i).getSixes();
            if (battingProfiles.get(i).getBallFaced() != 0) {
                data.strike_rate = CommanData.getStrikeRate(battingProfiles.get(i).getBallFaced(), battingProfiles.get(i).getRuns());
                System.out.println("_____hiii" + data.strike_rate);
            }
            scoreCardDetailData.addBatsmanDetails(data);

        }


        for (int i = 0; i < bowlingProfiles.size(); i++) {
            ScoreCardDetailData.BowlersDetail data = new ScoreCardDetailData.BowlersDetail();
            // System.out.println("eeee"+bowlingProfiles.get(i).getPlayerID());
            data.name = RealmDB.getPlayer(realm, bowlingProfiles.get(i).getPlayerID()).getName();
            data.outAs = String.valueOf(bowlingProfiles.get(i).getWickets().size());
            data.runs = bowlingProfiles.get(i).getRunsGranted();
            data.overs = bowlingProfiles.get(i).OversBowled();
            data.maiden = bowlingProfiles.get(i).getMaiden();
            data.ecnomic_rate = CommanData.getER(data.runs, data.overs);
            data.wicket = bowlingProfiles.get(i).getWickets().size();
            scoreCardDetailData.addBowlersDetails(data);

        }

        for (int i = 0; i < fow.size(); i++) {
            ScoreCardDetailData.FOW data = new ScoreCardDetailData.FOW();
            data.name = RealmDB.getPlayer(realm, fow.get(i).getWicket().getBatsman()).getName();
            data.score = (fow.get(i).getTotal_score());
            data.overs = String.valueOf(fow.get(i).getOver());
            scoreCardDetailData.addFow(data);

        }
        int wide = 0, lb = 0, b = 0, noball = 0;
        for (int i = 0; i < extraTypes.size(); i++) {
            if (extraTypes.get(i).getBallType() == CommanData.BALL_WIDE) {
                wide += MainActivity.legalRun + extraTypes.get(i).getRun();
            }
            if (extraTypes.get(i).getBallType() == CommanData.BALL_NO_BALL || extraTypes.get(i).getBallType() == CommanData.BALL_NO_OVER_STEP)
                noball += MainActivity.legalRun + extraTypes.get(i).getRun();
            if (extraTypes.get(i).getBallType() == CommanData.BALL_LEGAL_BYES)
                b += extraTypes.get(i).getRun();
            if (extraTypes.get(i).getBallType() == CommanData.BALL_LB)
                lb += extraTypes.get(i).getRun();


        }
        scoreCardDetailData.setTotal_extras(wide + lb + b + noball);
        scoreCardDetailData.setExtras_detail("wd " + wide + ",b " + b + ",lb " + lb + ",nb " + noball);
        if (InningsData.getDelivery() != 0)
            scoreCardDetailData.setCurrent_run_rate(InningsData.getRun() / InningsData.getDelivery());
        return scoreCardDetailData;
    }

    public String wicketToString(Wicket wicket) {
        String s = "";
        String as = "";
        if (wicket.getType() == CommanData.W_CAUGHT)
            as = RealmDB.getPlayer(realm, wicket.getCaughtBy()).getName();
        else if (wicket.getType() == CommanData.W_RUNOUT)
            as = RealmDB.getPlayer(realm, wicket.getRunoutBy()).getName();


        s += wicketIdToString(wicket.getType()) + " " + as + " b " + RealmDB.getPlayer(realm, wicket.getBowler()).getName();
        return s;
    }

    private List<OverAdapterData> setOverDataForDB(boolean isFirstInnings) {
        List<OverAdapterData> datas = new ArrayList<>();
        RealmResults<InningsData> data = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id())
                .equalTo("firstInnings", isFirstInnings).notEqualTo("delivery", 0).findAllSorted("delivery", Sort.DESCENDING);
        // recOver = data.get(0).getOver();
        Set<String> batsmans = new TreeSet<>();
        Set<String> bowlers = new TreeSet<>();
        int total_run = 0;
        int extras = 0;
        float epsilon = (float) 0.00000001;
        ArrayList<String> deliveries = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            InningsData currentdata = data.get(i);
            boolean isOverCompleted = false;
            if (i != (data.size() - 1))
                isOverCompleted = data.get(i + 1).isOversCompleted();
            batsmans.add(RealmDB.getPlayer(realm, currentdata.getStriker()).getName());
            batsmans.add(RealmDB.getPlayer(realm, currentdata.getNonStriker()).getName());
            bowlers.add(RealmDB.getPlayer(realm, currentdata.getCurrentBowler()).getName());
            deliveries.add(Util.get_delivery_result(currentdata.getRun()
                    , currentdata.getWicket(), currentdata.isLegal(), currentdata.getBallType()));
            total_run += currentdata.getRun();

            if (currentdata.getBallType() == CommanData.BALL_WIDE || currentdata.getBallType() == CommanData.BALL_NO_BALL)
                total_run += legalRun;
//            total_run += extras;
            System.out.println("_______****ssvv" + datas.size());

            if (isOverCompleted || i == (data.size() - 1)) {

                String batsmansString = "";
                String bowlersString = "";

                Iterator batsmansIterator = batsmans.iterator();
                Iterator bowlersIterator = bowlers.iterator();
                System.out.println("_______****ssve" + (currentdata.getOver() + 1));
                System.out.println("_______****ssvd" + batsmansIterator.hasNext());
                if (batsmans.size() > 0) {
                    System.out.println("_______****ssvg" + batsmansIterator.hasNext());
                    do {
                        batsmansString += batsmansIterator.next() + " & ";
                    } while (batsmansIterator.hasNext());
                    do {
                        bowlersString += bowlersIterator.next() + " & ";
                    } while (bowlersIterator.hasNext());
                    OverAdapterData overData = new OverAdapterData();
                    overData.setBatsmans(batsmansString.substring(0, batsmansString.length() - 3));
                    overData.setBolwers(bowlersString.substring(0, bowlersString.length() - 3));
                    overData.setTotal_run(total_run);
                    int curOver = currentdata.getOver() < 1 ? 1 : (int) Math.floor(currentdata.getOver() + 1);
                    overData.setOver(curOver);
                    ArrayList<String> dd = (ArrayList<String>) deliveries.clone();
                    Collections.reverse(dd);
                    overData.setDeliveries(dd);
                    datas.add(overData);
                    batsmans.clear();
                    bowlers.clear();
                    deliveries.clear();
                    total_run = 0;
                    System.out.println("_______****ssv" + datas.size());
                }
            }

        }


        return datas;

    }


    private ScoreBoardData setScoreDataForDB(Wicket wicket, float over, InningsData inningsData) {
        ScoreBoardData scoreBoardData = detailedScoreBoardData.getScoreBoardData();
        int total_run = scoreBoardData.getTotalRuns() + runs;
        int total_balls = scoreBoardData.getTotalBalls();
        ScoreBoardData score_data = new ScoreBoardData();
        BatingProfile strikerProfile = RealmDB.getBattingProfile(realm, striker.getpID(), matchDetails.getMatch_id());
        BowlingProfile current_bowler_bf = RealmDB.getBowlingProfile(realm, current_bowler.getpID(), matchDetails.getMatch_id());
        BatingProfile non_strikerProfile = RealmDB.getBattingProfile(realm, non_striker.getpID(), matchDetails.getMatch_id());
        boolean legal = true;

        if (!normal_delivery && (extraType == CommanData.typeExtraEnum.WIDE || extraType == CommanData.typeExtraEnum.NO_BALL))
            legal = false;
        if (normal_delivery) {


            //NON_STRIKER
            if (non_striker != null) {
                score_data.nonStriker.setName(non_striker.getName());
                score_data.nonStriker.setRuns(non_strikerProfile.getRuns());
                score_data.nonStriker.setBalls(non_strikerProfile.getBallFaced());
            }
            //CURRENT BOWLER
            if (current_bowler != null) {

                score_data.curr_bowlers.setName(current_bowler.getName());
                score_data.curr_bowlers.setRuns(current_bowler_bf.getRunsGranted());
                int extra_ball = current_bowler_bf.getNoBall() + current_bowler_bf.getWide();
                score_data.curr_bowlers.setBalls(current_bowler_bf.getBallsBowled() - extra_ball);
                score_data.curr_bowlers.setMaiden(current_bowler_bf.getMaiden());
                score_data.curr_bowlers.setWicket(current_bowler_bf.getWickets().size());
            }

            //NEXT BOWLER
            if (next_bowler != null) {
                BowlingProfile nextBowler_bf = RealmDB.getBowlingProfile(realm, next_bowler.getpID(), matchDetails.getMatch_id());
                score_data.next_bowlers.setName(next_bowler.getName());
                score_data.next_bowlers.setRuns(nextBowler_bf.getRunsGranted());
                score_data.next_bowlers.setMaiden(nextBowler_bf.getMaiden());
                score_data.next_bowlers.setWicket(nextBowler_bf.getWickets().size());
                int extra_ball = nextBowler_bf.getNoBall() + nextBowler_bf.getWide();
                score_data.next_bowlers.setWicket(nextBowler_bf.getWickets().size());
                score_data.next_bowlers.setBalls(nextBowler_bf.getBallsBowled() - extra_ball);
                System.out.println("_________nextBowleree" + (nextBowler_bf.getBallsBowled()) + "__" + extra_ball);
            }
            score_data.setTotalRuns(total_run);
            //  score_data.striker.setBalls(strikerProfile.getBallFaced());


            total_balls = total_balls + 1;
        } else {

            if (legal) {
                if (current_bowler != null) {
                    score_data.curr_bowlers.setName(current_bowler.getName());
                    score_data.curr_bowlers.setRuns(current_bowler_bf.getRunsGranted());
                }
            }


            score_data.nonStriker.setRuns(non_strikerProfile.getRuns());
//
            //NON_STRIKER
            score_data.nonStriker.setName(non_striker.getName());
            score_data.nonStriker.setBalls(non_strikerProfile.getBallFaced());
            score_data.nonStriker.setRuns(non_strikerProfile.getRuns());
            score_data.nonStriker.setFours(non_strikerProfile.getFours());
            score_data.nonStriker.setSixes(non_strikerProfile.getSixes());

            //CURRENT BOWLER
            score_data.curr_bowlers.setName(current_bowler.getName());
            score_data.curr_bowlers.setName(current_bowler.getName());
            int extra_ball = current_bowler_bf.getNoBall() + current_bowler_bf.getWide();
            score_data.curr_bowlers.setBalls(current_bowler_bf.getBallsBowled() - extra_ball);
            score_data.curr_bowlers.setRuns(current_bowler_bf.getRunsGranted());
            score_data.curr_bowlers.setMaiden(current_bowler_bf.getMaiden());
            score_data.curr_bowlers.setWicket(current_bowler_bf.getWickets().size());

            switch (extraType) {

                case WIDE:
                    current_bowler_bf.setWide(current_bowler_bf.getWide() + 1);
                    break;
                case NO_BALL:
                    current_bowler_bf.setNoBall(current_bowler_bf.getNoBall() + 1);
                    break;
                case L_BYES:
                    current_bowler_bf.setByes(current_bowler_bf.getByes() + 1);
                    break;
                case LEG_BYES:
                    current_bowler_bf.setByes(current_bowler_bf.getByes() + 1);
                    break;
            }


            //NEXT BOWLER
            if (next_bowler != null) {
                BowlingProfile nextBowler_bf = RealmDB.getBowlingProfile(realm, next_bowler.getpID(), matchDetails.getMatch_id());
                score_data.next_bowlers.setName(next_bowler.getName());
                score_data.next_bowlers.setRuns(nextBowler_bf.getRunsGranted());
                score_data.next_bowlers.setMaiden(nextBowler_bf.getMaiden());
                System.out.println("_________nextBowlers" + (RealmDB.getBowlingProfile(realm, next_bowler.getpID(), matchDetails.getMatch_id()).getBallsBowled()));
                int extra_balls = nextBowler_bf.getNoBall() + nextBowler_bf.getWide();
                score_data.next_bowlers.setBalls(nextBowler_bf.getBallsBowled() - extra_balls);
                score_data.next_bowlers.setWicket(nextBowler_bf.getWickets().size());
            } else {

            }
//Totals

            //CURRENT BOWLER
            if (extraType == CommanData.typeExtraEnum.L_BYES || extraType == CommanData.typeExtraEnum.LEG_BYES) {
                score_data.setTotalRuns(total_run + 0);
                total_balls = total_balls + 1;
            } else {
                score_data.setTotalRuns(total_run + legalRun);
            }


        }
        int fstInningsTotal = 0;
        score_data.setFirstInnings(!matchDetails.isFirstInningsCompleted());
        if (matchDetails.isFirstInningsCompleted()) {
            fstInningsTotal = RealmDB.getFirstInningsTotal(realm, matchDetails);
            int overRemaining = (matchDetails.getOvers() * 6) - total_balls;
            score_data.setReqRunRate(CommanData.getReqRunRate(fstInningsTotal, total_balls, total_run, overRemaining));
        }

        detailedScoreBoardData.setScoreBoardData(score_data);
        updateStriker(detailedScoreBoardData, strikerProfile);
//        score_data.striker.setFours(strikerProfile.getFours());
//        score_data.striker.setSixes(strikerProfile.getSixes());
        score_data.nonStriker.setFours(non_strikerProfile.getFours());
        score_data.nonStriker.setSixes(non_strikerProfile.getSixes());
        System.out.println("_________sixes" + strikerProfile.getRuns() + "__" + strikerProfile.getSixes() + "__" + non_strikerProfile.getRuns() + "__" + non_strikerProfile.getSixes() + "__");
        boolean current_overCompleted = false;
        if (legal)
            current_overCompleted = (over % 1 == 0);
        score_data.setTotalBalls(total_balls);
        score_data.nonStriker.setBalls(non_strikerProfile.getBallFaced());
        int totalWicket = RealmDB.noOfWicket(getActivity(), realm, matchDetails.getMatch_id(), !matchDetails.isFirstInningsCompleted());
        score_data.setTotal_wicket(totalWicket);
        if (redoCount == 0 && undoCount == 0) {
            if (scoreBoardData.getTotalBalls() == 0) {
                if (runs % 2 == 1) {
                    score_data.setBatsmanSwitched(true);
                    score_data.setAskNextBowler(false);

                    // Toast.makeText(getActivity(), "switching12", Toast.LENGTH_SHORT).show();
                } else score_data.setBatsmanSwitched(false);
            } else {
                if (!normal_delivery && (extraType == CommanData.typeExtraEnum.NO_BALL || extraType == CommanData.typeExtraEnum.WIDE)) {
                    score_data.setAskNextBowler(false);
                } else {
                    if (current_overCompleted && over < matchDetails.getOvers()) {
                        // Toast.makeText(getActivity(), "asssssss" + matchDetails.getMatchStatus(), Toast.LENGTH_SHORT).show();

                        //  next_bowler = current_bowler;
                        askBowler = true;
                        assignToPlayer = 2;
                        boolean ishome = false;
                        if (!matchDetails.isHomeTeamBatting())
                            ishome = true;

                        hideEntry(getString(R.string.select_current_bowler));
                        ((MainFragmentActivity) getActivity()).showSelectplayer(matchDetails.getMatch_id(), ishome, current_bowler, getString(R.string.next_bowler), assignToPlayer);
                        score_data.setAskNextBowler(true);

                    } else
                        score_data.setAskNextBowler(false);
                }
                boolean oddRunAndOverNotComplete = ((runs % 2 == 1 && (scoreBoardData.getTotalBalls() + (legal ? 1 : 0)) % 6 != 0));
                boolean evenRunOverComplete = (runs % 2 == 0 && (scoreBoardData.getTotalBalls() + (legal ? 1 : 0)) % 6 == 0);

                if (runs % 2 == 0)
                    if ((scoreBoardData.getTotalBalls() + (legal ? 1 : 0)) % 6 == 0) {
                        if (!legal) {
                            InningsData prevDel = RealmDB.getInningsData(getActivity(), realm, inningsData.getIndex() - 1, matchDetails.getMatch_id(), !matchDetails.isFirstInningsCompleted());
                            if (prevDel.isOversCompleted() == true)
                                evenRunOverComplete = false;
                        }
                    }
                //  System.out.println("switching123_____" + oddRunAndOverNotComplete + "___" + evenRunOverComplete);
                if ((oddRunAndOverNotComplete
                        || evenRunOverComplete) && checkInningsGoingOn()) {
                    // Toast.makeText(getActivity(), "switching_____", Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(getActivity(), "switching123", Toast.LENGTH_SHORT).show();
                    /**
                     * not to swap batsmans if wicket gone to avoid confusion
                     */
                    if (wicket != null)
                        score_data.setBatsmanSwitched(false);
                    else
                        score_data.setBatsmanSwitched(true);
                } else
                    score_data.setBatsmanSwitched(false);
            }
        }

        if (score_data.isBatsmanSwitched())
            swapStriker(true, false);

        score_data.setHomeTeam(matchDetails.getHomeTeam().nick_name);
        score_data.setAwayTeam(matchDetails.getAwayTeam().nick_name);
        score_data.setHomeTeamBatting(matchDetails.isHomeTeamBatting());
        if (matchDetails.isFirstInningsCompleted()) {
            score_data.setFirstInningsTotal(fstInningsTotal);
            score_data.setfirstInningsWicket(RealmDB.noOfWicket(getActivity(), realm, matchDetails.getMatch_id(), true));

            score_data.setFirstIinningsOver(RealmDB.getFirstInningsOver(realm, matchDetails));
            int reqRun = ((scoreBoardData.getFirstInningsTotal() + 1) - total_run);
            int ballrem = ((matchDetails.getOvers() * 6) - CommanData.overToBall(String.valueOf(over)));
            if (reqRun <= 0) {
                if (reqRun == 0 && ballrem <= 0)
                    score_data.setMatchQuote(getString(R.string.match_draw));
                else
                    score_data.setMatchQuote(matchDetails.getCurrentBattingTeam().name + " " + getString(R.string.won_by) + " " + (matchDetails.getTotalPlayers() - totalWicket) + " " + getString(R.string.wickets));
            }
//            else if(ballrem<=0)
//                score_data.setMatchQuote(matchDetails.getCurrentBattingTeam().name + " " + getString(R.string.won_by) + " " + (matchDetails.getTotalPlayers() - totalWicket) + " " + getString(R.string.wickets));
            else if (matchDetails.getTotalPlayers() == (totalWicket - 1) || ballrem <= 0)
                score_data.setMatchQuote(matchDetails.getCurrentBowlingTeam().name + " " + getString(R.string.won_by) + " " + (reqRun - 1) + " " + getString(R.string.runs));
            else
                score_data.setMatchQuote(matchDetails.getCurrentBattingTeam().name + " " + getString(R.string.needs) + " " + reqRun + " " + getString(R.string.runs_in) + " " + ballrem + " " + getString(R.string.balls));
        } else {
            score_data.setMatchQuote(matchDetails.getToss().nick_name + " " + getString(R.string.won_and_elect) + " " + matchDetails.getChooseTo());
        }
        // score_data.setShotAt(groundFragment.getCordinate());
        ArrayList<String> ss = scoreBoardData.getLastThreeOvers();
        if (ss == null)
            ss = new ArrayList<>();
        if (ss.size() > 12) {
            ss.remove(0);
        }
        ss.add(Util.get_delivery_result(runs, wicket, normal_delivery, extraType));
        if (current_overCompleted) {
            //"haaaaa"

            boolean maiden = true;
//            if (ss.size() >= 6)
//                for (int i = ss.size() - 1; i >= ss.size() - 6; i--) {
//                    if (ss.get(i).charAt(0) != '0') {
//                        maiden = false;
//                    }
//                }
            ss.add("|");
            // current_bowler_bf.setMaiden(maiden ? current_bowler_bf.getMaiden() + 1 : current_bowler_bf.getMaiden());
            inningsData.setOversCompleted(true);
        }
        score_data.setLastThreeOvers(ss);
        System.out.println("check_overrr" + over);
        score_data.setTotalOver(String.valueOf(over));


        //To check whether delivery is boundary
        //if(runs==4||runs==6)


        MatchShortSummaryData matchShortSummaryData = CommanData.fromJson(matchDetails.getmatchShortSummary(), MatchShortSummaryData.class);
        if (matchShortSummaryData == null)
            matchShortSummaryData = new MatchShortSummaryData();
        matchShortSummaryData.setBattingTeamName(matchDetails.getCurrentBattingTeam().nick_name);
        matchShortSummaryData.setBowlingTeamName(matchDetails.getCurrentBowlingTeam().nick_name);
        matchShortSummaryData.setFirstInnings(!matchDetails.isFirstInningsCompleted());

        InningsSummary currentInningsSummary = new InningsSummary();
        matchShortSummaryData.setQuotes(detailedScoreBoardData.getScoreBoardData().getMatchQuote());
        currentInningsSummary.overs = new DecimalFormat("##.##").format(over);
        currentInningsSummary.run = total_run;
        currentInningsSummary.wicket = score_data.getTotal_wicket();
        if (!matchDetails.isFirstInningsCompleted()) {
            matchShortSummaryData.setFirstInningsSummary(currentInningsSummary);
        } else {
            matchShortSummaryData.setSecondInningsSummary(currentInningsSummary);
        }
        if (matchDetails.isOnlineMatch() && current_overCompleted) {
            String s = "matchList/" + "ongoing" + "/" + matchDetails.getMatch_id();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(s);
            myRef.setValue(matchShortSummaryData);
            removeMatchFromUpcoming();
        }

        matchDetails.setmatchShortSummary(CommanData.toString(matchShortSummaryData));
        return score_data;
    }

    private void removeMatchFromUpcoming() {
        if (matchDetails.isOnlineMatch() && !removedFromUpcoming) {
            String s = "matchList/" + "upcoming" + "/" + matchDetails.getMatch_id();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(s);
            myRef.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null)
                        removedFromUpcoming = true;
                }
            });
        }
    }

    private void removeMatchFromOncoming(MatchShortSummaryData data) {
        if (matchDetails.isOnlineMatch()) {
            System.out.println("callinggg" + data.getQuotes());
            String s = "matchList/" + "recent" + "/" + matchDetails.getMatch_id();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(s);
            myRef.setValue(data, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        String s = "matchList/" + "ongoing" + "/" + matchDetails.getMatch_id();
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(s);
                        myRef.removeValue();
                        System.out.println("callingggvvvv");
                    } else
                        System.out.println("callinggg***");
                }
            });
        }
    }


    String updateStriker(DetailedScoreData detailedScoreData, BatingProfile strikerProfile) {
        ScoreBoardData score_data = detailedScoreData.getScoreBoardData();
        if (striker != null) {
            score_data.striker.setName(striker.getName());
            score_data.striker.setRuns(strikerProfile.getRuns());
            score_data.striker.setBalls(strikerProfile.getBallFaced());
            score_data.striker.setFours(strikerProfile.getFours());
            score_data.striker.setSixes(strikerProfile.getSixes());

        } else {
            score_data.striker.setName("");
            score_data.striker.setRuns(0);
            score_data.striker.setBalls(0);
            score_data.striker.setFours(0);
            score_data.striker.setSixes(0);
        }

        if (score_data != null) {
            detailedScoreData.setScoreBoardData(score_data);
            return CommanData.toString(detailedScoreData);
        } else
            return "";
    }


    private void swapStriker(boolean withText, boolean redo) {
        System.out.println("_______Resumingswap");
        Player dummy = striker;
        striker = non_striker;
        non_striker = dummy;
        scoreBoardFragment.swapStrikerText(detailedScoreBoardData.getScoreBoardData(), withText, redo);
    }

//

    private void checkUnOrRedo() {
        System.out.println("__________UUUa" + lastInningsDataItem.getDelivery());
        if (undoCount > 0) {
            if (lastInningsDataItem.getDelivery() > 0) {
                InningsData id = realm.where(InningsData.class).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).equalTo("match_id", matchDetails.getMatch_id()).equalTo("delivery", (lastInningsDataItem.getDelivery() - 1)).findFirst();
                System.out.println("__________UUU" + id);
                if (id != null) {
                    //   System.out.println("__________UUU"+realm.where(InningsData.class).equalTo("match_id",matchDetails.getMatch_id()).findAllSorted("index").last().getScoreBoardData());
                    lastInningsDataItem = id;
                    detailedScoreBoardData = CommanData.fromJson(lastInningsDataItem.getDetailedScoreBoardData(), DetailedScoreData.class);

                    undoCount = 0;
                }
            } else {
                //  Toast.makeText(MainActivity.this, "rajinimurugan2", Toast.LENGTH_SHORT).show();
            }
        }
        if (redoCount > 0) {
            InningsData id = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).equalTo("delivery", (lastInningsDataItem.getDelivery() + 1)).findFirst();
            if (id != null && id.getDelivery() <= realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).findAllSorted("delivery", Sort.DESCENDING).first().getDelivery()) {
                lastInningsDataItem = id;
                detailedScoreBoardData = CommanData.fromJson(lastInningsDataItem.getDetailedScoreBoardData(), DetailedScoreData.class);
                redoCount = 0;
            } else {
                //  Toast.makeText(MainActivity.this, "rajinimurugan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialData() {
        System.out.println("______DDinit");
        InningsData inningsData = RealmDB.getInningsData(getActivity(), realm, 0, matchDetails.getMatch_id(), matchDetails.isFirstInningsCompleted());
        realm.beginTransaction();
        ScoreBoardData score_data = new ScoreBoardData();
        score_data.striker.setName(striker.getName());
        score_data.nonStriker.setName(non_striker.getName());
        score_data.curr_bowlers.setName(current_bowler.getName());
        score_data.setHomeTeam(matchDetails.getHomeTeam().nick_name);
        score_data.setAwayTeam(matchDetails.getAwayTeam().nick_name);

        inningsData.setMatch_id(matchDetails.getMatch_id());
        score_data.setBatsmanSwitched(false);
        inningsData.setNormalDelivery(normal_delivery);
        inningsData.setRun(runs);
        DetailedScoreData detailedScoreData = new DetailedScoreData();
        detailedScoreData.setScoreBoardData(score_data);
        inningsData.setDetailedScoreBoardData(CommanData.toString(detailedScoreData));
        inningsData.setStriker(striker.getpID());
        inningsData.setNonStriker(non_striker.getpID());
        inningsData.setCurrentBowler(current_bowler.getpID());
        inningsData.setOver(0);
        inningsData.setFirstInnings(!matchDetails.isFirstInningsCompleted());
        realm.commitTransaction();
        lastInningsDataItem = realm.where(InningsData.class).equalTo("match_id", matchDetails.getMatch_id()).equalTo("firstInnings", !matchDetails.isFirstInningsCompleted()).findAll().last();
        detailedScoreBoardData = CommanData.fromJson(lastInningsDataItem.getDetailedScoreBoardData(), DetailedScoreData.class);

        checkAndUpdateUI();
    }

    private void callData() {
        CoreClient client = new ServiceGenerator().createService(CoreClient.class);

        Call<ResponseBody> LoginResponse = client.coreDetails();
        LoginResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (MainActivity.this != null) {
                    // closeDialog();
                    try {
                        String data = response.body().string();
                        System.out.println("ressssss____" + data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.scorer));
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((MainFragmentActivity) getActivity()).removeNaviHome();

        }
    }

    @Override
    public void messageFromDialog(int dialogType, boolean success, String data, String message) {
        System.out.println("_____Out" + dialogType + "__" + data + "__" + success);
        if (success) {
            ((MainFragmentActivity) getActivity()).closePrevSelectPlayer();
            if (dialogType == CommanData.DIALOG_OUT) {

                Wicket wicket = RealmDB.getWicket(getActivity(), realm, data);
                System.out.println("_____Out" + dialogType + "__" + data + "__" + wicket.getType());


                // System.out.println("_____Outsdf1" + dialogType + "__" + data + "__" + wicket.getType());
                if (wicket.getType() != CommanData.W_RUNOUT) {
                    submitbuttonClicked(wicket);
                    realm.beginTransaction();
                    // System.out.println("_____Outsdf2" + dialogType + "__" + data + "__" + wicket.getType()+"__"+CommanData.toString( realm.copyFromRealm(wicket)));
                    detailedScoreBoardData.getScoreBoardData().setWicket(CommanData.toString(realm.copyFromRealm(wicket)));
                    //System.out.println("_____Outsdf3" + dialogType + "__" + data + "__" + wicket.getType());


                    lastInningsDataItem.setDetailedScoreBoardData(CommanData.toString(detailedScoreBoardData));
                    RealmDB.getBattingProfile(realm, striker.getpID(), matchDetails.getMatch_id()).setWicket(wicket);
                    RealmDB.getBowlingProfile(realm, current_bowler.getpID(), matchDetails.getMatch_id()).addWickets(wicket);
                    realm.commitTransaction();

                    striker = null;

                } else if (wicket.getType() == CommanData.W_RUNOUT) {

                    if (message != null && CommanData.isInteger(message))
                        runs = Integer.parseInt(message);
                    detailedScoreBoardData.getScoreBoardData().setWicket(CommanData.toString(realm.copyFromRealm(wicket)));
                    System.out.println("_____Outsdf3" + dialogType + "__" + data + "__" + runs + "__" + message);

                    submitbuttonClicked(wicket);
                    realm.beginTransaction();
                    lastInningsDataItem.setDetailedScoreBoardData(CommanData.toString(detailedScoreBoardData));

                    Player p = RealmDB.getPlayer(realm, wicket.getBatsman());
                    RealmDB.getBattingProfile(realm, p.getpID(), matchDetails.getMatch_id()).setWicket(wicket);
                    realm.commitTransaction();
                    if (p.getpID() == striker.getpID())
                        striker = null;
                    else
                        non_striker = null;

                }

            }
            System.out.println("__________nulled" + striker + "__" + non_striker);
            checkPlayerNotNull();
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void messageFromDialog(int dialogType, boolean success, String data, String message, int assignTo) {
        System.out.println("_____Out" + dialogType + "__" + data + "__" + success);
        if (success) {
            ((MainFragmentActivity) getActivity()).closePrevSelectPlayer();
            Player bb;

            bb = RealmDB.getPlayer(realm, Integer.parseInt(data));
            boolean ss = success;
            System.out.println("checkkkk" + ss);
//

            if (assignTo == 0) {
                striker = RealmDB.getPlayer(realm, bb.getpID());
                BatingProfile bf = RealmDB.getBattingProfile(realm, striker.getpID(), matchDetails.getMatch_id());
                realm.beginTransaction();
                if (matchDetails.isHomeTeamBatting())
                    matchDetails.addHomePlayer(striker);
                else
                    matchDetails.addAwayPlayer(striker);
//                setScoreDataForDB(null,)
                if (lastInningsDataItem != null) {
                    DetailedScoreData sd = CommanData.fromJson(lastInningsDataItem.getDetailedScoreBoardData(), DetailedScoreData.class);
                    lastInningsDataItem.setDetailedScoreBoardData(updateStriker(sd, bf));
                }
                realm.commitTransaction();

            } else if (assignTo == 1) {
                if (striker.getpID() != bb.getpID()) {
                    non_striker = RealmDB.getPlayer(realm, bb.getpID());
                    BatingProfile bf = RealmDB.getBattingProfile(realm, non_striker.getpID(), matchDetails.getMatch_id());
                    if (bf == null)
                        bf = RealmDB.createBattingProfile(realm, non_striker.getpID(), matchDetails.getMatch_id());
                    realm.beginTransaction();
                    if (matchDetails.isHomeTeamBatting())
                        matchDetails.addHomePlayer(non_striker);
                    else
                        matchDetails.addAwayPlayer(non_striker);
                    realm.commitTransaction();
                } else
                    Toast.makeText(getContext(), getString(R.string.player_in_striker_end), Toast.LENGTH_SHORT).show();
            } else if (assignTo == 2) {
                askBowler = false;
                current_bowler = RealmDB.getPlayer(realm, bb.getpID());
                //  BowlingProfile bf = RealmDB.getBowlingProfile(realm, current_bowler.getpID(), matchDetails.getMatch_id());
//                if (bf == null)
//                    bf = RealmDB.getBowlingProfile(realm, current_bowler.getpID(), matchDetails.getMatch_id());
                realm.beginTransaction();
                if (matchDetails.isHomeTeamBatting())
                    matchDetails.addAwayPlayer(current_bowler);
                else
                    matchDetails.addHomePlayer(current_bowler);
                realm.commitTransaction();
                //checkAndUpdateUI();

                //ScoreBoardData dummySData = detailedScoreBoardData;


// dummySData.set
            } else if (assignTo == 3) {
                next_bowler = RealmDB.getPlayer(realm, bb.getpID());
                BowlingProfile bf = RealmDB.getBowlingProfile(realm, next_bowler.getpID(), matchDetails.getMatch_id());
                if (bf == null)
                    bf = RealmDB.getBowlingProfile(realm, next_bowler.getpID(), matchDetails.getMatch_id());
                realm.beginTransaction();
                if (matchDetails.isHomeTeamBatting())
                    matchDetails.addAwayPlayer(next_bowler);
                else
                    matchDetails.addHomePlayer(next_bowler);
                realm.commitTransaction();
            } else if (assignTo == 5) {
                Player dummy = RealmDB.getPlayer(realm, bb.getpID());
                BatingProfile bf = RealmDB.getBattingProfile(realm, dummy.getpID(), matchDetails.getMatch_id());
                if (bf == null)
                    bf = RealmDB.createBattingProfile(realm, dummy.getpID(), matchDetails.getMatch_id());
                realm.beginTransaction();
                if (matchDetails.isHomeTeamBatting())
                    matchDetails.addAwayPlayer(dummy);
                else
                    matchDetails.addHomePlayer(dummy);
                detailedScoreBoardData.getScoreBoardData().next_bowlers.setName(dummy.getName());
                detailedScoreBoardData.getScoreBoardData().next_bowlers.setBalls(bf.getBallFaced());
                detailedScoreBoardData.getScoreBoardData().next_bowlers.setRuns(bf.getRuns());

                lastInningsDataItem.setDetailedScoreBoardData(CommanData.toString(detailedScoreBoardData));
                realm.commitTransaction();
                if (striker == null) {
                    striker = null;
                    striker = dummy;
                } else {
                    non_striker = null;
                    non_striker = dummy;
                }
            }
            if (refreshViewForData() != null)
                scoreBoardFragment.updateUI(refreshViewForData());
            checkAndUpdateUI();

        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private ScoreBoardData refreshViewForData() {

        if (detailedScoreBoardData != null) {

            ScoreBoardData score_data = detailedScoreBoardData.getScoreBoardData();
            if (current_bowler != null) {
                BowlingProfile current_bowler_bf = RealmDB.getBowlingProfile(realm, current_bowler.getpID(), matchDetails.getMatch_id());
                score_data.curr_bowlers.setName(current_bowler.getName());
                score_data.curr_bowlers.setRuns(current_bowler_bf.getRunsGranted());
                int extra_ball = current_bowler_bf.getNoBall() + current_bowler_bf.getWide();
                score_data.curr_bowlers.setBalls(current_bowler_bf.getBallsBowled() - extra_ball);
            }

            //NEXT BOWLER
            if (next_bowler != null) {
                BowlingProfile nextBowler_bf = RealmDB.getBowlingProfile(realm, next_bowler.getpID(), matchDetails.getMatch_id());
                score_data.next_bowlers.setName(next_bowler.getName());
                score_data.next_bowlers.setMaiden(nextBowler_bf.getMaiden());
                score_data.next_bowlers.setRuns(nextBowler_bf.getRunsGranted());
                int extra_ball = nextBowler_bf.getNoBall() + nextBowler_bf.getWide();
                score_data.next_bowlers.setBalls(nextBowler_bf.getBallsBowled() - extra_ball);
                System.out.println("_________nextBowler" + (nextBowler_bf.getBallsBowled() - extra_ball));
            }


            if (striker != null) {
                System.out.println("next_striker____" + striker.getName());
                BatingProfile strikerProfile = RealmDB.getBattingProfile(realm, striker.getpID(), matchDetails.getMatch_id());
                score_data.striker.setName(striker.getName());
                score_data.striker.setRuns(strikerProfile.getRuns());
                score_data.striker.setBalls(strikerProfile.getBallFaced());
                score_data.striker.setSixes(strikerProfile.getSixes());
                score_data.striker.setFours(strikerProfile.getFours());
            }
            //NON_STRIKER
            if (non_striker != null) {
                BatingProfile non_strikerProfile = RealmDB.getBattingProfile(realm, non_striker.getpID(), matchDetails.getMatch_id());
                score_data.nonStriker.setName(non_striker.getName());
                score_data.nonStriker.setBalls(non_strikerProfile.getBallFaced());
                score_data.nonStriker.setRuns(non_strikerProfile.getRuns());
                score_data.nonStriker.setSixes(non_strikerProfile.getSixes());
                score_data.nonStriker.setFours(non_strikerProfile.getFours());
            }
            score_data.setBatsmanSwitched(false);
            return score_data;

        }

        return null;
    }

    @Override
    public void messageFromDialog(int dialogType, boolean success, ArrayList<Integer> data, String message) {

    }

    @Override
    public void strikerClicked() {
        if (striker != null) {
            BatingProfile bf = RealmDB.getBattingProfile(realm, striker.getpID(), matchDetails.getMatch_id());
            if (bf.getBallFaced() == 0) {
                realm.beginTransaction();
                bf.setCurrentStatus(CommanData.StatusInMatch);
                realm.commitTransaction();
                striker = null;
                checkPlayerNotNull();
            } else {
                showAlertDialog(getString(R.string.striker_cant_change));
            }
        }
    }

    @Override
    public void nonStrikerClicked() {
        if (non_striker != null) {
            BatingProfile bf = RealmDB.getBattingProfile(realm, non_striker.getpID(), matchDetails.getMatch_id());
            if (bf.getBallFaced() == 0) {
                realm.beginTransaction();
                bf.setCurrentStatus(CommanData.StatusInMatch);
                realm.commitTransaction();
                non_striker = null;
                checkPlayerNotNull();
            } else {
                showAlertDialog(getString(R.string.non_striker_cant_change));
            }
        }
    }

    @Override
    public void bowlerClicked() {
        if (current_bowler != null) {
            BowlingProfile bf = RealmDB.getBowlingProfile(realm, current_bowler.getpID(), matchDetails.getMatch_id());
            if (bf.getBallsBowled() == 0) {
                realm.beginTransaction();
                bf.setCurrentBowlerStatus(CommanData.StatusInMatch);
                realm.commitTransaction();
                current_bowler = null;
                checkPlayerNotNull();
            } else {
                showAlertDialog(getString(R.string.bowler_cant_change));
            }
        }
    }


    private void showAlertDialog(String dialog_message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialog_message);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        // positive button logic
                        dialog.dismiss();
                    }
                });

//        String negativeText = getString(android.R.string.cancel);
//        builder.setNegativeButton(negativeText,
//                new android.content.DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(android.content.DialogInterface dialog, int which) {
//                        // negative button logic
//                    }
//                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
}





