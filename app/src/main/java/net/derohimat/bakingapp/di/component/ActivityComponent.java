package net.derohimat.bakingapp.di.component;

import net.derohimat.bakingapp.di.ActivityScope;
import net.derohimat.bakingapp.view.activity.main.MainActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface ActivityComponent extends ApplicationComponent {

    void inject(MainActivity mainActivity);
}