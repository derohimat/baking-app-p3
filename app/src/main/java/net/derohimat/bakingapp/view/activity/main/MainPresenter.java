package net.derohimat.bakingapp.view.activity.main;

import android.content.Context;

import net.derohimat.bakingapp.BaseApplication;
import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.data.remote.ApiService;
import net.derohimat.bakingapp.events.FavoriteEvent;
import net.derohimat.bakingapp.model.RecipeDao;
import net.derohimat.baseapp.presenter.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class MainPresenter implements BasePresenter<MainMvpView> {

    @Inject
    MainPresenter(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    ApiService mAPIService;
    @Inject
    EventBus mEventBus;
    @Inject
    Realm mRealm;

    private MainMvpView mView;
    private Subscription mSubscription;
    private List<RecipeDao> mBakingList;
    private BaseApplication mBaseApplication;

    @Override
    public void attachView(MainMvpView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    void getBakingList() {

        mView.showProgress();
        if (mSubscription != null) mSubscription.unsubscribe();


        if (mBaseApplication != null) {
            mBaseApplication = BaseApplication.get(mView.getContext());
        } else {
            mBaseApplication = BaseApplication.get(mView.getContext());
        }

        mSubscription = mAPIService.bakingList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(mBaseApplication.getSubscribeScheduler())
                .subscribe(new Subscriber<List<RecipeDao>>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("Baking loaded " + mBakingList);
                        mView.showBakingList(mBakingList);
                        mView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.e("Error loading Movies", error);
                        if (isHttp404(error)) {
                            mEventBus.post(new FavoriteEvent(false, mBaseApplication.getString(R.string.error_not_found)));
                        } else {
                            mEventBus.post(new FavoriteEvent(false, mBaseApplication.getString(R.string.error_loading_movie)));
                        }

                        mView.hideProgress();
                    }

                    @Override
                    public void onNext(List<RecipeDao> response) {
                        mBakingList = response;
                    }
                });
    }

    void closeRealm() {
        mRealm.close();
    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }
}