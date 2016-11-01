package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
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
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.OnFilmSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmListService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadGenreListService;

public class MainActivity extends AppCompatActivity implements OnFilmSelectListener {
    private boolean mTwoPane;
    private ListView genresLV;
    private TextView mEmptyGenresTV;
    private DrawerNavigationAdapter mDrawerNavigationAdapter;
    private LocalBroadcastManager mBroadcastManager;
    public static final String ACTION_SEND_RESULTS_GENRES = "SEND_RESULTS_GENRES";
    public static final String ACTION_INTERNET_CHANGE = "INTERNET_CHANGE";

    public static ArrayList<Genre> mGenreList = new ArrayList<Genre>(){{
        add(new Genre(0L, "Zobrazované žánry", false));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Intent intent = new Intent(this, DownloadGenreListService.class);
        this.startService(intent);
        mBroadcastManager = LocalBroadcastManager.getInstance(this);

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

    @Override
    protected void onStart() {
        super.onStart();
        mBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_SEND_RESULTS_GENRES));
        mBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_INTERNET_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(DownloadGenreListService.RESULT_CODE, Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK && intent.getAction().equals(MainActivity.ACTION_SEND_RESULTS_GENRES)) {
                ArrayList<Genre> data = intent.getParcelableArrayListExtra(DownloadGenreListService.RESULT_VALUE);
                setListGenre(data);
            } else if (intent.getAction().equals(FilmsListFragment.ACTION_INTERNET_CHANGE)) {
                intent = new Intent(context, DownloadGenreListService.class);
                context.startService(intent);
            }
        }
    };

    private void setListGenre(List<Genre> list) {
        mGenreList = new ArrayList<>();
        mGenreList.add(0, new Genre(0L, "Zobrazené žánry", false));
        for (Genre genre : list) {
            genre.setShow(true);
            mGenreList.add(genre);
        }
        mDrawerNavigationAdapter.setList(mGenreList);
        genresLV.setAdapter(mDrawerNavigationAdapter);
        if (list.size() == 0) {
            if (!Connectivity.isConnected(getApplicationContext())) {
                mEmptyGenresTV.setText("Žádné připojení");
            } else {
                mEmptyGenresTV.setText("Žádná data");
            }
        }
    }
}
