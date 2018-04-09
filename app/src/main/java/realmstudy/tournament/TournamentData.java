package realmstudy.tournament;

/**
 * Created by developer on 18/9/17.
 */

public class TournamentData {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getPlayerwon() {
        return playerwon;
    }

    public void setPlayerwon(int playerwon) {
        this.playerwon = playerwon;
    }

    public int getBullwon() {
        return bullwon;
    }

    public void setBullwon(int bullwon) {
        this.bullwon = bullwon;
    }

    public int getTotalPlayer() {
        return totalPlayer;
    }

    public void setTotalPlayer(int totalPlayer) {
        this.totalPlayer = totalPlayer;
    }

    public int getTotalBull() {
        return totalBull;
    }

    public void setTotalBull(int totalBull) {
        this.totalBull = totalBull;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    String name;
    long date;
    long toDate=0;

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    int playerwon,bullwon;
    int totalPlayer,totalBull;
    String type;
    String venue;
    int status;
    String key;
    String tournamentCoverPhoto;

    public String getViewers() {
        return viewers;
    }

    public void setViewers(String viewers) {
        this.viewers = viewers;
    }

    String viewers;

    public String getTournamentCoverPhoto() {
        return tournamentCoverPhoto;
    }

    public void setTournamentCoverPhoto(String tournamentCoverPhoto) {
        this.tournamentCoverPhoto = tournamentCoverPhoto;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String bio) {
        this.about = bio;
    }

    String about

            ;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
