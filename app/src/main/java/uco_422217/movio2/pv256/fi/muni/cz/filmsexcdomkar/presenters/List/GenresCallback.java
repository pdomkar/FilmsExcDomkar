package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.BuildConfig;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.MainActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreFindAllLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreUpdateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmRetrofitInterface;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.GenreResponse;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;

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
        if(BuildConfig.logging) {
            Log.i("MainActivity", "+++ onCreateLoader() called! +++");
        }
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
        if(BuildConfig.logging) {
            Log.i("MainActivity", "+++ onLoadFinished() called! +++");
        }
        switch (loader.getId()) {
            case Consts.LOADER_GENRE_FIND_ALL_ID:
                if (data.size() == 0) {
                    //nacist
                    if (Connectivity.isConnected(mContext)) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Consts.MOVIE_API_BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        FilmRetrofitInterface filmRetrofitInterface = retrofit.create(FilmRetrofitInterface.class);
                        filmRetrofitInterface.findGenres(Consts.MOVIE_API_KEY).enqueue(new Callback<GenreResponse>() {
                            @Override
                            public void onResponse(retrofit2.Call<GenreResponse> call, retrofit2.Response<GenreResponse> response) {
                                if (response.isSuccessful()) {
                                    activity.saveListGenre(response.body().getGenres());
                                } else {
                                    Log.i(TAG, response.code() + "");
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<GenreResponse> call, Throwable t) {
                                Log.d(TAG, t.getMessage());
                            }
                        });
                    }
                } else {
                    loaderManager.initLoader(Consts.LOADER_GENRE_FIND_ALL_LIST_ID, null, GenresCallback.this).forceLoad();
                }
                break;
            case Consts.LOADER_GENRE_FIND_ALL_LIST_ID:
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
                break;
            default:
                throw new UnsupportedOperationException("Not know loader id");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Genre>> loader) {
        if(BuildConfig.logging) {
            Log.i("MainActivity", "+++ onLoadReset() called! +++");
        }

    }
}