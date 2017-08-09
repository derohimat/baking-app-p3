package net.derohimat.bakingapp.features.recipedetail;

import android.content.Context;

import net.derohimat.bakingapp.BaseApplication;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.data.sources.remote.ApiService;
import net.derohimat.baseapp.presenter.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;

public class RecipeDetailPresenter implements BasePresenter<RecipeDetailMvpView> {

    private RecipeDetailMvpView mView;
    private Subscription mSubscription;
    private BaseApplication mBaseApplication;
    private RecipeDao mData;
//    private BaseListApiDao<ReviewDao> mReviews;
//    private BaseListApiDao<VideoDao> mVideos;

    @Inject
    RecipeDetailPresenter(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    ApiService mAPIService;
    @Inject
    Realm mRealm;
    @Inject
    EventBus mEventBus;

    @Override
    public void attachView(RecipeDetailMvpView view) {
        mView = view;
        mBaseApplication = BaseApplication.get(mView.getContext());
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    void loadRecipe(long id) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mData = mRealm.where(RecipeDao.class).equalTo("id", id).findFirst();

        mView.showRecipe(mData);
    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }
}