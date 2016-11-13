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
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Credits;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Crew;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Director;
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
    public static final String DETAIL_FILM = "film";
    public static final String DETAIL_DIRECTOR = "director";
    public static final String DETAIL_CAST = "cast";
    private Context mContext;
    private Film mFilm;
    private LocalBroadcastManager mBroadcastManager;
    public static FilmDetailFragment ins;
    private FilmManager mFilmManager;
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
                intent.putExtra(DETAIL_ID, mFilm.getId());
                getActivity().startService(intent);
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

        TextView titleTV = (TextView) view.findViewById(R.id.titleDetailTV);
        TextView releaseDateTV = (TextView) view.findViewById(R.id.releaseDateDetailTV);
        ImageView backdropIV = (ImageView) view.findViewById(R.id.backdropDetailIV);
        final ImageView posterIV = (ImageView) view.findViewById(R.id.posterDetailIV);
        TextView overviewTitleTV = (TextView) view.findViewById(R.id.overviewTitleTV);
        TextView castTitleTV = (TextView) view.findViewById(R.id.castTitleTV);
        TextView overviewContentTV = (TextView) view.findViewById(R.id.overviewContentTV);
        final FloatingActionButton plusFAB = (FloatingActionButton) view.findViewById(R.id.plusFAB);

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
                SimpleDateFormat df = new SimpleDateFormat("dd. MM. yyyy");
                year = df.format(date);
            } catch (ParseException e) {
                Log.i(TAG, "parse exception", e);
            }
            releaseDateTV.setText(year);
            overviewContentTV.setText(mFilm.getOverview());
            //foto
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(IMAGE_BASE_PATH + mFilm.getBackdropPath(), backdropIV);
            imageLoader.displayImage(IMAGE_BASE_PATH + mFilm.getCoverPath(), posterIV);
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
                    Bundle args = new Bundle();
                    args.putLong(DETAIL_ID, mFilm.getId());
                    getLoaderManager().initLoader(LOADER_FILM_FIND_ID, args, new FilmCallback(getActivity().getApplicationContext())).forceLoad();
                }
            });

            //director

            if (mFilm.getCredits() == null) {
                Log.i("a", "bbbb");
                Bundle args = new Bundle();
                args.putLong(DETAIL_ID, mFilm.getId());
                getLoaderManager().initLoader(LOADER_DIRECTOR_FIND_ID, args, new DirectorCallback(getActivity().getApplicationContext())).forceLoad();
            }

            //obsazeni
            if (mFilm.getCredits() == null) {
                Bundle args = new Bundle();
                args.putLong(DETAIL_ID, mFilm.getId());
                getLoaderManager().initLoader(LOADER_CAST_FIND_ID, args, new CastCallback(getActivity().getApplicationContext())).forceLoad();
            }
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

        Toolbar toolbar = ((DetailActivity) getActivity()).getToolbar();
        if (toolbar != null) {
            Switch savedS = (Switch) ((DetailActivity) getActivity()).getToolbar().findViewById(R.id.savedS);
            savedS.setVisibility(View.INVISIBLE);
        }
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
                                ImageLoader imageLoader = ImageLoader.getInstance();
                                imageLoader.displayImage(IMAGE_BASE_PATH + cast.getProfilePath(), castProfileIV);
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
                Credits data = intent.getParcelableExtra(DownloadFilmDetailService.RESULT_VALUE);
                setFilmCredits(data);
            }
        }
    };


    private class FilmCallback implements LoaderManager.LoaderCallbacks<List<Film>> {
        Context mContext;

        public FilmCallback(Context context) {
            mContext = context;
        }

        @Override
        public Loader<List<Film>> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
            switch (id) {
                case LOADER_FILM_FIND_ID:
                    return new FilmFindLoader(mContext, args.getLong(DETAIL_ID, 0));
                case LOADER_FILM_CREATE_ID:
                    return new FilmCreateLoader(mContext, (Film) args.getParcelable(DETAIL_FILM));
                case LOADER_FILM_DELETE_ID:
                    return new FilmDeleteLoader(mContext, (Film) args.getParcelable(DETAIL_FILM));
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoadFinished(Loader<List<Film>> loader, List<Film> data) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
            switch (loader.getId()) {
                case LOADER_FILM_FIND_ID:
                    Bundle args = new Bundle();
                    args.putLong(DETAIL_ID, mFilm.getId());
                    args.putParcelable(DETAIL_FILM, mFilm);
                    String name = "";
                    if (mFilm.getCredits() != null) {
                        name = getDirectorNameFromCrew(mFilm.getCredits().getCrew());
                    }
                    Director director = new Director(mFilm.getId(), name);
                    args.putParcelable(DETAIL_DIRECTOR, director);
                    if (mFilm.getCredits() != null) {
                        args.putParcelableArray(DETAIL_CAST, mFilm.getCredits().getCast());
                    }

                    if (data.size() == 0) {
                        getLoaderManager().initLoader(LOADER_FILM_CREATE_ID, args, FilmCallback.this).forceLoad();
                        getLoaderManager().initLoader(LOADER_DIRECTOR_CREATE_ID, args, new DirectorCallback(mContext)).forceLoad();
                        getLoaderManager().initLoader(LOADER_CAST_CREATE_ID, args, new CastCallback(mContext)).forceLoad();
                    } else {
                        getLoaderManager().initLoader(LOADER_FILM_DELETE_ID, args, FilmCallback.this).forceLoad();
                        getLoaderManager().initLoader(LOADER_DIRECTOR_DELETE_ID, args, new DirectorCallback(mContext)).forceLoad();
                        getLoaderManager().initLoader(LOADER_CAST_DELETE_ID, args, new CastCallback(mContext)).forceLoad();
                    }
                    break;
                case LOADER_FILM_CREATE_ID:
                    if (FilmDetailFragment.getInstace() != null) {
                        View view = FilmDetailFragment.getInstace().getView();
                        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.plusFAB);
                        fab.setImageResource(R.drawable.ic_trash);
                    }
                    break;
                case LOADER_FILM_DELETE_ID:
                    if (FilmDetailFragment.getInstace() != null) {
                        View view = FilmDetailFragment.getInstace().getView();
                        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.plusFAB);
                        fab.setImageResource(R.drawable.ic_plus);
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Film>> loader) {
            Log.i(TAG, "+++ onLoadReset() called! +++");

        }
    }

    private class DirectorCallback implements LoaderManager.LoaderCallbacks<List<Director>> {
        Context mContext;

        public DirectorCallback(Context context) {
            mContext = context;
        }

        @Override
        public Loader<List<Director>> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
            switch (id) {
                case LOADER_DIRECTOR_FIND_ID:
                    return new DirectorFindLoader(mContext, args.getLong(DETAIL_ID, 0));
                case LOADER_DIRECTOR_CREATE_ID:
                    return new DirectorCreateLoader(mContext, (Director) args.getParcelable(DETAIL_DIRECTOR));
                case LOADER_DIRECTOR_DELETE_ID:
                    return new DirectorDeleteLoader(mContext, (Director) args.getParcelable(DETAIL_DIRECTOR));
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoadFinished(Loader<List<Director>> loader, List<Director> data) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
            switch (loader.getId()) {
                case LOADER_DIRECTOR_FIND_ID:
                    if (data.size() > 0) {
                        View view = FilmDetailFragment.getInstace().getView();
                        TextView directorTV = (TextView) view.findViewById(R.id.directorTV);
                        if (directorTV != null) {
                            directorTV.setText(((Director) data.get(0)).getName());
                        }
                    }
                    break;
                case LOADER_DIRECTOR_CREATE_ID:

                    break;
                case LOADER_DIRECTOR_DELETE_ID:

                    break;
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Director>> loader) {
            Log.i(TAG, "+++ onLoadReset() called! +++");

        }
    }

    private class CastCallback implements LoaderManager.LoaderCallbacks<List<Cast>> {
        Context mContext;

        public CastCallback(Context context) {
            mContext = context;
        }

        @Override
        public Loader<List<Cast>> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
            switch (id) {
                case LOADER_CAST_FIND_ID:
                    return new CastFindLoader(mContext, args.getLong(DETAIL_ID, 0));
                case LOADER_CAST_CREATE_ID:
                    return new CastCreateLoader(mContext, (Cast[]) args.getParcelableArray(DETAIL_CAST), args.getLong(DETAIL_ID));
                case LOADER_CAST_DELETE_ID:
                    return new CastDeleteLoader(mContext, args.getLong(DETAIL_ID));
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoadFinished(Loader<List<Cast>> loader, List<Cast> data) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
            switch (loader.getId()) {
                case LOADER_CAST_FIND_ID:
                    if (data.size() > 0) {
                        View view = FilmDetailFragment.getInstace().getView();

                        for (int i = 0; i < data.size(); i++) {
                            if (i < 2) {
                                Cast cast = data.get(i);
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
                                    ImageLoader imageLoader = ImageLoader.getInstance();
                                    imageLoader.displayImage(IMAGE_BASE_PATH + cast.getProfilePath(), castProfileIV);
                                }
                                if (castNameTV != null) {
                                    castNameTV.setText(cast.getName());
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    break;
                case LOADER_CAST_CREATE_ID:

                    break;
                case LOADER_CAST_DELETE_ID:

                    break;
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Cast>> loader) {
            Log.i(TAG, "+++ onLoadReset() called! +++");

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

}
