package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters.FilmAdapter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.OnFilmSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmListService;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmsListFragment extends Fragment {
    private static final String TAG = FilmsListFragment.class.getName();
    private static final String SELECTED_KEY = "selected_position";
    public static final String ACTION_SEND_RESULTS = "SEND_RESULTS";
    public static final String ACTION_INTERNET_CHANGE = "INTERNET_CHANGE";

    private int mPosition = ListView.INVALID_POSITION;
    private OnFilmSelectListener mListener;
    private Context mContext;
    private FilmAdapter filmAdapter;
    private ListView mFilmsLV;
    private TextView mEmptyTV;
    private LocalBroadcastManager mBroadcastManager;
    private ArrayList<Object> mAdapterArrayList;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Intent intent = new Intent(getActivity(), DownloadFilmListService.class);
        getActivity().startService(intent);
        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
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
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        mBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_SEND_RESULTS));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
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
        mAdapterArrayList.add(title);
        mAdapterArrayList.addAll(filmArrayList);
        filmAdapter.setList(mAdapterArrayList);
        mFilmsLV.setAdapter(filmAdapter);
        if (mAdapterArrayList.size() == 0) {
            if (!Connectivity.isConnected(getActivity().getApplicationContext())) {
                mEmptyTV.setText("Žádné připojení");
            } else {
                mEmptyTV.setText("Žádná data");
            }
        }
    }
}
