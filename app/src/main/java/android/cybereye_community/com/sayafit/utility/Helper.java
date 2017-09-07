package android.cybereye_community.com.sayafit.utility;

import android.content.Context;
import android.cybereye_community.com.sayafit.BuildConfig;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Rezky Aulia Pratama on 9/7/2017.
 */

public class Helper {
    private static final String DIRECTION_API = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    public static final String API_KEY = "AIzaSyC5RVXSPr3QCfCeoE0y10g93pCT7F1Oe1A";
    public static final int MY_SOCKET_TIMEOUT_MS = 5000;
    public static String getUrl(String originLat, String originLon, String destinationLat, String destinationLon){
        return Helper.DIRECTION_API + originLat+","+originLon+"&destination="+destinationLat+","+destinationLon+"&key="+API_KEY;
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
