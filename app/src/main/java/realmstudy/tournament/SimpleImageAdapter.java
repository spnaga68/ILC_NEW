package realmstudy.tournament;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import realmstudy.R;
import realmstudy.base.BaseQuickAdapter;
import realmstudy.base.BaseViewHolder;
import realmstudy.extras.Utils;


/**
 * Created by developer on 8/11/17.
 */



public class SimpleImageAdapter extends BaseQuickAdapter<Media, BaseViewHolder> {
    private boolean check;
    private Context context;
    private List<Media> itemPlayer;

    public SimpleImageAdapter(Context context, int layoutResId, List<Media> data) {
        super(layoutResId, data);
        this.itemPlayer = data;
        this.context = context;
        this.check = check;
    }

    protected void convert(BaseViewHolder holder, final Media item) {

        holder.setText(R.id.tv_android,item.getOwnerID());
//        System.out.println("imageurlssss" + imagedata.get(i).getMediaUrl());
//        Picasso.with(context).load(imagedata.get(i).getMediaUrl()).placeholder(R.drawable.ic_loading).resize(240, 120).into(viewHolder.img_android);
//        holder.setText((int) R.id.txt_name, item.getPlayerName());
//        holder.setText((int) R.id.txt_team,  item.getPrize());
//        holder.setText((int) R.id.txt_detail, Html.fromHtml(item.getDetail()));
//        // holder.setText((int) R.id.txt_count, "" + (holder.getLayoutPosition() + 1));
//        holder.setText((int) R.id.txt_count, "" + item.getPosition());
        if(item.getType()==1){
            holder.getView(R.id.main_layout).getLayoutParams().height=250;
        }
        ImageView img_android = (ImageView) holder.getView(R.id.img_android);
        System.out.println("mediaurl__"+item.getMediaUrl());
        if (Utils.isEmptyString(item.getThumbnail())) {
            img_android.setImageResource(R.drawable.ic_cricket_player_with_bat);
        } else {
            Picasso.with(this.context).load(item.getThumbnail() ).placeholder((int) R.drawable.ic_cricket_player_with_bat).error((int) R.drawable.ic_cricket_player_with_bat).fit().into(img_android);;
        }
//        imgBg.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                //  Utils.showFullImage(SimpleImageAdapter.this.context, item.getProfilePhoto());
//            }
//        });
        img_android.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            }
        });
    }
}


//public class SimpleImageAdapter extends RecyclerView.Adapter<SimpleImageAdapter.ViewHolder> {
//    private ArrayList<Media> imagedata;
//    private Context context;
//
//    public SimpleImageAdapter(Context context, ArrayList<Media> imagedata) {
//        this.imagedata = imagedata;
//        this.context = context;
//    }
//
//    @Override
//    public SimpleImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_image_item, viewGroup, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(SimpleImageAdapter.ViewHolder viewHolder, int i) {
//
//        viewHolder.tv_android.setText(imagedata.get(i).getOwnerID());
//        System.out.println("imageurlssss" + imagedata.get(i).getMediaUrl());
//        Picasso.with(context).load(imagedata.get(i).getMediaUrl()).placeholder(R.drawable.ic_loading).resize(240, 120).into(viewHolder.img_android);
//    }
//
//    @Override
//    public int getItemCount() {
//        return imagedata.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView tv_android;
//        private ImageView img_android;
//
//        public ViewHolder(View view) {
//            super(view);
//
//            tv_android = (TextView) view.findViewById(R.id.tv_android);
//            img_android = (ImageView) view.findViewById(R.id.img_android);
//        }
//    }
//
//}