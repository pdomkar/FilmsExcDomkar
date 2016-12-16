package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.R;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.CastCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.CastDeleteLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.CastFindLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.DirectorCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.DirectorDeleteLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.DirectorFindLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmDeleteLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmFindLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmsContract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Crew;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Director;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

import static uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment.DETAIL_ID;

/**
 * Created by Petr on 16. 12. 2016.
 */

public class DetailPresenter implements FilmsContract.DetailListeners{
    public static final String TAG = DetailPresenter.class.getSimpleName();
    private static final int LOADER_FILM_FIND_ID = 1;
    private static final int LOADER_FILM_CREATE_ID = 2;
    private static final int LOADER_FILM_DELETE_ID = 3;
    private static final int LOADER_DIRECTOR_FIND_ID = 4;
    private static final int LOADER_DIRECTOR_CREATE_ID = 5;
    private static final int LOADER_DIRECTOR_DELETE_ID = 6;
    private static final int LOADER_CAST_FIND_ID = 7;
    private static final int LOADER_CAST_CREATE_ID = 8;
    private static final int LOADER_CAST_DELETE_ID = 9;
    private static final String ARGS_FILM = "args_film";
    public static final String ACTION_SEND_DETAIL_RESULTS = "SEND_DETAIL_RESULTS";
    private static final String IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/w500";
    public static final String DETAIL_ID = "id";
    public static final String DETAIL_FILM = "film";
    public static final String DETAIL_DIRECTOR = "director";
    public static final String DETAIL_CAST = "cast";
    private View view;
    private Context context;
    private LoaderManager loaderManager;
    private Film mFilm;
    private FilmDetailFragment thisFr;
    public DetailPresenter(View view, Context context, LoaderManager loaderManager, Film film, FilmDetailFragment thisFr) {
        this.view = view;
        this.context = context;
        this.loaderManager = loaderManager;
        this.mFilm = film;
        this.thisFr = thisFr;
    }

    @Override
    public void onClickSaved(@NonNull Long filmId) {
        Bundle args = new Bundle();
        args.putLong(DETAIL_ID, filmId);
        loaderManager.initLoader(LOADER_FILM_FIND_ID, args, new FilmCallback(context)).forceLoad();
    }

    private class FilmCallback implements LoaderManager.LoaderCallbacks<List<Film>> {
        Context mContext;
        public FilmCallback(Context context) {
            mContext = context;
        }

        @Override
        public Loader<List<Film>> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
            switch (id) {
                case LOADER_FILM_FIND_ID:
                    return new FilmFindLoader(mContext, args.getLong(DETAIL_ID, 0));
                case LOADER_FILM_CREATE_ID:
                    return new FilmCreateLoader(mContext, (Film) args.getParcelable(DETAIL_FILM));
                case LOADER_FILM_DELETE_ID:
                    return new FilmDeleteLoader(mContext, (Film) args.getParcelable(DETAIL_FILM));
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoadFinished(Loader<List<Film>> loader, List<Film> data) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
            switch (loader.getId()) {
                case LOADER_FILM_FIND_ID:
                    Bundle args = new Bundle();
                    args.putLong(DETAIL_ID, mFilm.getId());
                    args.putParcelable(DETAIL_FILM, mFilm);
                    String name = "";
                    if (mFilm.getCredits() != null) {
                        name = getDirectorNameFromCrew(mFilm.getCredits().getCrew());
                    }
                    Director director = new Director(mFilm.getId(), name);
                    args.putParcelable(DETAIL_DIRECTOR, director);
                    if (mFilm.getCredits() != null) {
                        args.putParcelableArray(DETAIL_CAST, mFilm.getCredits().getCast());
                    }

                    if (data.size() == 0) {
                        loaderManager.initLoader(LOADER_FILM_CREATE_ID, args, FilmCallback.this).forceLoad();
                        loaderManager.initLoader(LOADER_DIRECTOR_CREATE_ID, args, new DirectorCallback(mContext)).forceLoad();
                        loaderManager.initLoader(LOADER_CAST_CREATE_ID, args, new CastCallback(mContext)).forceLoad();
                    } else {
                        loaderManager.initLoader(LOADER_FILM_DELETE_ID, args, FilmCallback.this).forceLoad();
                        loaderManager.initLoader(LOADER_DIRECTOR_DELETE_ID, args, new DirectorCallback(mContext)).forceLoad();
                        loaderManager.initLoader(LOADER_CAST_DELETE_ID, args, new CastCallback(mContext)).forceLoad();
                    }
                    break;
                case LOADER_FILM_CREATE_ID:
                    thisFr.changeFab(true);
                    break;
                case LOADER_FILM_DELETE_ID:
                    thisFr.changeFab(false);
                    break;
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Film>> loader) {
            Log.i(TAG, "+++ onLoadReset() called! +++");

        }
    }

    private class DirectorCallback implements LoaderManager.LoaderCallbacks<List<Director>> {
        Context mContext;

        public DirectorCallback(Context context) {
            mContext = context;
        }

        @Override
        public Loader<List<Director>> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
            switch (id) {
                case LOADER_DIRECTOR_FIND_ID:
                    return new DirectorFindLoader(mContext, args.getLong(DETAIL_ID, 0));
                case LOADER_DIRECTOR_CREATE_ID:
                    return new DirectorCreateLoader(mContext, (Director) args.getParcelable(DETAIL_DIRECTOR));
                case LOADER_DIRECTOR_DELETE_ID:
                    return new DirectorDeleteLoader(mContext, (Director) args.getParcelable(DETAIL_DIRECTOR));
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoadFinished(Loader<List<Director>> loader, List<Director> data) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
            switch (loader.getId()) {
                case LOADER_DIRECTOR_FIND_ID:
                    if (data.size() > 0) {
                        thisFr.changeDirectorTV(data.get(0).getName());
                    }
                    break;
                case LOADER_DIRECTOR_CREATE_ID:
                    break;
                case LOADER_DIRECTOR_DELETE_ID:
                    break;
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Director>> loader) {
            Log.i(TAG, "+++ onLoadReset() called! +++");

        }
    }

    private class CastCallback implements LoaderManager.LoaderCallbacks<List<Cast>> {
        Context mContext;

        public CastCallback(Context context) {
            mContext = context;
        }

        @Override
        public Loader<List<Cast>> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
            switch (id) {
                case LOADER_CAST_FIND_ID:
                    return new CastFindLoader(mContext, args.getLong(DETAIL_ID, 0));
                case LOADER_CAST_CREATE_ID:
                    return new CastCreateLoader(mContext, (Cast[]) args.getParcelableArray(DETAIL_CAST), args.getLong(DETAIL_ID));
                case LOADER_CAST_DELETE_ID:
                    return new CastDeleteLoader(mContext, args.getLong(DETAIL_ID));
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoadFinished(Loader<List<Cast>> loader, List<Cast> data) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
            switch (loader.getId()) {
                case LOADER_CAST_FIND_ID:
                    if (data.size() > 0) {
                        thisFr.changeCasts(data.toArray(new Cast[data.size()]));
                    }
                    break;
                case LOADER_CAST_CREATE_ID:
                    break;
                case LOADER_CAST_DELETE_ID:
                    break;
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Cast>> loader) {
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
