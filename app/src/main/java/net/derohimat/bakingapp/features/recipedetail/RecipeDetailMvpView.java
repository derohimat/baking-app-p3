package net.derohimat.bakingapp.features.recipedetail;

import net.derohimat.bakingapp.data.models.IngredientsDao;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.data.models.StepsDao;
import net.derohimat.bakingapp.features.MvpView;

import java.util.List;

interface RecipeDetailMvpView extends MvpView {

    void showProgress();

    void hideProgress();

    void showRecipe(RecipeDao data);
}