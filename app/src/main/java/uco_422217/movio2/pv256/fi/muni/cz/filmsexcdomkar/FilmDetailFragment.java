package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmDetailFragment extends Fragment {
    public static final String TAG = FilmDetailFragment.class.getSimpleName();
    private static final String ARGS_FILM = "args_film";

    private Context mContext;
    private Film mFilm;

    public static FilmDetailFragment newInstance(Film film) {
        FilmDetailFragment fragment = new FilmDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_FILM, film);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle args = getArguments();
        if (args != null) {
            mFilm = args.getParcelable(ARGS_FILM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_detail_layout, container, false);

        TextView titleTV = (TextView) view.findViewById(R.id.titleDetailTV);
        TextView releaseDateTV = (TextView) view.findViewById(R.id.releaseDateDetailTV);
        ImageView backdropIV = (ImageView) view.findViewById(R.id.backdropDetailIV);
        ImageView posterIV = (ImageView) view.findViewById(R.id.posterDetailIV);
        TextView overviewContentTV = (TextView) view.findViewById(R.id.overviewContentTV);
        FloatingActionButton plusFAB = (FloatingActionButton) view.findViewById(R.id.plusFAB);

        if (mFilm != null) {
            titleTV.setText(mFilm.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            String year = "";
            try {
                date = format.parse(mFilm.getReleaseDate());
                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                year = df.format(date);
            } catch (ParseException e) {
                Log.i(TAG, "parse exception", e);
            }
            releaseDateTV.setText(year);
            overviewContentTV.setText(mFilm.getOverview());
            //foto
            plusFAB.show();
        }
        return view;
    }
}
