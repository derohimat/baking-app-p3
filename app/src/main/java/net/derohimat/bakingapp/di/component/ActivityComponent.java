package net.derohimat.bakingapp.di.component;

import net.derohimat.bakingapp.di.ActivityScope;
import net.derohimat.bakingapp.features.recipelist.RecipeListActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface ActivityComponent extends ApplicationComponent {

    void inject(RecipeListActivity recipeListActivity);
}