package net.derohimat.bakingapp.features.recipelist;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.features.AppBaseActivity;
import net.derohimat.bakingapp.features.steps.StepsDetailActivity;
import net.derohimat.bakingapp.util.DialogFactory;
import net.derohimat.baseapp.ui.view.BaseRecyclerView;

import java.util.List;

import butterknife.Bind;

public class RecipeListActivity extends AppBaseActivity implements RecipeListMvpView {

    @Bind(R.id.recyclerview) BaseRecyclerView mRecyclerView;
    private ProgressBar mProgressBar = null;
    private RecipeListPresenter mPresenter;
    private RecipeListAdapter mAdapter;
    private int mGridColumnCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.recipe_list_activity;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        mGridColumnCount = getResources().getInteger(R.integer.grid_column_count);
        getBaseActionBar().setElevation(0);

        setUpAdapter();
        setUpRecyclerView();
        setUpPresenter();
    }

    @Override
    public void setUpPresenter() {
        mPresenter = new RecipeListPresenter(this);
        mPresenter.attachView(this);
        mPresenter.getRecipeList(false);
    }

    @Override
    public void setUpAdapter() {
        mAdapter = new RecipeListAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            RecipeDao selectedItem = mAdapter.getDatas().get(position - 1);

            startActivity(StepsDetailActivity.prepareIntent(getContext(), selectedItem));
        });
    }

    @Override
    public void setUpRecyclerView() {
        mRecyclerView.setUpAsGrid(mGridColumnCount);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.getRecipeList(true);
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showBakingList(List<RecipeDao> datas) {
        mAdapter.clear();
        mAdapter.addAll(datas);
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
}
