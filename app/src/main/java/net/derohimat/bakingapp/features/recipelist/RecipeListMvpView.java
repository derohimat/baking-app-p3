package net.derohimat.bakingapp.features.recipelist;

import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.features.MvpView;

import java.util.List;

interface RecipeListMvpView extends MvpView {


    void setUpPresenter();

    void setUpAdapter();

    void setUpRecyclerView();

    void showBakingList(List<RecipeDao> datas);

    void showError(Throwable throwable);

    void showProgress();

    void hideProgress();
}