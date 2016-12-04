package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

public class MainActivity extends FragmentActivity implements OnFilmSelectListener {
    private boolean mTwoPane;

    public static ArrayList<Film> mFilmList = new ArrayList<Film>(){{
        add(new Film(2362464, "Path", "Pán prstenu", "sdf", 4.6f));
        add(new Film(2362464, "Path", "Pán pruhů", "sdf", 4.5f));
        add(new Film(46362464, "Path", "Pán draků", "sdf", 4.4f));
        add(new Film(26462464, "Path", "Pán lesů", "sdf", 2.6f));
        add(new Film(2362464, "Path", "Pán kamenů", "sdf", 3.6f));
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
        if (findViewById(R.id.fragment_container) != null){

            //if is instanceState save -> nothing
            if (savedInstanceState != null){
                return;
            }

            // Create an Instance of Fragment
            FilmsListFragment filmsListFragment = new FilmsListFragment();
            filmsListFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, filmsListFragment)
                    .commit();
        }
    }

    @Override
    public void OnFilmSelect(int versionNameIndex) {
        FilmDetailFragment filmDetailFragment = (FilmDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.film_detail_fragment);

        if (filmDetailFragment != null){
            //twopanepanel - set Film
                    filmDetailFragment.setFilmDetail(mFilmList.get(versionNameIndex));
        } else {
            FilmDetailFragment newFilmDetailFragment = new FilmDetailFragment();
            Bundle args = new Bundle();
            args.putParcelable(FilmDetailFragment.SELECTED_FILM, mFilmList.get(versionNameIndex));

            newFilmDetailFragment.setArguments(args);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            //change fragment
            fragmentTransaction.replace(R.id.fragment_container, newFilmDetailFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
