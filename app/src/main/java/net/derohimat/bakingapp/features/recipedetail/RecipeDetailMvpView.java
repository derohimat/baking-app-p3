package net.derohimat.bakingapp.features.recipedetail;

import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.features.MvpView;

interface RecipeDetailMvpView extends MvpView {

    void showRecipe(RecipeDao data);
}