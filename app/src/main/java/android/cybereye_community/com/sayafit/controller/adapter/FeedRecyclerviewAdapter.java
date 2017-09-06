package android.cybereye_community.com.sayafit.controller.adapter;

import android.content.Context;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.database.entity.FeedTbl;
import android.cybereye_community.com.sayafit.controller.database.entity.FeedTblDao;
import android.cybereye_community.com.sayafit.controller.database.entity.UserTbl;
import android.cybereye_community.com.sayafit.databinding.ItemListFeedBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.squareup.picasso.Picasso;

import java.util.List;

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
        holder.binding.tvTime.setText(item.getDate());
        holder.binding.tvContent.setText(item.getFeed());
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
