package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmsContract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Credits;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Crew;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail.DetailPresenter;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmDetailFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener, FilmsContract.DetailView {
    public static final String TAG = FilmDetailFragment.class.getSimpleName();
    private static final String ARGS_FILM = "args_film";
    public static final String ACTION_SEND_DETAIL_RESULTS = "SEND_DETAIL_RESULTS";
    private Context mContext;
    private Film mFilm;
    private LocalBroadcastManager mBroadcastManager;
    public static FilmDetailFragment ins;
    private FilmManager mFilmManager;

    private DetailPresenter mDetailPresenter;

    FloatingActionButton plusFAB;
    TextView releaseDateTV;
    TextView titleTV;
    ImageView posterIV;
    TextView titleDetailCollapsedTV;
    TextView directorTV;
    ImageView cast1ProfileIV;
    TextView cast1NameTV;
    ImageView cast2ProfileIV;
    TextView cast2NameTV;

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
        }
        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        ins = FilmDetailFragment.this;
        mFilmManager = new FilmManager(getActivity().getApplicationContext());

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
        AppBarLayout collapseAppBarL = (AppBarLayout) view.findViewById(R.id.collapseBar);
        titleTV = (TextView) view.findViewById(R.id.titleDetailExpandedTV);
        titleDetailCollapsedTV = (TextView) view.findViewById(R.id.titleDetailCollapsedTV);
        releaseDateTV = (TextView) view.findViewById(R.id.releaseDateDetailTV);
        ImageView backdropIV = (ImageView) view.findViewById(R.id.backdropDetailIV);
        posterIV = (ImageView) view.findViewById(R.id.posterDetailIV);
        TextView overviewTitleTV = (TextView) view.findViewById(R.id.overviewTitleTV);
        TextView castTitleTV = (TextView) view.findViewById(R.id.castTitleTV);
        cast1ProfileIV = (ImageView) view.findViewById(R.id.cast1ProfileIV);
        cast1NameTV = (TextView) view.findViewById(R.id.cast1NameTV);
        cast2ProfileIV = (ImageView) view.findViewById(R.id.cast2ProfileIV);
        cast2NameTV = (TextView) view.findViewById(R.id.cast2NameTV);
        TextView overviewContentTV = (TextView) view.findViewById(R.id.overviewContentTV);
        plusFAB = (FloatingActionButton) view.findViewById(R.id.plusFAB);
        directorTV = (TextView) view.findViewById(R.id.directorTV);


        if (mFilm != null) {
            mDetailPresenter = new DetailPresenter(getActivity().getApplicationContext(), getLoaderManager(), mFilm, this);
            mDetailPresenter.loadFilmDetails();
            collapseAppBarL.addOnOffsetChangedListener(this);
            titleTV.setText(mFilm.getTitle());
            titleDetailCollapsedTV.setText(mFilm.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            String year = "";
            try {
                date = format.parse(mFilm.getReleaseDate());
                SimpleDateFormat df = new SimpleDateFormat("dd. MM. yyyy");
                year = df.format(date);
            } catch (ParseException e) {
                if(BuildConfig.logging) {
                    Log.i(TAG, "parse exception", e);
                }
            }
            releaseDateTV.setText(year);
            overviewContentTV.setText(mFilm.getOverview());
            //foto
            ImageLoader imageLoader = ImageLoader.getInstance();
            if (mFilm.getBackdropPath() != null) {
                imageLoader.displayImage(Consts.IMAGE_BASE_PATH + mFilm.getBackdropPath(), backdropIV);
            } else {
                imageLoader.displayImage("drawable://" + R.drawable.image_not_found, backdropIV);
            }
            if (mFilm.getCoverPath() != null) {
                imageLoader.displayImage(Consts.IMAGE_BASE_PATH + mFilm.getCoverPath(), posterIV);
            } else {
                imageLoader.displayImage("drawable://" + R.drawable.image_not_found_poster, posterIV);
            }
            overviewTitleTV.setVisibility(View.VISIBLE);
            castTitleTV.setVisibility(View.VISIBLE);
            if (mFilmManager.findFilmsById(mFilm.getId()).size() == 0) {
                plusFAB.setImageResource(R.drawable.ic_plus);
            } else {
                plusFAB.setImageResource(R.drawable.ic_trash);
            }
            plusFAB.show();
            plusFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDetailPresenter.onClickSaved(mFilm.getId());
                }
            });

            backdropIV.setVisibility(View.VISIBLE);
            posterIV.setVisibility(View.VISIBLE);

            //director
            if (mFilm.getCredits() == null) {
                mDetailPresenter.loadDirectorFromDb();
            }

            //obsazeni
            if (mFilm.getCredits() == null) {
                mDetailPresenter.loadCastFromDb();
            }
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    public void setFilmCredits(Credits credits) {
        if (FilmDetailFragment.getInstace() != null) {
            View view = FilmDetailFragment.getInstace().getView();
            if (mFilm != null && credits != null) {
                mFilm.setCredits(credits);
                Credits filmCredits = mFilm.getCredits();
                if (filmCredits != null) {
                    TextView directorTV = (TextView) view.findViewById(R.id.directorTV);
                    if (directorTV != null) {
                        directorTV.setText(getDirectorNameFromCrew(filmCredits.getCrew()));
                    }

                    changeCasts(filmCredits.getCast());
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        ins = FilmDetailFragment.this;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset)
    {
        if (Math.abs(offset) == appBarLayout.getTotalScrollRange()) {
            plusFAB.setVisibility(View.GONE);
            releaseDateTV.setVisibility(View.GONE);
            titleTV.setVisibility(View.GONE);
            posterIV.setVisibility(View.GONE);
            titleDetailCollapsedTV.setVisibility(View.VISIBLE);
        } else {
            plusFAB.setVisibility(View.VISIBLE);
            releaseDateTV.setVisibility(View.VISIBLE);
            titleTV.setVisibility(View.VISIBLE);
            posterIV.setVisibility(View.VISIBLE);
            titleDetailCollapsedTV.setVisibility(View.GONE);
        }

    }

    private String getDirectorNameFromCrew(Crew[] crews) {
        for (Crew crew : crews) {
            if (crew.getJob().equals(getString(R.string.director))) {
                return crew.getName();
            }
        }
        return "";
    }

    @Override
    public void changeFab(Boolean showTrash) {
        if(showTrash) {
            plusFAB.setImageResource(R.drawable.ic_trash);
        } else {
            plusFAB.setImageResource(R.drawable.ic_plus);
        }
    }

    @Override
    public void changeDirectorTV(String name) {
        directorTV.setText(name);
    }

    @Override
    public void changeCasts(Cast[] casts) {
        for (int i = 0; i < casts.length; i++) {
            if (i < 2) {
                Cast cast = casts[i];
                TextView castNameTV = null;
                ImageView castProfileIV = null;
                if (i == 0) {
                    castProfileIV = cast1ProfileIV;
                    castNameTV = cast1NameTV;
                } else if (i == 1) {
                    castProfileIV = cast2ProfileIV;
                    castNameTV = cast2NameTV;
                }
                if (castProfileIV != null) {
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    if (cast.getProfilePath() != null) {
                        imageLoader.displayImage(Consts.IMAGE_BASE_PATH + cast.getProfilePath(), castProfileIV);
                    } else {
                        imageLoader.displayImage("drawable://" + R.drawable.image_not_found, castProfileIV);
                    }
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
