package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks;

import android.os.AsyncTask;
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
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.DetailActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.MainActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;

/**
 * - prepare for next ukol
 * Created by Petr on 25. 10. 2016.
 */

public class DownloadGenreListManager {
    private static final String TAG = DownloadGenreListManager.class.getName();
    private boolean isGenresTaskExecuting;
    private GenresTask mGenresTask;
    private AppCompatActivity mActivity;

    public DownloadGenreListManager(AppCompatActivity activity) {
        mActivity = activity;
    }

    public void startGenresTask() {
        if (!isGenresTaskExecuting) {
            mGenresTask = new GenresTask(mActivity);
            mGenresTask.execute();
            isGenresTaskExecuting = true;
        }
    }

    public void cancelGenresTask() {
        if (isGenresTaskExecuting) {
            mGenresTask.cancel(true);
            isGenresTaskExecuting = false;
        }
    }

    private static class GenresTask extends AsyncTask<Void, Integer, List<Genre>> {
        private final WeakReference<AppCompatActivity> mAppCompatActivityWeakReference;
        private final String MOVIE_API_SCHEME = "https";
        private final String MOVIE_API_HOST = "api.themoviedb.org";
        private final String MOVIE_API_VERSION = "3";
        private final String MOVIE_API_KEY = "9abf76a6b9a507feb496c4d4bc7cb670";

        OkHttpClient mOkHttpClient = new OkHttpClient();

        GenresTask(AppCompatActivity activity) {
            mAppCompatActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute - thread: " + Thread.currentThread().getName());
        }

        @Override
        protected List<Genre> doInBackground(Void... params) {
            List<Genre> list = new ArrayList<>();
            if (Connectivity.isConnected(mAppCompatActivityWeakReference.get())) {
                Request request = new Request.Builder()
                        .url(getHttpUrl().toString())
                        .build();

                Call call = mOkHttpClient.newCall(request);
                Response response = null;
                try {
                    response = call.execute();
                    if (response.isSuccessful()) {
                        list.add(new Genre(0L, "Zobrazené žánry", false));
                        list.addAll(convertToList(response.body().string()));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: IOException", e);
                } catch (IllegalArgumentException en) {
                    Log.e(TAG, "doInBackground: IlleagarArgumentException", en);
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Genre> result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute - thread: " + Thread.currentThread().getName());
            // Result to fragment adapter
            if (Connectivity.isConnected(mAppCompatActivityWeakReference.get())) {
                AppCompatActivity appCompatActivity = mAppCompatActivityWeakReference.get();
                if (appCompatActivity == null) {
                    return;
                }

                if (appCompatActivity instanceof MainActivity) {
                    ((MainActivity) appCompatActivity).setListGenre(result);
                } else if (appCompatActivity instanceof DetailActivity) {
                    ((DetailActivity) appCompatActivity).setListGenre(result);
                }
            }
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "onCancelled - thread: " + Thread.currentThread().getName());
        }

        private List<Genre> convertToList(String data) {
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(data, JsonObject.class);
            JsonArray results = json.getAsJsonArray("genres");
            return gson.fromJson(results, new TypeToken<List<Genre>>() {
            }.getType());
        }


        private HttpUrl getHttpUrl() {
            return new HttpUrl.Builder()
                    .scheme(MOVIE_API_SCHEME)
                    .host(MOVIE_API_HOST)
                    .addPathSegment(MOVIE_API_VERSION)
                    .addPathSegment("genre")
                    .addPathSegment("movie")
                    .addPathSegment("list")
                    .addQueryParameter("api_key", MOVIE_API_KEY)
                    .build();
        }
    }
}
