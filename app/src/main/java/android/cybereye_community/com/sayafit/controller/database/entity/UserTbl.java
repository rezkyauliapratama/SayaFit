package android.cybereye_community.com.sayafit.controller.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

@Entity(nameInDb = "UserTbl",indexes = {
        @Index(value = "email", unique = true)
})
public class UserTbl {
    private static final long serialVersionUID=1L;
    @Id
    @Property(nameInDb = "email")
    public String email;
    @Property(nameInDb = "nama")
    public String nama;
    @Property(nameInDb = "token")
    public String token;
    @Property(nameInDb = "gender")
    public String gender;
    @Property(nameInDb = "photo")
    public String photo;
    @Property(nameInDb = "city")
    public String city;
    @Property(nameInDb = "phone")
    public String phone;
    @Property(nameInDb = "date")
    public String date;
@Generated(hash = 882418572)
public UserTbl(String email, String nama, String token, String gender,
        String photo, String city, String phone, String date) {
    this.email = email;
    this.nama = nama;
    this.token = token;
    this.gender = gender;
    this.photo = photo;
    this.city = city;
    this.phone = phone;
    this.date = date;
}
@Generated(hash = 585658511)
public UserTbl() {
}
public String getEmail() {
    return this.email;
}
public void setEmail(String email) {
    this.email = email;
}
public String getNama() {
    return this.nama;
}
public void setNama(String nama) {
    this.nama = nama;
}
public String getToken() {
    return this.token;
}
public void setToken(String token) {
    this.token = token;
}
public String getGender() {
    return this.gender;
}
public void setGender(String gender) {
    this.gender = gender;
}
public String getPhoto() {
    return this.photo;
}
public void setPhoto(String photo) {
    this.photo = photo;
}
public String getCity() {
    return this.city;
}
public void setCity(String city) {
    this.city = city;
}
public String getPhone() {
    return this.phone;
}
public void setPhone(String phone) {
    this.phone = phone;
}
public String getDate() {
    return this.date;
}
public void setDate(String date) {
    this.date = date;
}


}
