package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.R;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmRetrofitInterface;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmResponse;

/**
 * Created by Petr on 25. 10. 2016.
 */

public class DownloadFilmListService extends IntentService {
    private static final String TAG = DownloadFilmListService.class.getName();
    private static final String IN_THEATRE = "Právě v kině", POPULAR_IN_YEAR = "Populární v ";
    private final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/";
    private final String MOVIE_API_KEY = "9abf76a6b9a507feb496c4d4bc7cb670";
    public static final String RESULT_CODE = "resultCode";
    public static final String RESULT_VALUE = "resultValue";
    public static final String RESULT_VALUE_TITLE = "resultValueTitle";
    private static final int NOTIFICATION_DOWNLOAD = 10;
    private static final int NOTIFICATION_DONE = 20;
    private static final int NOTIFICATION_ERROR = 30;
    private NotificationManager mNotificationManager;

    public DownloadFilmListService() {
        super(DownloadFilmListService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Connectivity.isConnected(getApplicationContext())) {
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
            callRequest(filmRetrofitInterface.findFilmsPopularInYear(yearFormat.format(cal.getTime()), "vote_average.desc", MOVIE_API_KEY), POPULAR_IN_YEAR + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            callRequest(filmRetrofitInterface.findFilmsInTheatre(ymdFormat.format(startDate), ymdFormat.format(endDate), "vote_average.desc", MOVIE_API_KEY), IN_THEATRE);
        } else {
            mNotificationManager.notify(NOTIFICATION_ERROR, getDownloadErrorNotification("Not internet connection").build());
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
        mNotificationManager.cancel(NOTIFICATION_DONE);
        mNotificationManager.cancel(NOTIFICATION_ERROR);
    }

    private void callRequest(final Call<FilmResponse> films, final String title) {
        films.enqueue(new Callback<FilmResponse>() {
            @Override
            public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                if (response.isSuccessful()) {
                    mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
                    mNotificationManager.cancel(NOTIFICATION_ERROR);
                    mNotificationManager.notify(NOTIFICATION_DONE, getDownloadDoneNotification().build());

                    Intent intent = new Intent(FilmsListFragment.ACTION_SEND_RESULTS);
                    intent.putExtra(RESULT_CODE, Activity.RESULT_OK);
                    intent.putParcelableArrayListExtra(RESULT_VALUE, (response.body()).getFilms());
                    intent.putExtra(RESULT_VALUE_TITLE, title);
                    LocalBroadcastManager.getInstance(DownloadFilmListService.this).sendBroadcast(intent);
                } else {
                    if (response.code() == 404) {
                        mNotificationManager.cancel(NOTIFICATION_DOWNLOAD);
                        mNotificationManager.cancel(NOTIFICATION_DONE);
                        mNotificationManager.notify(NOTIFICATION_ERROR, getDownloadErrorNotification("Not found resource").build());
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
    }

    private NotificationCompat.Builder getDownloadRunningNotification() {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_downloading)
                .setContentTitle("Films downloading")
                .setContentText("Films are downloading")
                .setProgress(0, 0, true)
                .setAutoCancel(true);
    }

    private NotificationCompat.Builder getDownloadDoneNotification() {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_downloading_done)
                .setContentTitle("Films downloading")
                .setContentText("Films were downloaded")
                .setAutoCancel(true);
    }

    private NotificationCompat.Builder getDownloadErrorNotification(String error) {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Error while downloading films")
                .setContentText(error)
                .setAutoCancel(true);
    }
}
