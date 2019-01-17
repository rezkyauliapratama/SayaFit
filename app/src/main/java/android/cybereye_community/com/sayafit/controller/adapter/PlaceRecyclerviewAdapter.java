package android.cybereye_community.com.sayafit.controller.adapter;

import android.content.Context;
import android.cybereye_community.com.sayafit.EventBus;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.fragment.ShareLocationFragment;
import android.cybereye_community.com.sayafit.databinding.ItemListFeedBinding;
import android.cybereye_community.com.sayafit.databinding.ItemListPlaceBinding;
import android.cybereye_community.com.sayafit.model.place;
import android.cybereye_community.com.sayafit.utility.Utils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.airbnb.lottie.L;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/7/2017.
 */

public class PlaceRecyclerviewAdapter extends RecyclerView.Adapter<PlaceRecyclerviewAdapter.ViewHolder> {
    private List<place> mItems;
    private Context mContext;


    private int lastPosition = -1;
    private int animationCount = 0;

    public PlaceRecyclerviewAdapter(List<place> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @Override
    public PlaceRecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_place, parent, false);
        return new PlaceRecyclerviewAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(PlaceRecyclerviewAdapter.ViewHolder holder, int position) {
        final place item = mItems.get(position);

        holder.binding.tvName.setText(item.getName());
        try {
            holder.binding.tvAddress.setText(Utils.getInstance().setLocation(mContext,item.getLatLng()));
        } catch (IOException e) {
            Timber.e("ADD ERR : "+e.getMessage());
        }

        if (item.getType() == Place.TYPE_GYM){
            holder.binding.ivIcon.setImageResource(R.drawable.ic_gym);
        }else if (item.getType() == Place.TYPE_PHYSIOTHERAPIST){
            holder.binding.ivIcon.setImageResource(R.drawable.ic_massage);
        } else if (item.getType() == Place.TYPE_PARK){
            holder.binding.ivIcon.setImageResource(R.drawable.ic_park);
        } else if (item.getType() == Place.TYPE_HEALTH){
            holder.binding.ivIcon.setImageResource(R.drawable.ic_healthy);
        }

        if (item.getPhone().isEmpty()){
            holder.binding.tvPhone.setVisibility(View.GONE );
        }else{
            holder.binding.tvPhone.setText("  "+item.getPhone());
        }

        float rate = 0;
        rate = item.getRate();
        Timber.e("RATE : "+item.getRate());
        holder.binding.ratingStart.setNumStars(5);
        holder.binding.ratingStart.setRating(rate);
        holder.binding.ratingStart.setEnabled(false);


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getInstance().setObservable(item.latLng);
            }
        });

        setAnimation(holder.binding.getRoot(), position);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(final View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            final Animation animation = AnimationUtils.loadAnimation(
                    viewToAnimate.getContext(), android.R.anim.slide_in_left);
            animationCount++;
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animationCount--;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setDuration(300);
            viewToAnimate.setVisibility(View.GONE);
            viewToAnimate.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewToAnimate.setVisibility(View.VISIBLE);
                    viewToAnimate.startAnimation(animation);

                }
            }, animationCount * 100);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.binding.getRoot().setVisibility(View.VISIBLE);
        holder.binding.getRoot().clearAnimation();

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListPlaceBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemListPlaceBinding.bind(itemView);

        }
    }
}
