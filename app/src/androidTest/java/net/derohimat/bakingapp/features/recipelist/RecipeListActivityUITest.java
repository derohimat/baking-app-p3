package net.derohimat.bakingapp.features.recipelist;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.derohimat.bakingapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListActivityUITest {

    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = activityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void testRecyclerViewShow() {
        onView(withId(R.id.recipe_list_recyclerview)).check(matches(isDisplayed()));
    }

    @Test
    public void verifyData() {
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));

        onView(withText("Brownies")).check(matches(isDisplayed()));

        onView(withText("Yellow Cake")).check(matches(isDisplayed()));

        onView(withText("Cheesecake")).check(matches(isDisplayed()));
    }

    @Test
    public void verifyNutellaPie() {
        onView(withText("Nutella Pie")).perform(click());
        onView(withId(R.id.container_rellayout)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_recyclerview)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}