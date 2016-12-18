package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.view.View;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmsContract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmDetailService;


/**
 * Created by Petr on 16. 12. 2016.
 */

public class DetailPresenter implements FilmsContract.DetailListeners{
    public static final String TAG = uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail.DetailPresenter.class.getSimpleName();
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
        args.putLong(Consts.DETAIL_ID, filmId);
        loaderManager.initLoader(Consts.LOADER_FILM_FIND_ID, args, new FilmCallback(context, loaderManager, mFilm, thisFr)).forceLoad();
    }

    public void loadFilmDetails() {
        Intent intent = new Intent(context, DownloadFilmDetailService.class);
        intent.putExtra(Consts.DETAIL_ID, mFilm.getId());
        context.startService(intent);
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
