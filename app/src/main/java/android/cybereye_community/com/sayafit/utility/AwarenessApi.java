package android.cybereye_community.com.sayafit.utility;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/4/2017.
 */

public class AwarenessApi {


    private void initApi(Activity context){

        GoogleApiClient client = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .build();
        client.connect();


        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constant.getInstance().PERMISSION_REQUEST
            );
            return;
        }

        final String[] info = {""};
        Awareness.SnapshotApi.getLocation(client )
                .setResultCallback(new ResultCallback<LocationResult>() {
                    @Override
                    public void onResult(@NonNull LocationResult locationResult) {
                        if (!locationResult.getStatus().isSuccess()) {
                            Timber.e("Could not get location.");
                            return;
                        }
                        Location location = locationResult.getLocation();
//                        Timber.e( "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude());

                    }
                });


        Awareness.SnapshotApi.getPlaces(client)
                .setResultCallback(new ResultCallback<PlacesResult>() {
                    @Override
                    public void onResult(@NonNull PlacesResult placesResult) {
                        if (placesResult.getStatus().isSuccess()) {
                            List<PlaceLikelihood> placeLikelihood =
                                    placesResult.getPlaceLikelihoods();
                            // do stuff with likely places!


                        }
                    }
                });


        Awareness.SnapshotApi.getWeather(client)
                .setResultCallback(new ResultCallback<WeatherResult>() {
                    @Override
                    public void onResult(@NonNull WeatherResult weatherResult) {
                        if (weatherResult.getStatus().isSuccess()) {
                            Weather weather = weatherResult.getWeather();
                            // do stuff with Weather object!


                        }
                    }
                });

        Awareness.SnapshotApi.getDetectedActivity(client)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        if (detectedActivityResult.getStatus().isSuccess()) {
                            ActivityRecognitionResult activityRecognitionResult =
                                    detectedActivityResult.getActivityRecognitionResult();
                            // do stuff with ActivityRecognitionResult object!

                        }
                    }
                });

    }
    private String getWeather(int weather){
        switch (weather){
            case 0 :
                return "Unknown";
            case 1 :
                return "Clear";
            case 2 :
                return "Cloudy";
            case 3 :
                return "Foggy";
            case 4 :
                return "Hazy";
            case 5 :
                return "ICY";
            case 6 :
                return "Rainy";
            case 7 :
                return "Snowy";
            case 8 :
                return "Stormy";
            case 9 :
                return "Windy";

            default:
                return  "";
        }
    }

    private String getActivity(int activity){
        switch (activity){
            case 0 :
                return "in Vehicle";
            case 1 :
                return "On Bicycle";
            case 2 :
                return "On Foot";
            case 3 :
                return "Still";
            case 4 :
                return "Unknown";
            case 5 :
                return "TIlting";
            case 6 :
                return "6";
            case 7 :
                return "Walking";
            case 8 :
                return "Running";
            default:
                return  "";
        }
    }

    // Handle the callback on the Intent.
    public class MyFenceReceiver extends BroadcastReceiver {
        public static final String FENCE_RECEIVER_ACTION =
                "com.hitherejoe.aware.ui.fence.FenceReceiver.FENCE_RECEIVER_ACTION";
        @Override
        public void onReceive(Context context, Intent intent) {
            FenceState fenceState = FenceState.extract(intent);

            if (TextUtils.equals(fenceState.getFenceKey(), "headphoneFenceKey")) {
                switch(fenceState.getCurrentState()) {
                    case FenceState.TRUE:
                        Timber.e("Headphones are plugged in.");
                        break;
                    case FenceState.FALSE:
                        Timber.e("Headphones are NOT plugged in.");
                        break;
                    case FenceState.UNKNOWN:
                        Timber.e( "The headphone fence is in an unknown state.");
                        break;
                }
            }
        }
    }
}
