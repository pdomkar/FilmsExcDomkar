package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.view.View;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.MainActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmsContract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmDetailService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmListService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadGenreListService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail.DirectorCallback;


/**
 * Created by Petr on 16. 12. 2016.
 */

public class ListPresenter {
    public static final String TAG = ListPresenter.class.getSimpleName();
    private View view;
    private Context context;
    private LoaderManager loaderManager;
    private FilmsListFragment thisFr;
    private MainActivity activity;
    public ListPresenter(View view, Context context, LoaderManager loaderManager, FilmsListFragment thisFr, MainActivity activity) {
        this.view = view;
        this.context = context;
        this.loaderManager = loaderManager;
        this.thisFr = thisFr;
        this.activity = activity;
    }

    public void loadFilmsApi(int order) {
        Intent intent = new Intent(context, DownloadFilmListService.class);
        intent.putExtra(Consts.ORDER, order);
        context.startService(intent);
    }

    public void loadFilmsDb() {
        loaderManager.initLoader(Consts.LOADER_FILM_FIND_ALL_ID, null, new FilmsCallback(context, thisFr)).forceLoad();
    }

    public void doFilteringFilmsByGenres(Film[] films, String title) {
        Bundle args = new Bundle();
        args.putParcelableArray(Consts.FILM_API_LIST, films);
        args.putString(Consts.TITLE_FILMS, title);
        if (loaderManager.getLoader(Consts.LOADER_GENRE_FIND_SHOW_ID) != null) {
            loaderManager.restartLoader(Consts.LOADER_GENRE_FIND_SHOW_ID, args, new GenreCallback(context, thisFr)).forceLoad();
        } else {
            loaderManager.initLoader(Consts.LOADER_GENRE_FIND_SHOW_ID, args, new GenreCallback(context, thisFr)).forceLoad();
        }
    }

    public void loadFilmGenres() {
        loaderManager.initLoader(Consts.LOADER_GENRE_FIND_ALL_ID, null, new GenresCallback(context, loaderManager, activity)).forceLoad();
    }

    public void loadFilmGenresApi() {
        Intent intent = new Intent(context, DownloadGenreListService.class);
        context.startService(intent);
    }

    public void changeStateGenre(Genre genre) {
        Bundle args = new Bundle();
        args.putParcelable(Consts.GENRE_DETAIL, genre);
        if (loaderManager.getLoader(Consts.LOADER_GENRE_UPDATE_ID) != null) {
            loaderManager.restartLoader(Consts.LOADER_GENRE_UPDATE_ID, args, new GenresCallback(context, loaderManager, activity)).forceLoad();
        } else {
            loaderManager.initLoader(Consts.LOADER_GENRE_UPDATE_ID, args, new GenresCallback(context, loaderManager, activity)).forceLoad();
        }
    }

    public void createGenres(Genre[] genres) {
        Bundle args = new Bundle();
        args.putParcelableArray(Consts.GENRES_DB_LIST, genres);
        loaderManager.initLoader(Consts.LOADER_GENRE_CREATE_ID, args, new GenresCallback(context, loaderManager, activity)).forceLoad();
    }



}
