package net.derohimat.bakingapp.view.fragment.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.model.RecipeDao;
import net.derohimat.bakingapp.util.DialogFactory;
import net.derohimat.baseapp.ui.fragment.BaseFragment;
import net.derohimat.baseapp.ui.view.BaseImageView;

import butterknife.Bind;
import butterknife.OnClick;

public class DetailFragment extends BaseFragment implements DetailMvpView {

    private static final String ARG_DATA = "ARG_DATA";
    @Bind(R.id.iv_backdrop)
    BaseImageView mIvBackdrop;
    @Bind(R.id.iv_poster)
    BaseImageView mIvPoster;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_releasedate)
    TextView mTvReleaseDate;
    @Bind(R.id.tv_voteavg)
    TextView mTvVoteAvg;
    @Bind(R.id.tv_favorite)
    TextView mTvFavorite;
    @Bind(R.id.tv_synopsis)
    TextView mTvSynopsis;

    private DetailPresenter mPresenter;
    private ProgressBar mProgressBar = null;
    private RecipeDao recipeDao;

    public static DetailFragment newInstance(RecipeDao recipeDao) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, recipeDao);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.detail_fragment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        recipeDao = getArguments().getParcelable(ARG_DATA);

        setUpPresenter();

        //for recreation of the toolbar
        setHasOptionsMenu(true);
    }

    private void setUpPresenter() {
        mPresenter = new DetailPresenter(getActivity());
        mPresenter.attachView(this);
        showMovie(recipeDao);
//        mPresenter.showVideos();
//        mPresenter.showReviews();
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
        mProgressBar.setVisibility(View.GONE);
    }

    public void showMovie(RecipeDao data) {
        mTvTitle.setText(data.getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_fragment_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_detail_refresh:
//                mPresenter.loadMovie(recipeDao);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.tv_favorite)
    void onFavoriteClicked() {
    }
}
