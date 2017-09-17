package net.derohimat.bakingapp.features.steps;

import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.features.MvpView;

interface StepsListMvpView extends MvpView {

    void setUpPresenter();

    void setUpAdapter();

    void setUpRecyclerView();

    void showRecipe(RecipeDao data);

    void showError(Throwable throwable);

    void showProgress();

    void hideProgress();
}