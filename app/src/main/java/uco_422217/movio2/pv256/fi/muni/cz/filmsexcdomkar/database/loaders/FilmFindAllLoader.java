package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * https://github.com/alexjlockwood/adp-applistloader
 * Created by Petr on 2. 11. 2016.
 */

public class FilmFindAllLoader extends AsyncTaskLoader<List<Film>> {
    private Context mContext;
    private FilmManager mFilmManager;

    public FilmFindAllLoader(Context context) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
    }

    @Override
    public List<Film> loadInBackground() {
        return mFilmManager.findFilms();
    }
}