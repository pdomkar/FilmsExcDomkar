package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Petr on 4. 11. 2016.
 */

public class Director implements Parcelable {
    private Long filmId;
    private String name;

    public Director() {

    }

    public Director(Long filmIId, String name) {
        this.filmId = filmIId;
        this.name = name;
    }

    public Long getFilmId() {
        return filmId;
    }

    public void setFilmId(Long id) {
        this.filmId = filmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.filmId);
        dest.writeString(this.name);
    }


    protected Director(Parcel in) {
        this.filmId = in.readLong();
        this.name = in.readString();
    }

    public static final Creator<Director> CREATOR = new Creator<Director>() {
        @Override
        public Director createFromParcel(Parcel source) {
            return new Director(source);
        }

        @Override
        public Director[] newArray(int size) {
            return new Director[size];
        }
    };
}
