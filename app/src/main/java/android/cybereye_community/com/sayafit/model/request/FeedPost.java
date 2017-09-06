package android.cybereye_community.com.sayafit.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class FeedPost {
    @SerializedName(value = "feed")
    String feed;
    @SerializedName(value = "email")
    String email;
    @SerializedName(value = "nama")
    String nama;
    @SerializedName(value = "preview")
    String preview;
    @SerializedName(value = "image")
    String image;
    @SerializedName(value = "lat")
    String latitude;
    @SerializedName(value = "lng")
    String longitude;
    @SerializedName(value = "description")
    String description;
    @SerializedName(value = "awareness_activity")
    String awereness_activity;

    public FeedPost(){}
    public FeedPost(String feed, String email, String nama, String preview, String image) {
        this.feed = feed;
        this.email = email;
        this.nama = nama;
        this.preview = preview;
        this.image = image;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAwereness_activity() {
        return awereness_activity;
    }

    public void setAwereness_activity(String awereness_activity) {
        this.awereness_activity = awereness_activity;
    }
}
