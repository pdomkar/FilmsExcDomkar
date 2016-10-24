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

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters.FilmAdapter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.listeners.OnFilmSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmsListFragment extends Fragment {
    private static final String TAG = FilmsListFragment.class.getName();
    private static final String SELECTED_KEY = "selected_position";

    private int mPosition = ListView.INVALID_POSITION;
    private OnFilmSelectListener mListener;
    private Context mContext;
    private ListView mListView;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFilmSelectListener) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, "Activity must implement OnMovieSelectListener", e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        ListView filmsLV = (ListView) view.findViewById(R.id.filmsLV);
        filmsLV.setEmptyView(view.findViewById(R.id.empty_list_item));
        TextView emptyTV = (TextView) view.findViewById(R.id.empty_list_item);

        ArrayList<Object> list;

             list = MainActivity.mFilmList;
            if(list.size() == 0) {
                emptyTV.setText("Žádné data");
            }


        FilmAdapter filmAdapter = new FilmAdapter(list, getContext());
        filmsLV.setAdapter(filmAdapter);

        filmsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                mListener.onFilmSelect(MainActivity.mFilmList.get(position));
            }
        });

        filmsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(MainActivity.mFilmList.get(position) instanceof Film) {
                    Film f = (Film)MainActivity.mFilmList.get(position);
                    Toast.makeText(mContext, f.getTitle(), Toast.LENGTH_SHORT)
                            .show();
                }
                return true;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            if (mPosition != ListView.INVALID_POSITION) {
                mListView.smoothScrollToPosition(mPosition);
            }
        }
        return view;
    }
}
