package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Director;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * https://github.com/alexjlockwood/adp-applistloader
 * Created by Petr on 2. 11. 2016.
 */

public class DirectorDeleteLoader extends AsyncTaskLoader<List<Director>> {
    private Context mContext;
    private FilmManager mFilmManager;
    private Director mDirector;

    public DirectorDeleteLoader(Context context, Director director) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
        mDirector = director;
    }

    @Override
    public List<Director> loadInBackground() {
        mFilmManager.deleteDirector(mDirector);
        return Collections.emptyList();
    }
}