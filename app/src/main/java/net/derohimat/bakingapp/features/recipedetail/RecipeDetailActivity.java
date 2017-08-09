package net.derohimat.bakingapp.features.recipedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.features.AppBaseActivity;

public class RecipeDetailActivity extends AppBaseActivity {
    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";

    private long mRecipeId;

    @Override
    protected int getResourceLayout() {
        return R.layout.recipe_detail_activity;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        mRecipeId = getIntent().getExtras().getLong(EXTRA_RECIPE_ID);
        getBaseFragmentManager().beginTransaction().replace(R.id.container_rellayout,
                RecipeDetailFragment.newInstance(mRecipeId)).addToBackStack(null).commit();
    }

    public static Intent prepareIntent(Context context, long recipeId) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }
}
