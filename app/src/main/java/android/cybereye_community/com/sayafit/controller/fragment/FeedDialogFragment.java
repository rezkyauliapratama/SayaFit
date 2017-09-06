package android.cybereye_community.com.sayafit.controller.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.database.entity.UserTbl;
import android.cybereye_community.com.sayafit.databinding.DialogPostFeedBinding;
import android.cybereye_community.com.sayafit.model.request.FeedPost;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class FeedDialogFragment extends DialogFragment {

    public final static int TARGET = 1;
    public final static String DiALOG= "DIALOG";
    private final static String ARG_PARAM1="ARG_PARAM1";
    private final static String ARG_PARAM2="ARG_PARAM2";

    DialogPostFeedBinding binding;

    private onFragmentInteraction mListener;


    public static FeedDialogFragment newInstance(){
        FeedDialogFragment dialogFragment = new FeedDialogFragment();
        Bundle args = new Bundle();
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  DataBindingUtil.inflate(inflater, R.layout.dialog_post_feed,container,false);
        getDialog().setTitle("Post");

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_feed = binding.etFeed.getText().toString();
                if (str_feed.length() > 0){
                    UserTbl user = Facade.getInstance().getManageUserTbl().get();
                    FeedPost feed = new FeedPost();
                    feed.setEmail(user.getEmail());
                    feed.setFeed(binding.etFeed.getText().toString());
                    feed.setNama(user.getNama());
                    feed.setPreview("1");
                    feed.setImage("");
                    mListener.onPostFeedInteraction(feed);
                    dismiss();
                }

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentInteraction) {
            mListener = (onFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface onFragmentInteraction {
        // TODO: Update argument type and name
        void onPostFeedInteraction(FeedPost feed);
        void onAlertInteraction(String str);
    }


}
