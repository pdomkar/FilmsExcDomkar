package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_FILM = "extra_film";
    private Toolbar toolbar;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton backIB = (ImageButton) findViewById(R.id.backIB);
        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(savedInstanceState == null){
            Film film = getIntent().getParcelableExtra(EXTRA_FILM);
            FragmentManager fm = getSupportFragmentManager();
            FilmDetailFragment fragment = (FilmDetailFragment) fm.findFragmentById(R.id.film_detail_container);

            if (fragment == null) {
                fragment = FilmDetailFragment.newInstance(film);
                fm.beginTransaction()
                        .add(R.id.film_detail_container, fragment)
                        .commit();
            }
        }
    }


    public Toolbar getToolbar() {
        return toolbar;
    }

}
