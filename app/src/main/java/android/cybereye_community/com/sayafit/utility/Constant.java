package android.cybereye_community.com.sayafit.utility;

import org.jetbrains.annotations.Contract;

/**
 * Created by Rezky Aulia Pratama on 9/3/2017.
 */

public class Constant {
    // Static member class member that holds only one instance of the
    // SingletonExample class
    private static class SingletonHolder{
        public static Constant singletonInstance =
                new Constant();
    }
    // SingletonExample prevents any other class from instantiating
    private Constant() {
    }

    // Providing Global point of access
    @Contract(pure = true)
    public static Constant getInstance() {
        return SingletonHolder.singletonInstance;
    }


    public final int PERMISSION_REQUEST = 10000;

    //FAB
    public final String POST = "Post";
    public final String SHARE_LOCATION = "Share location";
    public final String ADD_SCHEDULE = "Add schedule";

    public final String BASE_URL = "http://sayafit.cybereye-community.com/";

}
