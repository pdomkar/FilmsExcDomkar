package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters.DrawerNavigationAdapter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmFindAllLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreFindAllLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreUpdateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.OnFilmSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.OnGenreSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmListService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadGenreListService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.sync.RefreshDataDb;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.sync.UpdaterSyncAdapter;

public class MainActivity extends AppCompatActivity implements OnFilmSelectListener, OnGenreSelectListener {
    private boolean mTwoPane;
    private ListView genresLV;
    private TextView mEmptyGenresTV;
    private DrawerNavigationAdapter mDrawerNavigationAdapter;
    private LocalBroadcastManager mBroadcastManager;
    public static final String ACTION_SEND_RESULTS_GENRES = "SEND_RESULTS_GENRES";
    public static final String ACTION_INTERNET_CHANGE = "INTERNET_CHANGE";
    private static final int LOADER_GENRE_FIND_ID = 1;
    private static final int LOADER_GENRE_FIND_ALL_ID = 2;
    private static final int LOADER_GENRE_FIND_ALL_LIST_ID = 3;
    private static final int LOADER_GENRE_FIND_SHOW_ID = 4;
    private static final int LOADER_GENRE_CREATE_ID = 5;
    private static final int LOADER_GENRE_UPDATE_ID = 6;
    private static final String GENRES_DB_LIST = "genres_db_list";
    private static final String GENRE_DETAIL = "genre_detail";
    private Toolbar toolbar;
    public static ArrayList<Genre> mGenreList = new ArrayList<Genre>(){{
        add(new Genre(0L, "Zobrazované žánry", false));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        mDrawerNavigationAdapter = new DrawerNavigationAdapter(mGenreList, getApplicationContext(), this);
        genresLV.setAdapter(mDrawerNavigationAdapter);

        getSupportLoaderManager().initLoader(LOADER_GENRE_FIND_ALL_ID, null, new GenreCallback(getApplicationContext())).forceLoad();
    }


    public Toolbar getToolbar() {
        return toolbar;
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

    @Override
    public void onGenreClick(Genre genre) {
        Bundle args = new Bundle();
        args.putParcelable(GENRE_DETAIL, genre);
        if (getSupportLoaderManager().getLoader(LOADER_GENRE_UPDATE_ID) != null) {
            getSupportLoaderManager().restartLoader(LOADER_GENRE_UPDATE_ID, args, new GenreCallback(getApplicationContext())).forceLoad();
        } else {
            getSupportLoaderManager().initLoader(LOADER_GENRE_UPDATE_ID, args, new GenreCallback(getApplicationContext())).forceLoad();
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(DownloadGenreListService.RESULT_CODE, Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK && intent.getAction().equals(MainActivity.ACTION_SEND_RESULTS_GENRES)) {
                ArrayList<Genre> data = intent.getParcelableArrayListExtra(DownloadGenreListService.RESULT_VALUE);
                saveListGenre(data);
            } else if (intent.getAction().equals(FilmsListFragment.ACTION_INTERNET_CHANGE)) {
                intent = new Intent(context, DownloadGenreListService.class);
                context.startService(intent);
            }
        }
    };

    private void saveListGenre(ArrayList<Genre> list) {
        Genre[] genres = new Genre[list.size()];
        int i = 0;
        for (Genre genre : list) {
            genre.setShow(true);
            genres[i++] = genre;
        }

        Bundle args = new Bundle();
        args.putParcelableArray(GENRES_DB_LIST, genres);

        getSupportLoaderManager().initLoader(LOADER_GENRE_CREATE_ID, args, new GenreCallback(getApplicationContext())).forceLoad();
    }

    public class GenreCallback implements LoaderManager.LoaderCallbacks<List<Genre>> {
        Context mContext;

        public GenreCallback(Context context) {
            mContext = context;
        }

        @Override
        public Loader<List<Genre>> onCreateLoader(int id, Bundle args) {
            Log.i("MainActivity", "+++ onCreateLoader() called! +++");
            switch (id) {
                case LOADER_GENRE_FIND_ALL_ID:
                    return new GenreFindAllLoader(mContext);
                case LOADER_GENRE_FIND_ALL_LIST_ID:
                    return new GenreFindAllLoader(mContext);
                case LOADER_GENRE_CREATE_ID:
                    return new GenreCreateLoader(mContext, (Genre[]) args.getParcelableArray(GENRES_DB_LIST));
                case LOADER_GENRE_UPDATE_ID:
                    return new GenreUpdateLoader(mContext, (Genre) args.getParcelable(GENRE_DETAIL));
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoadFinished(Loader<List<Genre>> loader, List<Genre> data) {
            Log.i("MainActivity", "+++ onLoadFinished() called! +++");
            switch (loader.getId()) {
                case LOADER_GENRE_FIND_ALL_ID:
                    if (data.size() == 0) {
                        //nacist
                        Intent intent = new Intent(mContext, DownloadGenreListService.class);
                        mContext.startService(intent);
                    } else {
                        getSupportLoaderManager().initLoader(LOADER_GENRE_FIND_ALL_LIST_ID, null, new GenreCallback(getApplicationContext())).forceLoad();
                    }
                    break;
                case LOADER_GENRE_FIND_ALL_LIST_ID:
                    //vypsat
                    mGenreList = new ArrayList<>();
                    mGenreList.add(0, new Genre(0L, "Zobrazené žánry", false));
                    mGenreList.addAll(data);
                    mDrawerNavigationAdapter.setList(mGenreList);
                    genresLV.setAdapter(mDrawerNavigationAdapter);

                    if (data.size() == 0) {
                        if (!Connectivity.isConnected(getApplicationContext())) {
                            mEmptyGenresTV.setText("Žádné připojení");
                        } else {
                            mEmptyGenresTV.setText("Žádná data");
                        }
                    }
                    break;
                case LOADER_GENRE_FIND_SHOW_ID:

                    break;
                case LOADER_GENRE_FIND_ID:

                    break;
                case LOADER_GENRE_CREATE_ID:
                    getSupportLoaderManager().initLoader(LOADER_GENRE_FIND_ALL_LIST_ID, null, new GenreCallback(getApplicationContext())).forceLoad();
                    break;
                case LOADER_GENRE_UPDATE_ID:

                    break;
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Genre>> loader) {
            Log.i("MainActivity", "+++ onLoadReset() called! +++");

        }
    }

}
