package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters.FilmAdapter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmsListFragment extends Fragment {
    private static final String TAG = FilmsListFragment.class.getName();
    private boolean mTwoPane;

    public FilmsListFragment() {
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Log.i("FL", "onAttach");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("FL", "onDetatch");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FL", "onCrate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_film_list_layout, container, false);

        if (view.findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }

        ListView listView = (ListView) view.findViewById(R.id.filmsLV);
        listView.setEmptyView(view.findViewById(R.id.empty_list_item));

        ArrayList<Object> list = MainActivity.mFilmList;

        if (list == null || list.size() == 0) {

//            EmptyRecyclerViewAdapter emptyRecyclerViewAdapter = new EmptyRecyclerViewAdapter("NO INTERNET");
//            recyclerView.setAdapter(emptyRecyclerViewAdapter);
        } else {
            FilmAdapter filmAdapter = new FilmAdapter(list, getContext(), mTwoPane, getActivity().getSupportFragmentManager());
            listView.setAdapter(filmAdapter);
        }

        return view;
    }
}
