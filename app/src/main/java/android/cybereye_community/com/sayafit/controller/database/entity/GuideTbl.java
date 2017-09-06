package android.cybereye_community.com.sayafit.controller.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by edikurniawan on 9/6/17.
 */


@Entity(nameInDb = "GuideTbl")
public class GuideTbl  implements Parcelable {
    @Id
    @Property(nameInDb = "GuideID")
    public Long GuideID;

    @Property(nameInDb = "nm_guideline")
    public String nm_guideline;
    @Property(nameInDb = "nm_category")
    public String nm_category;
    @Property(nameInDb = "date")
    public String date;
    @Property(nameInDb = "ket")
    public String ket;
    @Generated(hash = 421561112)
    public GuideTbl(Long GuideID, String nm_guideline, String nm_category,
            String date, String ket) {
        this.GuideID = GuideID;
        this.nm_guideline = nm_guideline;
        this.nm_category = nm_category;
        this.date = date;
        this.ket = ket;
    }
    @Generated(hash = 534982197)
    public GuideTbl() {
    }
    public Long getGuideID() {
        return this.GuideID;
    }
    public void setGuideID(Long GuideID) {
        this.GuideID = GuideID;
    }
    public String getNm_guideline() {
        return this.nm_guideline;
    }
    public void setNm_guideline(String nm_guideline) {
        this.nm_guideline = nm_guideline;
    }
    public String getNm_category() {
        return this.nm_category;
    }
    public void setNm_category(String nm_category) {
        this.nm_category = nm_category;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getKet() {
        return this.ket;
    }
    public void setKet(String ket) {
        this.ket = ket;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.GuideID);
        dest.writeString(this.nm_guideline);
        dest.writeString(this.nm_category);
        dest.writeString(this.date);
        dest.writeString(this.ket);
    }

    protected GuideTbl(Parcel in) {
        this.GuideID = (Long) in.readValue(Long.class.getClassLoader());
        this.nm_guideline = in.readString();
        this.nm_category = in.readString();
        this.date = in.readString();
        this.ket = in.readString();
    }

    public static final Creator<GuideTbl> CREATOR = new Creator<GuideTbl>() {
        @Override
        public GuideTbl createFromParcel(Parcel source) {
            return new GuideTbl(source);
        }

        @Override
        public GuideTbl[] newArray(int size) {
            return new GuideTbl[size];
        }
    };
}
