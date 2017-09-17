package net.derohimat.bakingapp.di.component;

import net.derohimat.bakingapp.di.ActivityScope;
import net.derohimat.bakingapp.features.steps.StepsDetailActivity;
import net.derohimat.bakingapp.features.recipelist.RecipeListActivity;
import net.derohimat.bakingapp.features.widgets.WidgetConfigurationActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface ActivityComponent extends ApplicationComponent {

    void inject(RecipeListActivity activity);
    void inject(StepsDetailActivity activity);

    void inject(WidgetConfigurationActivity activity);
}