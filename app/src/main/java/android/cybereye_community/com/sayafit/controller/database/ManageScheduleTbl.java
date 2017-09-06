package android.cybereye_community.com.sayafit.controller.database;

import android.cybereye_community.com.sayafit.controller.database.entity.ScheduleTbl;
import android.cybereye_community.com.sayafit.controller.database.entity.ScheduleTblDao;

import java.util.List;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class ManageScheduleTbl {

    private Facade facade;
    private ScheduleTblDao dao;

    ManageScheduleTbl(Facade facade) {
        this.facade = facade;
        this.dao = facade.session.getScheduleTblDao();
    }


    public long add(ScheduleTbl object) {
        return dao.insertOrReplace(object);
    }

    public void add(List<ScheduleTbl> object) {
        dao.insertInTx(object);
    }

    public List<ScheduleTbl> getAll() {
        return dao.queryBuilder().list();
    }

    public ScheduleTbl get(long id) {
        return dao.queryBuilder().where(ScheduleTblDao.Properties.IdSchedule.eq(id)).unique();
    }

    public void removeAll() {
        dao.deleteAll();
    }

    public void remove(ScheduleTbl object) {
        dao.delete(object);
    }

}

