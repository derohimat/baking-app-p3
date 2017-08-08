package net.derohimat.bakingapp.di.component;

import net.derohimat.bakingapp.BaseApplication;
import net.derohimat.bakingapp.data.local.PreferencesHelper;
import net.derohimat.bakingapp.data.remote.ApiService;
import net.derohimat.bakingapp.data.remote.UnauthorisedInterceptor;
import net.derohimat.bakingapp.di.module.ApplicationModule;
import net.derohimat.bakingapp.view.fragment.detail.DetailPresenter;
import net.derohimat.bakingapp.view.activity.main.MainPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(MainPresenter mainPresenter);
    void inject(DetailPresenter detailPresenter);
    void inject(BaseApplication baseApplication);
    void inject(UnauthorisedInterceptor unauthorisedInterceptor);

    ApiService apiService();
    EventBus eventBus();
    Realm realm();
    PreferencesHelper prefsHelper();

}