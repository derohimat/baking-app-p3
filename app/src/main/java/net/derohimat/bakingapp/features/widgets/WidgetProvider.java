package net.derohimat.bakingapp.features.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.data.models.IngredientsDao;
import net.derohimat.bakingapp.util.StringUtils;

import java.util.List;

import timber.log.Timber;

public class WidgetProvider extends AppWidgetProvider {

    private WidgetDataHelper widgetDataHelper;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Timber.d("Widget onUpdate");

        widgetDataHelper = new WidgetDataHelper(context);

        for (int appWidgetId : appWidgetIds) {
            String recipeName = widgetDataHelper.getRecipeNameFromPrefs(appWidgetId);

            widgetDataHelper.getIngredientsList(recipeName);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        widgetDataHelper = new WidgetDataHelper(context);

        for (int appWidgetId : appWidgetIds) {
            widgetDataHelper.deleteRecipeFromPrefs(appWidgetId);
        }
    }

    public static void updateAppWidgetContent(Context context, AppWidgetManager appWidgetManager,
                                              int appWidgetId, String recipeName, List<IngredientsDao> ingredients) {

        Timber.d("Widget onUpdate call...");
        Timber.d("id: " + appWidgetId + ", name: " + recipeName + "ingredients: " + ingredients.size());

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_list);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.removeAllViews(R.id.widget_ingredients_container);

        for (IngredientsDao ingredient : ingredients) {
            RemoteViews ingredientView = new RemoteViews(context.getPackageName(),
                    R.layout.widget_ingredients_list_item);

            String line = StringUtils.formatIngdedient(
                    context, ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure());

            ingredientView.setTextViewText(R.id.widget_ingredient_name, line);
            views.addView(R.id.widget_ingredients_container, ingredientView);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}