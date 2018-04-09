package realmstudy.tournament;


import realmstudy.base.BaseQuickAdapter;
import realmstudy.base.entity.MultiItemEntity;

public abstract class BaseDashboardMultiItem implements MultiItemEntity {
    public static final int ASSOCIATION_ITEM_TYPE = 50;
    public static final int CRICKET_TIPS = 10;
    public static final int CRIC_POLL = 16;
    public static final int CRIC_QUIZ = 15;
    public static final int FEATURED_PLAYER_ITEM_TYPE = 22;
    public static final int INVITE_AND_WIN = 17;
    public static final int MATCH_ITEM_TYPE = 11;
    public static final int MEDIA_ITEM_TYPE = 44;
    public static final int NEWS_ITEM_TYPE = 77;
    public static final int PLAYER_YOU_KNOW_ITEM_TYPE = 66;
    public static final int SUPER_PLAYER_ITEM_TYPE = 20;
    public static final int TEAM_ITEM_TYPE = 33;
    public static final int TOP_MENU_TYPE = 88;
    public static final int TOURNAMENT_ITEM_TYPE = 55;
    public static final int VIDEO = 99;
    private String description;
    private int itemType;
    private String title;

    protected abstract BaseQuickAdapter getAdapter();

    public BaseDashboardMultiItem(String title, String description, int itemType) {
        this.title = title;
        this.description = description;
        this.itemType = itemType;
    }

    public BaseDashboardMultiItem(int itemType) {
        this.itemType = itemType;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public int getItemType() {
        return this.itemType;
    }
}
