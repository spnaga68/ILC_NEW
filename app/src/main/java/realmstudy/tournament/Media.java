package realmstudy.tournament;

import java.util.HashMap;

/**
 * Created by developer on 15/9/17.
 */

public class Media {
    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    String mediaUrl;
    int likes;
    int type;
    String id;
    String ownerID;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    long date;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    String thumbnail;
    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    HashMap<String, String> likedBy = new HashMap<String, String>() {
    };

    public HashMap<String, String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(HashMap<String, String> media) {
        this.likedBy = media;
    }
}
