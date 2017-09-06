package android.cybereye_community.com.sayafit;

import android.app.Application;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.database.entity.DaoMaster;
import android.cybereye_community.com.sayafit.controller.database.entity.DaoSession;
import android.cybereye_community.com.sayafit.utility.PreferencesManager;

import com.app.infideap.stylishwidget.view.Stylish;

import org.greenrobot.greendao.database.Database;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 8/31/2017.
 */

public class BaseApplication extends Application {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize lib
        EventBus.instanceOf();
        Timber.plant(new Timber.DebugTree());
        PreferencesManager.init(this);

        String fontFolder = "fonts/Exo_2/Exo2-";
        Stylish.getInstance().set(
                fontFolder.concat("Regular.ttf"),
                fontFolder.concat("Medium.ttf"),
                fontFolder.concat("RegularItalic.ttf")
        );

        Stylish.getInstance().setFontScale(
                PreferencesManager.getInstance().getFontSize()
        );

        Timber.e(Stylish.getInstance().getFontScale()+"");

        //init database
        String databaseName = "Sayafit";
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, databaseName);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        Facade.init(daoSession);


    }
}
