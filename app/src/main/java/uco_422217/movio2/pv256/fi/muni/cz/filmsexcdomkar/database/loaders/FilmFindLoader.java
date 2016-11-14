package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * https://github.com/alexjlockwood/adp-applistloader
 * Created by Petr on 2. 11. 2016.
 */

public class FilmFindLoader extends AsyncTaskLoader<List<Film>> {
    private Context mContext;
    private FilmManager mFilmManager;
    private Long mId;

    public FilmFindLoader(Context context, Long id) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
        mId = id;
    }

    @Override
    public List<Film> loadInBackground() {
        if (mId != null && mId != 0) {
            return mFilmManager.findFilmsById(mId);
        }
        return Collections.emptyList();
    }
}