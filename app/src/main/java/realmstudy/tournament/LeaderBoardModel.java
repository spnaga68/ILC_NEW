package realmstudy.tournament;

/**
 * Created by developer on 6/11/17.
 */

public class LeaderBoardModel {
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

   // String playerName,playerId,detail,prize,position,playerImage;
    String playerName="Naga",playerId="213cdf",detail="Tamped: <b>27</b>  &nbsp;&nbsp; |&nbsp;&nbsp;   Atempted:43",prize="Won: Bullet classic 350";
    int position ;

    public String getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(String playerImage) {
        this.playerImage = playerImage;
    }

    String playerImage="https://firebasestorage.googleapis.com/v0/b/ilovecricket-5c636.appspot.com/o/images%2FrNrPm6uevAPM5NaTr0wLdBZnbAu1%2Ffeed2378.jpg?alt=media&token=6f141e32-f7d7-46ad-992f-21458cf22830";
}
