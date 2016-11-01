package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters.DrawerNavigationAdapter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_FILM = "extra_film";
    private ListView genresLV;
    private TextView mEmptyGenresTV;
    private DrawerNavigationAdapter mDrawerNavigationAdapter;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();

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

        //navitagion drawer - prepare for next ukol
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ImageButton menuIB = (ImageButton)findViewById(R.id.menuIB);
        menuIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        genresLV = (ListView) findViewById(R.id.genresLV);
        genresLV.setEmptyView(findViewById(R.id.empty_genres_list_item));
        mEmptyGenresTV = (TextView) findViewById(R.id.empty_genres_list_item);

        mDrawerNavigationAdapter = new DrawerNavigationAdapter(MainActivity.mGenreList, getApplicationContext());
        genresLV.setAdapter(mDrawerNavigationAdapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

}
