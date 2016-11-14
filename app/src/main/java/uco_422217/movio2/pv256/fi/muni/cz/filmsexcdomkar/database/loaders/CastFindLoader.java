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

public class CastFindLoader extends AsyncTaskLoader<List<Cast>> {
    private Context mContext;
    private FilmManager mFilmManager;
    private Long mCastFilmId;

    public CastFindLoader(Context context, Long castFilmId) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
        mCastFilmId = castFilmId;
    }

    @Override
    public List<Cast> loadInBackground() {
        if (mCastFilmId != null && mCastFilmId != 0) {
            return mFilmManager.findCastByFilmId(mCastFilmId);
        }
        return Collections.emptyList();
    }
}