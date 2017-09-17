package net.derohimat.bakingapp.features.recipelist;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static net.derohimat.bakingapp.features.steps.StepsDetailActivity.EXTRA_RECIPE_ID;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListActivityIntentTest {
    private static final long EXTRA_RECIPE_ID_VALUE = 1;

    @Rule
    public IntentsTestRule<RecipeListActivity> intentsTestRule
            = new IntentsTestRule<>(RecipeListActivity.class);

    @Test
    public void testRecipeDetailsActivityIntent() {
        onView(withText("Nutella Pie")).perform(click());
        intended(
                hasExtra(EXTRA_RECIPE_ID, EXTRA_RECIPE_ID_VALUE)
        );
    }
}
