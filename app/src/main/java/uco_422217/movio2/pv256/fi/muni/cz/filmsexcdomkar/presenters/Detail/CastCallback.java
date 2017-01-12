package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.BuildConfig;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.CastCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.CastDeleteLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.CastFindLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;

/**
 * Created by Petr on 17. 12. 2016.
 */

class CastCallback implements LoaderManager.LoaderCallbacks<List<Cast>> {
    public static final String TAG = CastCallback.class.getSimpleName();
    private Context mContext;
    private FilmDetailFragment thisFr;

    CastCallback(Context context, FilmDetailFragment thisFr) {
        mContext = context;
        this.thisFr = thisFr;
    }

    @Override
    public Loader<List<Cast>> onCreateLoader(int id, Bundle args) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
        }
        switch (id) {
            case Consts.LOADER_CAST_FIND_ID:
                return new CastFindLoader(mContext, args.getLong(Consts.DETAIL_ID, 0));
            case Consts.LOADER_CAST_CREATE_ID:
                return new CastCreateLoader(mContext, (Cast[]) args.getParcelableArray(Consts.DETAIL_CAST), args.getLong(Consts.DETAIL_ID));
            case Consts.LOADER_CAST_DELETE_ID:
                return new CastDeleteLoader(mContext, args.getLong(Consts.DETAIL_ID));
            default:
                throw new UnsupportedOperationException("Not know loader id");
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Cast>> loader, List<Cast> data) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
        }
        switch (loader.getId()) {
            case Consts.LOADER_CAST_FIND_ID:
                if (data.size() > 0) {
                    thisFr.changeCasts(data.toArray(new Cast[data.size()]));
                }
                break;
            case Consts.LOADER_CAST_CREATE_ID:
                break;
            case Consts.LOADER_CAST_DELETE_ID:
                break;
            default:
                throw new UnsupportedOperationException("Not know loader id");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Cast>> loader) {
        if(BuildConfig.logging) {
            Log.i(TAG, "+++ onLoadReset() called! +++");
        }
    }
}