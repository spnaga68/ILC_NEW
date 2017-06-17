package realmstudy.adapter;

/**
 * Created by developer on 27/12/16.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import io.realm.Realm;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.data.RealmObjectData.Player;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.extras.AvatarImageBehavior;
import realmstudy.fragments.EditPlayerProfile;


public class PlayerListAdapter extends RealmRecyclerViewAdapter<Player, PlayerListAdapter.MyViewHolder> {

    private Context context;
    @Inject
    Realm realm;
    String[] batStyleArray, bowlStyleArray;

    public PlayerListAdapter(Context activity, OrderedRealmCollection<Player> data) {
        super(activity, data, true);
        this.context = activity;
        batStyleArray = (context.getResources().getStringArray(R.array.bat_style));
        bowlStyleArray = (context.getResources().getStringArray(R.array.bowl_style));
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
        Player obj = getData().get(position);
        holder.data = obj;
        holder.title.setText(obj.getName());
        holder.ph_no.setText(batStyleArray[obj.getBattingSytle()] + (bowlStyleArray[obj.getBowlingStyle()].equalsIgnoreCase("none") ? "" : " | " + bowlStyleArray[obj.getBowlingStyle()]));
        holder.delete_item.setTag(position);

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView title, ph_no;
        public Player data;
        public RelativeLayout list_item_lay;

        public ImageView delete_item;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.playername);
            ph_no = (TextView) view.findViewById(R.id.time);
            delete_item = (ImageView) view.findViewById(R.id.delete_item);
            list_item_lay = (RelativeLayout) view.findViewById(R.id.list_item_lay);
            delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = getData().get(getAdapterPosition()).getpID();
                    boolean removed = RealmDB.removePlayer(realm, id);
                    if (!removed)
                        Toast.makeText(context, context.getString(R.string.player_in_match), Toast.LENGTH_SHORT).show();
                }
            });
            list_item_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle b = new Bundle();
                    b.putInt("id", getData().get(getAdapterPosition()).getpID());
                    EditPlayerProfile f = new EditPlayerProfile();
                    f.setArguments(b);
                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, f).addToBackStack(null).commit();
                }
            });
            view.setOnLongClickListener(this);
            // Uri s=Uri.EMPTY;
            //   s.getQueryParameter("q");
        }

        @Override
        public boolean onLongClick(View v) {
            // context.deleteItem(data);
            return true;
        }
    }
}