package realmstudy.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import realmstudy.MainFragmentActivity;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.data.RealmObjectData.Team;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.interfaces.ItemClickInterface;

/**
 * Created by developer on 21/2/17.
 */

public class TeamListAdapter extends RealmRecyclerViewAdapter<Team, TeamListAdapter.MyViewHolder> {

    private Context context;
    @Inject
    Realm realm;
    boolean isEditable;

    public TeamListAdapter(Context activity, OrderedRealmCollection<Team> realmResults, boolean isEditable) {
        super(activity, realmResults, true);
        this.context = activity;
        this.isEditable = isEditable;
        ((MyApplication) ((Activity) context).getApplication()).getComponent().inject(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Team obj = getData().get(position);
        holder.data = obj;
        holder.title.setText(obj.name);
        holder.ph_no.setText(obj.nick_name);
        holder.view2.setVisibility(View.GONE);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView title, ph_no;
        public Team data;
        public ImageView delete_item,view2;
        RelativeLayout list_item_lay;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.playername);
            ph_no = (TextView) view.findViewById(R.id.time);
            list_item_lay=(RelativeLayout)view.findViewById(R.id.list_item_lay);
            delete_item = (ImageView) view.findViewById(R.id.delete_item);
            view2= (ImageView) view.findViewById(R.id.view2);
            if (!isEditable)
                delete_item.setVisibility(View.GONE);
            delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = getData().get(getAdapterPosition()).team_id;
                    boolean removed = RealmDB.removeTeam(realm, id);
                    if (!removed)
                        Toast.makeText(context, context.getString(R.string.team_in_match), Toast.LENGTH_SHORT).show();
                }
            });
            list_item_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = getData().get(getAdapterPosition()).team_id;

                    if(context instanceof ItemClickInterface)
                    ((ItemClickInterface)context).itemPicked(id,"");
                }
            });
//            list_item_lay.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    v.setSelected(true);
//                    System.out.println("LongClick");
//                    return true;
//                }
//            });
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            // context.deleteItem(data);
            v.setSelected(true);
            System.out.println("LongClick");
            return true;
        }
    }
}