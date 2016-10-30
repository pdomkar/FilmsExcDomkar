package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Petr on 27. 10. 2016.
 */

public class Credits {
    @SerializedName("cast")
    private Cast[] mCast;
    @SerializedName("crew")
    private Crew[] mCrew;

    public Credits(Cast[] cast, Crew[] crew) {
        this.mCast = cast;
        this.mCrew = crew;
    }

    public Cast[] getCast() {
        return mCast;
    }

    public void setCast(Cast[] cast) {
        this.mCast = cast;
    }

    public Crew[] getCrew() {
        return mCrew;
    }

    public void setCrew(Crew[] crew) {
        this.mCrew = crew;
    }

    @Override
    public String toString() {
        return super.toString() + "Credits: Cast: " + mCast.toString() + ", Crew: " + mCrew.toString();
    }


}