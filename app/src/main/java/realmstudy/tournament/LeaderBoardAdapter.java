package realmstudy.tournament;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import realmstudy.R;
import realmstudy.base.BaseQuickAdapter;
import realmstudy.base.BaseViewHolder;
import realmstudy.extras.Utils;
import realmstudy.view.CircleImageView;


public class LeaderBoardAdapter extends BaseQuickAdapter<LeaderBoardModel, BaseViewHolder> {
    private boolean check;
    private Context context;
    private List<LeaderBoardModel> itemPlayer;

    public LeaderBoardAdapter(Context context, int layoutResId, List<LeaderBoardModel> data, boolean check) {
        super(layoutResId, data);
        this.itemPlayer = data;
        this.context = context;
        this.check = check;
    }

    protected void convert(BaseViewHolder holder, final LeaderBoardModel item) {
        holder.setText((int) R.id.txt_name, item.getPlayerName());
        holder.setText((int) R.id.txt_team,  item.getPrize());
        holder.setText((int) R.id.txt_detail, Html.fromHtml(item.getDetail()));
       // holder.setText((int) R.id.txt_count, "" + (holder.getLayoutPosition() + 1));
        holder.setText((int) R.id.txt_count, "" + item.getPosition());
        ImageView imgBg = (CircleImageView) holder.getView(R.id.img_player);
        if (Utils.isEmptyString(item.getPlayerImage())) {
            imgBg.setImageResource(R.drawable.ic_cricket_player_with_bat);
        } else {
            Picasso.with(this.context).load(item.getPlayerImage() ).placeholder((int) R.drawable.ic_cricket_player_with_bat).error((int) R.drawable.ic_cricket_player_with_bat).fit().centerCrop().into(imgBg);
        }
        imgBg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
              //  Utils.showFullImage(LeaderBoardAdapter.this.context, item.getProfilePhoto());
            }
        });
//        ((LinearLayout) holder.getView(R.id.linearLayoutGrid)).setOnClickListener(new OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(LeaderBoardAdapter.this.context, PlayerProfileActivity.class);
//                intent.putExtra(AppConstants.EXTRA_MY_PROFILE, false);
//                intent.putExtra(AppConstants.EXTRA_PLAYER_ID, item.getPlayerId());
//                LeaderBoardAdapter.this.context.startActivity(intent);
//            }
//        });
    }
}
