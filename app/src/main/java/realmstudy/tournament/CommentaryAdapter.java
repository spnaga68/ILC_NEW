package realmstudy.tournament;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import realmstudy.R;
import realmstudy.base.BaseMultiItemQuickAdapter;
import realmstudy.base.BaseQuickAdapter;
import realmstudy.base.BaseViewHolder;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * modify by AllenCoder
 */
public class CommentaryAdapter extends BaseMultiItemQuickAdapter<BaseDashboardMultiItem, BaseViewHolder> {
    Context context;

    public CommentaryAdapter(Context context, List data) {
        super(data);
        this.context = context;
        addItemType(MultipleItem.TEXT, R.layout.commentay_text);
     //   addItemType(11, R.layout.commentay_text);
        addItemType(MultipleItem.IMG_TEXT, R.layout.commentary_video);
//        addItemType(99, R.layout.raw_dashboard_media);
//        addItemType(MultipleItem.IMG_TEXT, R.layout.raw_player_leaderboard);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseDashboardMultiItem item) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.mContext, 0, false);
        BaseQuickAdapter adapter;
        switch (helper.getItemViewType()) {
            case MultipleItem.TEXT:
                helper.setText(R.id.commentary_time, item.getTitle());
                helper.setText(R.id.commentary_text, Html.fromHtml( getHighlightedText(item.getTitle(),true)+" "+item.getDescription()));
                break;


            case MultipleItem.IMG_TEXT:
                helper.setText(R.id.commentary_time, item.getTitle());
                helper.setText(R.id.commentary_text, Html.fromHtml( getHighlightedText(item.getTitle()+" "+item.getDescription(),true)));
//                helper.setText(R.id.commentary_text, item.getDescription());

                break;


        }

    }
    private String getHighlightedText(String text, boolean isHighlight) {
        if (isHighlight) {
            return "<b>" + text + "</b>";
        }
        return text;
    }
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = super.onCreateDefViewHolder(parent, viewType);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.mContext, 0, false);
        final BaseViewHolder baseViewHolder;
        switch (viewType) {

            case MultipleItem.IMG_TEXT:
                ImageView media_share = (ImageView) viewHolder.getView(R.id.media_share);
                media_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
//                rvVideoDataViewer.addOnItemTouchListener(new realmstudy.base.listener.OnItemClickListener() {
//                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int i) {
////                        if (CommentaryAdapter.this.dashboardItemClickListener != null) {
////                            CommentaryAdapter.this.dashboardItemClickListener.onSuperPlayerClick((Player) ((DashboardSuperPlayerAdapter) adapter).getItem(i), baseViewHolder.itemView);
////                        }
//                        mContext.startActivity(new Intent(mContext, VideoActivityMain.class));
//                    }
//                });

                break;

        }
        return viewHolder;
    }
}
