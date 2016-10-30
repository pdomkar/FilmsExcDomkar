package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks;

import android.os.AsyncTask;
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
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Credits;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 25. 10. 2016.
 */

public class DownloadFilmDetailManager {
    private static final String TAG = DownloadFilmDetailManager.class.getName();
    private boolean isFilmCreditsTaskExecuting;
    private FilmCreditsTask mFilmCreditsTask;
    private FilmDetailFragment mFilmDetailFragment;

    public DownloadFilmDetailManager(FilmDetailFragment filmDetailFragment) {
        mFilmDetailFragment = filmDetailFragment;
    }

    public void startFilmCreditsTask(Long id) {
        if (!isFilmCreditsTaskExecuting) {
            mFilmCreditsTask = new FilmCreditsTask(mFilmDetailFragment);
            mFilmCreditsTask.execute(id);
            isFilmCreditsTaskExecuting = true;
        }
    }

    public void cancelFilmCreditsTask() {
        if (isFilmCreditsTaskExecuting) {
            mFilmCreditsTask.cancel(true);
            isFilmCreditsTaskExecuting = false;
        }
    }

    private static class FilmCreditsTask extends AsyncTask<Long, Integer, Credits> {
        private final WeakReference<FilmDetailFragment> mFilmDetailFragmentWeakReference;

        private final String MOVIE_API_SCHEME = "https";
        private final String MOVIE_API_HOST = "api.themoviedb.org";
        private final String MOVIE_API_VERSION = "3";
        private final String MOVIE_API_KEY = "9abf76a6b9a507feb496c4d4bc7cb670";

        OkHttpClient mOkHttpClient = new OkHttpClient();

        FilmCreditsTask(FilmDetailFragment filmDetailFragment) {
            mFilmDetailFragmentWeakReference = new WeakReference<>(filmDetailFragment);
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute - thread: " + Thread.currentThread().getName());
        }

        @Override
        protected Credits doInBackground(Long... id) {
            Credits credits = null;
            if (Connectivity.isConnected(mFilmDetailFragmentWeakReference.get().getContext())) {
                Request request = new Request.Builder()
                        .url(getHttpUrl(id[0]).toString())
                        .build();

                Call call = mOkHttpClient.newCall(request);
                Response response;
                try {
                    response = call.execute();
                    if (response.isSuccessful()) {
                        credits = convertToCredits(response.body().string());
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: IOException", e);
                } catch (IllegalArgumentException en) {
                    Log.e(TAG, "doInBackground: IlleagarArgumentException", en);
                }
            }

            return credits;
        }

        @Override
        protected void onPostExecute(Credits result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute - thread: " + Thread.currentThread().getName());
            // Result to fragment adapter
            if (Connectivity.isConnected(mFilmDetailFragmentWeakReference.get().getContext())) {
                FilmDetailFragment filmDetailFragment = mFilmDetailFragmentWeakReference.get();
                if (filmDetailFragment == null) {
                    return;
                }

                filmDetailFragment.setFilmCredits(result, mFilmDetailFragmentWeakReference.get().getView());
            }
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "onCancelled - thread: " + Thread.currentThread().getName());
        }

        private Credits convertToCredits(String data) {
            Gson gson = new Gson();
            return gson.fromJson(data, Credits.class);
        }

        private HttpUrl getHttpUrl(long id) {
            return new HttpUrl.Builder()
                    .scheme(MOVIE_API_SCHEME)
                    .host(MOVIE_API_HOST)
                    .addPathSegment(MOVIE_API_VERSION)
                    .addPathSegment("movie")
                    .addPathSegment(String.valueOf(id))
                    .addPathSegment("credits")
                    .addQueryParameter("api_key", MOVIE_API_KEY)
                    .build();
        }
    }
}
