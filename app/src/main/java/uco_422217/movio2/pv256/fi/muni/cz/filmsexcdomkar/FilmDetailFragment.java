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
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.OnFilmSelectListener;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Credits;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Crew;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmDetailService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmListService;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmDetailFragment extends Fragment {
    public static final String TAG = FilmDetailFragment.class.getSimpleName();
    private static final String ARGS_FILM = "args_film";
    public static final String ACTION_SEND_DETAIL_RESULTS = "SEND_DETAIL_RESULTS";
    private static final String IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/w500";
    public static final String DETAIL_ID = "id";
    private Context mContext;
    private Film mFilm;
    private LocalBroadcastManager mBroadcastManager;
    public static FilmDetailFragment ins;

    public static FilmDetailFragment newInstance(Film film) {
        FilmDetailFragment fragment = new FilmDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_FILM, film);
        fragment.setArguments(args);
        return fragment;
    }

    public static FilmDetailFragment getInstace() { // kvuli možnost ziskat view v onReciev
        return ins;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        if (args != null) {
            mFilm = args.getParcelable(ARGS_FILM);
            if (mFilm != null) {
                Intent intent = new Intent(getActivity(), DownloadFilmDetailService.class);
                intent.putExtra(DETAIL_ID, mFilm.getId());
                getActivity().startService(intent);
            }
        }
        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        ins = FilmDetailFragment.this;
        ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        ins = FilmDetailFragment.this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_detail_layout, container, false);

        TextView titleTV = (TextView) view.findViewById(R.id.titleDetailTV);
        TextView releaseDateTV = (TextView) view.findViewById(R.id.releaseDateDetailTV);
        ImageView backdropIV = (ImageView) view.findViewById(R.id.backdropDetailIV);
        final ImageView posterIV = (ImageView) view.findViewById(R.id.posterDetailIV);
        TextView overviewTitleTV = (TextView) view.findViewById(R.id.overviewTitleTV);
        TextView castTitleTV = (TextView) view.findViewById(R.id.castTitleTV);
        TextView overviewContentTV = (TextView) view.findViewById(R.id.overviewContentTV);
        FloatingActionButton plusFAB = (FloatingActionButton) view.findViewById(R.id.plusFAB);

        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.headContentLL);
        ViewTreeObserver viewTreeObserver = relativeLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                posterIV.getLayoutParams().height = relativeLayout.getMeasuredHeight();
            }
        });

        if (mFilm != null) {
            titleTV.setText(mFilm.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
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
            Picasso.with(mContext).load(IMAGE_BASE_PATH + mFilm.getBackdropPath()).placeholder(R.drawable.image_not_found).into(backdropIV);
            Picasso.with(mContext).load(IMAGE_BASE_PATH + mFilm.getCoverPath()).placeholder(R.drawable.image_not_found_poster).into(posterIV);
            overviewTitleTV.setVisibility(View.VISIBLE);
            castTitleTV.setVisibility(View.VISIBLE);
            plusFAB.show();
        }
        return view;
    }

    public void setFilmCredits(Credits credits) {
        if (FilmDetailFragment.getInstace() != null) {
            View view = FilmDetailFragment.getInstace().getView();
            if (mFilm != null && credits != null) {
                mFilm.setCredits(credits);
                Credits filmCredits = mFilm.getCredits();
                if (filmCredits != null) {
                    for (Crew crew : filmCredits.getCrew()) {
                        if (crew.getJob().equals("Director")) {
                            TextView directorTV = (TextView) view.findViewById(R.id.directorTV);
                            if (directorTV != null) {
                                directorTV.setText(crew.getName());
                            }
                            break;
                        }
                    }

                    for (int i = 0; i < filmCredits.getCast().length; i++) {
                        if (i < 2) {
                            Cast cast = filmCredits.getCast()[i];
                            TextView castNameTV = null;
                            ImageView castProfileIV = null;
                            if (i == 0) {
                                castProfileIV = (ImageView) view.findViewById(R.id.cast1ProfileIV);
                                castNameTV = (TextView) view.findViewById(R.id.cast1NameTV);
                            } else if (i == 1) {
                                castProfileIV = (ImageView) view.findViewById(R.id.cast2ProfileIV);
                                castNameTV = (TextView) view.findViewById(R.id.cast2NameTV);
                            }
                            if (castProfileIV != null) {
                                Picasso.with(mContext).load(IMAGE_BASE_PATH + cast.getProfilePath()).placeholder(R.drawable.image_not_found).into(castProfileIV);
                            }
                            if (castNameTV != null) {
                                castNameTV.setText(cast.getName());
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        mBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_SEND_DETAIL_RESULTS));
        ins = FilmDetailFragment.this;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        FilmDetailFragment.ins = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
        FilmDetailFragment.ins = null;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(DownloadFilmListService.RESULT_CODE, Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK && intent.getAction().equals(FilmDetailFragment.ACTION_SEND_DETAIL_RESULTS)) {
                Credits data = intent.getParcelableExtra(DownloadFilmListService.RESULT_VALUE);
                setFilmCredits(data);
            }
        }
    };
}
