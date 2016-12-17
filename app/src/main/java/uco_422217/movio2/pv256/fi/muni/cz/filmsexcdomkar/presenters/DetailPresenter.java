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

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
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


/**
 * Created by Petr on 16. 12. 2016.
 */

public class DetailPresenter implements FilmsContract.DetailListeners{
    public static final String TAG = DetailPresenter.class.getSimpleName();
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


}
