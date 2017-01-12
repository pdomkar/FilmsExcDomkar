package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces;

import android.support.annotation.NonNull;

import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmsGenresBlock;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;

/**
 * Created by Petr on 16. 12. 2016.
 */

public interface FilmsContract {
    interface DetailView {
        void changeFab(Boolean showTrash);
        void changeDirectorTV(String name);
        void changeCasts(Cast[] casts);
    }

    interface ListView {
        void setFilmsDb(List<Film> data);
        void setAdapterList(List<Object> data);
        void setEmptyAdapter();
    }

    interface GenreView {
        void setGenreList(List<Genre> data);
    }

    interface DetailListeners {
        void onClickSaved(@NonNull Long filmId);
    }
}
