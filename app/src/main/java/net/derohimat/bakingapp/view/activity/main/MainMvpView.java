package net.derohimat.bakingapp.view.activity.main;

import net.derohimat.bakingapp.model.RecipeDao;
import net.derohimat.bakingapp.view.MvpView;

import java.util.List;

interface MainMvpView extends MvpView {


    void setUpPresenter();

    void setUpAdapter();

    void setUpRecyclerView();

    void showBakingList(List<RecipeDao> datas);

    void showProgress();

    void hideProgress();
}