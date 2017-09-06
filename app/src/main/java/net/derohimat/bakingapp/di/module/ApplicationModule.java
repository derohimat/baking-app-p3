package net.derohimat.bakingapp.di.module;

import android.content.Context;

import net.derohimat.bakingapp.data.sources.local.PreferencesHelper;
import net.derohimat.bakingapp.data.sources.remote.ApiService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public ApiService provideApiService() {
        return ApiService.Factory.create(context);
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
        return new PreferencesHelper(context);
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

}