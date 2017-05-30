package realmstudy.data.RealmObjectData;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by developer on 26/12/16.
 */
public class Player extends RealmObject {
    String name;
//    BatingProfile recentBatingProfile;
//    BowlingProfile recentBowlingProfile;

    int status;
    @PrimaryKey
    String ph_no;

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

    int pID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        String phno = ph_no != null ? "  (" + ph_no + ")" : "";
        return name + phno;
    }
}
