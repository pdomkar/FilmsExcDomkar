package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.DetailActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.MainActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces.FilmRetrofitInterface;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmResponse;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.GenreResponse;

/**
 * - prepare for next ukol
 * Created by Petr on 25. 10. 2016.
 */

public class DownloadGenreListService extends IntentService {
    private static final String TAG = DownloadGenreListService.class.getName();
    private final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/";
    private final String MOVIE_API_KEY = "9abf76a6b9a507feb496c4d4bc7cb670";
    public static final String RESULT_CODE = "resultCode";
    public static final String RESULT_VALUE = "resultValue";

    public DownloadGenreListService() {
        super(DownloadFilmListService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (Connectivity.isConnected(getApplicationContext())) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FilmRetrofitInterface filmRetrofitInterface = retrofit.create(FilmRetrofitInterface.class);
            callRequest(filmRetrofitInterface.findGenres(MOVIE_API_KEY));
        }
    }

    private void callRequest(final retrofit2.Call<GenreResponse> genres) {
        genres.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(retrofit2.Call<GenreResponse> call, retrofit2.Response<GenreResponse> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.ACTION_SEND_RESULTS_GENRES);
                    intent.putExtra(RESULT_CODE, Activity.RESULT_OK);
                    intent.putParcelableArrayListExtra(RESULT_VALUE, (response.body()).getGenres());
                    LocalBroadcastManager.getInstance(DownloadGenreListService.this).sendBroadcast(intent);
                } else {
                    Log.i(TAG, response.code() + "");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<GenreResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}
