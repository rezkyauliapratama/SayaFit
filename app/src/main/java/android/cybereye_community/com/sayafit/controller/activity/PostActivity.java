package android.cybereye_community.com.sayafit.controller.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.database.entity.UserTbl;
import android.cybereye_community.com.sayafit.controller.service.GPSTracker;
import android.cybereye_community.com.sayafit.databinding.DialogPostFeedBinding;
import android.cybereye_community.com.sayafit.handler.ApiClient;
import android.cybereye_community.com.sayafit.model.request.FeedPost;
import android.cybereye_community.com.sayafit.utility.Constant;
import android.cybereye_community.com.sayafit.utility.Utils;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
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
import com.infideap.atomic.Atom;
import com.infideap.atomic.FutureCallback;
import com.infideap.atomic.ProgressCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

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

    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;
    File newPath, mFile;
    private File newFile;
    DetectedActivity mostProbableActivity;

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
        getUserActivity();

        initCamera();
        initPost();


        if (centerLatLng.longitude == 0 && centerLatLng.latitude == 0){
            GPSTracker gps = new GPSTracker(this, new GPSTracker.OnLocationEventListener() {
                @Override
                public void onChange(GPSTracker gpsTracker, Location location) {
                    if (location != null) {
                        centerLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        relocate();

                    }

                    gpsTracker.stopUsingGPS();
                }
            });

        }
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

    private void getUserActivity() {
        Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        if (detectedActivityResult.getStatus().isSuccess()) {
                            ActivityRecognitionResult activityRecognitionResult =
                                    detectedActivityResult.getActivityRecognitionResult();

                            long detectedActivity =
                                    activityRecognitionResult.getTime();
                            String dateString = DateFormat.format("dd/MM/yyyy hh:mm:ss",
                                    new Date(detectedActivity)).toString();

                            Timber.e("DATE : "+dateString);
                            mostProbableActivity =
                                    activityRecognitionResult.getMostProbableActivity();

                            getActivityString(mostProbableActivity.getType());

                        }
                    }
                });
    }

    private void initCamera(){
        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Image");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, TAKE_PICTURE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    File folder = Utils.getFolder(Constant.getInstance().PROFILE_FOLDER);/*new File(getFilesDir(), Constant.getInstance().PROFILE_FOLDER);*/
                    String filename = Utils.getInstance().time().getDateForFilename() + ".jpg";

                   /* for (String folderName : folderNames) {
                        folder = new File(getFilesDir(), folderName);
                        if (!folder.exists())
                            folder.mkdirs();
                    }
                    if(!folder.exists()){
                        Timber.e("folder tidak eksis");
                        folder.mkdir();
                    }*/
                    newFile = new File(folder, filename);
//
//                    Timber.d("onActivityResult : mFile :"+mFile.getAbsolutePath());
//                    Timber.d("onActivityResult : newFile :"+newFile.getAbsolutePath());
//                    try {
//                        FileUtils.moveFile(mFile, newFile);
//                        ContentResolver cr = getContext().getContentResolver();
                    Bitmap bitmap;
                    try {
//                            bitmap = BitmapFactory.decodeFile(newFile.getAbsolutePath());
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                        Timber.e("Location photo : " + newFile.getAbsolutePath());
                        Timber.e("Bitmap ori SIZE : "+bitmap.getWidth() + " | "+bitmap.getHeight());

                        float scale = (float) 1240/ bitmap.getHeight();
                        int newWidth = (int) Math.round(bitmap.getWidth() * scale);
                        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, 1240, true);
                        Timber.e("Bitmap RESIZE : "+bitmap.getWidth() + " | "+bitmap.getHeight());


                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(newFile.getAbsolutePath());
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out); // bmp is your Bitmap instance
                            // PNG is a lossless format, the compression factor (100) is ignored
                        } catch (Exception e) {
                            Timber.e("CAMERA ERR 1 : "+e.getMessage());
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                Timber.e("CAMERA ERR 2: "+e.getMessage());

                            }
                        }
                        binding.layoutButton.ivFeed.setVisibility(View.VISIBLE);
                        binding.layoutButton.ivFeed.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        Log.e("Camera", e.toString());
                    }


                }

        }
    }
    private void getActivityString(int activity) {
        binding.containerStatus.setVisibility(View.VISIBLE);

        switch (activity) {
            case DetectedActivity.ON_BICYCLE:
                binding.ivActivity.setImageResource(R.drawable.ic_run);
                binding.tvStatus.setText("This place is suitable for running activity");
            case DetectedActivity.RUNNING:
                binding.ivActivity.setImageResource(R.drawable.ic_bicycle);
                binding.tvStatus.setText("This place is suitable for bicycle activity");
            default:
                binding.tvStatus.setText("");
                binding.containerStatus.setVisibility(View.GONE);
        }
    }

    private void initPost(){
        binding.layoutButton.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageName = "";
                if (newFile != null){
                    imageName = newFile.getName();
                }
                UserTbl user = Facade.getInstance().getManageUserTbl().get();
                FeedPost feed = new FeedPost();
                feed.setEmail(user.getEmail());
                feed.setFeed(binding.layoutButton.etFeed.getText().toString());
                feed.setNama(user.getNama());
                feed.setPreview("1");
                feed.setImage(imageName);
                feed.setLatitude(centerLatLng.latitude+"");
                feed.setLongitude(centerLatLng.longitude+"");
                feed.setDescription("");
                feed.setAwereness_activity(mostProbableActivity.getType()+"");

                ApiClient.getInstance().feed().post(feed)
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(final String response) {
                                Timber.e("RESPONSE :"+response);
                                Toast.makeText(PostActivity.this,"Your post will we analyse",Toast.LENGTH_LONG).show();
                                if (newFile != null){

                                    Atom.with(PostActivity.this)
                                            .load("http://sayafit.cybereye-community.com/upload/")
                                            .setMultipartFile("image", newFile)
                                            //Optional: Upload Progress
                                            .uploadProgress(new ProgressCallback() {
                                                @Override
                                                public void onProgress(long uploaded, long total) {
                                                    Timber.e("prof : "+(uploaded/total));

                                                }
                                            })
                                            .asString()
                                            .setCallback(new FutureCallback<String>() {
                                                @Override
                                                public void onCompleted(Exception e, String result) {
                                                    finish();

                                                    if (e != null){
                                                        Timber.e("ERROR UP : "+e.getMessage());
                                                        finish();
                                                        return;
                                                    }
                                                    Timber.e("result upload : "+result);
                                                    finish();
                                                }

                                            });
                                }

                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });

            }
        });
    }

    private void uploadFile(){
        AndroidNetworking.upload("http://sayafit.cybereye-community.com/upload/")
                .addMultipartFile("image",newFile)
                .setTag("uploadTest")
                .setPriority(Priority.MEDIUM)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
//                                                    Timber.e("IMAGE prog: "+(double)(bytesUploaded/totalBytes));
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Timber.e("Image Resp : "+response);

                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("Image Err : "+new Gson().toJson(error));
                        finish();
                    }
                });
    }
}
