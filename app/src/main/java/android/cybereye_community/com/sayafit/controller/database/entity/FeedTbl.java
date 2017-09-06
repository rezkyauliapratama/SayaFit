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
public class FeedTbl implements Parcelable {
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
@Generated(hash = 508568245)
public FeedTbl(Long FeedID, String feed, String image, String date,
        String email, String status, String preview) {
    this.FeedID = FeedID;
    this.feed = feed;
    this.image = image;
    this.date = date;
    this.email = email;
    this.status = status;
    this.preview = preview;
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
    }

    protected FeedTbl(Parcel in) {
        this.FeedID = (Long) in.readValue(Long.class.getClassLoader());
        this.feed = in.readString();
        this.image = in.readString();
        this.date = in.readString();
        this.email = in.readString();
        this.status = in.readString();
        this.preview = in.readString();
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
