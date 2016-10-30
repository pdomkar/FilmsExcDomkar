package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters.DrawerNavigationAdapter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.listeners.OnFilmSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmListManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadGenreListManager;

public class MainActivity extends AppCompatActivity implements OnFilmSelectListener {
    private boolean mTwoPane;
    private ListView genresLV;
    private TextView mEmptyGenresTV;
    private DrawerNavigationAdapter mDrawerNavigationAdapter;
    private DownloadGenreListManager mDownloadGenreListManager;

    public static ArrayList<Genre> mGenreList = new ArrayList<Genre>(){{
        add(new Genre(0L, "Zobrazované žánry", false));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        getApplicationContext().registerReceiver(broadcastReceiver, new IntentFilter("INTERNET_CHANGE"));

        //if mobile add fragment
        if (findViewById(R.id.film_detail_container) != null){
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.film_detail_container, new FilmDetailFragment(), FilmDetailFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        //navitagion drawer- prepare for next ukol
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

        mDrawerNavigationAdapter = new DrawerNavigationAdapter(mGenreList, getApplicationContext());
        genresLV.setAdapter(mDrawerNavigationAdapter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mDownloadGenreListManager.cancelGenresTask();
            mDownloadGenreListManager.startGenresTask();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mDownloadGenreListManager = new DownloadGenreListManager(this);
        mDownloadGenreListManager.startGenresTask();
    }

    public void setListGenre(List<Genre> list) {
        for (Genre genre : list) {
            genre.setShow(true);
        }
        mDrawerNavigationAdapter.setList(list);
        genresLV.setAdapter(mDrawerNavigationAdapter);
        if (list.size() == 0) {
            if (!Connectivity.isConnected(getApplicationContext())) {
                mEmptyGenresTV.setText("Žádné připojení");
            } else {
                mEmptyGenresTV.setText("Žádná data");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDownloadGenreListManager.cancelGenresTask();
        mDownloadGenreListManager = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplicationContext().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onFilmSelect(Object film) {
        if(film instanceof Film) {
            if (mTwoPane) {
                FragmentManager fm = getSupportFragmentManager();

                FilmDetailFragment fragment = FilmDetailFragment.newInstance((Film)film);
                fm.beginTransaction()
                        .replace(R.id.film_detail_container, fragment, FilmDetailFragment.TAG)
                        .commit();
            } else {
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_FILM, (Film) film);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onFilmShowTitle(View v) {
        TextView tv = (TextView) v.findViewById(R.id.titleTV);
        tv.setVisibility(View.VISIBLE);
    }
}
