package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Jonáš Ševčík
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DetailFilmTest {

    private MainActivity mainActivity;

    @Rule
    public final ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    IntentServiceIdlingResource idlingResource;

    @Before
    public void init() {
        Instrumentation instrumentation
                = InstrumentationRegistry.getInstrumentation();
        Context ctx = instrumentation.getTargetContext();
        idlingResource = new IntentServiceIdlingResource(ctx);
        Espresso.registerIdlingResources(idlingResource);
        mainActivity = rule.getActivity();
    }

    @After
    public void after() {
        Espresso.unregisterIdlingResources(idlingResource);

    }

    @Test
    public void testClickOnFilmInList() throws Exception {
        Thread.sleep(5000);

        onData(Matchers.anything()).inAdapterView(withId(R.id.filmsLV)).atPosition(0).perform(click());


//        onData(Matchers.anything())
//                .inAdapterView(withId(R.id.filmsLV))
//                .atPosition(0)
//                .onChildView(withId(R.id.titleTV))
//                .perform(click());


        onView(withId(R.id.titleDetailExpandedTV)).check(matches(isDisplayed()));
    }




}