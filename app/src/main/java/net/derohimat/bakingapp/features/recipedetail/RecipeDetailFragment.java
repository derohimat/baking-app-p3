package net.derohimat.bakingapp.features.recipedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.data.models.IngredientsDao;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.data.models.StepsDao;
import net.derohimat.bakingapp.util.DialogFactory;
import net.derohimat.baseapp.ui.fragment.BaseFragment;
import net.derohimat.baseapp.ui.view.BaseImageView;

import java.util.List;

import butterknife.Bind;

import static net.derohimat.bakingapp.features.recipedetail.RecipeDetailActivity.EXTRA_RECIPE_ID;

public class RecipeDetailFragment extends BaseFragment implements RecipeDetailMvpView {

    @Bind(R.id.iv_poster) BaseImageView mImgThumbnail;
    @Bind(R.id.tv_title) TextView mTxtTitle;
    @Bind(R.id.tv_steps) TextView mTxtSteps;
    @Bind(R.id.tv_ingredients) TextView mTxtIngredients;
    @Bind(R.id.tv_servings) TextView mTxtServings;

    private RecipeDetailPresenter mPresenter;
    private ProgressBar mProgressBar = null;
    private RecipeDao mRecipeDao;
    private long mRecipeId;

    public static RecipeDetailFragment newInstance(long id) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_RECIPE_ID, id);
        recipeDetailFragment.setArguments(args);
        return recipeDetailFragment;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.detail_fragment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        mRecipeId = getArguments().getLong(EXTRA_RECIPE_ID);

        setUpPresenter();

        setHasOptionsMenu(true);
    }

    private void setUpPresenter() {
        mPresenter = new RecipeDetailPresenter(getActivity());
        mPresenter.attachView(this);
        mPresenter.loadRecipe(mRecipeId);
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

    @Override
    public void showRecipe(RecipeDao data) {
        mRecipeDao = data;
        mImgThumbnail.setImageUrl(data.getImage(), R.drawable.ic_recipes);
        mTxtTitle.setText(data.getName());
        mTxtIngredients.setText(String.valueOf(data.getIngredients().size()));
        mTxtSteps.setText(String.valueOf(data.getSteps().size()));
        mTxtServings.setText(String.valueOf(data.getServings()));
    }

    @Override
    public void showSteps(List<StepsDao> data) {

    }

    @Override
    public void showIngredients(List<IngredientsDao> data) {

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
                mPresenter.loadRecipe(mRecipeId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
