package net.derohimat.bakingapp.features.recipedetail;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.data.models.StepsDao;
import net.derohimat.baseapp.ui.adapter.BaseRecyclerAdapter;
import net.derohimat.baseapp.ui.adapter.viewholder.BaseItemViewHolder;

import butterknife.Bind;


/**
 * Created by deni rohimat on 17/02/15.
 */
class StepsListAdapter extends BaseRecyclerAdapter<StepsDao, StepsListAdapter.StepsHolder> {

    int selectedPosition = 0;

    StepsListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_steps;
    }

    @Override
    public StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StepsHolder(mContext, getView(parent, viewType), mItemClickListener, mLongItemClickListener);
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    class StepsHolder extends BaseItemViewHolder<StepsDao> {

        @Bind(R.id.bg_card) CardView mBgCard;
        @Bind(R.id.iv_play) ImageView mImgPlay;
        @Bind(R.id.tv_title) TextView mTxtTitle;

        StepsHolder(Context context, View itemView, OnItemClickListener itemClickListener,
                    OnLongItemClickListener longItemClickListener) {
            super(itemView, itemClickListener, longItemClickListener);
            this.mContext = context;
        }

        @Override
        public void bind(StepsDao item) {
            mTxtTitle.setText(item.getId() + " " + item.getShortDescription());
            if (item.getVideoURL().equals("")) {
                mImgPlay.setVisibility(View.GONE);
            } else {
                mImgPlay.setVisibility(View.VISIBLE);
            }

            if (selectedPosition == item.getId()) {
                mBgCard.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryLight));
            } else {
                mBgCard.setCardBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
            }
        }
    }

}
