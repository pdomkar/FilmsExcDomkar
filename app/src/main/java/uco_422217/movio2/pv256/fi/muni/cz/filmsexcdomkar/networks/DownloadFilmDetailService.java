package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmRetrofitInterface;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Credits;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmResponse;

/**
 * Created by Petr on 25. 10. 2016.
 */

public class DownloadFilmDetailService extends IntentService {
    private static final String TAG = DownloadFilmDetailService.class.getName();
    private final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/";
    private final String MOVIE_API_KEY = "9abf76a6b9a507feb496c4d4bc7cb670";
    public static final String RESULT_CODE = "resultCode";
    public static final String RESULT_VALUE = "resultValue";

    public DownloadFilmDetailService() {
        super(DownloadFilmDetailService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (Connectivity.isConnected(getApplicationContext())) {
            Long id = intent.getLongExtra(FilmDetailFragment.DETAIL_ID, 0L);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FilmRetrofitInterface filmRetrofitInterface = retrofit.create(FilmRetrofitInterface.class);
            callRequest(filmRetrofitInterface.findFilmCredits(id, MOVIE_API_KEY));
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }


    private void callRequest(final retrofit2.Call<Credits> credits) {
        credits.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(retrofit2.Call<Credits> call, retrofit2.Response<Credits> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(FilmDetailFragment.ACTION_SEND_DETAIL_RESULTS);
                    intent.putExtra(RESULT_CODE, Activity.RESULT_OK);
                    intent.putExtra(RESULT_VALUE, response.body());
                    LocalBroadcastManager.getInstance(DownloadFilmDetailService.this).sendBroadcast(intent);
                } else {
                    Log.i(TAG, response.code() + "");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Credits> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}
