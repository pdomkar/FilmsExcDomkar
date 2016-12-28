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
import java.util.Calendar;
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
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmsContract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.OnFilmSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmsGenresBlock;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List.GenreCallback;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List.ListPresenter;

import static android.R.attr.focusable;
import static android.R.attr.key;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmsListFragment extends Fragment implements ContentObserverGenreCallback, FilmsContract.ListView {
    private static final String TAG = FilmsListFragment.class.getName();
    private static final String SELECTED_KEY = "selected_position";
    public static final String ACTION_SEND_RESULTS = "SEND_RESULTS";
    private static final String FILM_API_LIST = "film_api_list";
    private static final String TITLE_FILMS = "title_films";
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
    private ListPresenter mListPresenter;


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mListPresenter = new ListPresenter(this.getView(), getActivity().getApplicationContext(), getLoaderManager(), this, null);
        mListPresenter.loadFilmsApi(0);

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
                        mListPresenter.loadFilmsDb();
                    } else {
                        mAdapterArrayList.clear();
                        filmAdapter.setList(mAdapterArrayList);
                        mFilmsLV.setAdapter(filmAdapter);
                        if (!Connectivity.isConnected(getActivity().getApplicationContext())) {
                            mEmptyTV.setText("Žádné připojení");
                        } else {
                            mListPresenter.loadFilmsApi(0);
                            mEmptyTV.setText("Načítání dat . . .");
                        }
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
        mBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(Consts.ACTION_INTERNET_CHANGE));
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
            int resultCode = intent.getIntExtra(Consts.RESULT_CODE, Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK && intent.getAction().equals(FilmsListFragment.ACTION_SEND_RESULTS)) {
                ArrayList<Film> data = intent.getParcelableArrayListExtra(Consts.RESULT_VALUE);
                String title = intent.getStringExtra(Consts.RESULT_VALUE_TITLE);
                setList(data, title);
                Log.i("y", "a");
            } else if (intent.getAction().equals(Consts.ACTION_INTERNET_CHANGE)) {
                mListPresenter.loadFilmsApi(0);
            }
        }
    };

    public void setList(ArrayList<Film> filmArrayList, String title) {
        Film[] films = new Film[filmArrayList.size()];
        int i = 0;
        for (Film film : filmArrayList) {
            films[i++] = film;
        }

        mListPresenter.doFilteringFilmsByGenres(films, title);
    }

    private Boolean isAnyValueInArray(int[] values, List<Integer> array) {
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
        mListPresenter.loadFilmsApi(0);
    }

    @Override
    public void setFilmsDb(List<Film> data) {
        mAdapterArrayList.clear();
        mAdapterArrayList.add("Uložené");
        mAdapterArrayList.addAll(data);
        filmAdapter.setList(mAdapterArrayList);
        mFilmsLV.setAdapter(filmAdapter);
    }

    @Override
    public void setFilteredFilmsByGenres(FilmsGenresBlock filmsGenresBlock) {
        if (filmsGenresBlock != null) {
            List<Film> films = filmsGenresBlock.getFilms();
            List<Genre> genresShow = filmsGenresBlock.getGenresShow();
            String title = filmsGenresBlock.getTitle();
            Log.i("c", "ccccc");

            if (title.equals(Consts.IN_THEATRE)) {
                //clear
                mAdapterArrayList.clear();
            }
            if (title.equals(Consts.POPULAR_IN_YEAR + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {
                boolean remove = false;
                for(int i = 0; i<mAdapterArrayList.size(); i++) {
                    if(!(mAdapterArrayList.get(i) instanceof Film) && mAdapterArrayList.get(i).equals(Consts.POPULAR_IN_YEAR + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {
                        remove = true;
                    }
                    if(remove) {
                        mAdapterArrayList.remove(i);
                    }
                }
            }

            ArrayList<Integer> genresIdShow = new ArrayList<>();
            for (Genre genre : genresShow) {
                genresIdShow.add((int) (long) genre.getId());
            }
            Log.i("genres", genresIdShow.toString());
            ArrayList<Film> filmsToShow = new ArrayList<>();
            for (Film film : films) {
                Log.i("sf", Arrays.toString(film.getGenres()));
                if (isAnyValueInArray(film.getGenres(), genresIdShow)) {
                    filmsToShow.add(film);
                }
            }
            Log.i("sfdsdff", films.size() + " " + filmsToShow.size());
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

            if (title.equals(Consts.IN_THEATRE)) {
                mListPresenter.loadFilmsApi(1);
            }

        } else {
            if (!Connectivity.isConnected(getActivity().getApplicationContext())) {
                mEmptyTV.setText("Žádné připojení");
            } else {
                mEmptyTV.setText("Žádná data");
            }
        }
    }
}
