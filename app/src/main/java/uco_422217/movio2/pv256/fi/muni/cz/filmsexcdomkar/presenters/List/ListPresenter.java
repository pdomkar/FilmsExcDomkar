package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.MainActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.R;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmRetrofitInterface;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmsContract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmResponse;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmsGenresBlock;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmDetailService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadFilmListService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.DownloadGenreListService;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.Detail.DirectorCallback;


/**
 * Created by Petr on 16. 12. 2016.
 */

public class ListPresenter {
    public static final String TAG = ListPresenter.class.getSimpleName();
    private final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/";
    private final String MOVIE_API_KEY = "9abf76a6b9a507feb496c4d4bc7cb670";
    private static final int NOTIFICATION_DOWNLOAD = 10;
    private static final int NOTIFICATION_DONE = 20;
    private static final int NOTIFICATION_ERROR = 30;
    private NotificationManager mNotificationManager;
    private Context context;
    private LoaderManager loaderManager;
    private FilmsListFragment thisFr;
    private MainActivity activity;
    public ListPresenter(Context context, LoaderManager loaderManager, FilmsListFragment thisFr, MainActivity activity) {
        this.context = context;
        this.loaderManager = loaderManager;
        this.thisFr = thisFr;
        this.activity = activity;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void onLoadFilms() {
        if (Connectivity.isConnected(context)) {
            mNotificationManager.notify(NOTIFICATION_DOWNLOAD, getDownloadRunningNotification().build());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FilmRetrofitInterface filmRetrofitInterface = retrofit.create(FilmRetrofitInterface.class);

            Calendar cal = GregorianCalendar.getInstance();
            SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, -7);
            Date startDate = cal.getTime();
            cal.add(Calendar.DAY_OF_YEAR, +14);
            Date endDate = cal.getTime();


            filmRetrofitInterface.findFilmsInTheatre(ymdFormat.format(startDate), ymdFormat.format(endDate), "vote_average.desc", MOVIE_API_KEY).enqueue(new Callback<FilmResponse>() {
                @Override
                public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                    if (response.isSuccessful()) {
                        mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
                        mNotificationManager.cancel(NOTIFICATION_ERROR);
                        mNotificationManager.notify(NOTIFICATION_DONE, getDownloadDoneNotification().build());
                        ArrayList<Film> resFilms = response.body().getFilms();
                        Film[] films = new Film[resFilms.size()];
                        int i = 0;
                        for (Film film : resFilms) {
                            films[i++] = film;
                        }
                        doFilteringFilmsByGenres(films, Consts.IN_THEATRE);
                    } else {
                        if (response.code() == 404) {
                            mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
                            mNotificationManager.cancel(NOTIFICATION_DONE);
                            mNotificationManager.notify(NOTIFICATION_ERROR, getDownloadErrorNotification("Not found resource").build());
                        } else if(response.code() == 429) {

                        } else {
                            mNotificationManager.cancel(NOTIFICATION_DONE);
                            mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
                            mNotificationManager.notify(NOTIFICATION_ERROR, getDownloadErrorNotification(String.valueOf(response.code())).build());
                        }
                    }
                }

                @Override
                public void onFailure(Call<FilmResponse> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                    mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
                    mNotificationManager.notify(NOTIFICATION_ERROR, getDownloadErrorNotification(String.valueOf(t.getMessage())).build());
                }
            });

            filmRetrofitInterface.findFilmsPopularInYear(yearFormat.format(cal.getTime()), "vote_average.desc", MOVIE_API_KEY).enqueue(new Callback<FilmResponse>() {
                @Override
                public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                    if (response.isSuccessful()) {
                        mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
                        mNotificationManager.cancel(NOTIFICATION_ERROR);
                        mNotificationManager.notify(NOTIFICATION_DONE, getDownloadDoneNotification().build());
                        ArrayList<Film> resFilms = response.body().getFilms();
                        Film[] films = new Film[resFilms.size()];
                        int i = 0;
                        for (Film film : resFilms) {
                            films[i++] = film;
                        }
                        doFilteringFilmsByGenres(films, Consts.POPULAR_IN_YEAR + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                    } else {
                        if (response.code() == 404) {
                            mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
                            mNotificationManager.cancel(NOTIFICATION_DONE);
                            mNotificationManager.notify(NOTIFICATION_ERROR, getDownloadErrorNotification("Not found resource").build());
                        } else if(response.code() == 429) {

                        } else {
                            mNotificationManager.cancel(NOTIFICATION_DONE);
                            mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
                            mNotificationManager.notify(NOTIFICATION_ERROR, getDownloadErrorNotification(String.valueOf(response.code())).build());
                        }
                    }
                }

                @Override
                public void onFailure(Call<FilmResponse> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                    mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
                    mNotificationManager.notify(NOTIFICATION_ERROR, getDownloadErrorNotification(String.valueOf(t.getMessage())).build());
                }
            });

        } else {
            mNotificationManager.notify(NOTIFICATION_ERROR, getDownloadErrorNotification("Not internet connection").build());
        }

    }

    public void loadFilmsDb() {
        loaderManager.initLoader(Consts.LOADER_FILM_FIND_ALL_ID, null, new FilmsCallback(context, thisFr)).forceLoad();
    }

    private void doFilteringFilmsByGenres(Film[] films, String title) {
        Bundle args = new Bundle();
        args.putParcelableArray(Consts.FILM_API_LIST, films);
        args.putString(Consts.TITLE_FILMS, title);
        if (loaderManager.getLoader(Consts.LOADER_GENRE_FIND_SHOW_ID) != null) {
            loaderManager.restartLoader(Consts.LOADER_GENRE_FIND_SHOW_ID, args, new GenreCallback(context, this)).forceLoad();
        } else {
            loaderManager.initLoader(Consts.LOADER_GENRE_FIND_SHOW_ID, args, new GenreCallback(context, this)).forceLoad();
        }
    }

    void filterFilmsByGenre(FilmsGenresBlock filmsGenresBlock) {
        ArrayList<Object> filmsToShow = new ArrayList<>();
        if (filmsGenresBlock != null) {
            List<Film> films = filmsGenresBlock.getFilms();
            List<Genre> genresShow = filmsGenresBlock.getGenresShow();
            String title = filmsGenresBlock.getTitle();
            filmsToShow.add(title);

            ArrayList<Integer> genresIdShow = new ArrayList<>();
            for (Genre genre : genresShow) {
                genresIdShow.add((int) (long) genre.getId());
            }

            for (Film film : films) {
                if (isAnyValueInArray(film.getGenres(), genresIdShow)) {
                    filmsToShow.add(film);
                }
            }
            thisFr.setAdapterList(title, filmsToShow);
        } else {
            thisFr.setAdapterList("", filmsToShow);
        }
    }

    public void loadFilmGenres() {
        loaderManager.initLoader(Consts.LOADER_GENRE_FIND_ALL_ID, null, new GenresCallback(context, loaderManager, activity)).forceLoad();
    }

    public void loadFilmGenresApi() {
        Intent intent = new Intent(context, DownloadGenreListService.class);
        context.startService(intent);
    }

    public void changeStateGenre(Genre genre) {
        Bundle args = new Bundle();
        args.putParcelable(Consts.GENRE_DETAIL, genre);
        if (loaderManager.getLoader(Consts.LOADER_GENRE_UPDATE_ID) != null) {
            loaderManager.restartLoader(Consts.LOADER_GENRE_UPDATE_ID, args, new GenresCallback(context, loaderManager, activity)).forceLoad();
        } else {
            loaderManager.initLoader(Consts.LOADER_GENRE_UPDATE_ID, args, new GenresCallback(context, loaderManager, activity)).forceLoad();
        }
    }

    public void createGenres(Genre[] genres) {
        Bundle args = new Bundle();
        args.putParcelableArray(Consts.GENRES_DB_LIST, genres);
        loaderManager.initLoader(Consts.LOADER_GENRE_CREATE_ID, args, new GenresCallback(context, loaderManager, activity)).forceLoad();
    }


    private NotificationCompat.Builder getDownloadRunningNotification() {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_downloading)
                .setContentTitle("Films downloading")
                .setContentText("Films are downloading")
                .setProgress(0, 0, true)
                .setAutoCancel(true);
    }

    private NotificationCompat.Builder getDownloadDoneNotification() {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_downloading_done)
                .setContentTitle("Films downloading")
                .setContentText("Films were downloaded")
                .setAutoCancel(true);
    }

    private NotificationCompat.Builder getDownloadErrorNotification(String error) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Error while downloading films")
                .setContentText(error)
                .setAutoCancel(true);
    }

    private Boolean isAnyValueInArray(int[] values, List<Integer> array) {
        Boolean result = false;
        for (int i = 0; i < values.length; i++) {
            if (array.contains(values[i])) {
                result = true;
                break;
            }
        }
        return result;
    }
}
