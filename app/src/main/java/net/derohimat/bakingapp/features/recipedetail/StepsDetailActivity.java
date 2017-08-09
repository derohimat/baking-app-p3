package net.derohimat.bakingapp.features.recipedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.data.models.StepsDao;
import net.derohimat.bakingapp.features.AppBaseActivity;
import net.derohimat.bakingapp.util.DialogFactory;
import net.derohimat.baseapp.ui.view.BaseRecyclerView;

import butterknife.Bind;

public class StepsDetailActivity extends AppBaseActivity implements StepsListMvpView {
    public static final String EXTRA_RECIPE = "RECIPE_DATA";
    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";

    @Bind(R.id.recyclerview) BaseRecyclerView mRecyclerView;
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
        mRecipeDao = getIntent().getExtras().getParcelable(EXTRA_RECIPE);
        assert mRecipeDao != null;
        mRecipeId = mRecipeDao.getId();

        initToolbar();
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
        mPresenter.loadRecipe(mRecipeDao.getId());
    }

    @Override
    public void setUpAdapter() {
        mAdapter = new StepsListAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            StepsDao selectedItem = mAdapter.getDatas().get(position - 1);

            getBaseFragmentManager().beginTransaction().replace(R.id.container_rellayout,
                    RecipeDetailFragment.newInstance(mRecipeId, selectedItem.getId())).addToBackStack(null).commit();
        });
    }

    @Override
    public void setUpRecyclerView() {
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
        mAdapter.clear();
        mAdapter.addAll(data.getSteps());
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

    @Override
    public Context getContext() {
        return this;
    }

    public static Intent prepareIntent(Context context, RecipeDao recipeDao) {
        Intent intent = new Intent(context, StepsDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipeDao);
        return intent;
    }

}
