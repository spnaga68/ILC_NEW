package realmstudy.tournament;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import realmstudy.R;
import realmstudy.extras.Utils;
import realmstudy.tournament.StaisticsData;
import realmstudy.view.TextView;

/**
 * Created by developer on 8/11/17.
 */


public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {
    private ArrayList<StaisticsData> imagedata;
    private Context context;

    public StatisticsAdapter(Context context, ArrayList<StaisticsData> imagedata) {
        this.imagedata = imagedata;
        this.context = context;
    }

    @Override
    public StatisticsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statistics_item_lay, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StatisticsAdapter.ViewHolder viewHolder, int i) {

        viewHolder.statistics_count.setText(""+imagedata.get(i).getCount());
        viewHolder.tv_android.setText(imagedata.get(i).getTitle());
        System.out.println("imageurlssss" + imagedata.get(i).getTitle());
        //Picasso.with(context).load(imagedata.get(i).getStaisticsDataUrl()).placeholder(R.drawable.ic_cricket_player_with_bat).resize(240, 120).into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return imagedata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_android;
        private TextView statistics_count;

        public ViewHolder(View view) {
            super(view);

            tv_android = (TextView) view.findViewById(R.id.tv_android);
            statistics_count = (TextView) view.findViewById(R.id.statistics_count);
            statistics_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.showAlert(context, imagedata.get(getAdapterPosition()).getTitle(), imagedata.get(getAdapterPosition()).getDiscription(), context.getString(R.string.ok), "", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },true, null);
                }
            });
        }
    }

}