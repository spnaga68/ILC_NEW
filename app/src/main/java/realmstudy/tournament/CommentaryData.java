package realmstudy.tournament;


/**
 * Created by Admin on 05-11-2017.
 */

 class CommentaryData {
    public String getLong_description() {
        return long_description;
    }

    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    Media media;
    String long_description;
    long time;

    public String getTournament_key() {
        return tournament_key;
    }

    public void setTournament_key(String tournament_key) {
        this.tournament_key = tournament_key;
    }

    String tournament_key;
}
