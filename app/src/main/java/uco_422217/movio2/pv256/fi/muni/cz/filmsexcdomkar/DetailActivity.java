package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 21. 10. 2016.
 */

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_FILM = "extra_film";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(savedInstanceState == null){
            Film film = getIntent().getParcelableExtra(EXTRA_FILM);
            FragmentManager fm = getSupportFragmentManager();
            FilmDetailFragment fragment = (FilmDetailFragment) fm.findFragmentById(R.id.movie_detail_container);

            if (fragment == null) {
              //  fragment = FilmDetailFragment.newInstance(film);
                fm.beginTransaction()
                        .add(R.id.movie_detail_container, fragment)
                        .commit();
            }
        }
    }
}
