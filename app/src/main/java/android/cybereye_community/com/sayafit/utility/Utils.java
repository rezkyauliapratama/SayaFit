package android.cybereye_community.com.sayafit.utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.cybereye_community.com.sayafit.R;
import android.location.Address;
import android.location.Geocoder;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.securepreferences.SecurePreferences;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Shiburagi on 09/07/2016.
 */
public class Utils {
    // Static member class member that holds only one instance of the
    // SingletonExample class
    private static class SingletonHolder{
        public static Utils singletonInstance =
                new Utils();
    }
    // SingletonExample prevents any other class from instantiating
    private Utils() {
    }

    // Providing Global point of access
    @Contract(pure = true)
    public static Utils getInstance() {
        return SingletonHolder.singletonInstance;
    }

    public TimeUtility time(){
        return new TimeUtility();
    }

    public  double toMB(long l) {
        return (((double) l) / (double) 1024.0f) / 1024.0f;
    }

    public AppInfo request(String packageName, Context context)
            throws PackageManager.NameNotFoundException {
        PackageManager manager = context.getPackageManager();
        PackageInfo packageInfo = manager.getPackageInfo(packageName, 0);
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;

        return extractInfo(manager, packageInfo, applicationInfo);
    }

    private AppInfo extractInfo(PackageManager pm, PackageInfo pi, ApplicationInfo ai) {
        long delta_rx = TrafficStats.getUidRxBytes(ai.uid);
        long delta_tx = TrafficStats.getUidTxBytes(ai.uid);

        final AppInfo appInfo = new AppInfo();
        appInfo.packageName = ai.packageName;
        appInfo.appInstall = time().getDateTimeString(pi.firstInstallTime);
        appInfo.appUpdate = time().getDateTimeString(pi.lastUpdateTime);
        appInfo.updatedDate = time().getDateTimeString();
        appInfo.version = pi.versionName;
        appInfo.uploadSize = toMB(delta_tx);
        appInfo.downloadSize = toMB(delta_rx);

        return appInfo;
    }

    public  class AppInfo {
        public int id;
        public String extra;
        @SerializedName("PackageName")
        public String packageName;
        @SerializedName("AppUpdatedDate")
        public String appUpdate;
        @SerializedName("AppInstalledDate")
        public String appInstall;
        @SerializedName("LastUpdatedDate")
        public String updatedDate;
        public String createdDate;
        @SerializedName("InstallFlag")
        public int install;
        @SerializedName("Version")
        public String version;
        public int status;
        @SerializedName("DataSize")
        public double dataSize;
        @SerializedName("CacheSize")
        public double cacheSize;
        public double uploadSize;
        public double downloadSize;
    }


    public float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public void setStatusBarColor(Activity context){
        Window window = context.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryTransparent));
        }
    }

    public String setLocation(Context context,LatLng latLng) throws IOException {


        Timber.e("KOORDINAT : "+new Gson().toJson(latLng));

        Geocoder geocoder;
        List<Address> addresses;
        try {
            geocoder = new Geocoder(context, Locale.getDefault());


            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Timber.e("ADDRESS : "+new Gson().toJson(addresses));
            Timber.e("ADDRESS Lenght : "+addresses.size());
            if (addresses.size() == 0)
                return "";

            Address address = addresses.get(0);
            Timber.e("address.getMaxAddressLineIndex() : "+address.getMaxAddressLineIndex());

            String addressString = "";
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressString += "\n" + address.getAddressLine(i);
            }
            addressString = addressString.substring(1);
            return addressString;
        } catch (Exception e) {
            Timber.e("ERROR MAP : "+e.getMessage());
            return "";
        }
    }

    public static File getFolder(String questionFileFolder) {
        File folder = Environment.getExternalStorageDirectory();
        String[] folderNames = questionFileFolder.split("/");
        for (String folderName : folderNames) {
            folder = new File(folder, folderName);
            if (!folder.exists())
                folder.mkdirs();
        }

        return folder;
    }


}
