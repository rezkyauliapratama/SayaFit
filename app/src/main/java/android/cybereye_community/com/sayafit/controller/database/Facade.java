package android.cybereye_community.com.sayafit.controller.database;


import android.cybereye_community.com.sayafit.controller.database.entity.DaoSession;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class Facade {

    private static Facade instance;
    final DaoSession session;

    public static void init(DaoSession daoSession) {
        instance = new Facade(daoSession);
    }

    public static Facade getInstance() {
        return instance;
    }

    private ManageUserTbl manageUserTbl;
    private ManageFeedTbl manageFeedTbl;
    private ManageScheduleTbl manageScheduleTbl;


    Facade(DaoSession daoSession) {
        this.session = daoSession;
        manageUserTbl = new ManageUserTbl(this);
        manageFeedTbl = new ManageFeedTbl(this);
        manageScheduleTbl = new ManageScheduleTbl(this);
    }

    public ManageUserTbl getManageUserTbl() {
        return manageUserTbl;
    }

    public ManageFeedTbl getManageFeedTbl() {
        return manageFeedTbl;
    }
    public ManageScheduleTbl getManageScheduleTbl() {
        return manageScheduleTbl;
    }

}

