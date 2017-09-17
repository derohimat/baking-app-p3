package net.derohimat.bakingapp.features.recipedetail;

import android.content.Context;

import net.derohimat.bakingapp.BaseApplication;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.baseapp.presenter.BasePresenter;

import javax.inject.Inject;

import io.realm.Realm;

public class RecipeDetailPresenter implements BasePresenter<RecipeDetailMvpView> {

    private RecipeDetailMvpView mView;

    @Inject
    RecipeDetailPresenter(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    Realm mRealm;

    @Override
    public void attachView(RecipeDetailMvpView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    void loadRecipe(long id) {
        RecipeDao recipeDao = mRealm.where(RecipeDao.class).equalTo("id", id).findFirst();

        mView.showRecipe(recipeDao);
    }
}