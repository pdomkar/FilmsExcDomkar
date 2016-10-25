package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters.FilmAdapter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.listeners.OnFilmSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadManager;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmsListFragment extends Fragment {
    private static final String TAG = FilmsListFragment.class.getName();
    private static final String SELECTED_KEY = "selected_position";

    private int mPosition = ListView.INVALID_POSITION;
    private OnFilmSelectListener mListener;
    private Context mContext;
    private DownloadManager mDownloadManager;
    private FilmAdapter filmAdapter;
    private ListView mFilmsLV;
    private TextView mEmptyTV;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mDownloadManager = new DownloadManager(this);
        mDownloadManager.startFilmsTask();
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

        ArrayList<Object> list = new ArrayList<>();
        if(!Connectivity.isConnected(getActivity().getApplicationContext())) {
            mEmptyTV.setText("Žádné připojení");
        } else {
            mEmptyTV.setText("Načítání dat . . .");
        }

        filmAdapter = new FilmAdapter(list, getContext());
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




    public void setList(List<Object> list) {
        filmAdapter.setList(list);
        mFilmsLV.setAdapter(filmAdapter);
        if(list.size() == 0) {
            mEmptyTV.setText("Žádná data");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDownloadManager.cancelFilmsTask();
        mDownloadManager = null;
        mListener = null;
    }

}
