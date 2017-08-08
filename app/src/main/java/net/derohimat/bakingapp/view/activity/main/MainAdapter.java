package net.derohimat.bakingapp.view.activity.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.model.RecipeDao;
import net.derohimat.baseapp.ui.adapter.BaseRecyclerAdapter;
import net.derohimat.baseapp.ui.adapter.viewholder.BaseItemViewHolder;
import net.derohimat.baseapp.ui.view.BaseImageView;

import butterknife.Bind;
import timber.log.Timber;


/**
 * Created by deni rohimat on 17/02/15.
 */
class MainAdapter extends BaseRecyclerAdapter<RecipeDao, MainAdapter.BakingHolder> {

    MainAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_recipes;
    }

    @Override
    public BakingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BakingHolder(mContext, getView(parent, viewType), mItemClickListener, mLongItemClickListener);
    }

    class BakingHolder extends BaseItemViewHolder<RecipeDao> {

        @Bind(R.id.iv_thumbnail) BaseImageView mImgThumbnail;
        @Bind(R.id.tv_title) TextView mTxtTitle;
        @Bind(R.id.tv_steps) TextView mTxtSteps;
        @Bind(R.id.tv_ingredients) TextView mTxtIngredients;

        BakingHolder(Context context, View itemView, BaseRecyclerAdapter.OnItemClickListener itemClickListener,
                     BaseRecyclerAdapter.OnLongItemClickListener longItemClickListener) {
            super(itemView, itemClickListener, longItemClickListener);
            this.mContext = context;
        }

        @Override
        public void bind(RecipeDao item) {
            mImgThumbnail.setImageUrl(item.getImage(), R.drawable.ic_recipes);
            mTxtTitle.setText(item.getName());
            Timber.i("Adapter ", item.getName());
//            mTxtIngredients.setText(item.getIngredients().size());
//            mTxtSteps.setText(item.getSteps().size());
        }
    }

}
