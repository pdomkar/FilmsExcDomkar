package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Petr on 6. 10. 2016.
 * <p>
 * https://www.themoviedb.org/documentation/api/discover
 * https://www.themoviedb.org/account/domki9/api
 * https://developers.themoviedb.org/3/discover
 */

public class FilmResponse {
    @SerializedName("results")
    private ArrayList<Film> films;

    public FilmResponse() {
        this.films = new ArrayList<>();
    }

    public FilmResponse(ArrayList<Film> films) {
        this.films = films;
    }

    public ArrayList<Film> getFilms() {
        return films;
    }

    public void setFilms(ArrayList<Film> films) {
        this.films = films;
    }
}
