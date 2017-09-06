package android.cybereye_community.com.sayafit.controller.fragment;


import android.cybereye_community.com.sayafit.controller.adapter.GuideRecyclerviewAdapter;
import android.cybereye_community.com.sayafit.controller.database.entity.GuideTbl;
import android.cybereye_community.com.sayafit.databinding.ItemListGuideBinding;
import android.cybereye_community.com.sayafit.view.LayoutEmptyInflate;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.cybereye_community.com.sayafit.R;

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
    ItemListGuideBinding binding;

    public GuideFragment newInstance()  {
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
    public void onSaveInstanceState(Bundle outState) {
        listState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);

        outState.putParcelableArrayList(EXTRA1, new ArrayList<GuideTbl>(guides));
        outState.putInt(EXTRA2, mPage);
        super.onSaveInstanceState(outState);
    }

}
