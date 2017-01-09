package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.BuildConfig;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmFindAllLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 17. 12. 2016.
 */

public class FilmsCallback implements LoaderManager.LoaderCallbacks<List<Film>> {
    public static final String TAG = FilmsCallback.class.getSimpleName();
    private Context mContext;
    private FilmsListFragment thisFr;
    public FilmsCallback(Context context, FilmsListFragment thisFr) {
        mContext = context;
        this.thisFr = thisFr;
    }

    @Override
    public Loader<List<Film>> onCreateLoader(int id, Bundle args) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
        }
        return new FilmFindAllLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<List<Film>> loader, List<Film> data) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
        }
        thisFr.setFilmsDb(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Film>> loader) {
            if(BuildConfig.logging) {
                Log.i(TAG, "+++ onLoadReset() called! +++");
            }
    }
}