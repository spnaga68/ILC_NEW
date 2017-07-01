package realmstudy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import realmstudy.MainActivity;
import realmstudy.MainFragmentActivity;
import realmstudy.MatchDetailActivity;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.data.CommanData;
import realmstudy.data.MatchShortSummaryData;
import realmstudy.data.RealmObjectData.BatingProfile;
import realmstudy.data.RealmObjectData.BowlingProfile;
import realmstudy.data.RealmObjectData.InningsData;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.fragments.MatchInfo;
import realmstudy.fragments.MatchInfo;
import realmstudy.interfaces.SlideRecyclerView;

/**
 * Created by developer on 21/2/17.
 */


public class SavedGameListAdapter extends RecyclerView.Adapter {
    private Context context;
    private static final int PENDING_REMOVAL_TIMEOUT = 1500; // 3sec

    //    List<String> items;
//    List<String> itemsPendingRemoval;
    int lastInsertedIndex; // so we can add some more items for testing purposes
    boolean undoOn = true; // is undo on, you can turn it on from the toolbar menu

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be
    List<MatchDetails> data;
    @Inject
    Realm realm;

    public SavedGameListAdapter(Context context, RealmResults<MatchDetails> data) {
        ((MyApplication) ((Activity) context).getApplication()).getComponent().inject(this);

        this.data = realm.copyFromRealm(data);
        this.context = context;
//        items = new ArrayList<>();
//        itemsPendingRemoval = new ArrayList<>();
//        lastInsertedIndex = data.size();
//
//        // let's generate some items
//        SavedGameListAdapter.this.context = context;
//        for (int i = 0; i < data.size(); i++) {
//            items.add(String.valueOf(data.get(i).getMatch_id()));
//
//        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TestViewHolder viewHolder = (TestViewHolder) holder;
        //final String item = items.get(position);

//        if (itemsPendingRemoval.contains(item)) {
//            // we need to show the "undo" state of the row
//            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
//            viewHolder.titleView.setVisibility(View.GONE);
//            // viewHolder.undoButton.setVisibility(View.VISIBLE);
//            viewHolder.titleView.setTag(position);
////
//        } else {
        // we need to show the "normal" state
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
        viewHolder.titleView.setVisibility(View.VISIBLE);
        // viewHolder.titleView.setText(item);
        viewHolder.venue.setText(data.get(position).getLocation());
        viewHolder.home_team_name.setText(data.get(position).getHomeTeam().nick_name);
        viewHolder.away_team_name.setText(data.get(position).getAwayTeam().nick_name);
        viewHolder.status.setText(CommanData.getDateCurrentTimeZone(data.get(position).getTime()));
//            viewHolder.undoButton.setVisibility(View.GONE);
//            viewHolder.undoButton.setOnClickListener(null);
        viewHolder.titleView.setTag(position);


        if (data.get(position).getMatchStatus() == CommanData.MATCH_STARTED_FI) {
            // if (data.get(position).isHomeTeamBatting()) {

            MatchShortSummaryData matchShortSummaryData = CommanData.fromJson(data.get(position).getmatchShortSummary(), MatchShortSummaryData.class);

            if (matchShortSummaryData != null) {
                viewHolder.home_team_name.setText(matchShortSummaryData.getBattingTeamName());
                viewHolder.away_team_name.setText(matchShortSummaryData.getBowlingTeamName());
                viewHolder.home_team_scr.setText(matchShortSummaryData.getFirstInningsSummary().run + " - " + matchShortSummaryData.getFirstInningsSummary().wicket + " (" +
                        matchShortSummaryData.getFirstInningsSummary().overs + ")");
                viewHolder.home_team_scr.setVisibility(View.VISIBLE);
                viewHolder.home_team_scr.setVisibility(View.VISIBLE);
                viewHolder.away_team_scr.setVisibility(View.GONE);
                viewHolder.status.setText(matchShortSummaryData.getQuotes());
            }
//                } else {
//                    viewHolder.away_team_scr.setText("193-4");
//                    viewHolder.away_team_scr.setVisibility(View.VISIBLE);
//                }
        } else if (data.get(position).getMatchStatus() == CommanData.MATCH_STARTED_SI || data.get(position).getMatchStatus() == CommanData.MATCH_COMPLETED) {

            MatchShortSummaryData matchShortSummaryData = CommanData.fromJson(data.get(position).getmatchShortSummary(), MatchShortSummaryData.class);

            if (matchShortSummaryData != null) {
                viewHolder.home_team_name.setText(matchShortSummaryData.getBowlingTeamName());
                viewHolder.away_team_name.setText(matchShortSummaryData.getBattingTeamName());
                viewHolder.home_team_scr.setText(matchShortSummaryData.getFirstInningsSummary().run + " - " + matchShortSummaryData.getFirstInningsSummary().wicket + " (" +
                        matchShortSummaryData.getFirstInningsSummary().overs + ")");
                viewHolder.away_team_scr.setText(matchShortSummaryData.getSecondInningsSummary().run + " - " + matchShortSummaryData.getSecondInningsSummary().wicket + " (" +
                        matchShortSummaryData.getSecondInningsSummary().overs + ")");
                viewHolder.home_team_scr.setVisibility(View.VISIBLE);
                viewHolder.away_team_scr.setVisibility(View.VISIBLE);
                viewHolder.status.setText(matchShortSummaryData.getQuotes());
            }
        } else {

            viewHolder.home_team_scr.setVisibility(View.GONE);
            viewHolder.away_team_scr.setVisibility(View.GONE);
        }

//            if (data.get(position).getMatchStatus() == CommanData.MATCH_COMPLETED) {
//                viewHolder.status.setText(context.getString(R.string.completed));
//                viewHolder.status.setTextColor(ContextCompat.getColor(context, R.color.red_M));
//            } else if (data.get(position).getMatchStatus() == CommanData.MATCH_NOT_YET_STARTED) {
//                viewHolder.status.setText(context.getString(R.string.match_not_started));
//                viewHolder.status.setTextColor(ContextCompat.getColor(context, R.color.yellow_M));
//            } else {
//                viewHolder.status.setText(context.getString(R.string.on_going));
//                viewHolder.status.setTextColor(ContextCompat.getColor(context, R.color.green_M));
//            }
        viewHolder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("__matachi_"+data.get(position).getMatch_id());
                MatchDetails md = RealmDB.getMatchById(context, realm, data.get(position).getMatch_id());
                if (md.getToss() == null) {
                    Bundle b = new Bundle();
                    MatchInfo fragment = new MatchInfo();
                    b.putInt("match_id", md.getMatch_id());
                    b.putString("venue", md.getLocation());
                    b.putString("teamIDs", String.valueOf(md.getHomeTeam().team_id) + "__" + String.valueOf(md.getAwayTeam().team_id));
                    fragment.setArguments(b);
                    ((MainFragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, fragment).commit();

                } else if (md.getMatchStatus() != CommanData.MATCH_COMPLETED) {
                    Bundle b = new Bundle();
                    MainActivity fragment = new MainActivity();
                    b.putInt("match_id", md.getMatch_id());
                    // Toast.makeText(context,  String.valueOf(md.getMatch_id()), Toast.LENGTH_SHORT).show();
                    fragment.setArguments(b);
                    ((MainFragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, fragment).commit();
                } else {
                    //Toast.makeText(context, context.getString(R.string.game_over), Toast.LENGTH_SHORT).show();
                    Bundle b = new Bundle();
                    MatchDetailActivity fragment = new MatchDetailActivity();
                    b.putInt("match_id", md.getMatch_id());
                    // Toast.makeText(context,  String.valueOf(md.getMatch_id()), Toast.LENGTH_SHORT).show();
                    fragment.setArguments(b);
                    ((MainFragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, fragment).commit();
                }
            }
        });
//        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

//    public void pendingRemoval(int position) {
//        final String item = items.get(position);
//        if (!itemsPendingRemoval.contains(item)) {
//            itemsPendingRemoval.add(item);
//            // this will redraw row in "undo" state
//            notifyItemChanged(position);
//            // let's create, store and post a runnable to remove the item
//            Runnable pendingRemovalRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    remove(items.indexOf(item));
//                }
//            };
//            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
//            pendingRunnables.put(item, pendingRemovalRunnable);
//        }
//    }


//    public boolean isPendingRemoval(int position) {
//        String item = items.get(position);
//        return itemsPendingRemoval.contains(item);
//    }


    /**
     * ViewHolder capable of presenting two states: "normal" and "undo" state.
     */
    class TestViewHolder extends RecyclerView.ViewHolder {

        ImageView
                home_team_image, away_team_image;
        TextView home_team_name;
        TextView away_team_name;
        TextView venue, status;
        TextView home_team_scr, away_team_scr;
        CardView titleView;
        AppCompatImageView delete_item;


        public TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_list_item, parent, false));
            titleView = (CardView) itemView.findViewById(R.id.titleView);
            status = (TextView) itemView.findViewById(R.id.status);
            home_team_image = (ImageView) itemView.findViewById(R.id.home_team_image);
            home_team_name = (TextView) itemView.findViewById(R.id.home_team_name);
            away_team_name = (TextView) itemView.findViewById(R.id.away_team_name);
            venue = (TextView) itemView.findViewById(R.id.venue);
            delete_item = (AppCompatImageView) itemView.findViewById(R.id.delete_item);
            home_team_scr = (TextView) itemView.findViewById(R.id.home_team_scr);
            away_team_scr = (TextView) itemView.findViewById(R.id.away_team_scr);
            away_team_image = (ImageView) itemView.findViewById(R.id.away_team_image);
            delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(getAdapterPosition());
                }
            });
        }

        public void remove(int position) {

            // String item = items.get(position);
            //     Toast.makeText(context, "dsr"+item, Toast.LENGTH_SHORT).show();
//        if (itemsPendingRemoval.contains(item)) {
//            itemsPendingRemoval.remove(item);
//        }
//        if (items.contains(item)) {
//            items.remove(position);
//            notifyItemRemoved(position);
//        }

            int item = data.get(position).getMatch_id();

            //  System.out.println("_______________II" + item + "___" + position);
            realm.beginTransaction();
            MatchDetails md = realm.where(MatchDetails.class).equalTo("match_id", data.get(position).getMatch_id()).findFirst();
            RealmResults<InningsData> inningsDatas = realm.where(InningsData.class).equalTo("match_id", (item)).findAll();
            RealmResults<BowlingProfile> bowlingProfiles = realm.where(BowlingProfile.class).equalTo("match_id", (item)).findAll();
            RealmResults<BatingProfile> battingProfiles = realm.where(BatingProfile.class).equalTo("match_id", (item)).findAll();
            if (md != null)
                md.deleteFromRealm();
            if (inningsDatas != null)
                inningsDatas.deleteAllFromRealm();
            if (bowlingProfiles != null)
                bowlingProfiles.deleteAllFromRealm();
            if (battingProfiles != null)
                battingProfiles.deleteAllFromRealm();
            realm.commitTransaction();
            data.remove(position);
            notifyItemRemoved(position);
        }

    }

}

