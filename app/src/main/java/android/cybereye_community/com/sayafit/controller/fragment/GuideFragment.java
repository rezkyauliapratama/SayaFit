package android.cybereye_community.com.sayafit.controller.fragment;


import android.cybereye_community.com.sayafit.controller.adapter.FeedRecyclerviewAdapter;
import android.cybereye_community.com.sayafit.controller.adapter.GuideRecyclerviewAdapter;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.database.entity.GuideTbl;
import android.cybereye_community.com.sayafit.databinding.FragmentGuideBinding;
import android.cybereye_community.com.sayafit.databinding.ItemListGuideBinding;
import android.cybereye_community.com.sayafit.handler.ApiClient;
import android.cybereye_community.com.sayafit.handler.api.FeedApi;
import android.cybereye_community.com.sayafit.handler.api.GuideApi;
import android.cybereye_community.com.sayafit.handler.api.UserApi;
import android.cybereye_community.com.sayafit.handler.listener.OnLoadMoreListener;
import android.cybereye_community.com.sayafit.model.request.FeedPost;
import android.cybereye_community.com.sayafit.view.LayoutEmptyInflate;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.cybereye_community.com.sayafit.R;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String EXTRA1 = "EXTRA1";
    public static final String EXTRA2 = "EXTRA2";
    public static final String EXTRA3 = "EXTRA3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;
    List<GuideTbl> guides;
    GuideRecyclerviewAdapter adapter;
    LinearLayoutManager mLayoutManager;
    int mPage;
    FragmentGuideBinding binding;

    public static GuideFragment newInstance()  {
        // Required empty public constructor
        GuideFragment fragment = new GuideFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        guides = new ArrayList<>();

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_guide,container,false);

        if(savedInstanceState != null){
            Timber.e("SAVEDINSTACESTATE");
            guides = savedInstanceState.getParcelableArrayList(EXTRA1);
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            mPage = savedInstanceState.getInt(EXTRA2);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPage =1;
        mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        adapter = new GuideRecyclerviewAdapter(getContext(),guides);
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Timber.e("NEXT PAGE");
                mPage++;
                loadData();
            }
        });

        loadData();

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Timber.e("SWIPEREFRESHLAYOUT");
                mPage = 1;
                loadData();
            }
        });
    }

    private void loadData(){
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(true);
        }

        ApiClient.getInstance().guide().get(mPage)
                .getAsObject(GuideApi.GetResponse.class, new ParsedRequestListener<GuideApi.GetResponse>() {
                    @Override
                    public void onResponse(GuideApi.GetResponse response) {
                        if (response.ApiList.size()>0)
                            new DownloadTask().execute(response);
                        else{
                            adapter.setLoaded(true);
                            if (binding.swipeRefreshLayout != null) {
                                binding.swipeRefreshLayout.setRefreshing(false);
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Timber.e("ERROR :".concat(anError.getMessage()));
                        mPage--;
                        if (binding.swipeRefreshLayout != null) {
                            binding.swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        listState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);

        outState.putParcelableArrayList(EXTRA1, new ArrayList<GuideTbl>(guides));
        outState.putInt(EXTRA2, mPage);
        super.onSaveInstanceState(outState);
    }

    private class DownloadTask extends AsyncTask<GuideApi.GetResponse, Void, Void> {

        @Override
        protected Void doInBackground(GuideApi.GetResponse... params) {
            if (params[0] != null){
                if (params[0].ApiList.size() > 0){

                        guides.clear();

                    int i=0;
                    for (GuideTbl item : params[0].ApiList){
                        long id = Facade.getInstance().getManageGuideTbl().add(item);
                        if (id>0){
                            guides.add(item);
                            i++;
                        }
                    }


                    if (i>0){
                        Timber.e("SIZE MOVIES  : "+i);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Timber.e("onPreExecute");
            if (binding.swipeRefreshLayout != null) {
                binding.swipeRefreshLayout.setRefreshing(true);
            }
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Timber.e("On Post Execute");
            adapter.notifyDataSetChanged();
            adapter.setLoaded(false);
            if (binding.swipeRefreshLayout != null) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        }
    }



}
