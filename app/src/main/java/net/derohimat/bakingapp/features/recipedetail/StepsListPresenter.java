package net.derohimat.bakingapp.features.recipedetail;

import android.content.Context;

import net.derohimat.bakingapp.BaseApplication;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.data.sources.remote.ApiService;
import net.derohimat.baseapp.presenter.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Subscription;

public class StepsListPresenter implements BasePresenter<StepsListMvpView> {

    @Inject
    StepsListPresenter(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    ApiService mApiService;
    @Inject
    EventBus mEventBus;
    @Inject
    Realm mRealm;

    private StepsListMvpView mView;
    private Subscription mSubscription;
    private RecipeDao mData;

    @Override
    public void attachView(StepsListMvpView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadRecipe(long id) {

        mView.showProgress();

        if (mSubscription != null) mSubscription.unsubscribe();

        mData = mRealm.where(RecipeDao.class).equalTo("id", id).findFirst();

        mView.showRecipe(mData);

        mView.hideProgress();
    }

    void closeRealm() {
        mRealm.close();
    }
}