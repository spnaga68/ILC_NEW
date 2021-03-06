package realmstudy.data;

import java.util.ArrayList;

/**
 * Created by developer on 2/5/17.
 */
public class ScoreCardDetailData {



    String teamName;
    String TeamRun_over;
    int total_extras;
    String extras_detail;
    float current_run_rate;
    ArrayList<BatsmanDetail> batsmanDetails=new ArrayList<>();
    ArrayList<BowlersDetail> bowlersDetails=new ArrayList<>();
    ArrayList<FOW> fow=new ArrayList<>();
    ArrayList<PowerPlay> powerPlay=new ArrayList<>();
    public static class BatsmanDetail{
        public  String name,outAs;
        public  int runs,balls,fours,sixes;
                public String strike_rate="";
    }
    public static class BowlersDetail{
        public String name,outAs,overs,ecnomic_rate;

        public   int maiden,runs,wicket;
    }
    public static class FOW{
        public   String name,overs;
        public int score;
    }
    public static class PowerPlay{
        public String name,overs;
        public int runs;
    }



    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamRun_over() {
        return TeamRun_over;
    }

    public void setTeamRun_over(String teamRun_over) {
        TeamRun_over = teamRun_over;
    }

    public int getTotal_extras() {
        return total_extras;
    }

    public void setTotal_extras(int total_extras) {
        this.total_extras = total_extras;
    }

    public String getExtras_detail() {
        return extras_detail;
    }

    public void setExtras_detail(String extras_detail) {
        this.extras_detail = extras_detail;
    }

    public float getCurrent_run_rate() {
        return current_run_rate;
    }

    public void setCurrent_run_rate(float current_run_rate) {
        this.current_run_rate = current_run_rate;
    }

    public ArrayList<BatsmanDetail> getBatsmanDetails() {
        return batsmanDetails;
    }

    public void addBatsmanDetails(BatsmanDetail batsmanDetails) {
        this.batsmanDetails.add(batsmanDetails);
    }

    public ArrayList<BowlersDetail> getBowlersDetails() {
        return bowlersDetails;
    }

    public void addBowlersDetails(BowlersDetail bowlersDetails) {
        this.bowlersDetails.add(bowlersDetails);
    }

    public ArrayList<FOW> getFow() {
        return fow;
    }

    public void addFow(FOW fow) {
        this.fow.add(fow);
    }

    public ArrayList<PowerPlay> getPowerPlay() {
        return powerPlay;
    }

    public void setPowerPlay(ArrayList<PowerPlay> powerPlay) {
        this.powerPlay = powerPlay;
    }


}
