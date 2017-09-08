package android.cybereye_community.com.sayafit.controller.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.service.GPSTracker;
import android.cybereye_community.com.sayafit.model.DirectionObject;
import android.cybereye_community.com.sayafit.model.LegsObject;
import android.cybereye_community.com.sayafit.model.PolylineObject;
import android.cybereye_community.com.sayafit.model.RouteObject;
import android.cybereye_community.com.sayafit.model.StepsObject;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                dismiss();

            }
        });

        setupGoogleApiClient();
        getLocation();


        if (centerLatLng.longitude == 0 && centerLatLng.latitude == 0){
            GPSTracker gps = new GPSTracker(getContext(), new GPSTracker.OnLocationEventListener() {
                @Override
                public void onChange(GPSTracker gpsTracker, Location location) {
                    if (location != null) {
                        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        locateMe();

                    }

                    gpsTracker.stopUsingGPS();
                }
            });

        }
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
                addressTextView.setText("Destination : \n"+knownName+"\n\n");
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

        if (currentLatLng!= null && centerLatLng != null){
            getDirection();
        }

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
        getDirection();
        }

    }


    private void setRouteDistanceAndDuration(String distance, String duration){

        addressTextView.append("Distance : "+distance+"\n");
        addressTextView.append("Duration : "+duration+"\n");
    }
    private List<LatLng> getDirectionPolylines(List<RouteObject> routes){
        List<LatLng> directionList = new ArrayList<LatLng>();
        for(RouteObject route : routes){
            List<LegsObject> legs = route.getLegs();
            for(LegsObject leg : legs){
                String routeDistance = leg.getDistance().getText();
                String routeDuration = leg.getDuration().getText();
                setRouteDistanceAndDuration(routeDistance, routeDuration);
                List<StepsObject> steps = leg.getSteps();
                for(StepsObject step : steps){
                    PolylineObject polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePoly(points);
                    for (LatLng direction : singlePolyline){
                        directionList.add(direction);
                    }
                }
            }
        }
        return directionList;
    }

    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
        options.addAll(positions);
        Polyline polyline = map.addPolyline(options);
    }
    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    private void getDirection(){
        //use Google Direction API to get the route between these Locations
        String directionApiPath = Helper.getUrl(String.valueOf(currentLatLng.latitude), String.valueOf(currentLatLng.longitude),
                String.valueOf(centerLatLng.latitude), String.valueOf(centerLatLng.longitude));

        Timber.e("directionApiPath : "+directionApiPath);


        AndroidNetworking.get(directionApiPath)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(DirectionObject.class,new ParsedRequestListener<DirectionObject>() {
                    @Override
                    public void onResponse(DirectionObject response) {
                        // do anything with response
                        Timber.e("RESPONSE : "+new Gson().toJson(response));
                        List<LatLng> mDirections = getDirectionPolylines(response.getRoutes());
                        drawRouteOnMap(mMap, mDirections);

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("ERROR : "+error.getMessage());

                    }
                });
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
