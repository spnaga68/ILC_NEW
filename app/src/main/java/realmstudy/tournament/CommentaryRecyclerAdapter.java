package realmstudy.tournament;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import realmstudy.R;
import realmstudy.base.BaseQuickAdapter;
import realmstudy.base.BaseViewHolder;
import realmstudy.extras.Utils;


/**
 * Created by Admin on 05-11-2017.
 */

 class MyViewHolder extends BaseViewHolder {
    public TextView commentary_time, commentary_text, genre;
   // SimpleExoPlayerView media_video;
    ImageView media_image;

    public MyViewHolder(View view) {
        super(view);
        commentary_time = (TextView) view.findViewById(R.id.commentary_time);
        commentary_text = (TextView) view.findViewById(R.id.commentary_text);
      //  media_video = (SimpleExoPlayerView) view.findViewById(R.id.media_video);
        media_image = (ImageView) view.findViewById(R.id.media_image);

    }
}


public class CommentaryRecyclerAdapter extends BaseQuickAdapter<CommentaryData, BaseViewHolder> {
    private boolean check;
    private Context context;
    private List<CommentaryData> itemPlayer;

    public CommentaryRecyclerAdapter(Context context, int layoutResId, List<CommentaryData> data, boolean check) {
        super(layoutResId, data);
        this.itemPlayer = data;
        this.context = context;
        this.check = check;
    }
    public CommentaryRecyclerAdapter(Context context, int layoutResId, List<CommentaryData> data) {
        super(layoutResId, data);
        this.itemPlayer = data;
        this.context = context;
        this.check = check;
    }   private String getHighlightedText(String text, boolean isHighlight) {
        if (isHighlight) {
            return "<b>" + text + "</b>";
        }
        return text;
    }
    protected void convert(BaseViewHolder holder, final CommentaryData item) {
        holder.setText(R.id.commentary_text, Html.fromHtml( getHighlightedText(Utils.getTimeOnly((item.getTime()))+"     ",true)+item.getLong_description().trim()));
        holder.setText(R.id.commentary_time, Utils.getDateOnly((item.getTime())));
        ImageView imgBg = (ImageView) holder.getView(R.id.media_image);
        if(item.getMedia()!=null){
            if(item.getMedia().getType()==0){
                holder.setVisible(R.id.media_image,false);
                //holder.media_video.setVisibility(View.GONE);
               // Picasso.with(context).load(item.getMedia().getMediaUrl()).into(imgBg);

            }else{
                holder.setVisible(R.id.media_image,false);
                //holder.media_video.setVisibility(View.GONE);
                //Picasso.with(context).load(item.getMedia().getMediaUrl()).into(imgBg);
            }
        }
//        holder.setText((int) R.id.txt_name, item.getPlayerName());
//        holder.setText((int) R.id.txt_team,  item.getPrize());
//        holder.setText((int) R.id.txt_detail, Html.fromHtml(item.getDetail()));
//        // holder.setText((int) R.id.txt_count, "" + (holder.getLayoutPosition() + 1));
//        holder.setText((int) R.id.txt_count, "" + item.getPosition());
//        ImageView imgBg = (CircleImageView) holder.getView(R.id.img_player);
//        if (Utils.isEmptyString(item.getPlayerImage())) {
//            imgBg.setImageResource(R.drawable.ic_cricket_player_with_bat);
//        } else {
//            Picasso.with(this.context).load(item.getPlayerImage() + AppConstants.THUMB_IMAGE).placeholder((int) R.drawable.ic_cricket_player_with_bat).error((int) R.drawable.ic_cricket_player_with_bat).fit().centerCrop().into(imgBg);
//        }
//        imgBg.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                //  Utils.showFullImage(CommentaryRecyclerAdapter.this.context, item.getProfilePhoto());
//            }
//        });
//        ((LinearLayout) holder.getView(R.id.linearLayoutGrid)).setOnClickListener(new OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(CommentaryRecyclerAdapter.this.context, PlayerProfileActivity.class);
//                intent.putExtra(AppConstants.EXTRA_MY_PROFILE, false);
//                intent.putExtra(AppConstants.EXTRA_PLAYER_ID, item.getPlayerId());
//                CommentaryRecyclerAdapter.this.context.startActivity(intent);
//            }
//        });
    }
}

//public class CommentaryRecyclerAdapter extends BaseQuickAdapter<CommentaryData, MyViewHolder> {
//    ArrayList<CommentaryData> data;
//    Context c;
//    int layoutResId;
//
//    public CommentaryRecyclerAdapter(Context context, int layoutResId, ArrayList<CommentaryData> data) {
//        super(layoutResId, data);
//        this.data=data;
//        this.c=context;
//        this.layoutResId=layoutResId;
//        
//    }
//
//    
////     CommentaryRecyclerAdapter (ArrayList<CommentaryData> itemArrayList, Context c){
////         this.itemArrayList=itemArrayList;
////         this.c=c;
////     }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////        if (viewType == 0) {
////            View itemView = LayoutInflater.from(parent.getContext())
////                    .inflate(R.layout.commentay_text, parent, false);
////
////            return new MyViewHolder(itemView);
////        } else {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.commentary_video, parent, false);
//
//            return new MyViewHolder(itemView);
//       // }
//    }
//
//    @Override
//    protected void convert(MyViewHolder holder, CommentaryData item) {
//      //  CommentaryData commentaryData=itemArrayList.get(position);
//        holder.commentary_text.setText(item.getLong_description());
//        holder.commentary_time.setText(item.getTime());
//        if(item.getMedia()!=null){
//            if(item.getMedia().getType()==0){
//                holder.media_image.setVisibility(View.VISIBLE);
//                //holder.media_video.setVisibility(View.GONE);
//                Picasso.with(c).load(item.getMedia().getMediaUrl()).into(holder.media_image);
//
//            }else{
//                holder.media_image.setVisibility(View.VISIBLE);
//                //holder.media_video.setVisibility(View.GONE);
//                Picasso.with(c).load(item.getMedia().getMediaUrl()).into(holder.media_image);
//            }
//        }
//
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return  data.get(position).getMedia()== null? 0:1;
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//            }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//}
