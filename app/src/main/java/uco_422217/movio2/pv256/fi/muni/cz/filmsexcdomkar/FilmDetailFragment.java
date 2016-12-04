package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmDetailFragment extends Fragment {
    public static final String SELECTED_FILM = "selected_film";
    final static String KEY_POSITION = "position";
    int mCurrentPosition = -1;

    TextView mTtile;
    TextView mReleaseDate;
    TextView mBackdrop;
    FloatingActionButton mPlusFAB;
    Film mFilm;

    public FilmDetailFragment() {
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Log.i("FD", "onAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("FD", "onCreateView");
        //activity is recreated -> restore saveInstance state
        if (savedInstanceState != null){
            Log.i("FD", "onReCreated");
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
        }

        View view = inflater.inflate(R.layout.fragment_film_detail_layout, container, false);

        this.mTtile = (TextView) view.findViewById(R.id.titleDetailTV);
        this.mReleaseDate = (TextView) view.findViewById(R.id.releaseDateDetailTV);
        this.mBackdrop = (TextView) view.findViewById(R.id.backdropDetailTV);
        this.mPlusFAB = (FloatingActionButton) view.findViewById(R.id.plusFAB);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("FD", "onActivityCreated");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("FD", "onStart");
            //get Film from Args parcelable
        Bundle args = getArguments();
        if (args != null){
            Film film = args.getParcelable(SELECTED_FILM);
            setFilmDetail(film);
        } else if(mCurrentPosition != -1){
            // Set Film based on savedInstanceState defined during onCreateView()
            setFilmDetail(MainActivity.mFilmList.get(mCurrentPosition));
        } else {
            mPlusFAB.hide();
        }
    }


    public void setFilmDetail(Film film) {
        if(film != null) {
            this.mFilm = film;
            this.mTtile.setText(film.getTitle());
            this.mBackdrop.setText(film.getBackdrop());
            Date date = new Date(film.getReleaseDate());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            this.mReleaseDate.setText(year + "");
            this.mTtile.setText(film.getTitle());
            mPlusFAB.show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("FD", "onSaveInstanceState");
        // Save the current film selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION, mCurrentPosition);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("FD", "onDetatch");
    }
}
