package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters.FilmAdapter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmCotract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmProvider;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.MyGenreObserver;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmFindAllLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmFindLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.GenreFindByShowLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.ContentObserverGenreCallback;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.OnFilmSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmsGenresBlock;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmListService;

import static android.R.attr.focusable;
import static android.R.attr.key;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmsListFragment extends Fragment implements ContentObserverGenreCallback {
    private static final String TAG = FilmsListFragment.class.getName();
    private static final String SELECTED_KEY = "selected_position";
    public static final String ACTION_SEND_RESULTS = "SEND_RESULTS";
    public static final String ACTION_INTERNET_CHANGE = "INTERNET_CHANGE";
    private static final String FILM_API_LIST = "film_api_list";
    private static final String TITLE_FILMS = "title_films";
    private static final int LOADER_FILM_FIND_ALL_ID = 1;
    private static final int LOADER_GENRE_FIND_SHOW_ID = 2;
    private int mPosition = ListView.INVALID_POSITION;
    private OnFilmSelectListener mListener;
    private Context mContext;
    private FilmAdapter filmAdapter;
    private ListView mFilmsLV;
    private TextView mEmptyTV;
    private LocalBroadcastManager mBroadcastManager;
    private ArrayList<Object> mAdapterArrayList;
    private MyGenreObserver myGenreObserver;
    private FilmManager mFilmManager;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Intent intent = new Intent(getActivity(), DownloadFilmListService.class);
        intent.putExtra("order", 0);
        getActivity().startService(intent);
        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        mFilmManager = new FilmManager(getActivity().getApplicationContext());
        try {
            mListener = (OnFilmSelectListener) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, "Activity must implement OnMovieSelectListener", e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_INTERNET_CHANGE));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_list_layout, container, false);
        mFilmsLV = (ListView) view.findViewById(R.id.filmsLV);
        mFilmsLV.setEmptyView(view.findViewById(R.id.empty_list_item));
        mEmptyTV = (TextView) view.findViewById(R.id.empty_list_item);

        mAdapterArrayList = new ArrayList<>();
        if(!Connectivity.isConnected(getActivity().getApplicationContext())) {
            mEmptyTV.setText("Žádné připojení");
        } else {
            mEmptyTV.setText("Načítání dat . . .");
        }

        filmAdapter = new FilmAdapter(mAdapterArrayList, getContext());
        mFilmsLV.setAdapter(filmAdapter);

        mFilmsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                mListener.onFilmSelect(filmAdapter.getItem(position));
            }
        });

        mFilmsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(filmAdapter.getItem(position) instanceof Film) {
                    Film f = (Film) filmAdapter.getItem(position);
                    Toast.makeText(mContext, f.getTitle(), Toast.LENGTH_SHORT)
                            .show();
                }
                return true;
            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            if (mPosition != ListView.INVALID_POSITION) {
                mFilmsLV.smoothScrollToPosition(mPosition);
            }
        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        if (toolbar != null) {
            Switch savedS = (Switch) ((MainActivity) getActivity()).getToolbar().findViewById(R.id.savedS);
            savedS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        getLoaderManager().initLoader(LOADER_FILM_FIND_ALL_ID, null, new FilmCallback(getActivity().getApplicationContext())).forceLoad();
                    } else {
                        mAdapterArrayList.clear();
                        filmAdapter.setList(mAdapterArrayList);
                        mFilmsLV.setAdapter(filmAdapter);
                        Intent intent = new Intent(getActivity(), DownloadFilmListService.class);
                        intent.putExtra("order", 0);
                        getActivity().startService(intent);
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        if (myGenreObserver == null) {
            myGenreObserver = new MyGenreObserver(this);
        }

        // register ContentObserver in onResume
        getActivity().getContentResolver().
                registerContentObserver(
                        FilmCotract.GenreEntry.CONTENT_URI,
                        true,
                        myGenreObserver);

        mBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_SEND_RESULTS));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        getActivity().getContentResolver().unregisterContentObserver(myGenreObserver);
        mBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(DownloadFilmListService.RESULT_CODE, Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK && intent.getAction().equals(FilmsListFragment.ACTION_SEND_RESULTS)) {
                ArrayList<Film> data = intent.getParcelableArrayListExtra(DownloadFilmListService.RESULT_VALUE);
                String title = intent.getStringExtra(DownloadFilmListService.RESULT_VALUE_TITLE);
                setList(data, title);
            } else if (intent.getAction().equals(FilmsListFragment.ACTION_INTERNET_CHANGE)) {
                intent = new Intent(getActivity(), DownloadFilmListService.class);
                getActivity().startService(intent);
            }
        }
    };

    public void setList(ArrayList<Film> filmArrayList, String title) {
        Film[] films = new Film[filmArrayList.size()];
        int i = 0;
        for (Film film : filmArrayList) {
            films[i++] = film;
        }
        Bundle args = new Bundle();
        args.putParcelableArray(FILM_API_LIST, films);
        args.putString(TITLE_FILMS, title);
        if (getLoaderManager().getLoader(LOADER_GENRE_FIND_SHOW_ID) != null) {
            getLoaderManager().restartLoader(LOADER_GENRE_FIND_SHOW_ID, args, new GenreCallback(getActivity().getApplicationContext())).forceLoad();
        } else {
            getLoaderManager().initLoader(LOADER_GENRE_FIND_SHOW_ID, args, new GenreCallback(getActivity().getApplicationContext())).forceLoad();
        }
    }

    private class FilmCallback implements LoaderManager.LoaderCallbacks<List<Film>> {
        Context mContext;

        public FilmCallback(Context context) {
            mContext = context;
        }

        @Override
        public Loader<List<Film>> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
            return new FilmFindAllLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<List<Film>> loader, List<Film> data) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");

            mAdapterArrayList.clear();
            mAdapterArrayList.add("Uložené");
            mAdapterArrayList.addAll(data);
            filmAdapter.setList(mAdapterArrayList);
            mFilmsLV.setAdapter(filmAdapter);
        }

        @Override
        public void onLoaderReset(Loader<List<Film>> loader) {
            Log.i(TAG, "+++ onLoadReset() called! +++");

        }
    }

    private class GenreCallback implements LoaderManager.LoaderCallbacks<FilmsGenresBlock> {
        Context mContext;

        public GenreCallback(Context context) {
            mContext = context;
        }

        @Override
        public Loader<FilmsGenresBlock> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
            return new GenreFindByShowLoader(mContext, true, (Film[]) args.getParcelableArray(FILM_API_LIST), args.getString(TITLE_FILMS));
        }

        @Override
        public void onLoadFinished(Loader<FilmsGenresBlock> loader, FilmsGenresBlock filmsGenresBlock) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");

            if (filmsGenresBlock != null) {
                List<Film> films = filmsGenresBlock.getFilms();
                List<Genre> genresShow = filmsGenresBlock.getGenresShow();
                String title = filmsGenresBlock.getTitle();

                if (title.equals(DownloadFilmListService.IN_THEATRE)) {
                    //clear
                    mAdapterArrayList.clear();
                }

                ArrayList<Integer> genresIdShow = new ArrayList<>();
                for (Genre genre : genresShow) {
                    genresIdShow.add((int) (long) genre.getId());
                }
                ArrayList<Film> filmsToShow = new ArrayList<>();
                for (Film film : films) {
                    if (isAnyValueInAray(film.getGenres(), genresIdShow)) {
                        filmsToShow.add(film);
                    }
                }

                if (filmsToShow.size() > 0) {
                    mAdapterArrayList.add(title);
                    mAdapterArrayList.addAll(filmsToShow);
                }
                filmAdapter.setList(mAdapterArrayList);
                mFilmsLV.setAdapter(filmAdapter);
                if (mAdapterArrayList.size() == 0) {
                    if (!Connectivity.isConnected(getActivity().getApplicationContext())) {
                        mEmptyTV.setText("Žádné připojení");
                    } else {
                        mEmptyTV.setText("Žádná data");
                    }
                }

                if (title.equals(DownloadFilmListService.IN_THEATRE)) {
                    Intent intent = new Intent(getActivity(), DownloadFilmListService.class);
                    intent.putExtra("order", 1);
                    getActivity().startService(intent);
                }

            } else {
                if (!Connectivity.isConnected(getActivity().getApplicationContext())) {
                    mEmptyTV.setText("Žádné připojení");
                } else {
                    mEmptyTV.setText("Žádná data");
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<FilmsGenresBlock> loader) {
            Log.i(TAG, "+++ onLoadReset() called! +++");

        }
    }

    private Boolean isAnyValueInAray(int[] values, List<Integer> array) {
        Boolean result = false;
        for (int i = 0; i < values.length; i++) {
            if (array.contains(values[i])) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void updateFilmsList() {
        Intent intent = new Intent(getActivity(), DownloadFilmListService.class);
        intent.putExtra("order", 0);
        getActivity().startService(intent);
    }
}
