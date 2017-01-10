package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmsContract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail.DetailPresenter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List.FilmsCallback;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List.GenreCallback;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List.ListPresenter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ListPresenterTest {

        @Mock
        Context mMockContext;
        @Mock
        LoaderManager mLoaderManager;
        @Mock
        FilmsListFragment mFilmsListFragment;
        @Mock
        MainActivity mMainActivity;

        private ListPresenter mListPresenter;

        @Before
        public void setUp() throws Exception {
            this.mListPresenter = new ListPresenter(mMockContext, mLoaderManager, mFilmsListFragment, mMainActivity);
                mLoaderManager.initLoader(1, null, new GenreCallback(mMockContext, mListPresenter));
        }

        @Test
        public void testSetAdapterEmpty() throws Exception {
            this.mListPresenter.setAdapterFr(null);
            verify(this.mFilmsListFragment, times(1)).setEmptyAdapter();
        }

        @Test
        public void testSetAdapter() throws Exception {
                ArrayList<Object> arr = new ArrayList<>();
                arr.add("Populární");
                this.mListPresenter.setAdapterFr(arr);
                verify(this.mFilmsListFragment, times(1)).setAdapterList(arr);
        }



}