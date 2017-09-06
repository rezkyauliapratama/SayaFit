package android.cybereye_community.com.sayafit.controller.database;

import android.cybereye_community.com.sayafit.controller.database.entity.FeedTbl;
import android.cybereye_community.com.sayafit.controller.database.entity.FeedTblDao;

import java.util.List;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class ManageFeedTbl {
    private Facade facade;


    private FeedTblDao dao;

    ManageFeedTbl(Facade facade) {
        this.facade = facade;
        this.dao = facade.session.getFeedTblDao();
    }


    public long add(FeedTbl object) {
        return dao.insertOrReplace(object);
    }

    public void add(List<FeedTbl> object) {
        dao.insertInTx(object);
    }

    public List<FeedTbl> getAll() {
        return dao.queryBuilder().list();
    }

    public FeedTbl get(long id) {
        return dao.queryBuilder().where(FeedTblDao.Properties.FeedID.eq(id)).unique();
    }

    public void removeAll() {
        dao.deleteAll();
    }

    public void remove(FeedTbl object) {
        dao.delete(object);
    }

}

