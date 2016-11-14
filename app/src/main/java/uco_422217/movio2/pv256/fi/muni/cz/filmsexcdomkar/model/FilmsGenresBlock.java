package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import java.util.List;

/**
 * Created by Petr on 5. 11. 2016.
 */

public class FilmsGenresBlock {
    private List<Genre> genresShow;
    private List<Film> films;
    private String title;

    public FilmsGenresBlock(List<Genre> genresShow, List<Film> films, String title) {
        this.genresShow = genresShow;
        this.films = films;
        this.title = title;
    }

    public List<Genre> getGenresShow() {
        return genresShow;
    }

    public void setGenresShow(List<Genre> genresShow) {
        this.genresShow = genresShow;
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
