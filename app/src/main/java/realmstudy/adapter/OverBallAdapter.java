package realmstudy.adapter;

import android.content.Context;
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
import realmstudy.lib.customViews.AutoResizeTextView;

/**
 * Created by developer on 12/6/17.
 */

public class OverBallAdapter extends RecyclerView.Adapter<OverBallAdapter.MyViewHolder> {


    private final OverAdapterData datas;
    private final Context c;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AutoResizeTextView main_lay;

        public MyViewHolder(View view) {
            super(view);
            main_lay = (AutoResizeTextView) view.findViewById(R.id.main_lay);
        }
    }


    @Override
    public void onBindViewHolder(OverBallAdapter.MyViewHolder holder, int position) {
        //OverAdapterData data = datas.get(position);

        // for (int i = 0; i < datas.getDeliveries().size(); i++) {
        AutoResizeTextView textView = holder.main_lay;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
        layoutParams.setMargins(20, 20, 20, 10);
        textView.setLayoutParams(layoutParams);
        textView.setSolidColor("#BCAAA4");
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(5, 5, 5, 5);
        textView.setText(datas.getDeliveries().get(position));
        //holder.main_lay.addView(textView);
        //  }
    }

    @Override
    public int getItemCount() {
        return datas.getDeliveries().size();
    }

    public OverBallAdapter(Context c, OverAdapterData datas) {
        this.datas = datas;
        //System.out.println("________sss" + datas.size());
        this.c = c;
    }

    @Override
    public OverBallAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_layout, parent, false);
        return new OverBallAdapter.MyViewHolder(itemView);
    }
}
