package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.CastCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.CastDeleteLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.CastFindLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.DirectorCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.DirectorDeleteLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.DirectorFindLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmCreateLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmDeleteLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.loaders.FilmFindLoader;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmsContract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Credits;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Crew;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Director;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmDetailService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmListService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.CastCallback;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.DetailPresenter;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.DirectorCallback;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmDetailFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener, FilmsContract.DetailView {
    public static final String TAG = FilmDetailFragment.class.getSimpleName();
    private static final String ARGS_FILM = "args_film";
    public static final String ACTION_SEND_DETAIL_RESULTS = "SEND_DETAIL_RESULTS";
    private static final String IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/w500";
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

    private static final int LOADER_FILM_FIND_ID = 1;
    private static final int LOADER_FILM_CREATE_ID = 2;
    private static final int LOADER_FILM_DELETE_ID = 3;
    private static final int LOADER_DIRECTOR_FIND_ID = 4;
    private static final int LOADER_DIRECTOR_CREATE_ID = 5;
    private static final int LOADER_DIRECTOR_DELETE_ID = 6;
    private static final int LOADER_CAST_FIND_ID = 7;
    private static final int LOADER_CAST_CREATE_ID = 8;
    private static final int LOADER_CAST_DELETE_ID = 9;

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
                intent.putExtra(Consts.DETAIL_ID, mFilm.getId());
                getActivity().startService(intent);
                mDetailPresenter = new DetailPresenter(this.getView(), getActivity().getApplicationContext(), getLoaderManager(), mFilm, this);
            }

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
                Log.i(TAG, "parse exception", e);
            }
            releaseDateTV.setText(year);
            overviewContentTV.setText(mFilm.getOverview());
            //foto
            ImageLoader imageLoader = ImageLoader.getInstance();
            if (mFilm.getBackdropPath() != null) {
                imageLoader.displayImage(IMAGE_BASE_PATH + mFilm.getBackdropPath(), backdropIV);
            }
            if (mFilm.getCoverPath() != null) {
                imageLoader.displayImage(IMAGE_BASE_PATH + mFilm.getCoverPath(), posterIV);
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
                    Log.i("sdfsdf", "dfsf");
                    mDetailPresenter.onClickSaved(mFilm.getId());
                }
            });

            backdropIV.setVisibility(View.VISIBLE);
            posterIV.setVisibility(View.VISIBLE);

            //director

            if (mFilm.getCredits() == null) {
                Bundle args = new Bundle();
                args.putLong(Consts.DETAIL_ID, mFilm.getId());
                getLoaderManager().initLoader(LOADER_DIRECTOR_FIND_ID, args, new DirectorCallback(getActivity().getApplicationContext(), FilmDetailFragment.this)).forceLoad();
            }

            //obsazeni
            if (mFilm.getCredits() == null) {
                Bundle args = new Bundle();
                args.putLong(Consts.DETAIL_ID, mFilm.getId());
                getLoaderManager().initLoader(LOADER_CAST_FIND_ID, args, new CastCallback(getActivity().getApplicationContext(), FilmDetailFragment.this)).forceLoad();
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
                Credits data = intent.getParcelableExtra(DownloadFilmDetailService.RESULT_VALUE);
                setFilmCredits(data);
            }
        }
    };


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
            if (crew.getJob().equals("Director")) {
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
                        imageLoader.displayImage(IMAGE_BASE_PATH + cast.getProfilePath(), castProfileIV);
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
