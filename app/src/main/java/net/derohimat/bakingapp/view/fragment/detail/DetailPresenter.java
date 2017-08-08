package net.derohimat.bakingapp.view.fragment.detail;

import android.content.Context;

import net.derohimat.bakingapp.BaseApplication;
import net.derohimat.bakingapp.data.remote.ApiService;
import net.derohimat.baseapp.presenter.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;

public class DetailPresenter implements BasePresenter<DetailMvpView> {

    private DetailMvpView mView;
    private Subscription mSubscription;
    private BaseApplication mBaseApplication;
//    private MovieDao mMovieDao;
//    private BaseListApiDao<ReviewDao> mReviews;
//    private BaseListApiDao<VideoDao> mVideos;

    @Inject
    DetailPresenter(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    ApiService mAPIService;
    @Inject
    Realm mRealm;
    @Inject
    EventBus mEventBus;

    @Override
    public void attachView(DetailMvpView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

//    void loadMovie(MovieDao movieDao) {
//        if (mSubscription != null) mSubscription.unsubscribe();
//
//        if (mBaseApplication != null) {
//            mBaseApplication = BaseApplication.get(mView.getContext());
//        } else {
//            mBaseApplication = BaseApplication.get(mView.getContext());
//        }
//
//        mMovieDao = movieDao;
//        mView.showMovie(mMovieDao);
//    }

//    void showReviews() {
//        mView.showProgress();
//
//        mSubscription = mAPIService.movieReviews(mMovieDao.getId(), Constant.MOVIEDB_APIKEY)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(mBaseApplication.getSubscribeScheduler())
//                .subscribe(new Subscriber<BaseListApiDao<ReviewDao>>() {
//                    @Override
//                    public void onCompleted() {
//                        Timber.i("Reviews loaded " + mReviews);
//                        mView.showReviews(mReviews.getResults());
//                        mView.hideProgress();
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//                        Timber.e("Error loading Reviews", error);
//                        if (isHttp404(error)) {
//                            mEventBus.post(new FavoriteEvent(false, mBaseApplication.getString(R.string.error_not_found)));
//                        } else {
//                            mEventBus.post(new FavoriteEvent(false, mBaseApplication.getString(R.string.error_loading_reviews)));
//                        }
//
//                        mView.hideProgress();
//                    }
//
//                    @Override
//                    public void onNext(BaseListApiDao<ReviewDao> baseListApiDao) {
//                        mReviews = baseListApiDao;
//                    }
//                });
//    }
//
//    void showVideos() {
//        mView.showProgress();
//
//        mSubscription = mAPIService.movieVideos(mMovieDao.getId(), Constant.MOVIEDB_APIKEY)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(mBaseApplication.getSubscribeScheduler())
//                .subscribe(new Subscriber<BaseListApiDao<VideoDao>>() {
//                    @Override
//                    public void onCompleted() {
//                        Timber.i("Videos loaded " + mReviews);
//                        mView.showVideos(mVideos.getResults());
//                        mView.hideProgress();
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//                        Timber.e("Error loading Videos", error);
//                        if (isHttp404(error)) {
//                            mEventBus.post(new FavoriteEvent(false, mBaseApplication.getString(R.string.error_not_found)));
//                        } else {
//                            mEventBus.post(new FavoriteEvent(false, mBaseApplication.getString(R.string.error_loading_videos)));
//                        }
//
//                        mView.hideProgress();
//                    }
//
//                    @Override
//                    public void onNext(BaseListApiDao<VideoDao> baseListApiDao) {
//                        mVideos = baseListApiDao;
//                    }
//                });
//    }
//
//    void updateMovie(MovieDao movieDao, boolean isFavorite) {
//        if (isFavorite)
//            movieDao.setFavorite(!movieDao.isFavorite());
//
//        if (!mRealm.isInTransaction()) {
//            mRealm.beginTransaction();
//        }
//
//        mRealm.copyToRealmOrUpdate(movieDao);
//        mRealm.commitTransaction();
//
//        mMovieDao = movieDao;
//
//        mView.showMovie(mMovieDao);
//        mEventBus.post(new FavoriteEvent(true, "success update favorite"));
//    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }
}