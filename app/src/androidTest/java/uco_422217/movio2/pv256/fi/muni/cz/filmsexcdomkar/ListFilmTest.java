package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

/**
 *
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListFilmTest {

    private MainActivity mainActivity;
    private MockWebServer server;

    @Rule
    public final ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() throws IOException {
        server = new MockWebServer();
        server.start();
        mainActivity = rule.getActivity();
    }

    @After
    public void finish() throws IOException {
        server.shutdown();
    }

    @Test
    public void testShowSavedFilms() throws IOException, InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.savedS)).perform(click());
        Thread.sleep(3000);
        onData(CoreMatchers.anything())
                .inAdapterView(withId(R.id.filmsLV))
                .atPosition(0)
                .onChildView(withId(R.id.categoryTV)).check(matches(withText(startsWith(rule.getActivity().getString(R.string.saved)))));
    }

    @Test
    public void testClickAndGoDetailFilm() throws IOException, InterruptedException {
        Thread.sleep(3000);
        onData(CoreMatchers.anything())
                .inAdapterView(withId(R.id.filmsLV))
                .atPosition(1)
                .onChildView(withId(R.id.titleTV)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.overviewTitleTV)).check(matches(isDisplayed()));
    }

    @Test
    public void testDetailSave() throws IOException, InterruptedException {
        Thread.sleep(3000);
        onData(CoreMatchers.anything())
                .inAdapterView(withId(R.id.filmsLV))
                .atPosition(1)
                .onChildView(withId(R.id.titleTV)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.plusFAB)).perform(click());
        onView(withText(R.string.saved_film)).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testShowSavedAndNotSaved() throws Exception {
        onView(withId(R.id.menuIB))
                .perform(click());
        onView(withId(R.id.nameTV)).check(matches(isDisplayed()));
    }

}