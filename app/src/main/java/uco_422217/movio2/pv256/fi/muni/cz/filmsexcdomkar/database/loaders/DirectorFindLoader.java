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

public class DirectorFindLoader extends AsyncTaskLoader<List<Director>> {
    private Context mContext;
    private FilmManager mFilmManager;
    private Long mId;

    public DirectorFindLoader(Context context, Long id) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
        mId = id;
    }

    @Override
    public List<Director> loadInBackground() {
        if (mId != null && mId != 0) {
            return mFilmManager.findDirectorByFilmId(mId);
        }
        return Collections.emptyList();
    }
}