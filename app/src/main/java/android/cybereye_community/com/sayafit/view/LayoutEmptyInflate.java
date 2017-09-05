package android.cybereye_community.com.sayafit.view;

import android.content.Context;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.databinding.LayoutEmptyBinding;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 31/01/2017.
 */

public class LayoutEmptyInflate {

    LayoutEmptyBinding binding;
    ViewGroup mView;
    Context mContext;

    public LayoutEmptyInflate(Context context, ViewGroup view){
        this.mView = view;
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding =  DataBindingUtil.inflate(inflater, R.layout.layout_empty,mView,false); // LayoutInflater.from(context).inflate(R.layout.content_progressbar,view,false);
        mView.addView(binding.containerEmpty);

        setVisibility(View.VISIBLE);
    }

    public void setVisibility(int visible){
        binding.containerEmpty.setVisibility(visible);
    }

    public int getVisibility(){
        return binding.containerEmpty.getVisibility();
    }


}
