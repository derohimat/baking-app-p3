package net.derohimat.bakingapp.features.steps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.data.idlingresources.RecipesIdlingResource;
import net.derohimat.bakingapp.data.models.IngredientsDao;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.data.models.StepsDao;
import net.derohimat.bakingapp.features.AppBaseActivity;
import net.derohimat.bakingapp.features.recipedetail.RecipeDetailFragment;
import net.derohimat.bakingapp.util.DialogFactory;
import net.derohimat.baseapp.ui.view.BaseRecyclerView;

import java.text.DecimalFormat;

import butterknife.Bind;

public class StepsDetailActivity extends AppBaseActivity implements StepsListMvpView {
    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";
    public static final String SPACE = " ";
    public static final String NEW_LINE = "\n";
    public static final String DASH = "-";

    @Nullable private RecipesIdlingResource idlingResource;

    @Bind(R.id.detail_recyclerview) BaseRecyclerView mRecyclerView;
    private TextView mTvIngredients;
    private ProgressBar mProgressBar = null;
    private StepsListPresenter mPresenter;
    private StepsListAdapter mAdapter;
    private RecipeDao mRecipeDao;
    private long mRecipeId;

    @Override
    protected int getResourceLayout() {
        return R.layout.recipe_detail_activity;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        mRecipeId = getIntent().getExtras().getLong(EXTRA_RECIPE_ID);

        setUpAdapter();
        setUpRecyclerView();
        setUpPresenter();
    }

    private void initToolbar() {
        getBaseActionBar().setElevation(0);

        getBaseActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseActionBar().setTitle(mRecipeDao.getName());
    }

    @Override
    public void setUpPresenter() {
        mPresenter = new StepsListPresenter(this);
        mPresenter.attachView(this);
        mPresenter.loadRecipe(mRecipeId);
    }

    @Override
    public void setUpAdapter() {
        mAdapter = new StepsListAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            StepsDao selectedItem = mAdapter.getDatas().get(position - 2);

            mAdapter.setSelectedPosition(position - 2);
            mAdapter.notifyDataSetChanged();

            getBaseFragmentManager().beginTransaction().replace(R.id.container_rellayout,
                    RecipeDetailFragment.newInstance(mRecipeId, selectedItem.getId())).addToBackStack(null).commit();
        });
    }

    @Override
    public void setUpRecyclerView() {
        View headerView = View.inflate(getContext(), R.layout.header_ingredients, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTvIngredients = (TextView) headerView.findViewById(R.id.tv_title);

        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setUpAsList();
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadRecipe(mRecipeDao.getId());
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    @Override
    public void showRecipe(RecipeDao data) {
        if (data != null) {
            mRecipeDao = data;
            mAdapter.clear();
            mAdapter.addAll(data.getSteps());
            initToolbar();

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < data.getIngredients().size(); i++) {
                IngredientsDao item = data.getIngredients().get(i);

                DecimalFormat format = new DecimalFormat();
                format.setDecimalSeparatorAlwaysShown(false);

                builder.append(DASH)
                        .append(SPACE)
                        .append(format.format(item.getQuantity()))
                        .append(SPACE)
                        .append(item.getMeasure())
                        .append(SPACE)
                        .append(item.getIngredient())
                        .append(NEW_LINE);
            }
            mTvIngredients.setText(builder.toString());
        }
    }

    @Override
    public void showError(Throwable throwable) {
        DialogFactory.createGenericErrorDialog(getContext(), throwable.getMessage()).show();
    }

    @Override
    public void showProgress() {
        if (mProgressBar == null) {
            mProgressBar = DialogFactory.DProgressBar(mContext);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        mRecyclerView.refreshComplete();
        mProgressBar.setVisibility(View.GONE);
    }


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new RecipesIdlingResource();
        }
        return idlingResource;
    }

    @Override
    public Context getContext() {
        return this;
    }

    public static Intent prepareIntent(Context context, long id) {
        Intent intent = new Intent(context, StepsDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, id);
        return intent;
    }

}
