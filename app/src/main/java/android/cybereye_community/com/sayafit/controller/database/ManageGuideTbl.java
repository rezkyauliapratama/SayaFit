package android.cybereye_community.com.sayafit.controller.database;

import android.cybereye_community.com.sayafit.controller.database.entity.GuideTbl;
import android.cybereye_community.com.sayafit.controller.database.entity.GuideTblDao;

import java.util.List;

/**
 * Created by Rezky Aulia Pratama on 9/7/2017.
 */

public class ManageGuideTbl {
    private Facade facade;


    private GuideTblDao dao;

    ManageGuideTbl(Facade facade) {
        this.facade = facade;
        this.dao = facade.session.getGuideTblDao();
    }


    public long add(GuideTbl object) {
        return dao.insertOrReplace(object);
    }

    public void add(List<GuideTbl> object) {
        dao.insertInTx(object);
    }

    public List<GuideTbl> getAll() {
        return dao.queryBuilder().list();
    }

    public GuideTbl get(long id) {
        return dao.queryBuilder().where(GuideTblDao.Properties.GuideID.eq(id)).unique();
    }

    public void removeAll() {
        dao.deleteAll();
    }

    public void remove(GuideTbl object) {
        dao.delete(object);
    }

}
