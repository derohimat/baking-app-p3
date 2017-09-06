package net.derohimat.bakingapp.features.widgets;

import android.content.Context;

import net.derohimat.bakingapp.BaseApplication;
import net.derohimat.bakingapp.data.models.IngredientsDao;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.data.sources.local.PreferencesHelper;
import net.derohimat.bakingapp.data.sources.remote.ApiService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class WidgetDataHelper {

    @Inject PreferencesHelper mPreferenceHelper;
    @Inject Realm mRealm;
    @Inject ApiService mApiService;

    private BaseApplication mBaseApplication;
    private Subscription mSubscription;
    private List<RecipeDao> mBakingList = new ArrayList<>();

    @Inject
    WidgetDataHelper(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
        mBaseApplication = BaseApplication.get(context);
    }

    List<RecipeDao> getRecipe() {
        if (mPreferenceHelper.isRecipeListSynced()) {
            final RealmResults<RecipeDao> recipeDaos = mRealm.where(RecipeDao.class).findAll();
            if (recipeDaos.isEmpty()) {
                return new ArrayList<>();
            } else {
                return recipeDaos;
            }
        } else {
            return getRecipeList();
        }
    }

    void deleteRecipeFromPrefs(int widgetId) {
        mPreferenceHelper.deleteRecipeName(widgetId);
    }

    void saveRecipeNameToPrefs(int appWidgetId, String name) {
        mPreferenceHelper.saveChosenRecipeName(appWidgetId, name);
    }

    String getRecipeNameFromPrefs(int appWidgetId) {
        return mPreferenceHelper.getChosenRecipeName(appWidgetId);
    }

    List<IngredientsDao> getIngredientsList(String recipeName) {
        RecipeDao recipeDaos = mRealm.where(RecipeDao.class).equalTo("name", recipeName).findFirst();
        if (recipeDaos != null) {
            return recipeDaos.getIngredients();
        } else {
            return new ArrayList<>();
        }
    }


    List<RecipeDao> getRecipeList() {
        if (mSubscription != null) mSubscription.unsubscribe();

        mSubscription = mApiService.bakingList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(mBaseApplication.getSubscribeScheduler())
                .subscribe(new Subscriber<List<RecipeDao>>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("Recipe loaded " + mBakingList);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.e("Error loading Recipe", error);
                    }

                    @Override
                    public void onNext(List<RecipeDao> response) {
                        mBakingList = response;
                    }
                });
        return mBakingList;
    }

}
