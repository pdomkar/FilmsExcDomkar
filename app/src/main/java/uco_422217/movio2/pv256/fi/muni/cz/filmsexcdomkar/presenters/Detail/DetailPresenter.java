package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.View;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmRetrofitInterface;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmsContract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Credits;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;


/**
 * Created by Petr on 16. 12. 2016.
 */

public class DetailPresenter implements FilmsContract.DetailListeners{
    public static final String TAG = uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail.DetailPresenter.class.getSimpleName();
    private Context context;
    private LoaderManager loaderManager;
    private Film mFilm;
    private FilmDetailFragment thisFr;
    public DetailPresenter(Context context, LoaderManager loaderManager, Film film, FilmDetailFragment thisFr) {
        this.context = context;
        this.loaderManager = loaderManager;
        this.mFilm = film;
        this.thisFr = thisFr;
    }

    @Override
    public void onClickSaved(@NonNull Long filmId) {
        Bundle args = new Bundle();
        args.putLong(Consts.DETAIL_ID, filmId);
        loaderManager.initLoader(Consts.LOADER_FILM_FIND_ID, args, new FilmCallback(context, loaderManager, mFilm, thisFr)).forceLoad();
    }

    public void loadFilmDetails() {
        if (Connectivity.isConnected(context)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Consts.MOVIE_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FilmRetrofitInterface filmRetrofitInterface = retrofit.create(FilmRetrofitInterface.class);
            filmRetrofitInterface.findFilmCredits(mFilm.getId(), Consts.MOVIE_API_KEY).enqueue(new Callback<Credits>() {
                @Override
                public void onResponse(retrofit2.Call<Credits> call, retrofit2.Response<Credits> response) {
                    if (response.isSuccessful()) {
                        Credits data = response.body();
                        thisFr.setFilmCredits(data);
                    } else {
                        Log.i(TAG, response.code() + "");
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Credits> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });
        }
    }

    public void loadDirectorFromDb() {
        Bundle args = new Bundle();
        args.putLong(Consts.DETAIL_ID, mFilm.getId());
        loaderManager.initLoader(Consts.LOADER_DIRECTOR_FIND_ID, args, new DirectorCallback(context, thisFr)).forceLoad();
    }

    public void loadCastFromDb() {
        Bundle args = new Bundle();
        args.putLong(Consts.DETAIL_ID, mFilm.getId());
        loaderManager.initLoader(Consts.LOADER_CAST_FIND_ID, args, new CastCallback(context, thisFr)).forceLoad();
    }


}
