package net.derohimat.bakingapp.features.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.data.models.IngredientsDao;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.features.AppBaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class WidgetConfigurationActivity extends AppBaseActivity {

    private WidgetDataHelper widgetDataHelper;

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Bind(R.id.widget_config_radio_group)
    RadioGroup namesRadioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.widget_configuration_activity;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        widgetDataHelper = new WidgetDataHelper(this);

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }
        }

        List<RecipeDao> recipeDaos = widgetDataHelper.getRecipe();

        if (recipeDaos.isEmpty()) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Fill the radioGroup
        int index = 0;

        for (RecipeDao item : recipeDaos) {
            AppCompatRadioButton button = new AppCompatRadioButton(this);
            button.setText(item.getName());
            button.setId(index++);
            namesRadioGroup.addView(button);
        }

        // Check the first item when loaded
        if (namesRadioGroup.getChildCount() > 0) {
            ((AppCompatRadioButton) namesRadioGroup.getChildAt(0)).setChecked(true);
        }
    }

    @OnClick(R.id.widget_config_button)
    public void onOkButtonClick() {

        int checkedItemId = namesRadioGroup.getCheckedRadioButtonId();
        String recipeName = ((AppCompatRadioButton) namesRadioGroup
                .getChildAt(checkedItemId)).getText().toString();

        widgetDataHelper.saveRecipeNameToPrefs(appWidgetId, recipeName);

        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        List<IngredientsDao> ingredients = widgetDataHelper.getIngredientsList(recipeName);
        WidgetProvider.updateAppWidgetContent(context, appWidgetManager, appWidgetId, recipeName, ingredients);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
