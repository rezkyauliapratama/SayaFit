package android.cybereye_community.com.sayafit.controller.adapter;

import android.content.Context;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.database.entity.FeedTbl;
import android.cybereye_community.com.sayafit.controller.database.entity.FeedTblDao;
import android.cybereye_community.com.sayafit.controller.database.entity.UserTbl;
import android.cybereye_community.com.sayafit.databinding.ItemListFeedBinding;
import android.cybereye_community.com.sayafit.utility.Constant;
import android.cybereye_community.com.sayafit.utility.DimensionConverter;
import android.cybereye_community.com.sayafit.utility.Utils;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class FeedRecyclerviewAdapter extends BaseAdapter{
    private Context mContext;
    private List<FeedTbl> mItems;

    private int animationCount = 0;
    private int lastPosition = -1;

    public FeedRecyclerviewAdapter(Context context, List<FeedTbl> items) {
        mContext = context;
        mItems = items;
    }


    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_feed, parent, false);
        return new MainViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainViewHolder) {
            onMainBindViewHolder((MainViewHolder) holder, position);
        }
    }

    private void onMainBindViewHolder(MainViewHolder holder, int position) {
        FeedTbl item = mItems.get(position);

        UserTbl user = Facade.getInstance().getManageUserTbl().get();
        holder.binding.tvName.setText(user.getNama());

        Date now = Utils.getInstance().time().parseDateWithT(item.getDate());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now.getTime());
        holder.binding.tvTime.setText(Utils.getInstance().time().getUserFriendlyDuration(mContext,cal));

        holder.binding.tvContent.setText(item.getFeed());

        Timber.e("image : " + item.image);
        if (!item.getImage().isEmpty()){
            File folder = Utils.getFolder(Constant.getInstance().PROFILE_FOLDER);/*new File(getFilesDir(), Constant.getInstance().PROFILE_FOLDER);*/
            File newFile = new File(folder, item.image);

            holder.binding.ivFeed.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(newFile)
                    .into(holder.binding.ivFeed);
        }

        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(
                        DimensionConverter.getInstance().stringToDimensionPixelSize(
                                "12sp", mContext.getResources().getDisplayMetrics()
                        )
                )
                .withBorder(
                        DimensionConverter.getInstance().stringToDimensionPixelSize(
                                "1dp", mContext.getResources().getDisplayMetrics()
                        )
                )
                .bold()
                .textColor(generator.getColor(user.nama))
                .endConfig()
                .buildRound(
                        String.valueOf(user.nama.toUpperCase().charAt(0)),
                        Color.WHITE
                );

        holder.binding.ivAvatar.setImageDrawable(drawable);
            Timber.e("Lat Lng");
            LatLng latLng = new LatLng(item.getLat(),item.getLng());

            String knownName = "";
            try {
                knownName = Utils.getInstance().setLocation(mContext,latLng);
                Timber.e("knowName : "+knownName.length());
                if (knownName.length() > 0){
                    holder.binding.cardviewTop.setVisibility(View.VISIBLE);
                    holder.binding.textViewAddress.setText(knownName);
                }else{
                    holder.binding.cardviewTop.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                Timber.e("ERROR : "+e.getMessage());
                e.printStackTrace();
            }


    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        private final ItemListFeedBinding binding;

        public MainViewHolder(View itemView) {
            super(itemView);
            binding = ItemListFeedBinding.bind(itemView);

        }
    }
}
