package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Petr on 6. 10. 2016.
 * <p>
 * https://www.themoviedb.org/documentation/api/discover
 * https://www.themoviedb.org/account/domki9/api
 * https://developers.themoviedb.org/3/discover
 */

public class GenreResponse {

    @SerializedName("genres")
    private ArrayList<Genre> genres;

    public GenreResponse() {
        this.genres = new ArrayList<>();
    }

    public GenreResponse(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }
}
