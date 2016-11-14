package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database;

/**
 * Created by Petr on 14. 11. 2016.
 */

import android.annotation.SuppressLint;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.ContentObserverGenreCallback;

@SuppressLint("NewApi")
public class MyGenreObserver extends ContentObserver {
    ContentObserverGenreCallback mContentObserverGenreCallback;

    public MyGenreObserver(ContentObserverGenreCallback contentObserverGenreCallback) {
        super(null);
        mContentObserverGenreCallback = contentObserverGenreCallback;
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Log.i("change", "CHANGE IN GENR");
        mContentObserverGenreCallback.updateFilmsList();
    }
}

