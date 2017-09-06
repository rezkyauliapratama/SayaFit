package android.cybereye_community.com.sayafit.controller.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.databinding.DialogPostFeedBinding;
import android.cybereye_community.com.sayafit.utility.Utils;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class PostActivity extends BaseActivity implements OnMapReadyCallback {
    DialogPostFeedBinding binding;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;


    private LatLng currentLatLng;
    private boolean needRefresh = true;
    private LatLng centerLatLng;
    private CircleOptions circleOptions;
    SupportMapFragment supportMapFragment;

    private GoogleMap mMap;
    private String knownName;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_post_feed);

        double latitude = 0;
        double longitude= 0;
        centerLatLng = new LatLng(latitude,longitude);

        initMap();
        setupGoogleApiClient();
        getLocation();
    }

    private void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();

                } else {
                    Timber.e("ERROR UPS");
                }
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<LocationResult>() {
                        @Override
                        public void onResult(@NonNull LocationResult locationResult) {

                            if (locationResult.getStatus().isSuccess()) {
                                Location location = locationResult.getLocation();
                                centerLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                Timber.e("onREsult : "+new Gson().toJson(centerLatLng));
                                relocate();

                            } else {
                                /*Snackbar.make(mLayoutLocation,
                                        getString(R.string.error_general),
                                        Snackbar.LENGTH_LONG).show();*/
                                Timber.e("ERROR : MAP google client null");
                            }
                        }
                    });
        }
    }
    private void initMap(){
        if (Build.VERSION.SDK_INT < 21) {
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        } else {
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
        }
        supportMapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));


        } catch (Resources.NotFoundException e) {
            Timber.e("ERR RESOURCES : ".concat(e.getMessage()));
        }
        mMap = googleMap;

        relocate();

    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            vectorDrawable = getDrawable(id);
        }else{
            vectorDrawable = ContextCompat.getDrawable(this, id);
        }

        int h = ((int) vectorDrawable.getIntrinsicHeight());
        int w = ((int) vectorDrawable.getIntrinsicWidth());
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private void relocate() {

        if (mMap == null)
            return;

        mMap.clear();

        mMap.addMarker(new MarkerOptions()
                .position(centerLatLng)
                .icon(getBitmapDescriptor(R.drawable.ic_map_marker))
                .title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 16));
        circleOptions = new CircleOptions().center(centerLatLng)
                .fillColor(adjustAlpha(ContextCompat.getColor(this, R.color.colorAccentTransparent), 0.2f))
                .strokeColor(ContextCompat.getColor(this, R.color.colorAccentTransparent))
                .strokeWidth(10)
                .radius(500);
        mMap.addCircle(circleOptions);

        if (centerLatLng != null){
            try {
                knownName = Utils.getInstance().setLocation(this,centerLatLng);
                binding.textViewAddress.setText(knownName);

            } catch (IOException e) {
                Timber.e(e.getMessage());
            }
        }

    }
}
