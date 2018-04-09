package realmstudy.data;

import java.util.ArrayList;
import java.util.List;

import realmstudy.extras.Utils;

/**
 * Created by developer on 30/5/17.
 */

public class DetailedScoreData {
    List<OverAdapterData> overAdapterData = new ArrayList<>();
    ScoreBoardData scoreBoardData = new ScoreBoardData();
    ScoreCardDetailData scoreCardDetailData = new ScoreCardDetailData();
    ScoreCardDetailData secscoreCardDetailData = new ScoreCardDetailData();

    public ScoreCardDetailData getSecscoreCardDetailData() {
        return secscoreCardDetailData;
    }

    public void setSecscoreCardDetailData(ScoreCardDetailData secscoreCardDetailData) {
        this.secscoreCardDetailData = secscoreCardDetailData;
    }


    public List<OverAdapterData> getOverAdapterData() {
        return overAdapterData;
    }

    public void setOverAdapterData(List<OverAdapterData> overAdapterData) {
        this.overAdapterData = overAdapterData;
    }

    public ScoreBoardData getScoreBoardData() {
        return scoreBoardData;
    }

    public void setScoreBoardData(ScoreBoardData scoreBoardData) {
        this.scoreBoardData = scoreBoardData;
    }

    public ScoreCardDetailData getScoreCardDetailData() {
        return scoreCardDetailData;
    }

    public void setScoreCardDetailData(ScoreCardDetailData scoreCardDetailData) {
        this.scoreCardDetailData = scoreCardDetailData;
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }
}
