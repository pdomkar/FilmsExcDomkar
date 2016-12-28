package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Jonáš Ševčík
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListFilmTest {

    private MainActivity mainActivity;

    @Rule
    public final ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        mainActivity = rule.getActivity();
    }

    @Test
    public void testShowSavedAndNotSaved() throws Exception {
        onView(withId(R.id.menuIB))
                .perform(click());
        onView(withId(R.id.nameTV)).check(matches(isDisplayed()));
    }




}