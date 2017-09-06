package android.cybereye_community.com.sayafit.controller.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */
@Entity(nameInDb = "FeedTbl ",indexes = {
        @Index(value = "id_feed", unique = true)
})
public class FeedTbl {
    private static final long serialVersionUID=1L;
    @Property(nameInDb = "id_feed")
    public long id_feed;
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
@Generated(hash = 918876497)
public FeedTbl(long id_feed, String feed, String image, String date,
        String email, String status, String preview) {
    this.id_feed = id_feed;
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
public long getId_feed() {
    return this.id_feed;
}
public void setId_feed(long id_feed) {
    this.id_feed = id_feed;
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


}
