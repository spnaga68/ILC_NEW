package realmstudy.adapter;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import realmstudy.R;
import realmstudy.data.OverAdapterData;
import realmstudy.data.RealmObjectData.InningsData;
import realmstudy.lib.customViews.AutoResizeTextView;

/**
 * Created by developer on 17/4/17.
 */

public class OverRvAdapter extends RecyclerView.Adapter<OverRvAdapter.MyViewHolder> {

    private final Context c;
    private List<OverAdapterData> datas;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView over_players, overs, over_total_run;
        LinearLayout item_over_lay;

        public MyViewHolder(View view) {
            super(view);
            over_players = (TextView) view.findViewById(R.id.over_players);
            overs = (TextView) view.findViewById(R.id.overs);
            over_total_run = (TextView) view.findViewById(R.id.over_total_run);
            item_over_lay = (LinearLayout) view.findViewById(R.id.item_over_lay);
        }
    }


    public OverRvAdapter(Context c,List<OverAdapterData> datas) {
        this.datas = datas;
        System.out.println("________sss"+datas.size());
        this.c=c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.over_rv_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OverAdapterData data = datas.get(position);
        holder.over_players.setText(data.getBolwers() + " to " + data.getBatsmans());
        holder.overs.setText("Ov "+String.valueOf(data.getOver()));
        holder.over_total_run.setText(data.getTotal_run() + " runs");
        for(int i=0;i<data.getDeliveries().size();i++){
            AutoResizeTextView textView=new AutoResizeTextView(c);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60,60);
//            layoutParams.width=20;
//            layoutParams.height=20;
            layoutParams.setMargins(10,10,10,10);
            textView.setLayoutParams(layoutParams);
            textView.setSolidColor("#BCAAA4");
         //   textView.setStrokeColor("#43A047");
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(8,8,8,8);

            textView.setText(data.getDeliveries().get(i));
            holder.item_over_lay.addView(textView);
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}