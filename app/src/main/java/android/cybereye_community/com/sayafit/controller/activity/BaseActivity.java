package android.cybereye_community.com.sayafit.controller.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.utility.Constant;
import android.cybereye_community.com.sayafit.utility.PreferencesManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mutya Nayavashti on 12/04/2017.
 */

public class BaseActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    private static final String TAG = BaseActivity.class.getSimpleName();
    private File photo;
    //    private float IMAGE_SIZE = 720f;
    protected PreferencesManager pref;

    public static String CURRENT_ACTIVITY = null;

    private String currentLocale;

    private long lastLockDialogShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferencesManager.getInstance();



    }

    @Override
    public void onStart() {
        super.onStart();
        checkAppPermission();
    }

    public void checkAppPermission() {
        final List<String> permissions = new ArrayList<>();
        boolean showMessage = location(permissions)
                || camera(permissions)
                ||writeExternalStorage(permissions)
                ||writeSetting(permissions);




        if (permissions.size() > 0) {
            final String strings[] = new String[permissions.size()];

            if (showMessage) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage(R.string.permissionrequestmessage)
                        .setPositiveButton(R.string.gotit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        permissions.toArray(strings),
                                        Constant.getInstance().PERMISSION_REQUEST);
                            }
                        });
                builder.create().show();
            } else
                ActivityCompat.requestPermissions(this,
                        permissions.toArray(strings),
                        Constant.getInstance().PERMISSION_REQUEST);
        }

    }
    private boolean location(List<String> permissions) {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                return true;

            }

        }
        return false;
    }

    private boolean writeExternalStorage(List<String> permissions) {
        if (ActivityCompat.checkSelfPermission(
                getApplication(), Manifest
                        .permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return true;

            }

        }
        return false;
    }


    private boolean camera(List<String> permissions) {
        if (ActivityCompat.checkSelfPermission(
                getApplication(), Manifest
                        .permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            permissions.add(Manifest.permission.CAMERA);

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                return true;

            }

        }
        return false;
    }

    private boolean writeSetting(List<String> permissions) {
        if (ActivityCompat.checkSelfPermission(
                getApplication(), Manifest
                        .permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            permissions.add(Manifest.permission.WRITE_SETTINGS);

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_SETTINGS)) {
                return true;

            }

        }
        return false;
    }
    public void addFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(id, fragment).commit();

    }

    public void displayFragment(int id, Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(id, fragment)
                    .commitAllowingStateLoss();
        } catch (Exception e) {

        }

    }

    public void removeFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slid_left, R.anim.do_nothing);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.do_nothing, R.anim.slid_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.do_nothing, R.anim.slid_right);
    }


    public void captureImage(View view) {

    }



}
