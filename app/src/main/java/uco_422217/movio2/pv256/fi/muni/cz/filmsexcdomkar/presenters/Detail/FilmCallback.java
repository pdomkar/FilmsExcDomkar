package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.BuildConfig;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmDeleteLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmFindLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Crew;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Director;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 17. 12. 2016.
 */

public class FilmCallback implements LoaderManager.LoaderCallbacks<List<Film>> {
    public static final String TAG = FilmCallback.class.getSimpleName();
    private Context mContext;
    private FilmDetailFragment thisFr;
    private LoaderManager loaderManager;
    private Film mFilm;
    public FilmCallback(Context context, LoaderManager loaderManager, Film film, FilmDetailFragment thisFr) {
        mContext = context;
        this.loaderManager = loaderManager;
        this.mFilm = film;
        this.thisFr = thisFr;

    }

    @Override
    public Loader<List<Film>> onCreateLoader(int id, Bundle args) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
        }
        switch (id) {
            case Consts.LOADER_FILM_FIND_ID:
                return new FilmFindLoader(mContext, args.getLong(Consts.DETAIL_ID, 0));
            case Consts.LOADER_FILM_CREATE_ID:
                return new FilmCreateLoader(mContext, (Film) args.getParcelable(Consts.DETAIL_FILM));
            case Consts.LOADER_FILM_DELETE_ID:
                return new FilmDeleteLoader(mContext, (Film) args.getParcelable(Consts.DETAIL_FILM));
            default:
                throw new UnsupportedOperationException("Not know loader id");
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Film>> loader, List<Film> data) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
        }
        switch (loader.getId()) {
            case Consts.LOADER_FILM_FIND_ID:
                Bundle args = new Bundle();
                args.putLong(Consts.DETAIL_ID, mFilm.getId());
                args.putParcelable(Consts.DETAIL_FILM, mFilm);
                String name = "";
                if (mFilm.getCredits() != null) {
                    name = getDirectorNameFromCrew(mFilm.getCredits().getCrew());
                }
                Director director = new Director(mFilm.getId(), name);
                args.putParcelable(Consts.DETAIL_DIRECTOR, director);
                if (mFilm.getCredits() != null) {
                    args.putParcelableArray(Consts.DETAIL_CAST, mFilm.getCredits().getCast());
                }

                if (data.size() == 0) {
                    loaderManager.initLoader(Consts.LOADER_FILM_CREATE_ID, args, FilmCallback.this).forceLoad();
                    loaderManager.initLoader(Consts.LOADER_DIRECTOR_CREATE_ID, args, new DirectorCallback(mContext, thisFr)).forceLoad();
                    loaderManager.initLoader(Consts.LOADER_CAST_CREATE_ID, args, new CastCallback(mContext, thisFr)).forceLoad();
                } else {
                    loaderManager.initLoader(Consts.LOADER_FILM_DELETE_ID, args, FilmCallback.this).forceLoad();
                    loaderManager.initLoader(Consts.LOADER_DIRECTOR_DELETE_ID, args, new DirectorCallback(mContext, thisFr)).forceLoad();
                    loaderManager.initLoader(Consts.LOADER_CAST_DELETE_ID, args, new CastCallback(mContext, thisFr)).forceLoad();
                }
                break;
            case Consts.LOADER_FILM_CREATE_ID:
                thisFr.changeFab(true);
                break;
            case Consts.LOADER_FILM_DELETE_ID:
                thisFr.changeFab(false);
                break;
            default:
                throw new UnsupportedOperationException("Not know loader id");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Film>> loader) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onLoadReset() called! +++");
        }
    }

    private String getDirectorNameFromCrew(Crew[] crews) {
        for (Crew crew : crews) {
            if (crew.getJob().equals("Director")) {
                return crew.getName();
            }
        }
        return "";
    }
}