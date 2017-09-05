package android.cybereye_community.com.sayafit.controller.fragment;

import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.databinding.FragmentHomeBinding;
import android.cybereye_community.com.sayafit.databinding.LayoutEmptyBinding;
import android.cybereye_community.com.sayafit.view.LayoutEmptyInflate;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/5/2017.
 */

public class HomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentHomeBinding binding;

    private boolean isLandscape = false;

    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, category);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null){
            mCategory = getArguments().getString(EXTRA1);
            movies = new ArrayList<>();
        }*/

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);

        if(savedInstanceState != null){
            Timber.e("SAVEDINSTACESTATE");
            /*mCategory = savedInstanceState.getString(EXTRA1);
            movies = savedInstanceState.getParcelableArrayList(EXTRA2);
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            mPage = savedInstanceState.getInt(EXTRA3);*/
        }
        new LayoutEmptyInflate(getContext(),binding.containerHome);

        return binding.getRoot();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        /*listState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);*/

        /*outState.putString(EXTRA1, mCategory);
        outState.putParcelableArrayList(EXTRA2, new ArrayList<MovieAbstract>(movies));
        outState.putInt(EXTRA3, mPage);*/
        super.onSaveInstanceState(outState);
    }


}
