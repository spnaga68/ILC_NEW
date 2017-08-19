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
    String toss;
    String location;
    double latitude, longtitue;
    String homeTeam;
    String awayTeam;

    public String getElectTo() {
        return electTo;
    }

    public void setElectTo(String electTo) {
        this.electTo = electTo;
    }

    String electTo;

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }


    public String getToss() {
        return toss;
    }

    public void setToss(String toss) {
        this.toss = toss;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitue() {
        return longtitue;
    }

    public void setLongtitue(double longtitue) {
        this.longtitue = longtitue;
    }



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    int status;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    long time;

    public String getQuotes() {
        return quotes;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    String quotes;


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




}
