package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class Genre {
    private String mName;
    private boolean mShow;

    public Genre(String name, boolean show) {
        mName = name;
        mShow = show;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isShow() {
        return mShow;
    }

    public void setShow(boolean show) {
        mShow = show;
    }

}
