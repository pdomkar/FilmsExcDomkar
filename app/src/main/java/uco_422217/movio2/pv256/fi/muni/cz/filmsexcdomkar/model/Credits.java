package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Petr on 27. 10. 2016.
 */

public class Credits implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(this.mCast, 0);
        dest.writeParcelableArray(this.mCrew, 0);
    }

    public Credits() {
    }

    protected Credits(Parcel in) {
        this.mCast = (Cast[]) in.readParcelableArray(Cast.class.getClassLoader());
        this.mCrew = (Crew[]) in.readParcelableArray(Crew.class.getClassLoader());
    }

    public static final Creator<Credits> CREATOR = new Creator<Credits>() {
        @Override
        public Credits createFromParcel(Parcel source) {
            return new Credits(source);
        }

        @Override
        public Credits[] newArray(int size) {
            return new Credits[size];
        }
    };
}