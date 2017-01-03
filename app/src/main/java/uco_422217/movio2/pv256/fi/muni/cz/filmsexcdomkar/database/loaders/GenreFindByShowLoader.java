package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmsGenresBlock;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;

/**
 * Created by Petr on 2. 11. 2016.
 */

public class GenreFindByShowLoader extends AsyncTaskLoader<FilmsGenresBlock> {
    private Context mContext;
    private FilmManager mFilmManager;
    private Boolean mShow;
    Film[] mFilmDbList;
    String mTitle;

    public GenreFindByShowLoader(Context context, Boolean show, Film[] filmDbList, String title) {
        super(context);
        mContext = context;
        mFilmManager = new FilmManager(context);
        mShow = show;
        mFilmDbList = filmDbList;
        mTitle = title;
    }

    @Override
    public FilmsGenresBlock loadInBackground() {
        if (mShow != null) {
            return new FilmsGenresBlock(mFilmManager.findGenresByShow(mShow), Arrays.asList(mFilmDbList), mTitle);
        }
        return null;
    }
}