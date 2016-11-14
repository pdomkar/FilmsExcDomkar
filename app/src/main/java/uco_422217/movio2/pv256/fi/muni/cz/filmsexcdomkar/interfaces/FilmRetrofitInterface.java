package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Credits;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.FilmResponse;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.GenreResponse;

/**
 * Created by Petr on 24. 10. 2016.
 */

public interface FilmRetrofitInterface {
    @GET("discover/movie/")
    Call<FilmResponse> findFilmsInTheatre(@Query("primary_release_date.gte") String startDate, @Query("primary_release_date.lte") String endDate, @Query("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("discover/movie/")
    Call<FilmResponse> findFilmsPopularInYear(@Query("primary_release_year.gte") String year, @Query("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<Credits> findFilmCredits(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Call<GenreResponse> findGenres(@Query("api_key") String apiKey);
}
