package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmsListFragment extends ListFragment {
    private Context mContext;
    private OnFilmSelectListener mListener;
    public FilmsListFragment() {
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Log.i("FL", "onAttach");
        try {
            mListener = (OnFilmSelectListener) activity;
        } catch (ClassCastException e) {
            Log.e("Error", "Activity must implement OnMovieSelectListener", e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("FL", "onDetatch");
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FL", "onCrate");
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("FL", "onActivityCreated");

        FilmAdapter filmAdapter = new FilmAdapter(MainActivity.mFilmList, mContext);
        setListAdapter(filmAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.OnFilmSelect(position);
    }
}
