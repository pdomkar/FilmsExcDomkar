package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.DirectorCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.DirectorDeleteLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.DirectorFindLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Director;

/**
 * Created by Petr on 17. 12. 2016.
 */

public class DirectorCallback implements LoaderManager.LoaderCallbacks<List<Director>> {
    public static final String TAG = DirectorCallback.class.getSimpleName();
    Context mContext;
    FilmDetailFragment thisFr;

    public DirectorCallback(Context context, FilmDetailFragment thisFr) {
        mContext = context;
        this.thisFr = thisFr;
    }

    @Override
    public Loader<List<Director>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "+++ onCreateLoader() called! +++");
        switch (id) {
            case Consts.LOADER_DIRECTOR_FIND_ID:
                return new DirectorFindLoader(mContext, args.getLong(Consts.DETAIL_ID, 0));
            case Consts.LOADER_DIRECTOR_CREATE_ID:
                return new DirectorCreateLoader(mContext, (Director) args.getParcelable(Consts.DETAIL_DIRECTOR));
            case Consts.LOADER_DIRECTOR_DELETE_ID:
                return new DirectorDeleteLoader(mContext, (Director) args.getParcelable(Consts.DETAIL_DIRECTOR));
            default:
                throw new UnsupportedOperationException("Not know loader id");
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Director>> loader, List<Director> data) {
        Log.i(TAG, "+++ onLoadFinished() called! +++");
        switch (loader.getId()) {
            case Consts.LOADER_DIRECTOR_FIND_ID:
                if (data.size() > 0) {
                    thisFr.changeDirectorTV(data.get(0).getName());
                }
                break;
            case Consts.LOADER_DIRECTOR_CREATE_ID:
                break;
            case Consts.LOADER_DIRECTOR_DELETE_ID:
                break;
            default:
                throw new UnsupportedOperationException("Not know loader id");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Director>> loader) {
        Log.i(TAG, "+++ onLoadReset() called! +++");

    }
}
