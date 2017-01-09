package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.BuildConfig;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;

/**
 * https://github.com/alexjlockwood/adp-applistloader
 * Created by Petr on 2. 11. 2016.
 */

public class GenreUpdateLoader extends AsyncTaskLoader<List<Genre>> {
    private Context mContext;
    private FilmManager mFilmManager;
    private Genre mGenre;

    public GenreUpdateLoader(Context context, Genre genre) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
        mGenre = genre;
    }

    @Override
    public List<Genre> loadInBackground() {
        if (mGenre != null) {
            if(BuildConfig.logging) {
                Log.i("ad", mGenre.getId() + "");
            }
            mFilmManager.updateGenre(mGenre);
        }

        return Collections.emptyList();
    }
}