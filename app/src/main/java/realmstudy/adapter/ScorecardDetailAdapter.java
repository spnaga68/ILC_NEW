package realmstudy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import realmstudy.R;
import realmstudy.data.CommanData;
import realmstudy.data.ScoreCardDetailData;
import realmstudy.extras.AnimatedExpandableListView;
import realmstudy.fragments.ScorecardDetailFragment;

public class ScorecardDetailAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private LayoutInflater inflater;
    Context c;
    ArrayList<ScoreCardDetailData> datas;
    ArrayList<View> battingItemView = new ArrayList<>();
    ArrayList<View> bowlingItemView = new ArrayList<>();
    ArrayList<View> fow = new ArrayList<>();
    ArrayList<View> pp = new ArrayList<>();

    public ScorecardDetailAdapter(Context context, ArrayList<ScoreCardDetailData> datas) {
        c = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
        battingItemView.add(getBattingItemView(0));
        bowlingItemView.add(getBowlingItemView(0));
        fow.add(getfowView(0));
        if (datas.size() > 1) {
            battingItemView.add(getBattingItemView(1));
            bowlingItemView.add(getBowlingItemView(1));
            fow.add(getfowView(1));
            System.out.println("sec_dataaa+"+datas.get(1).getBatsmanDetails().size());
        }


    }

    public View getBattingItemView(int groupPosition) {
        LinearLayout ll = new LinearLayout(c);
        ll.setOrientation(LinearLayout.VERTICAL);
//    ll.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
        for (int i = 0; i < datas.get(groupPosition).getBatsmanDetails().size(); i++) {
            ScoreCardDetailData.BatsmanDetail bData = datas.get(groupPosition).getBatsmanDetails().get(i);
            View v = inflater.inflate(R.layout.team_scorecard_item, null);
            TextView
                    batsman_txt, runs_txt, balls_txt, fours_txt, six_txt, strike_rate_txt, out_as;
            batsman_txt = (TextView) v.findViewById(R.id.batsman_txt);
            runs_txt = (TextView) v.findViewById(R.id.runs_txt);
            balls_txt = (TextView) v.findViewById(R.id.balls_txt);
            fours_txt = (TextView) v.findViewById(R.id.fours_txt);
            six_txt = (TextView) v.findViewById(R.id.six_txt);
            strike_rate_txt = (TextView) v.findViewById(R.id.strike_rate_txt);
            out_as = (TextView) v.findViewById(R.id.out_as);

            batsman_txt.setText(bData.name);
            runs_txt.setText("" + bData.runs);
            balls_txt.setText("" + bData.balls);
            fours_txt.setText("" + bData.fours);
            six_txt.setText("" + bData.sixes);
            System.out.println("_____hiiiss" + bData.strike_rate);
            strike_rate_txt.setText("" + String.valueOf(bData.strike_rate));
            out_as.setText("" + bData.outAs);
            ll.addView(v);

        }
        return ll;
    }

    public View getBowlingItemView(int groupPosition) {
        LinearLayout ll = new LinearLayout(c);
        ll.setOrientation(LinearLayout.VERTICAL);
//    ll.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
        for (int i = 0; i < datas.get(groupPosition).getBowlersDetails().size(); i++) {
            ScoreCardDetailData.BowlersDetail bData = datas.get(groupPosition).getBowlersDetails().get(i);
            View v = inflater.inflate(R.layout.team_scorecard_item, null);
            TextView
                    batsman_txt, overs_text, maiden_text, runs_text, wicket_text, ec_rate_text, out_as;
            batsman_txt = (TextView) v.findViewById(R.id.batsman_txt);
            overs_text = (TextView) v.findViewById(R.id.runs_txt);
            maiden_text = (TextView) v.findViewById(R.id.fours_txt);
            runs_text = (TextView) v.findViewById(R.id.balls_txt);
            wicket_text = (TextView) v.findViewById(R.id.six_txt);
            ec_rate_text = (TextView) v.findViewById(R.id.strike_rate_txt);
            out_as = (TextView) v.findViewById(R.id.out_as);

            batsman_txt.setText(bData.name);
            overs_text.setText("" + bData.overs);
            maiden_text.setText("" + bData.maiden);
            runs_text.setText("" + bData.runs);
            wicket_text.setText("" + bData.wicket);
            ec_rate_text.setText("" + bData.ecnomic_rate);
            out_as.setVisibility(View.GONE);
            //  out_as.setText("" + bData.outAs);
            if (i == (datas.get(groupPosition).getBowlersDetails().size() - 1)) {
                v.findViewById(R.id.bottom_view).setVisibility(View.GONE);
            }
            ll.addView(v);

        }
        return ll;
    }

    public View getfowView(int groupPosition) {
        LinearLayout ll = new LinearLayout(c);
        ll.setOrientation(LinearLayout.VERTICAL);
//    ll.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
        for (int i = 0; i < datas.get(groupPosition).getFow().size(); i++) {
            ScoreCardDetailData.FOW bData = datas.get(groupPosition).getFow().get(i);
            View v = inflater.inflate(R.layout.fow_item, null);
            TextView
                    name, score, overs;
            name = (TextView) v.findViewById(R.id.name);
            score = (TextView) v.findViewById(R.id.score);
            overs = (TextView) v.findViewById(R.id.overs);


            name.setText(bData.name);
            score.setText("" + bData.score);
            overs.setText("" + bData.overs);

            //  out_as.setText("" + bData.outAs);
            ll.addView(v);

        }
        return ll;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.scorecard_detail_frag, parent, false);
            holder.batsman_lay = (LinearLayout) convertView.findViewById(R.id.batsman_lay);
            holder.extras_run = (TextView) convertView.findViewById(R.id.extras_run);
            holder.extras_detail = (TextView) convertView.findViewById(R.id.extras_detail);
            holder.total_run = (TextView) convertView.findViewById(R.id.total_run);
            holder.current_run_rate_detail = (TextView) convertView.findViewById(R.id.current_run_rate_detail);
            holder.did_not_bat_lay = (LinearLayout) convertView.findViewById(R.id.did_not_bat_lay);
            holder.did_not_bat_text = (TextView) convertView.findViewById(R.id.did_not_bat_text);
            holder.bowlers_lay = (LinearLayout) convertView.findViewById(R.id.bowlers_lay);
            holder.fall_of_wicket_lay = (LinearLayout) convertView.findViewById(R.id.fall_of_wicket_lay);
            holder.power_play_lay = (LinearLayout) convertView.findViewById(R.id.power_play_lay);
            holder.power_play_head = (LinearLayout) convertView.findViewById(R.id.power_play_head);
            holder.fow_head_lay = (LinearLayout) convertView.findViewById(R.id.fow_head_lay);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        if (battingItemView.get(groupPosition).getParent() != null)
            ((ViewGroup) battingItemView.get(groupPosition).getParent()).removeView(battingItemView.get(groupPosition));
        holder.batsman_lay.addView(battingItemView.get(groupPosition));
        battingItemView.get(groupPosition).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        if (bowlingItemView.get(groupPosition).getParent() != null)
            ((ViewGroup) bowlingItemView.get(groupPosition).getParent()).removeView(bowlingItemView.get(groupPosition));
        holder.bowlers_lay.addView(bowlingItemView.get(groupPosition));
        bowlingItemView.get(groupPosition).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        if (fow.get(groupPosition).getParent() != null) {
            holder.fow_head_lay.setVisibility(View.VISIBLE);
            ((ViewGroup) fow.get(groupPosition).getParent()).removeView(fow.get(groupPosition));
            holder.fall_of_wicket_lay.addView(fow.get(groupPosition));
            fow.get(groupPosition).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            holder.fow_head_lay.setVisibility(View.GONE);

        }
        if (pp.size() > 0) {
            holder.power_play_head.setVisibility(View.VISIBLE);
            ((ViewGroup) fow.get(groupPosition).getParent()).removeView(fow.get(groupPosition));
            holder.power_play_lay.addView(fow.get(groupPosition));
            fow.get(groupPosition).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            holder.power_play_head.setVisibility(View.GONE);

        }
        holder.extras_run.setText("" + datas.get(groupPosition).getTotal_extras());
        holder.extras_detail.setText("" + datas.get(groupPosition).getExtras_detail());
        holder.total_run.setText("" + datas.get(groupPosition).getTeamRun_over());
        holder.current_run_rate_detail.setText("" + datas.get(groupPosition).getCurrent_run_rate());
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = inflater.inflate(R.layout.team_sc_group_item, parent, false);
            holder.team_name = (TextView) convertView.findViewById(R.id.team_name);
            holder.score_detail = (TextView) convertView.findViewById(R.id.score_detail);
            holder.arrow = (ImageView) convertView.findViewById(R.id.arrow);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        if (isExpanded)
            holder.arrow.setImageResource(R.drawable.up_arrow);
        else
            holder.arrow.setImageResource(R.drawable.down_arrow);
        holder.team_name.setText(datas.get(groupPosition).getTeamName());
        holder.score_detail.setText(datas.get(groupPosition).getTeamRun_over());
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    public class GroupItem {
        public String title;
        public List<ChildItem> items = new ArrayList<ChildItem>();
    }

    public class ChildItem {
        public String title;
        public String hint;
    }

    public class ChildHolder {
        public TextView title;
        public TextView hint;
        private LinearLayout
                batsman_lay, did_not_bat_lay, bowlers_lay, fall_of_wicket_lay, power_play_lay, power_play_head, fow_head_lay;
        private TextView extras_run;
        private TextView extras_detail;
        private TextView total_run;
        private TextView current_run_rate_detail;
        private TextView did_not_bat_text;
    }

    public class GroupHolder {
        public TextView team_name, score_detail;
        ImageView arrow;
    }
}