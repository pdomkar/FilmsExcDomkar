package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreFindByShowLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmsGenresBlock;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;

/**
 * Created by Petr on 17. 12. 2016.
 */

public class GenreCallback implements LoaderManager.LoaderCallbacks<FilmsGenresBlock> {
    public static final String TAG = GenreCallback.class.getSimpleName();
    private Context mContext;
    private ListPresenter listPresenter;

    public GenreCallback(Context context, ListPresenter listPresenter) {
        mContext = context;
        this.listPresenter = listPresenter;
    }

    @Override
    public Loader<FilmsGenresBlock> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "+++ onCreateLoader() called! +++");
        return new GenreFindByShowLoader(mContext, true, (Film[]) args.getParcelableArray(Consts.FILM_API_LIST), args.getString(Consts.TITLE_FILMS));
    }

    @Override
    public void onLoadFinished(Loader<FilmsGenresBlock> loader, FilmsGenresBlock filmsGenresBlock) {
        Log.i(TAG, "+++ onLoadFinished() called! +++");
        listPresenter.filterFilmsByGenre(filmsGenresBlock);
    }

    @Override
    public void onLoaderReset(Loader<FilmsGenresBlock> loader) {
        Log.i(TAG, "+++ onLoadReset() called! +++");

    }
}