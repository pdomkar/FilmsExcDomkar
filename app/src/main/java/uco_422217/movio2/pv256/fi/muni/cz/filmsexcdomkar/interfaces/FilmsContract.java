package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces;

import android.support.annotation.NonNull;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;

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

    }

    interface DetailListeners {
        void onClickSaved(@NonNull Long filmId);
    }
}
