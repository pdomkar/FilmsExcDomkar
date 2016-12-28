package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.MainActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreFindAllLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreFindByShowLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreUpdateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmsGenresBlock;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadGenreListService;

/**
 * Created by Petr on 17. 12. 2016.
 */

public class GenresCallback implements LoaderManager.LoaderCallbacks<List<Genre>> {
    public static final String TAG = GenresCallback.class.getSimpleName();
    private Context mContext;
    private LoaderManager loaderManager;
    private MainActivity activity;

    public GenresCallback(Context context, LoaderManager loaderManager, MainActivity activity) {
        mContext = context;
        this.loaderManager = loaderManager;
        this.activity = activity;
    }

    @Override
    public Loader<List<Genre>> onCreateLoader(int id, Bundle args) {
        Log.i("MainActivity", "+++ onCreateLoader() called! +++");
        switch (id) {
            case Consts.LOADER_GENRE_FIND_ALL_ID:
                return new GenreFindAllLoader(mContext);
            case Consts.LOADER_GENRE_FIND_ALL_LIST_ID:
                return new GenreFindAllLoader(mContext);
            case Consts.LOADER_GENRE_CREATE_ID:
                return new GenreCreateLoader(mContext, (Genre[]) args.getParcelableArray(Consts.GENRES_DB_LIST));
            case Consts.LOADER_GENRE_UPDATE_ID:
                return new GenreUpdateLoader(mContext, (Genre) args.getParcelable(Consts.GENRE_DETAIL));
            default:
                throw new UnsupportedOperationException("Not know loader id");
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Genre>> loader, List<Genre> data) {
        Log.i("MainActivity", "+++ onLoadFinished() called! +++");
        switch (loader.getId()) {
            case Consts.LOADER_GENRE_FIND_ALL_ID:
                if (data.size() == 0) {
                    //nacist
                    Intent intent = new Intent(mContext, DownloadGenreListService.class);
                    mContext.startService(intent);
                } else {
                    loaderManager.initLoader(Consts.LOADER_GENRE_FIND_ALL_LIST_ID, null, GenresCallback.this).forceLoad();
                }
                break;
            case Consts.LOADER_GENRE_FIND_ALL_LIST_ID:
                //vypsat
                activity.setGenreList(data);
                break;
            case Consts.LOADER_GENRE_FIND_SHOW_ID:
                break;
            case Consts.LOADER_GENRE_FIND_ID:
                break;
            case Consts.LOADER_GENRE_CREATE_ID:
                loaderManager.initLoader(Consts.LOADER_GENRE_FIND_ALL_LIST_ID, null, GenresCallback.this).forceLoad();
                break;
            case Consts.LOADER_GENRE_UPDATE_ID:
                Log.i("q", "q");
                break;
            default:
                throw new UnsupportedOperationException("Not know loader id");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Genre>> loader) {
        Log.i("MainActivity", "+++ onLoadReset() called! +++");

    }
}