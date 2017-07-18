package realmstudy.adapter;

/**
 * Created by developer on 27/12/16.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.data.RealmObjectData.Ground;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.fragments.AddNewGround;
import realmstudy.fragments.GroundListFragment;
import realmstudy.interfaces.ItemClickInterface;


public class GroundListAdapter extends RealmRecyclerViewAdapter<Ground, GroundListAdapter.MyViewHolder> {

    private Context context;
    @Inject
    Realm realm;
    String[] batStyleArray, bowlStyleArray;
    ItemClickInterface itemClickInterface;

    public GroundListAdapter(Context activity, OrderedRealmCollection<Ground> data) {
        super(activity, data, true);
        this.context = activity;
        batStyleArray = (context.getResources().getStringArray(R.array.bat_style));
        bowlStyleArray = (context.getResources().getStringArray(R.array.bowl_style));
        ((MyApplication) ((Activity) context).getApplication()).getComponent().inject(this);
    }

    public GroundListAdapter(Context activity, ItemClickInterface itemClickInterface, OrderedRealmCollection<Ground> data) {
        super(activity, data, true);
        this.context = activity;
        this.itemClickInterface = itemClickInterface;
        batStyleArray = (context.getResources().getStringArray(R.array.bat_style));
        bowlStyleArray = (context.getResources().getStringArray(R.array.bowl_style));
        ((MyApplication) ((Activity) context).getApplication()).getComponent().inject(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ground_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Ground obj = getData().get(position);
        holder.data = obj;
        holder.ground_name.setText(obj.getGroundName());
        holder.region.setText(obj.getRegionName());
        holder.delete_item.setTag(position);

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView ground_name, region;
        public Ground data;
        public RelativeLayout list_item_lay;

        public ImageView delete_item;

        public MyViewHolder(View view) {
            super(view);
            ground_name = (TextView) view.findViewById(R.id.ground_name);
            region = (TextView) view.findViewById(R.id.region);
            delete_item = (ImageView) view.findViewById(R.id.delete_item);
            list_item_lay = (RelativeLayout) view.findViewById(R.id.list_item_lay);
            delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = getData().get(getAdapterPosition()).getId();
                    RealmDB.removeGround(realm, id);
                }
            });
            list_item_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = getData().get(getAdapterPosition()).getId();
                    if (itemClickInterface != null)
                        ((ItemClickInterface) context).itemPicked(id, "");
                    else {
                        Bundle b = new Bundle();
                        b.putInt("id", id);
                        AddNewGround f = new AddNewGround();
                        f.setArguments(b);
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, f).addToBackStack(null).commit();
                    }
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