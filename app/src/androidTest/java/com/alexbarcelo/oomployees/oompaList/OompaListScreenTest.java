package com.alexbarcelo.oomployees.oompaList;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.alexbarcelo.commons.test.ElapsedTimeIdlingResource;
import com.alexbarcelo.oomployees.R;
import com.alexbarcelo.oomployees.data.source.OompaMockRepository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OompaListScreenTest {

    @Rule
    public ActivityTestRule<OompaListActivity> mActivityTestRule = new ActivityTestRule(OompaListActivity.class);

    private void waitForFirstPageToBeLoaded() {
        //Esperamos a que cargue la lista
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(3000);
        IdlingRegistry.getInstance().register(idlingResource);

        //Comprobamos que
        RecyclerView rv = mActivityTestRule.getActivity().findViewById(R.id.oompa_list_view);

        assertThat(rv.getAdapter().getItemCount(), is(OompaMockRepository.PAGE_SIZE + 1));

        //Clean up
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void clickOnSearchMenuItem_opensMovieSearchScreen() {
        // Click on the search icon in the action bar
        onView(withId(R.id.open_filter_menu_item)).perform(click());

        // Check if the search view is visible
        onView(withId(R.id.gender_spinner)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnItem_opensMovieSearchScreen() {
        // Click on the search icon in the action bar
        RecyclerView rv = mActivityTestRule.getActivity().findViewById(R.id.oompa_list_view);
        onView(withId(R.id.oompa_list_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check if the search view is visible
        onView(withId(R.id.detail_age)).check(matches(isDisplayed()));
    }

    @Test
    public void onStartPopularMovies_itemsAreDisplayed() {
        waitForFirstPageToBeLoaded();
    }

    @Test
    public void scrollDownToTheBottomOfTheList_loadsMoreItems() {
        waitForFirstPageToBeLoaded();

        IdlingResource idlingResource = new ElapsedTimeIdlingResource(2000);
        IdlingRegistry.getInstance().register(idlingResource);

        // Scroll down to the last item on the list
        RecyclerView rv = mActivityTestRule.getActivity().findViewById(R.id.oompa_list_view);
        onView(withId(R.id.oompa_list_view)).perform(RecyclerViewActions.scrollToPosition(
                rv.getAdapter().getItemCount() - 1));

        IdlingRegistry.getInstance().unregister(idlingResource);

        idlingResource = new ElapsedTimeIdlingResource(3000);
        IdlingRegistry.getInstance().register(idlingResource);

        // Check that first item of the next page has been loaded
        assertThat(rv.getAdapter().getItemCount(), is((OompaMockRepository.PAGE_SIZE * 2) + 1));

        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
