package realmstudy.data;

/**
 * Created by developer on 5/6/17.
 */

public class MatchShortSummaryData {

    boolean isFirstInnings;
    String battingTeamName;
    String bowlingTeamName;
    InningsSummary firstInningsSummary;
    InningsSummary secondInningsSummary;


    public boolean isFirstInnings() {
        return isFirstInnings;
    }

    public void setFirstInnings(boolean firstInnings) {
        isFirstInnings = firstInnings;
    }

    public String getBattingTeamName() {
        return battingTeamName;
    }

    public void setBattingTeamName(String battingTeamName) {
        this.battingTeamName = battingTeamName;
    }

    public String getBowlingTeamName() {
        return bowlingTeamName;
    }

    public void setBowlingTeamName(String bowlingTeamName) {
        this.bowlingTeamName = bowlingTeamName;
    }

    public InningsSummary getFirstInningsSummary() {
        return firstInningsSummary;
    }

    public void setFirstInningsSummary(InningsSummary firstInningsSummary) {
        this.firstInningsSummary = firstInningsSummary;
    }

    public InningsSummary getSecondInningsSummary() {
        return secondInningsSummary;
    }

    public void setSecondInningsSummary(InningsSummary secondInningsSummary) {
        this.secondInningsSummary = secondInningsSummary;
    }


    public class InningsSummary {
        public int wicket;
        public int run;
        public String overs;

    }

}
