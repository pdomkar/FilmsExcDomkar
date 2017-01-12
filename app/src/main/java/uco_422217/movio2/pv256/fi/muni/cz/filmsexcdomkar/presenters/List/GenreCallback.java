package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.BuildConfig;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreFindByShowLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;


/**
 * Created by Petr on 17. 12. 2016.
 */

public class GenreCallback implements LoaderManager.LoaderCallbacks<List<Genre>> {
    public static final String TAG = GenreCallback.class.getSimpleName();
    private Context mContext;
    private ListPresenter listPresenter;

    public GenreCallback(Context context, ListPresenter listPresenter) {
        mContext = context;
        this.listPresenter = listPresenter;
    }

    @Override
    public Loader<List<Genre>> onCreateLoader(int id, Bundle args) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
        }
        return new GenreFindByShowLoader(mContext, true);
    }

    @Override
    public void onLoadFinished(Loader<List<Genre>> loader, List<Genre> genres) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
        }
        listPresenter.downloadFilms(genres);
    }

    @Override
    public void onLoaderReset(Loader<List<Genre>> loader) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onLoadReset() called! +++");
        }
    }
}