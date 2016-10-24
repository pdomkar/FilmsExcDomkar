package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters.DrawerNavigationAdapter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane = true;
    ListView genresLV;

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

    public static ArrayList<Genre> mGenreList = new ArrayList<Genre>(){{
        add(new Genre("Akční", true));
        add(new Genre("Dobrudružný", false));
        add(new Genre("romanick", true));
        add(new Genre("komedie", false));
        add(new Genre("dokumenttist", true));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("activita", "onCreate");

        getSupportActionBar().hide();

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ImageButton menuIB = (ImageButton)findViewById(R.id.menuIB);
        menuIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        genresLV = (ListView) findViewById(R.id.genresLV);

        DrawerNavigationAdapter drawerNavigationAdapter = new DrawerNavigationAdapter(mGenreList, getApplicationContext());
        genresLV.setAdapter(drawerNavigationAdapter);

        //if mobile add fragment
        if (findViewById(R.id.fragment_container) != null){
            mTwoPane = false;

            //if is instanceState save -> nothing
            if (savedInstanceState != null){
                return;
            }

            // Create an Instance of Fragment
            FilmsListFragment filmsListFragment = FilmsListFragment.newInstance(mTwoPane);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, filmsListFragment, FilmDetailFragment.TAG)
                    .commit();
        }
    }
}
