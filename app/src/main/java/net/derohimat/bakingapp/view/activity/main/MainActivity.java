package net.derohimat.bakingapp.view.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.model.RecipeDao;
import net.derohimat.bakingapp.util.DialogFactory;
import net.derohimat.bakingapp.view.AppBaseActivity;
import net.derohimat.bakingapp.view.fragment.detail.DetailFragment;
import net.derohimat.baseapp.ui.view.BaseRecyclerView;

import java.util.List;

import butterknife.Bind;

public class MainActivity extends AppBaseActivity implements MainMvpView {

    @Bind(R.id.recyclerview) BaseRecyclerView mRecyclerView;
    private ProgressBar mProgressBar = null;
    private MainPresenter mPresenter;
    private MainAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.main_activity;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        getBaseActionBar().setElevation(0);

        getBaseFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getBaseActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getBaseActionBar().setTitle(getString(R.string.app_name));
                getBaseActionBar().setDisplayHomeAsUpEnabled(false);
            }
        });

        setUpPresenter();
        setUpAdapter();
        setUpRecyclerView();
    }

    @Override
    public void setUpPresenter() {
        mPresenter = new MainPresenter(this);
        mPresenter.attachView(this);
        mPresenter.getBakingList();
    }

    @Override
    public void setUpAdapter() {
        mAdapter = new MainAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            RecipeDao selectedItem = mAdapter.getDatas().get(position - 1);

            getBaseActionBar().setTitle(selectedItem.getName());
            getBaseFragmentManager().beginTransaction().replace(R.id.container_rellayout,
                    DetailFragment.newInstance(selectedItem)).addToBackStack(null).commit();
        });
    }

    @Override
    public void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.getBakingList();
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
