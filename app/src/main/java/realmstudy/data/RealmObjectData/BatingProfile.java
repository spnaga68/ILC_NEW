package realmstudy.data.RealmObjectData;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by developer on 10/12/16.
 */
public class BatingProfile extends RealmObject {
    int runs;
    int ballFaced;
    int playerID;
    @PrimaryKey
    String battingProfileID;
    int currentStatus;
    int battedAt;
    boolean inFirstinnings = true;
    int match_id;
    int fours, sixes, ones, twos, threes, dots;
    Wicket wicket;
    int matches;
    int innings;
    int highest, not_out;
    int ducks;
    float average, strike_rate;
    int match_type;
    int no_of_fities,no_of_thirties,no_of_hundreds;

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    String extraDetails;


    public int getBattedAt() {
        return battedAt;
    }

    public void setBattedAt(int battedAt) {
        this.battedAt = battedAt;
    }

    public int getFours() {
        return fours;
    }

    public void setFours(int fours) {
        this.fours = fours;
    }

    public int getSixes() {
        return sixes;
    }

    public void setSixes(int sixes) {
        this.sixes = sixes;
    }

    public int getOnes() {
        return ones;
    }

    public boolean isInFirstinnings() {
        return inFirstinnings;
    }

    public void setInFirstinnings(boolean inFirstinnings) {
        this.inFirstinnings = inFirstinnings;
    }

    public void setOnes(int ones) {
        this.ones = ones;
    }

    public int getTwos() {
        return twos;
    }

    public void setTwos(int twos) {
        this.twos = twos;
    }

    public int getThrees() {
        return threes;
    }

    public void setThrees(int threes) {
        this.threes = threes;
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public Wicket getWicket() {
        return wicket;
    }

    public void setWicket(Wicket wicket) {
        this.wicket = wicket;
    }


    /**
     * 0-->free
     * 1-->batting
     * 2-->out_match_inProgress
     *
     * @return current status of batsman
     */
    public int getCurrentStatus() {
        return currentStatus;
    }

    /**
     * StatusFree = 0;
     * StatusBatting = 1;
     * StatusInMatch = 2;
     * StatusOut = 3;
     *
     * @return current status of batsman
     */

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }


    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }


    public String getBattingProfileID() {
        return battingProfileID;
    }

    public void setBattingProfileID(String battingProfileID) {
        this.battingProfileID = battingProfileID;
    }


    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }


    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getBallFaced() {
        return ballFaced;
    }

    public void setBallFaced(int ballFaced) {
        this.ballFaced = ballFaced;
    }


}
