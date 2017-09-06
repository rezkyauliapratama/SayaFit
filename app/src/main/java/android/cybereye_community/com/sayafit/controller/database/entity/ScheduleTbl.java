package android.cybereye_community.com.sayafit.controller.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

@Entity(nameInDb = "ScheduleTbl",indexes = {
        @Index(value = "IdSchedule", unique = true)
})
public class ScheduleTbl {
    private static final long serialVersionUID=1L;
    @Property(nameInDb = "IdSchedule")
    public Long IdSchedule;
    @Property(nameInDb = "email")
    public String email;
    @Property(nameInDb = "date")
    public String date;
    @Property(nameInDb = "start")
    public String start;
    @Property(nameInDb = "end")
    public String end;
    @Property(nameInDb = "activity")
    public String activity;
@Generated(hash = 703300063)
public ScheduleTbl(Long IdSchedule, String email, String date, String start,
        String end, String activity) {
    this.IdSchedule = IdSchedule;
    this.email = email;
    this.date = date;
    this.start = start;
    this.end = end;
    this.activity = activity;
}
@Generated(hash = 1596512676)
public ScheduleTbl() {
}
public Long getIdSchedule() {
    return this.IdSchedule;
}
public void setIdSchedule(Long IdSchedule) {
    this.IdSchedule = IdSchedule;
}
public String getEmail() {
    return this.email;
}
public void setEmail(String email) {
    this.email = email;
}
public String getDate() {
    return this.date;
}
public void setDate(String date) {
    this.date = date;
}
public String getStart() {
    return this.start;
}
public void setStart(String start) {
    this.start = start;
}
public String getEnd() {
    return this.end;
}
public void setEnd(String end) {
    this.end = end;
}
public String getActivity() {
    return this.activity;
}
public void setActivity(String activity) {
    this.activity = activity;
}
}