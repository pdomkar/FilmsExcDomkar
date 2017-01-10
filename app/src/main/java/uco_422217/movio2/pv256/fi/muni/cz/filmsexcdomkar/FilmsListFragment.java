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
    private int mPosition = ListView.INVALID_POSITION;
    private OnFilmSelectListener mListener;
    private Context mContext;
    private FilmAdapter filmAdapter;
    private ListView mFilmsLV;
    private TextView mEmptyTV;
    private LocalBroadcastManager mBroadcastManager;
    private MyGenreObserver myGenreObserver;
    private FilmManager mFilmManager;
    private ListPresenter mListPresenter;


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

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

        if(!Connectivity.isConnected(getActivity().getApplicationContext())) {
            mEmptyTV.setText(R.string.no_conntection);
        } else {
            mEmptyTV.setText(R.string.load_data);
        }

        filmAdapter = new FilmAdapter(new ArrayList<>(), getContext());
        mFilmsLV.setAdapter(filmAdapter);

        mListPresenter = new ListPresenter(getActivity().getApplicationContext(), getLoaderManager(), this, null);
        mListPresenter.onLoadFilms();

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
                    filmAdapter.clearList();
                    mFilmsLV.setAdapter(filmAdapter);
                    if (!Connectivity.isConnected(getActivity().getApplicationContext())) {
                        mEmptyTV.setText(R.string.no_conntection);
                    } else {
                        mListPresenter.onLoadFilms();
                        mEmptyTV.setText(R.string.load_data);
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
            if (intent.getAction().equals(Consts.ACTION_INTERNET_CHANGE)) {
                filmAdapter.clearList();
                mListPresenter.onLoadFilms();
            }
        }
    };


    @Override
    public void updateFilmsList() {
//        filmAdapter.clearList();
        Log.i("updateLfilm list", "update");
        mListPresenter.onLoadFilms();
    }

    @Override
    public void setFilmsDb(List<Film> data) {
        List<Object> films = new ArrayList<>();
        films.add(getString(R.string.saved));
        films.addAll(data);
        filmAdapter.clearList();
        filmAdapter.addList(films);
        mFilmsLV.setAdapter(filmAdapter);
    }


    public void setAdapterList(ArrayList<Object> films) {
        if(films.size() == 0 && filmAdapter.getCount() == 0) {
            if (!Connectivity.isConnected(getActivity().getApplicationContext())) {
                mEmptyTV.setText(R.string.no_conntection);
            } else {
                mEmptyTV.setText(R.string.no_data);
            }
        } else {
            filmAdapter.addList(films);
            mFilmsLV.setAdapter(filmAdapter);
            filmAdapter.notifyDataSetChanged();
        }
    }

    public void setEmptyAdapter() {
        if(filmAdapter.getCount() == 0) {
            if (!Connectivity.isConnected(getActivity().getApplicationContext())) {
                mEmptyTV.setText(R.string.no_conntection);
            } else {
                mEmptyTV.setText(R.string.no_data);
            }
        }
    }
}
