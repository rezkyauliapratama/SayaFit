package android.cybereye_community.com.sayafit.controller.activity;

import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.databinding.DialogPostFeedBinding;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class PostActivity extends BaseActivity{
    DialogPostFeedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_post_feed);

    }

}
