package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;

/**
 * https://github.com/alexjlockwood/adp-applistloader
 * Created by Petr on 2. 11. 2016.
 */

public class GenreCreateLoader extends AsyncTaskLoader<List<Genre>> {
    private Context mContext;
    private FilmManager mFilmManager;
    private Genre[] mGenres;

    public GenreCreateLoader(Context context, Genre[] genres) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
        mGenres = genres;
    }

    @Override
    public List<Genre> loadInBackground() {
        if (mGenres != null && mGenres.length > 0) {
            Log.i("werwrwrwrwrw", mGenres.length + "");
            for (int i = 0; i < mGenres.length; i++) {
                mFilmManager.createGenre(mGenres[i]);
            }
        }

        return Collections.emptyList();
    }
}