package android.cybereye_community.com.sayafit.controller.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */
@Entity(nameInDb = "FeedTbl ")
public class FeedTbl implements Parcelable{
    private static final long serialVersionUID=1L;
    @Id
    @Property(nameInDb = "FeedID")
    public Long FeedID;

    @Property(nameInDb = "feed")
    public String feed;
    @Property(nameInDb = "image")
    public String image;
    @Property(nameInDb = "date")
    public String date;
    @Property(nameInDb = "email")
    public String email;
    @Property(nameInDb = "status")
    public String status;
    @Property(nameInDb = "preview")
    public String preview;
    @Property(nameInDb = "lat")
    public double lat;
    @Property(nameInDb = "lng")
    public double lng;
    @Property(nameInDb = "description")
    public String description;
    @Property(nameInDb = "awareness_activity")
    public String awarenessActivity;
    @Generated(hash = 1096379445)
    public FeedTbl(Long FeedID, String feed, String image, String date,
            String email, String status, String preview, double lat, double lng,
            String description, String awarenessActivity) {
        this.FeedID = FeedID;
        this.feed = feed;
        this.image = image;
        this.date = date;
        this.email = email;
        this.status = status;
        this.preview = preview;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
        this.awarenessActivity = awarenessActivity;
    }
    @Generated(hash = 1830382152)
    public FeedTbl() {
    }
    public Long getFeedID() {
        return this.FeedID;
    }
    public void setFeedID(Long FeedID) {
        this.FeedID = FeedID;
    }
    public String getFeed() {
        return this.feed;
    }
    public void setFeed(String feed) {
        this.feed = feed;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getPreview() {
        return this.preview;
    }
    public void setPreview(String preview) {
        this.preview = preview;
    }
    public double getLat() {
        return this.lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLng() {
        return this.lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAwarenessActivity() {
        return this.awarenessActivity;
    }
    public void setAwarenessActivity(String awarenessActivity) {
        this.awarenessActivity = awarenessActivity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.FeedID);
        dest.writeString(this.feed);
        dest.writeString(this.image);
        dest.writeString(this.date);
        dest.writeString(this.email);
        dest.writeString(this.status);
        dest.writeString(this.preview);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.description);
        dest.writeString(this.awarenessActivity);
    }

    protected FeedTbl(Parcel in) {
        this.FeedID = (Long) in.readValue(Long.class.getClassLoader());
        this.feed = in.readString();
        this.image = in.readString();
        this.date = in.readString();
        this.email = in.readString();
        this.status = in.readString();
        this.preview = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.description = in.readString();
        this.awarenessActivity = in.readString();
    }

    public static final Creator<FeedTbl> CREATOR = new Creator<FeedTbl>() {
        @Override
        public FeedTbl createFromParcel(Parcel source) {
            return new FeedTbl(source);
        }

        @Override
        public FeedTbl[] newArray(int size) {
            return new FeedTbl[size];
        }
    };
}
