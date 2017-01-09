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
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.R;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmRetrofitInterface;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 6. 12. 2016.
 */

public class RefreshDataDb {
    private final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/";
    private final String MOVIE_API_KEY = "9abf76a6b9a507feb496c4d4bc7cb670";
    private static final int NOTIFICATION_CHANGE = 1;
    private Context context;
    private static RefreshDataDb instance = null;
    private NotificationManager mNotificationManager;

    private RefreshDataDb() { }
    public static RefreshDataDb getInstance( ) {
        if (instance == null) {
            instance = new RefreshDataDb();
        }
        return instance;
    }
    public void init(Context context){
        this.context = context.getApplicationContext();
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void refreshFilmsInDb() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                FilmManager mFilmManager = new FilmManager(context);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MOVIE_API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                FilmRetrofitInterface filmRetrofitInterface = retrofit.create(FilmRetrofitInterface.class);
                List<Film> filmsDb = mFilmManager.findFilms();
                boolean updated = false;
                for (Film filmDb : filmsDb) {
                    final Call<Film> call = filmRetrofitInterface.findFilmById(filmDb.getId(), MOVIE_API_KEY);
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
                            Log.i("Error", "Fail by getting data");
                        }
                    } catch (IOException e) {
                        Log.i("Error", e.toString());
                    }
                }
                if(!updated) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Films are actual", Toast.LENGTH_SHORT).show();
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
                .setContentTitle("Films in db were updated")
                .setContentText("Films in db were updated")
                .setAutoCancel(true);
    }

    public static boolean compareStrNull(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

}
