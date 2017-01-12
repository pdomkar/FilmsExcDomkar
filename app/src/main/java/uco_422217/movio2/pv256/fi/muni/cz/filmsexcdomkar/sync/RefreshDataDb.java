package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.sync;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.BuildConfig;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.R;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmRetrofitInterface;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 6. 12. 2016.
 */

public class RefreshDataDb {
    private static final int NOTIFICATION_CHANGE = 1;
    private Context context;
    private static RefreshDataDb instance = null;
    private NotificationManager mNotificationManager;

    private RefreshDataDb() { }

    static RefreshDataDb getInstance( ) {
        if (instance == null) {
            instance = new RefreshDataDb();
        }
        return instance;
    }
    void init(Context context){
        this.context = context.getApplicationContext();
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    void refreshFilmsInDb() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                FilmManager mFilmManager = new FilmManager(context);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Consts.MOVIE_API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                FilmRetrofitInterface filmRetrofitInterface = retrofit.create(FilmRetrofitInterface.class);
                List<Film> filmsDb = mFilmManager.findFilms();
                boolean updated = false;
                for (Film filmDb : filmsDb) {
                    final Call<Film> call = filmRetrofitInterface.findFilmById(filmDb.getId(), Consts.MOVIE_API_KEY);
                    try {
                        Response<Film> newPostResponse = call.execute();

                        int statusCode = newPostResponse.code();
                        if (statusCode == 200) {
                            Film filmApi = newPostResponse.body();

                            if(
                                    !compareStrNull(filmDb.getTitle(), filmApi.getTitle()) ||
                                    !compareStrNull(filmDb.getReleaseDate(), filmApi.getReleaseDate()) ||
                                    !compareStrNull(filmDb.getCoverPath(), filmApi.getCoverPath()) ||
                                    !compareStrNull(filmDb.getBackdropPath(), filmApi.getBackdropPath()) ||
                                    filmDb.getVoteAverage() != filmApi.getVoteAverage() ||
                                    !compareStrNull(filmDb.getOverview(), filmApi.getOverview())
                            ) {
                                mFilmManager.updateFilm(filmApi);
                                updated = true;
                                mNotificationManager.notify(NOTIFICATION_CHANGE, getChangeNotification().build());
                            }
                        } else {
                            if(BuildConfig.logging) {
                                Log.i("Error", "Fail by getting data");
                            }
                        }
                    } catch (IOException e) {
                        if(BuildConfig.logging) {
                            Log.i("Error", e.toString());
                        }
                    }
                }
                if(!updated) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, R.string.actuals_films, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };

        thread.start();

    }

    private NotificationCompat.Builder getChangeNotification() {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_updated)
                .setContentTitle(context.getString(R.string.films_db_updated))
                .setContentText(context.getString(R.string.films_db_updated))
                .setAutoCancel(true);
    }

    private static boolean compareStrNull(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

}
