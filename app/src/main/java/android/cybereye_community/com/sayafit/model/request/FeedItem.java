
package android.cybereye_community.com.sayafit.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedItem {

    @SerializedName("id_feed")
    @Expose
    private String idFeed;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("feed")
    @Expose
    private Object feed;
    @SerializedName("date")
    @Expose
    private String date;

    public String getIdFeed() {
        return idFeed;
    }

    public void setIdFeed(String idFeed) {
        this.idFeed = idFeed;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getFeed() {
        return feed;
    }

    public void setFeed(Object feed) {
        this.feed = feed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
