package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Object> mFilmList = new ArrayList<Object>(){{
        add("Most popular movies");
        add(new Film(2362464, "Path", "Pán prstenu", "sdf", 4.6f));
        add(new Film(2362464, "Path", "Pán pruhů", "sdf", 4.5f));
        add(new Film(46362464, "Path", "Pán draků", "sdf", 4.4f));
        add(new Film(26462464, "Path", "Pán lesů", "sdf", 2.6f));
        add(new Film(2362464, "Path", "Pán kamenů", "sdf", 3.6f));
        add("Best movies from 2016");
        add(new Film(2674464, "Path", "Harry poter", "sdf", 4.6f));
        add(new Film(2362464, "Path", "Hyrry troter", "sdf", 4.7f));
        add(new Film(2362464, "Path", "Pán velký", "sdf", 4.3f));
        add(new Film(2362464, "Padth", "Pánd velký", "sddf", 4.3f));
        add(new Film(2362464, "Padtdgh", "Pánd dfgvelký", "sgddf", 4.3f));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("activita", "onCreate");

        //if mobile add fragment
        if (findViewById(R.id.movie_detail_container) != null){
            //if is instanceState save -> nothing
            if (savedInstanceState != null){
                return;
            }

            // Create an Instance of Fragment
            FilmsListFragment filmsListFragment = new FilmsListFragment();
            filmsListFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, filmsListFragment, FilmDetailFragment.TAG)
                    .commit();
        }
    }
}
