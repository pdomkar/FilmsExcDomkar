package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Credits;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Crew;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail.DetailPresenter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List.FilmsCallback;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List.GenreCallback;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List.ListPresenter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class DetailPresenterTest {
    @Mock
    Context mMockContext;
    @Mock
    LoaderManager mLoaderManager;
    @Mock
    FilmDetailFragment mFilmDetailFragment;
    @Mock
    MainActivity mMainActivity;

    private DetailPresenter mDetailPresenter;

    @Before
    public void setUp() throws Exception {
        Film film = new Film(1L, "nadpis", "0000-00-00", "a/a.jpg","a/b.jpg", 5.5f, "aaaa", new int[]{15});
        this.mDetailPresenter = new DetailPresenter(mMockContext, mLoaderManager, film, mFilmDetailFragment);
    }

    @Test
    public void testSetCredits() throws Exception {
        Credits credits = new Credits(new Cast[]{new Cast("str", "name", "path")}, new Crew[]{});
        this.mDetailPresenter.setCreditsFr(credits);
        verify(this.mFilmDetailFragment, times(1)).setFilmCredits(credits);
    }
}