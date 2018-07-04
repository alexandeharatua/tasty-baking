package com.example.aarta.tastybaking.ui.main;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.aarta.tastybaking.R;
import com.example.aarta.tastybaking.data.database.RecipeDao;
import com.example.aarta.tastybaking.data.database.RecipeDatabase;
import com.example.aarta.tastybaking.data.models.Recipe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.hamcrest.core.IsNot.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private String TAG = MainActivity.class.getName() + " test";
    private List<Recipe> recipes;

    @Before
    public void setUp() {
        Log.d(TAG, "setup");
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RecipeDao recipeDao = RecipeDatabase.getInstance(ctx).recipeDao();
        recipes = recipeDao.getStaticRecipes();
        Log.d(TAG, "First Recipe is " + recipes.get(0));
    }

    @Test
    public void recylerViewUITest() {
        // fragment with recylerView cards_list_holder has all cards with correct ids, names && labels displayed
        if (recipes != null) {
            for (int i = 0; i < recipes.size(); i++) {
                Recipe oneRecipe = recipes.get(i);
                String recipeName = oneRecipe.getName();
                Log.d(TAG, "Current list recipe on test has name " + recipeName);
                String recipeIngredientsNum = String.valueOf(oneRecipe.getIngredients().size());
                String recipeStepsNum = String.valueOf(oneRecipe.getSteps().size());
                String recipeServingsNum = String.valueOf(oneRecipe.getServings());
                onView(withId(R.id.cards_list_holder))
                        .perform(scrollTo(hasDescendant(allOf(withId(R.id.tv_recipe_name), withText(recipeName)))))
                        .check(matches(hasDescendant(allOf(withId(R.id.tv_recipe_name), withText(recipeName)))))
                        .check(matches(hasDescendant(allOf(withId(R.id.tv_ingredient_label), withText(R.string.ingredients)))))
                        .check(matches(hasDescendant(allOf(withId(R.id.tv_ingredient_num), withText(recipeIngredientsNum)))))
                        .check(matches(hasDescendant(allOf(withId(R.id.tv_steps_label), withText(R.string.steps)))))
                        .check(matches(hasDescendant(allOf(withId(R.id.tv_steps_num), withText(recipeStepsNum)))))
                        .check(matches(hasDescendant(allOf(withId(R.id.tv_servings_label), withText(R.string.servings)))))
                        .check(matches(hasDescendant(allOf(withId(R.id.tv_servings_num), withText(recipeServingsNum)))))
                        .check(matches(isDisplayed()));
            }
        }
    }

    @Test
    public void recylerViewPlayerTest() {
        // does exo player show up after clicking on the play button
        if (recipes != null) {
            for (int i = 0; i < recipes.size(); i++) {
                int cardsRecyclerViewID = R.id.cards_list_holder;
                ViewHolderMatcher VHMatcher = new ViewHolderMatcher();

                onView(withId(cardsRecyclerViewID))
                        .perform(scrollToHolder(VHMatcher).atPosition(i))
                        .check(matches(hasDescendant(withId(R.id.ib_recipe_play_icon))))
                        .check(matches(hasDescendant(withId(R.id.exo_step_player))));

//                onView(withId(cardsRecyclerViewID))
//                        .perform(scrollToHolder(VHMatcher).atPosition(i))
//                        // click play
//                        .perform(actionOnHolderItem(new ViewHolderMatcher(
//                                withId(R.id.ib_recipe_play_icon)),
//                                click()).atPosition(i))
//                        // thumbnail holder is gone or not visible
//                        .check(matches(allOf(
//                                hasDescendant(withId(R.id.fl_exo_thumbnail_holder)),
//                                not(isDisplayed()))))
//                        // exo player is visible
//                        .check(matches(allOf(
//                                hasDescendant(withId(R.id.exo_step_player)),
//                                isDisplayed())));
            }
        }
    }


    private static class ViewHolderMatcher extends TypeSafeMatcher<RecyclerView.ViewHolder> {
        private Matcher<View> itemMatcher = any(View.class);

        public ViewHolderMatcher() {
        }

        public ViewHolderMatcher(Matcher<View> itemMatcher) {
            this.itemMatcher = itemMatcher;
        }

        @Override
        public boolean matchesSafely(RecyclerView.ViewHolder viewHolder) {
            return RecyclerView.ViewHolder.class.isAssignableFrom(viewHolder.getClass())
                    && itemMatcher.matches(viewHolder.itemView);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("is assignable from CustomViewHolder");
        }
    }

}
