package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;

/**
 * Created by Petr on 2. 11. 2016.
 */

public class GenreFindAllLoader extends AsyncTaskLoader<List<Genre>> {
    private Context mContext;
    private FilmManager mFilmManager;

    public GenreFindAllLoader(Context context) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
    }

    @Override
    public List<Genre> loadInBackground() {
        return mFilmManager.findGenres();
    }
}