package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Director;

/**
 * https://github.com/alexjlockwood/adp-applistloader
 * Created by Petr on 2. 11. 2016.
 */

public class CastCreateLoader extends AsyncTaskLoader<List<Cast>> {
    private Context mContext;
    private FilmManager mFilmManager;
    private Cast[] mCasts;
    private Long mFilmId;

    public CastCreateLoader(Context context, Cast[] casts, Long filmId) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
        mCasts = casts;
        mFilmId = filmId;
    }

    @Override
    public List<Cast> loadInBackground() {
        if (mCasts != null && mCasts.length > 0) {
            for (Cast cast : mCasts) {
                mFilmManager.createCast(cast, mFilmId);
            }
        }

        return Collections.emptyList();
    }
}