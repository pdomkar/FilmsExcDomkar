package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters.DrawerNavigationAdapter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmsContract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.OnFilmSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.OnGenreSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List.ListPresenter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.sync.UpdaterSyncAdapter;

public class MainActivity extends AppCompatActivity implements OnFilmSelectListener, OnGenreSelectListener, FilmsContract.GenreView {
    private boolean mTwoPane;
    private ListView genresLV;
    private TextView mEmptyGenresTV;
    private DrawerNavigationAdapter mDrawerNavigationAdapter;
    private LocalBroadcastManager mBroadcastManager;
    private Toolbar toolbar;
    private ListPresenter mListPresenter;
    public ArrayList<Genre> mGenreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGenreList = new ArrayList<>();
        mGenreList.add(new Genre(0L, getString(R.string.displayed_genres), false));
        UpdaterSyncAdapter.initializeSyncAdapter(this);


        ImageButton pupupMenuIB = (ImageButton)findViewById(R.id.popupMenuIB);
        pupupMenuIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        UpdaterSyncAdapter.syncImmediately(MainActivity.this);
                        return true;
                    }
                });

                popup.show();
            }
        });

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

        mDrawerNavigationAdapter = new DrawerNavigationAdapter(mGenreList, getApplicationContext(), this);
        genresLV.setAdapter(mDrawerNavigationAdapter);

        mListPresenter = new ListPresenter(getApplicationContext(), getSupportLoaderManager(), null, MainActivity.this);
        mListPresenter.loadFilmGenres();
    }


    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(Consts.ACTION_INTERNET_CHANGE));
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

    @Override
    public void onGenreClick(Genre genre) {
        mListPresenter.changeStateGenre(genre);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Consts.ACTION_INTERNET_CHANGE)) {
            mListPresenter.loadFilmGenresApi();
        }
        }
    };

    public void saveListGenre(ArrayList<Genre> list) {
        Genre[] genres = new Genre[list.size()];
        int i = 0;
        for (Genre genre : list) {
            genre.setShow(true);
            genres[i++] = genre;
        }

        mListPresenter.createGenres(genres);
    }

    @Override
    public void setGenreList(List<Genre> data) {
        mGenreList = new ArrayList<>();
        mGenreList.add(0, new Genre(0L, getString(R.string.displayed_genres), false));
        mGenreList.addAll(data);
        mDrawerNavigationAdapter.setList(mGenreList);
        genresLV.setAdapter(mDrawerNavigationAdapter);

        if (data.size() == 0) {
            if (!Connectivity.isConnected(getApplicationContext())) {
                mEmptyGenresTV.setText(R.string.no_conntection);
            } else {
                mEmptyGenresTV.setText(R.string.no_data);
            }
        }
    }
}
