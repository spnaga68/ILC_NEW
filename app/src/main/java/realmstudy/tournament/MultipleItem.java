package realmstudy.tournament;


import realmstudy.base.BaseQuickAdapter;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MultipleItem extends BaseDashboardMultiItem {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int IMG_TEXT = 3;
    public static final int TEXT_SPAN_SIZE = 3;
    public static final int IMG_SPAN_SIZE = 1;
    public static final int IMG_TEXT_SPAN_SIZE = 4;
    public static final int IMG_TEXT_SPAN_SIZE_MIN = 2;
    private  int itemType;
    private String title;
    private String description;

    public MultipleItem(String title, String description, int itemType) {
        super(title, description, itemType);
        this.title = title;
        this.description = description;
        this.itemType = itemType;
    }

//    public MultipleItem(int itemType, int spanSize) {
//        super(title, description, itemType);
//        this.itemType = itemType;
//        this.spanSize = spanSize;
//    }

    public int getSpanSize() {
        return 4;
    }

    public void setSpanSize(int spanSize) {

    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return null;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
