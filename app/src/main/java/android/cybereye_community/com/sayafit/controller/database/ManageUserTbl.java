package android.cybereye_community.com.sayafit.controller.database;

import android.cybereye_community.com.sayafit.controller.database.entity.UserTbl;
import android.cybereye_community.com.sayafit.controller.database.entity.UserTblDao;

import java.util.List;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class ManageUserTbl {
    private Facade facade;


    private UserTblDao dao;

    ManageUserTbl(Facade facade) {
        this.facade = facade;
        this.dao = facade.session.getUserTblDao();
    }


    public long add(UserTbl object) {
        return dao.insertOrReplace(object);
    }

    public void add(List<UserTbl> object) {
        dao.insertInTx(object);
    }

    public List<UserTbl> getAll() {
        return dao.queryBuilder().list();
    }

    public UserTbl get(String email) {
        return dao.queryBuilder().where(UserTblDao.Properties.Email.eq(email)).unique();
    }

    public void removeAll() {
        dao.deleteAll();
    }

    public void remove(UserTbl object) {
        dao.delete(object);
    }

    public UserTbl get() {
        List<UserTbl> userTbls =
                dao.queryBuilder().limit(1).list();

        return userTbls.size() == 0 ? null : userTbls.get(0);
    }
}
