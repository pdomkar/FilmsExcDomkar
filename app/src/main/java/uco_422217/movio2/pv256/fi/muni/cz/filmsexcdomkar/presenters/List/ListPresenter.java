package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.presenters.List;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.BuildConfig;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.MainActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.R;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmRetrofitInterface;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmResponse;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.GenreResponse;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks.Connectivity;


/**
 * Created by Petr on 16. 12. 2016.
 */

public class ListPresenter {
    public static final String TAG = ListPresenter.class.getSimpleName();
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
        Bundle args = new Bundle();
        if (loaderManager.getLoader(Consts.LOADER_GENRE_FIND_SHOW_ID) != null) {
            loaderManager.restartLoader(Consts.LOADER_GENRE_FIND_SHOW_ID, args, new GenreCallback(context, this)).forceLoad();
        } else {
            loaderManager.initLoader(Consts.LOADER_GENRE_FIND_SHOW_ID, args, new GenreCallback(context, this)).forceLoad();
        }
    }

    public void loadFilmsDb() {
        loaderManager.initLoader(Consts.LOADER_FILM_FIND_ALL_ID, null, new FilmsCallback(context, thisFr)).forceLoad();
    }

    void downloadFilms(final List<Genre> genres) {
        if (Connectivity.isConnected(context)) {
            mNotificationManager.notify(Consts.NOTIFICATION_DOWNLOAD, getDownloadRunningNotification().build());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Consts.MOVIE_API_BASE_URL)
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


            filmRetrofitInterface.findFilmsInTheatre(ymdFormat.format(startDate), ymdFormat.format(endDate), "vote_average.desc", Consts.MOVIE_API_KEY).enqueue(new Callback<FilmResponse>() {
                @Override
                public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                    if (response.isSuccessful()) {
                        mNotificationManager.cancel(Consts.NOTIFICATION_DOWNLOAD);
                        mNotificationManager.cancel(Consts.NOTIFICATION_ERROR);
                        mNotificationManager.notify(Consts.NOTIFICATION_DONE, getDownloadDoneNotification().build());
                        ArrayList<Film> resFilms = response.body().getFilms();
                        Film[] films = new Film[resFilms.size()];
                        int i = 0;
                        for (Film film : resFilms) {
                            films[i++] = film;
                        }
                        filterFilmsByGenre(films, context.getString(R.string.in_theatre), genres);
                    } else {
                        ListPresenter.this.setAdapterFr(null);
                        if (response.code() == 404) {
                            mNotificationManager.cancel(Consts.NOTIFICATION_DOWNLOAD);
                            mNotificationManager.cancel(Consts.NOTIFICATION_DONE);
                            mNotificationManager.notify(Consts.NOTIFICATION_ERROR, getDownloadErrorNotification(context.getString(R.string.no_found_resource)).build());
                        } else if(response.code() == 429) {
                        } else {
                            mNotificationManager.cancel(Consts.NOTIFICATION_DONE);
                            mNotificationManager.cancel(Consts.NOTIFICATION_DOWNLOAD);
                            mNotificationManager.notify(Consts.NOTIFICATION_ERROR, getDownloadErrorNotification(String.valueOf(response.code())).build());
                        }
                    }
                }

                @Override
                public void onFailure(Call<FilmResponse> call, Throwable t) {
                    if(BuildConfig.logging) {
                        Log.d(TAG, t.getMessage());
                    }
                    ListPresenter.this.setAdapterFr(null);
                    mNotificationManager.cancel(Consts.NOTIFICATION_DOWNLOAD);
                    mNotificationManager.notify(Consts.NOTIFICATION_ERROR, getDownloadErrorNotification(String.valueOf(t.getMessage())).build());
                }
            });

            filmRetrofitInterface.findFilmsPopularInYear(yearFormat.format(cal.getTime()), "vote_average.desc", Consts.MOVIE_API_KEY).enqueue(new Callback<FilmResponse>() {
                @Override
                public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                    if (response.isSuccessful()) {
                        mNotificationManager.cancel(Consts.NOTIFICATION_DOWNLOAD);
                        mNotificationManager.cancel(Consts.NOTIFICATION_ERROR);
                        mNotificationManager.notify(Consts.NOTIFICATION_DONE, getDownloadDoneNotification().build());
                        ArrayList<Film> resFilms = response.body().getFilms();
                        Film[] films = new Film[resFilms.size()];
                        int i = 0;
                        for (Film film : resFilms) {
                            films[i++] = film;
                        }
                        filterFilmsByGenre(films, context.getString(R.string.popular_in) + " " + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), genres);
                    } else {
                        ListPresenter.this.setAdapterFr(null);
                        if (response.code() == 404) {
                            mNotificationManager.cancel(Consts.NOTIFICATION_DOWNLOAD);
                            mNotificationManager.cancel(Consts.NOTIFICATION_DONE);
                            mNotificationManager.notify(Consts.NOTIFICATION_ERROR, getDownloadErrorNotification(context.getString(R.string.no_found_resource)).build());
                        } else if(response.code() == 429) {
                        } else {
                            mNotificationManager.cancel(Consts.NOTIFICATION_DONE);
                            mNotificationManager.cancel(Consts.NOTIFICATION_DOWNLOAD);
                            mNotificationManager.notify(Consts.NOTIFICATION_ERROR, getDownloadErrorNotification(String.valueOf(response.code())).build());
                        }
                    }
                }

                @Override
                public void onFailure(Call<FilmResponse> call, Throwable t) {
                    if(BuildConfig.logging) {
                        Log.d(TAG, t.getMessage());
                    }
                    ListPresenter.this.setAdapterFr(null);
                    mNotificationManager.cancel(Consts.NOTIFICATION_DOWNLOAD);
                    mNotificationManager.notify(Consts.NOTIFICATION_ERROR, getDownloadErrorNotification(String.valueOf(t.getMessage())).build());
                }
            });

        } else {
            ListPresenter.this.setAdapterFr(null);
            mNotificationManager.notify(Consts.NOTIFICATION_ERROR, getDownloadErrorNotification(context.getString(R.string.no_conntection)).build());
        }
    }

    private void filterFilmsByGenre(Film[] films, String title, List<Genre> genres) {
        ArrayList<Object> filmsToShow = new ArrayList<>();
        if (genres.size() > 0) {
            filmsToShow.add(title);
            ArrayList<Integer> genresIdShow = new ArrayList<>();
            for (Genre genre : genres) {
                genresIdShow.add((int) (long) genre.getId());
            }

            for (Film film : films) {
                if (isAnyValueInArray(film.getGenres(), genresIdShow)) {
                    filmsToShow.add(film);
                }
            }
            ListPresenter.this.setAdapterFr(filmsToShow);
        } else {
            filmsToShow.add(title);
            for (Film film : films) {
                filmsToShow.add(film);
            }
            ListPresenter.this.setAdapterFr(filmsToShow);
        }
    }

    public void loadFilmGenres() {
        loaderManager.initLoader(Consts.LOADER_GENRE_FIND_ALL_ID, null, new GenresCallback(context, loaderManager, activity)).forceLoad();
    }

    public void loadFilmGenresApi() {
        if (Connectivity.isConnected(context)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Consts.MOVIE_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FilmRetrofitInterface filmRetrofitInterface = retrofit.create(FilmRetrofitInterface.class);
            filmRetrofitInterface.findGenres(Consts.MOVIE_API_KEY).enqueue(new Callback<GenreResponse>() {
                @Override
                public void onResponse(retrofit2.Call<GenreResponse> call, retrofit2.Response<GenreResponse> response) {
                    if (response.isSuccessful()) {
                        activity.saveListGenre(response.body().getGenres());
                    } else {
                        if(BuildConfig.logging) {
                            Log.i(TAG, response.code() + "");
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<GenreResponse> call, Throwable t) {
                    if(BuildConfig.logging) {
                        Log.d(TAG, t.getMessage());
                    }
                }
            });
        }
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

    public void setAdapterFr(ArrayList<Object> data) {
        if(data == null || data.size() == 0) {
            thisFr.setEmptyAdapter();
        } else {
            thisFr.setAdapterList(data);
        }
    }

    private NotificationCompat.Builder getDownloadRunningNotification() {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_downloading)
                .setContentTitle(context.getString(R.string.f_downloading))
                .setContentText(context.getString(R.string.f_downloading))
                .setProgress(0, 0, true)
                .setAutoCancel(true);
    }

    private NotificationCompat.Builder getDownloadDoneNotification() {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_downloading_done)
                .setContentTitle(context.getString(R.string.f_downloaded))
                .setContentText(context.getString(R.string.f_downloaded))
                .setAutoCancel(true);
    }

    private NotificationCompat.Builder getDownloadErrorNotification(String error) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(context.getString(R.string.f_error))
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
