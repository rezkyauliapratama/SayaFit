package android.cybereye_community.com.sayafit.controller.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.cybereye_community.com.sayafit.EventBus;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.adapter.FeedRecyclerviewAdapter;
import android.cybereye_community.com.sayafit.controller.adapter.PlaceRecyclerviewAdapter;
import android.cybereye_community.com.sayafit.controller.database.entity.FeedTbl;
import android.cybereye_community.com.sayafit.databinding.FragmentHomeBinding;
import android.cybereye_community.com.sayafit.databinding.FragmentShareLocationBinding;
import android.cybereye_community.com.sayafit.model.place;
import android.cybereye_community.com.sayafit.view.LayoutEmptyInflate;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/5/2017.
 */

public class ShareLocationFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    FragmentShareLocationBinding binding;

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;
    private GoogleApiClient mGoogleApiClient;

    private boolean isLandscape = false;

    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;

    List<place> mItems;
    PlaceRecyclerviewAdapter adapter;
    LinearLayoutManager mLayoutManager;


    public static ShareLocationFragment newInstance() {
        ShareLocationFragment fragment = new ShareLocationFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mItems = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_share_location,container,false);

        return binding.getRoot();
    }


    private void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void getPlaces() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Awareness.SnapshotApi.getPlaces(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<PlacesResult>() {
                        @Override
                        public void onResult(@NonNull PlacesResult placesResult) {
                            if (placesResult.getStatus().isSuccess()) {
                                mItems.clear();

                                List<PlaceLikelihood> placeLikelihood =
                                        placesResult.getPlaceLikelihoods();
                                if (placeLikelihood != null && !placeLikelihood.isEmpty()) {
                                    for (PlaceLikelihood likelihood : placeLikelihood) {

                                        for (int i : likelihood.getPlace().getPlaceTypes()){
                                            if (i == Place.TYPE_HEALTH || i == Place.TYPE_GYM || i == Place.TYPE_PHYSIOTHERAPIST
                                                    || i == Place.TYPE_PARK || i == Place.TYPE_CAFE || i == Place.TYPE_BUS_STATION
                                                    || i == Place.TYPE_BICYCLE_STORE || i == Place.TYPE_FOOD || i == Place.TYPE_TRAIN_STATION
                                                    || i == Place.TYPE_TAXI_STAND ){
                                                addPlace(likelihood,i);
                                                break;
                                            }
                                        }

                                    }


                                }

                                binding.recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        adapter = new PlaceRecyclerviewAdapter(mItems,getContext());
        binding.recyclerView.setAdapter(adapter);

        setupGoogleApiClient();
        getPlaces();

        EventBus.instanceOf().getObservable().subscribe(new Observer<LatLng>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LatLng latLng) {
                showDialogFragment(latLng);
            }


        });
    }

    private void showDialogFragment(LatLng currentLatLng){
        MapDialogFragment mapDialogFragment = MapDialogFragment.newInstance(currentLatLng.latitude, currentLatLng.longitude);
        mapDialogFragment.setTargetFragment(this,mapDialogFragment.TARGET);
        mapDialogFragment.show(getFragmentManager().beginTransaction(),mapDialogFragment.DiALOG);

    }
    private void addPlace(PlaceLikelihood likelihood, int type) {
        mItems.add(new place(likelihood.getPlace().getName().toString(),
                type,
                likelihood.getPlace().getRating(),
                        (likelihood.getPlace().getAttributions() == null ? "":likelihood.getPlace().getAttributions().toString()),
                likelihood.getPlace().getPhoneNumber().toString(),
                likelihood.getPlace().getLatLng()));
    }



}
