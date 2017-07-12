package realmstudy.data.RealmObjectData;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by developer on 26/12/16.
 */
public class Player extends RealmObject {
    String name,nick_name;
//    BatingProfile recentBatingProfile;
//    BowlingProfile recentBowlingProfile;

    int status;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    String ph_no;
    String dob;
    int battingSytle;
    int bowlingStyle;
    int allRounder;

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    int teamID;
    String phone_number;
    @PrimaryKey
    int pID;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getBattingSytle() {
        return battingSytle;
    }

    public void setBattingSytle(int battingSytle) {
        this.battingSytle = battingSytle;
    }

    public int getBowlingStyle() {
        return bowlingStyle;
    }

    public void setBowlingStyle(int bowlingStyle) {
        this.bowlingStyle = bowlingStyle;
    }

    public int getRole() {
        return allRounder;
    }

    public void setAllRounder(int allRounder) {
        this.allRounder = allRounder;
    }



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPh_no() {
        return ph_no;
    }

    public void setPh_no(String ph_no) {
        this.ph_no = ph_no;
    }


//    public BatingProfile getRecentBatingProfile() {
//
//        return recentBatingProfile;
//
//    }
//
//    public void setRecentBatingProfile(BatingProfile recentBatingProfile) {
//        this.recentBatingProfile = recentBatingProfile;
//    }
//
//    public BowlingProfile getRecentBowlingProfile() {
//        return recentBowlingProfile;
//    }
//
//    public void setRecentBowlingProfile(BowlingProfile recentBowlingProfile) {
//        this.recentBowlingProfile = recentBowlingProfile;
//    }


    public int getpID() {
        return pID;
    }

    public void setpID(int pID) {
        this.pID = pID;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
       // String phno = ph_no != null ? "  (" + ph_no + ")" : "";
        String phno = "";
        return name + phno;
    }
}
