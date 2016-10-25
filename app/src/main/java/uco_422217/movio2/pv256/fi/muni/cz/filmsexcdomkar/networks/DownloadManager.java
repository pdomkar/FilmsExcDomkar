package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 25. 10. 2016.
 */

public class DownloadManager {
    private static final String TAG = DownloadManager.class.getName();
    private boolean isFilmsTaskExecuting;
    private FilmsTask mFilmsTask;
    private FilmsListFragment mFilmsListFragment;
    private static final String IN_THEATRE = "Právě v kině", POPULAR_IN_YEAR = "Populární v ";

    public DownloadManager(FilmsListFragment filmsListFragment) {
        mFilmsListFragment = filmsListFragment;
    }

    public void startFilmsTask() {
        if(!isFilmsTaskExecuting) {
            mFilmsTask = new FilmsTask(mFilmsListFragment);
            mFilmsTask.execute(IN_THEATRE, POPULAR_IN_YEAR);
            isFilmsTaskExecuting = true;
        }
    }

    public void cancelFilmsTask() {
        if(isFilmsTaskExecuting) {
            mFilmsTask.cancel(true);
            isFilmsTaskExecuting = false;
        }
    }

    private static class FilmsTask extends AsyncTask<String, Integer, List<Object>> {
        private final WeakReference<FilmsListFragment> mFilmsListFragmentWeakReference; //avoid leaking Context

        private final String MOVIE_API_SCHEME = "https";
        private final String MOVIE_API_HOST = "api.themoviedb.org";
        private final String MOVIE_API_VERSION = "3";
        private final String MOVIE_API_KEY = "9abf76a6b9a507feb496c4d4bc7cb670";

        OkHttpClient mOkHttpClient = new OkHttpClient();

        public FilmsTask(FilmsListFragment filmsListFragment) {
            mFilmsListFragmentWeakReference = new WeakReference<>(filmsListFragment);
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute - thread: " + Thread.currentThread().getName());
        }

        @Override
        protected List<Object> doInBackground(String... params) {
            List<Object> list = new ArrayList<>();
            if(Connectivity.isConnected(mFilmsListFragmentWeakReference.get().getContext())) {
                for (String param : params) {
                    Request request = new Request.Builder()
                            .url(getHttpUrl(param).toString())
                            .build();

                    Call call = mOkHttpClient.newCall(request);
                    Response response = null;
                    try {
                        response = call.execute();
                        if (response.isSuccessful()) {
                            if (param.equals(DownloadManager.POPULAR_IN_YEAR)) {
                                list.add(param + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                            } else {
                                list.add(param);
                            }
                            list.addAll(convertToList(response.body().string()));
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "doInBackground: IOException", e);
                    } catch (IllegalArgumentException en) {
                        Log.e(TAG, "doInBackground: IlleagarArgumentException", en);
                    }
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Object> result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute - thread: " + Thread.currentThread().getName());
            Log.i("result", result.toString());
           // Result to fragment adapter
            FilmsListFragment filmsListFragment = mFilmsListFragmentWeakReference.get();
            if(filmsListFragment == null) {
                return;
            }
            filmsListFragment.setList(result);
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "onCancelled - thread: " + Thread.currentThread().getName());
        }

        private List<Object> convertToList(String data) {
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(data, JsonObject.class);
            JsonArray results = json.getAsJsonArray("results");
            return gson.fromJson(results, new TypeToken<List<Film>>(){}.getType());
        }


        private HttpUrl getHttpUrl(String param) {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(new Date());
            switch (param) {
                case DownloadManager.IN_THEATRE :
                    SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
                    cal.add(Calendar.DAY_OF_YEAR, -7);
                    Date startDate = cal.getTime();
                    cal.add(Calendar.DAY_OF_YEAR, +14);
                    Date endDate = cal.getTime();
                    return new HttpUrl.Builder()
                            .scheme(MOVIE_API_SCHEME)
                            .host(MOVIE_API_HOST)
                            .addPathSegment(MOVIE_API_VERSION)
                            .addPathSegment("discover")
                            .addPathSegment("movie")
                            .addQueryParameter("primary_release_date.gte", ymdFormat.format(startDate))
                            .addQueryParameter("primary_release_date.lte", ymdFormat.format(endDate))
                            .addQueryParameter("api_key", MOVIE_API_KEY)
                            .build();
                case DownloadManager.POPULAR_IN_YEAR :
                    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                    return new HttpUrl.Builder()
                            .scheme(MOVIE_API_SCHEME)
                            .host(MOVIE_API_HOST)
                            .addPathSegment(MOVIE_API_VERSION)
                            .addPathSegment("discover")
                            .addPathSegment("movie")
                            .addQueryParameter("primary_release_year", yearFormat.format(cal.getTime()))
                            .addQueryParameter("sort_by", "vote_average.desc")
                            .addQueryParameter("api_key", MOVIE_API_KEY)
                            .build();
                default:
                    throw new IllegalArgumentException("Wrong param argument");
            }
        }
    }
}
