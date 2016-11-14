package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders;

import android.content.Context;
import android.database.SQLException;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * https://github.com/alexjlockwood/adp-applistloader
 * Created by Petr on 2. 11. 2016.
 */

public class FilmCreateLoader extends AsyncTaskLoader<List<Film>> {
    private Context mContext;
    private FilmManager mFilmManager;
    private Film mFilm;

    public FilmCreateLoader(Context context, Film film) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
        mFilm = film;
    }

    @Override
    public List<Film> loadInBackground() {
        mFilmManager.createFilm(mFilm);
        return Collections.emptyList();
    }
}