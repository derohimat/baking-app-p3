package net.derohimat.bakingapp.features.recipelist;

import android.content.Context;

import net.derohimat.bakingapp.BaseApplication;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.data.sources.remote.ApiService;
import net.derohimat.baseapp.presenter.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class RecipeListPresenter implements BasePresenter<RecipeListMvpView> {

    @Inject
    RecipeListPresenter(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    ApiService mApiService;
    @Inject
    EventBus mEventBus;
    @Inject
    Realm mRealm;

    private RecipeListMvpView mView;
    private Subscription mSubscription;
    private List<RecipeDao> mBakingList;
    private BaseApplication mBaseApplication;

    @Override
    public void attachView(RecipeListMvpView view) {
        mView = view;
        mBaseApplication = BaseApplication.get(mView.getContext());
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    void getRecipeList(boolean refresh) {

        mView.showProgress();
        if (mSubscription != null) mSubscription.unsubscribe();

        if (refresh) {
            mSubscription = mApiService.bakingList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(mBaseApplication.getSubscribeScheduler())
                    .subscribe(new Subscriber<List<RecipeDao>>() {
                        @Override
                        public void onCompleted() {
                            Timber.i("Recipe loaded " + mBakingList);
                            mView.showBakingList(mBakingList);
                        }

                        @Override
                        public void onError(Throwable error) {
                            Timber.e("Error loading Movies", error);
                            mView.showError(error);
                        }

                        @Override
                        public void onNext(List<RecipeDao> response) {
                            mBakingList = response;
                            saveToLocal();
                            mView.hideProgress();
                        }
                    });
        } else {
            getFromLocal();
        }
    }

    private void getFromLocal() {
        final RealmResults<RecipeDao> recipeDaos = mRealm.where(RecipeDao.class).findAll();

        if (recipeDaos.isEmpty()) {
            getRecipeList(true);
        } else {
            mView.showBakingList(recipeDaos);
        }
    }

    private void saveToLocal() {
        if (!mRealm.isInTransaction()) {
            mRealm.beginTransaction();
        }

        mRealm.copyToRealmOrUpdate(mBakingList);
    }


    void closeRealm() {
        mRealm.close();
    }
}