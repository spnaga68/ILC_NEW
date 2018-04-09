package realmstudy.videopackage;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import realmstudy.R;
import realmstudy.extras.Utils;
import realmstudy.tournament.Media;


/**
 * Created by developer on 26/9/17.
 */


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private Context mContext;
    private Media[] albumList;
    int selectedItem;
    VideoInterface videoInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //        public TextView title, count;
//        public ImageView thumbnail, overflow;
        LinearLayout card_view;
        TextView video_desc, video_date;
        ImageView video_thumb;

        public MyViewHolder(View view) {
            super(view);
//            title = (TextView) view.findViewById(R.id.title);
//            count = (TextView) view.findViewById(R.id.count);
//            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
            card_view = (LinearLayout) view.findViewById(R.id.card_view);
            video_desc = (TextView) view.findViewById(R.id.video_desc);
            video_thumb = (ImageView) view.findViewById(R.id.video_thumb);
            video_date = (TextView) view.findViewById(R.id.video_date);
            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedItem = getAdapterPosition();
                    notifyDataSetChanged();
                    videoInterface.videoSelected(selectedItem);
                }
            });
        }
    }

    public interface VideoInterface {
        void videoSelected(int pos);
    }

    public VideoAdapter(Context mContext, Media[] media, int pos, VideoInterface videoInterface) {
        this.mContext = mContext;
        this.albumList = media;
        this.selectedItem = pos;
        this.videoInterface = videoInterface;
        // this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    void setSelection(int pos) {
        selectedItem = pos;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        Album album = albumList.get(position);
        if (position == selectedItem)
            holder.card_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.divider));
        else
            holder.card_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        holder.video_desc.setText(albumList[position].getDescription());
        holder.video_date.setText(Utils.getDateOnly(albumList[position].getDate()));
        Picasso.with(mContext).load(albumList[position].getThumbnail()).into(holder.video_thumb);
//        holder.count.setText(album.getNumOfSongs() + " songs");
//
//        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
//
//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.action_add_favourite:
//                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }
    @Override
    public int getItemCount() {
        return albumList.length;
    }
}