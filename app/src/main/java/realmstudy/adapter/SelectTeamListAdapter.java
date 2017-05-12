package realmstudy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.RealmResults;
import realmstudy.R;
import realmstudy.data.RealmObjectData.Team;

/**
 * Created by developer on 6/3/17.
 */

public class SelectTeamListAdapter extends BaseAdapter {
    RealmResults<Team> data;
    Context c;
    ArrayList<Integer> selected_data = new ArrayList<>();

    public SelectTeamListAdapter(Context c, RealmResults<Team> data) {
        this.data = data;
        this.c = c;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    private static class ViewHolder {
        public TextView title, ph_no;
        public CheckBox multi_player_checkbox;
        public RelativeLayout overall_lay;


    }

    @Override
    public Team getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.team_select_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.playername);
            viewHolder.ph_no = (TextView) convertView.findViewById(R.id.time);
            viewHolder.multi_player_checkbox = (CheckBox) convertView.findViewById(R.id.multi_player_checkbox);
            viewHolder.overall_lay=(RelativeLayout)convertView.findViewById(R.id.overall_lay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Team obj = data.get(position);
        viewHolder.title.setText(obj.nick_name);
        viewHolder.ph_no.setText(obj.name);
       viewHolder.overall_lay.setTag(position);
//        viewHolder.overall_lay.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//            //    (view).findViewById(R.id.multi_player_checkbox).performClick();
//                selected_data.clear();
//                selected_data.add(data.get((int) view.getTag()).team_id);
//
//            }
//        });
        viewHolder.multi_player_checkbox.setVisibility(View.GONE);
//        viewHolder.multi_player_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b)
//                    selected_data.add(data.get((int) compoundButton.getTag()).team_id);
//                else
//                    selected_data.remove(new Integer(data.get((int) compoundButton.getTag()).team_id));
//            }
//        });
        return convertView;
    }


    public ArrayList<Integer> selectedPlayersID() {


        return selected_data;
    }
}