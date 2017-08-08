package net.derohimat.bakingapp.di.module;

import net.derohimat.bakingapp.BaseApplication;
import net.derohimat.bakingapp.data.local.PreferencesHelper;
import net.derohimat.bakingapp.data.remote.ApiService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class ApplicationModule {

    private final BaseApplication mBaseApplication;

    public ApplicationModule(BaseApplication baseApplication) {
        this.mBaseApplication = baseApplication;
    }

    @Provides
    @Singleton
    public BaseApplication provideApplication() {
        return mBaseApplication;
    }

    @Provides
    @Singleton
    public ApiService provideApiService() {
        return ApiService.Factory.create(mBaseApplication);
    }

    @Provides
    @Singleton
    public EventBus eventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    public Realm realm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    public PreferencesHelper prefsHelper() {
        return new PreferencesHelper(mBaseApplication);
    }

}