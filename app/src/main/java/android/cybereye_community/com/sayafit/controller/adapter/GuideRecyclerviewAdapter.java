package android.cybereye_community.com.sayafit.controller.adapter;

import android.content.Context;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.database.entity.GuideTbl;
import android.cybereye_community.com.sayafit.controller.database.entity.UserTbl;
import android.cybereye_community.com.sayafit.databinding.ItemListFeedBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by edikurniawan on 9/6/17.
 */

public class GuideRecyclerviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<GuideTbl> mItems;

    private int animationCount = 0;
    private int lastPosition = -1;

    public GuideRecyclerviewAdapter(Context mContext, List<GuideTbl> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_guide, parent, false);
        return new MainViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GuideRecyclerviewAdapter.MainViewHolder) {
            onMainBindViewHolder((GuideRecyclerviewAdapter.MainViewHolder) holder, position);
        }
    }

    private void onMainBindViewHolder(GuideRecyclerviewAdapter.MainViewHolder holder, int position) {
        GuideTbl item = mItems.get(position);

        UserTbl user = Facade.getInstance().getManageUserTbl().get();
        holder.binding.tvName.setText(item.getNm_category());
        holder.binding.tvTime.setText(item.getDate());
        holder.binding.tvContent.setText(item.getKet());
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
