package android.cybereye_community.com.sayafit.controller.fragment;

import android.content.ContentValues;
import android.cybereye_community.com.sayafit.EventBus;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.adapter.FeedRecyclerviewAdapter;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.database.entity.FeedTbl;
import android.cybereye_community.com.sayafit.databinding.FragmentHomeBinding;
import android.cybereye_community.com.sayafit.databinding.LayoutEmptyBinding;
import android.cybereye_community.com.sayafit.handler.ApiClient;
import android.cybereye_community.com.sayafit.handler.api.FeedApi;
import android.cybereye_community.com.sayafit.handler.api.UserApi;
import android.cybereye_community.com.sayafit.handler.listener.OnLoadMoreListener;
import android.cybereye_community.com.sayafit.model.response.ApiResponse;
import android.cybereye_community.com.sayafit.view.LayoutEmptyInflate;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/5/2017.
 */

public class HomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String EXTRA1 = "EXTRA1";
    public static final String EXTRA2 = "EXTRA2";
    public static final String EXTRA3 = "EXTRA3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentHomeBinding binding;

    private boolean isLandscape = false;

    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;
    FeedRecyclerviewAdapter adapter;
    List<FeedTbl> feeds;
    LinearLayoutManager mLayoutManager;

    int mPage;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feeds = new ArrayList<>();

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);

        if(savedInstanceState != null){
            Timber.e("SAVEDINSTACESTATE");
            feeds = savedInstanceState.getParcelableArrayList(EXTRA1);
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
        adapter = new FeedRecyclerviewAdapter(getContext(),feeds);
        binding.recyclerView.setAdapter(adapter);

        EventBus.getInstance().getObservable().subscribe(new Observer<FeedTbl>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FeedTbl feedTbl) {
                feeds.add(0,feedTbl);
                binding.recyclerView.getAdapter().notifyDataSetChanged();
            }



        });


        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Timber.e("NEXT PAGE");
                mPage++;
                loadData();
            }
        });

        if(feeds.size() == 0 )
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

            ApiClient.getInstance().feed().get(mPage)
                    .getAsObject(FeedApi.GetResponse.class, new ParsedRequestListener<FeedApi.GetResponse>() {
                        @Override
                        public void onResponse(FeedApi.GetResponse response) {
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

        outState.putParcelableArrayList(EXTRA1, new ArrayList<FeedTbl>(feeds));
        outState.putInt(EXTRA2, mPage);
        super.onSaveInstanceState(outState);
    }


    private class DownloadTask extends AsyncTask<FeedApi.GetResponse, Void, Void> {

        @Override
        protected Void doInBackground(FeedApi.GetResponse... params) {
            if (params[0] != null){
                if (params[0].ApiList.size() > 0){
                    if (mPage == 1) {
                        feeds.clear();
                    }
                    int i=0;
                    for (FeedTbl feed : params[0].ApiList){
                        long id = Facade.getInstance().getManageFeedTbl().add(feed);
                        if (id>0){
                            feeds.add(feed);
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
