package android.cybereye_community.com.sayafit.controller.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.utility.Helper;
import android.cybereye_community.com.sayafit.utility.Utils;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 6/12/2017.
 */

public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback {
    public final static int TARGET = 1;
    public final static String DiALOG= "DIALOG";
    private final static String ARG_PARAM1="ARG_PARAM1";
    private final static String ARG_PARAM2="ARG_PARAM2";

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;

    private GoogleApiClient mGoogleApiClient;

    private GoogleMap mMap;
    private String knownName;

    TextView addressTextView;
    TextView button_cancel;

    private LatLng currentLatLng;
    private boolean needRefresh = true;
    private LatLng centerLatLng;
    private CircleOptions circleOptions;
    private boolean blockMap = false;
    private View rootView;

    SupportMapFragment supportMapFragment;


    public static MapDialogFragment newInstance(double latitude,double longitude){
        Timber.e("SET LATITUDE : "+latitude+" , LONGITUDE : "+longitude);
        MapDialogFragment dialogFragment = new MapDialogFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, latitude);
        args.putDouble(ARG_PARAM2, longitude);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            double latitude = getArguments().getDouble(ARG_PARAM1);
            double longitude= getArguments().getDouble(ARG_PARAM2);
            centerLatLng = new LatLng(latitude,longitude);
            Timber.e("GET LATITUDE : "+latitude+" , LONGITUDE : "+longitude);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_map,container,false);
        getDialog().setTitle("Custom Map");
        addressTextView = (TextView) rootView.findViewById(R.id.textView_address);
        button_cancel = (TextView) rootView.findViewById((R.id.button_save));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Build.VERSION.SDK_INT < 21) {
            supportMapFragment = (SupportMapFragment) getActivity()
                    .getSupportFragmentManager().findFragmentById(R.id.map);
        } else {
            supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.map);
        }
        supportMapFragment.getMapAsync(this);


        addressTextView.setText(".......\n.......\n.......");


        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationDialogListener listener = (LocationDialogListener) getTargetFragment();
                listener.onCancel();

            }
        });

        setupGoogleApiClient();
        getLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Timber.e("Map Ready");

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.map_style));


        } catch (Resources.NotFoundException e) {
            Timber.e("ERR RESOURCES : ".concat(e.getMessage()));
        }
        mMap = googleMap;

        if (centerLatLng != null){
            Timber.e("Center latlng not null");
            try {
                knownName = Utils.getInstance().setLocation(getContext(),centerLatLng);
                addressTextView.setText(knownName);
            } catch (IOException e) {
                Timber.e(e.getMessage());
            }
            destination();
        }


    }
    private void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<LocationResult>() {
                        @Override
                        public void onResult(@NonNull LocationResult locationResult) {

                            if (locationResult.getStatus().isSuccess()) {
                                Location location = locationResult.getLocation();
                                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                Timber.e("onREsult : "+new Gson().toJson(currentLatLng));
                                locateMe();

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
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != supportMapFragment)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(supportMapFragment)
                    .commit();
    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vectorDrawable = getContext().getDrawable(id);
        }else{
            vectorDrawable = ContextCompat.getDrawable(getContext(), id);
        }

        int h = ((int) vectorDrawable.getIntrinsicHeight());
        int w = ((int) vectorDrawable.getIntrinsicWidth());
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    private void destination() {
        if (getActivity() == null)
            return;

        if (mMap == null)
            return;


        mMap.addMarker(new MarkerOptions()
                .position(centerLatLng)
                .icon(getBitmapDescriptor(R.drawable.ic_map_marker_1))
                .title("Your Destination"));


    }

    private void locateMe(){
        if (getActivity() == null)
            return;

        if (mMap == null)
            return;


        mMap.addMarker(new MarkerOptions()
                .position(currentLatLng)
                .icon(getBitmapDescriptor(R.drawable.ic_map_marker))
                .title("Your Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

        if (currentLatLng!= null && centerLatLng != null){
        }

    }



    private void getDirection(){
        //use Google Direction API to get the route between these Locations
        String directionApiPath = Helper.getUrl(String.valueOf(currentLatLng.latitude), String.valueOf(currentLatLng.longitude),
                String.valueOf(centerLatLng.latitude), String.valueOf(centerLatLng.longitude));

        String url = getUrl(origin
    }
    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }



    public interface LocationDialogListener {

        void onGetLocation(LatLng latLng, String knownAddress);
        void onCancel();
    }
}
