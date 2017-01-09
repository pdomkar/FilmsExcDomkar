package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.BuildConfig;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Director;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;

/**
 * Created by Petr on 2. 11. 2016.
 */

public class FilmManager {
    public static final int COL_ID = 0;
    public static final int COL_FILM_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_RELEASE_DATE = 3;
    public static final int COL_POSTER_PATH = 4;
    public static final int COL_BACKDROP_PATH = 5;
    public static final int COL_VOTE_AVERAGE = 6;
    public static final int COL_OVERVIEW = 7;

    private static final String[] FILM_COLUMNS = {
            FilmCotract.FilmEntry._ID,
            FilmCotract.FilmEntry.COLUMN_FILM_ID,
            FilmCotract.FilmEntry.COLUMN_TITLE,
            FilmCotract.FilmEntry.COLUMN_RELEASE_DATE,
            FilmCotract.FilmEntry.COLUMN_POSTER_PATH,
            FilmCotract.FilmEntry.COLUMN_BACKDROP_PATH,
            FilmCotract.FilmEntry.COLUMN_VOTE_AVERAGE,
            FilmCotract.FilmEntry.COLUMN_OVERVIEW
    };

    public static final int DIRECTOR_COL_ID = 0;
    public static final int DIRECTOR_COL_FILM_ID = 1;
    public static final int DIRECTOR_COL_DIRECTOR_NAME = 2;

    private static final String[] DIRECTOR_COLUMNS = {
            FilmCotract.DirectorEntry._ID,
            FilmCotract.DirectorEntry.COLUMN_FILM_ID,
            FilmCotract.DirectorEntry.COLUMN_DIRECTOR_NAME,
    };

    public static final int CAST_COL_ID = 0;
    public static final int CAST_COL_FILM_ID = 1;
    public static final int CAST_COL_CHARACTER = 2;
    public static final int CAST_COL_NAME = 3;
    public static final int CAST_COL_PROFILE_PATH = 4;

    private static final String[] CAST_COLUMNS = {
            FilmCotract.FilmCastEntry._ID,
            FilmCotract.FilmCastEntry.COLUMN_FILM_ID,
            FilmCotract.FilmCastEntry.COLUMN_CHARACTER,
            FilmCotract.FilmCastEntry.COLUMN_NAME,
            FilmCotract.FilmCastEntry.COLUMN_PROFILE_PATH,
    };

    public static final int GENRE_COL_ID = 0;
    public static final int GENRE_COL_GENRE_ID = 1;
    public static final int GENRE_COL_NAME = 2;
    public static final int GENRE_COL_SHOW = 3;

    private static final String[] GENRE_COLUMNS = {
            FilmCotract.GenreEntry._ID,
            FilmCotract.GenreEntry.COLUMN_GENRE_ID,
            FilmCotract.GenreEntry.COLUMN_NAME,
            FilmCotract.GenreEntry.COLUMN_SHOW,
    };

    private static final String LOCAL_DATE_FORMAT = "yyyyMMdd";

    private static final String WHERE_FILM_ID = FilmCotract.FilmEntry.COLUMN_FILM_ID + " = ?";
    private static final String WHERE_DIRECTOR_FILM_ID = FilmCotract.DirectorEntry.COLUMN_FILM_ID + " = ?";
    private static final String WHERE_CAST_FILM_ID = FilmCotract.FilmCastEntry.COLUMN_FILM_ID + " = ?";
    private static final String WHERE_GENRE_ID = FilmCotract.GenreEntry.COLUMN_GENRE_ID + " = ?";
    private static final String WHERE_GENRE_SHOW = FilmCotract.GenreEntry.COLUMN_SHOW + " = ?";

    private Context mContext;

    public FilmManager(Context context) {
        mContext = context.getApplicationContext();
    }


    //FILM
    public List<Film> findFilms() {
        Cursor cursor = mContext.getContentResolver().query(FilmCotract.FilmEntry.CONTENT_URI, FILM_COLUMNS, "", new String[]{}, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Film> films = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    films.add(getFilm(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return films;
        }
        if (cursor != null) {
            cursor.close();
        }

        return Collections.emptyList();
    }

    public List<Film> findFilmsById(Long id) {
        Cursor cursor = mContext.getContentResolver().query(FilmCotract.FilmEntry.CONTENT_URI, FILM_COLUMNS, WHERE_FILM_ID, new String[]{String.valueOf(id)}, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Film> films = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    films.add(getFilm(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return films;
        }
        if (cursor != null) {
            cursor.close();
        }

        return Collections.emptyList();
    }


    public void createFilm(Film film) {
        if (film == null) {
            throw new NullPointerException("film == null");
        }
        if (film.getTitle() == null) {
            throw new IllegalStateException("film title cannot be null");
        }

        ContentUris.parseId(mContext.getContentResolver().insert(FilmCotract.FilmEntry.CONTENT_URI, prepareFilmValues(film)));
    }

    public void updateFilm(Film film) {
        if (film == null) {
            throw new NullPointerException("film == null");
        }
        if (film.getId() == null) {
            throw new IllegalStateException("film id cannot be null");
        }
        if (film.getTitle() == null) {
            throw new IllegalStateException("film title cannot be null");
        }

        mContext.getContentResolver().update(FilmCotract.FilmEntry.CONTENT_URI, prepareFilmValues(film), WHERE_FILM_ID, new String[]{String.valueOf(film.getId())});
    }

    public void deleteFilm(Film film) {
        if (film == null) {
            throw new NullPointerException("film == null");
        }
        if (film.getId() == null) {
            throw new IllegalStateException("film id must be set");
        }

        mContext.getContentResolver().delete(FilmCotract.FilmEntry.CONTENT_URI, WHERE_FILM_ID, new String[]{String.valueOf(film.getId())});
    }


    //DIRECTOR

    public List<Director> findDirectorByFilmId(Long id) {
        Cursor cursor = mContext.getContentResolver().query(FilmCotract.DirectorEntry.CONTENT_URI, DIRECTOR_COLUMNS, WHERE_DIRECTOR_FILM_ID, new String[]{String.valueOf(id)}, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Director> directors = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    directors.add(getDirector(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return directors;
        }
        if (cursor != null) {
            cursor.close();
        }

        return Collections.emptyList();
    }

    public void createDirector(Director director) {
        if (director == null) {
            throw new NullPointerException("director == null");
        }
        if (director.getFilmId() == null) {
            throw new IllegalStateException("director film id cannot be null");
        }
        if (director.getName() == null) {
            throw new IllegalStateException("director name cannot be null");
        }

        ContentUris.parseId(mContext.getContentResolver().insert(FilmCotract.DirectorEntry.CONTENT_URI, prepareDirectorValues(director)));
    }


    public void deleteDirector(Director director) {
        if (director == null) {
            throw new NullPointerException("director == null");
        }
        if (director.getFilmId() == null) {
            throw new IllegalStateException("director film id must be set");
        }

        mContext.getContentResolver().delete(FilmCotract.DirectorEntry.CONTENT_URI, WHERE_DIRECTOR_FILM_ID, new String[]{String.valueOf(director.getFilmId())});
    }


    //CAST
    public List<Cast> findCastByFilmId(Long castFilmId) {
        Cursor cursor = mContext.getContentResolver().query(FilmCotract.FilmCastEntry.CONTENT_URI, CAST_COLUMNS, WHERE_CAST_FILM_ID, new String[]{String.valueOf(castFilmId)}, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Cast> casts = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    casts.add(getCast(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return casts;
        }
        if (cursor != null) {
            cursor.close();
        }

        return Collections.emptyList();
    }

    public void createCast(Cast cast, Long castFilmId) {
        if (cast == null) {
            throw new NullPointerException("director == null");
        }
        if (castFilmId == null) {
            throw new IllegalStateException("film id cannot be null");
        }

        mContext.getContentResolver().insert(FilmCotract.FilmCastEntry.CONTENT_URI, prepareCastValues(cast, castFilmId));
    }


    public void deleteCast(Long castFilmId) {
        if (castFilmId == null) {
            throw new IllegalStateException("film id cannot be null");
        }

        mContext.getContentResolver().delete(FilmCotract.FilmCastEntry.CONTENT_URI, WHERE_CAST_FILM_ID, new String[]{String.valueOf(castFilmId)});
    }

    //GENRE
    public List<Genre> findGenreById(Long id) {
        Cursor cursor = mContext.getContentResolver().query(FilmCotract.GenreEntry.CONTENT_URI, GENRE_COLUMNS, WHERE_GENRE_ID, new String[]{String.valueOf(id)}, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Genre> genres = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    genres.add(getGenre(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return genres;
        }
        if (cursor != null) {
            cursor.close();
        }

        return Collections.emptyList();
    }

    public List<Genre> findGenresByShow(Boolean show) {
        Cursor cursor = mContext.getContentResolver().query(FilmCotract.GenreEntry.CONTENT_URI, GENRE_COLUMNS, WHERE_GENRE_SHOW, new String[]{String.valueOf(show ? 1 : 0)}, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Genre> genres = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    genres.add(getGenre(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return genres;
        }
        if (cursor != null) {
            cursor.close();
        }

        return Collections.emptyList();
    }

    public List<Genre> findGenres() {
        Cursor cursor = mContext.getContentResolver().query(FilmCotract.GenreEntry.CONTENT_URI, GENRE_COLUMNS, "", new String[]{}, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Genre> genres = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    genres.add(getGenre(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return genres;
        }
        if (cursor != null) {
            cursor.close();
        }

        return Collections.emptyList();
    }

    public void createGenre(Genre genre) {
        if (genre == null) {
            throw new NullPointerException("genre == null");
        }
        if (genre.getId() == null) {
            throw new IllegalStateException("genre id cannot be null");
        }

        mContext.getContentResolver().insert(FilmCotract.GenreEntry.CONTENT_URI, prepareGenreValues(genre));
    }

    public void updateGenre(Genre genre) {
        if (genre == null) {
            throw new NullPointerException("genre == null");
        }
        if (genre.getId() == null) {
            throw new IllegalStateException("genre id cannot be null");
        }
        if(BuildConfig.logging) {
            Log.i("manage", genre.getId() + "");
        }
        mContext.getContentResolver().update(FilmCotract.GenreEntry.CONTENT_URI, prepareGenreValues(genre), WHERE_GENRE_ID, new String[]{String.valueOf(genre.getId())});
    }

    private ContentValues prepareFilmValues(Film film) {
        ContentValues values = new ContentValues();
        values.put(FilmCotract.FilmEntry.COLUMN_FILM_ID, film.getId());
        values.put(FilmCotract.FilmEntry.COLUMN_TITLE, film.getTitle());
        values.put(FilmCotract.FilmEntry.COLUMN_RELEASE_DATE, film.getReleaseDate());
        values.put(FilmCotract.FilmEntry.COLUMN_POSTER_PATH, film.getCoverPath());
        values.put(FilmCotract.FilmEntry.COLUMN_BACKDROP_PATH, film.getBackdropPath());
        values.put(FilmCotract.FilmEntry.COLUMN_VOTE_AVERAGE, film.getVoteAverage());
        values.put(FilmCotract.FilmEntry.COLUMN_OVERVIEW, film.getOverview());
        return values;
    }

    private Film getFilm(Cursor cursor) {
        Film film = new Film();
        film.setId(cursor.getLong(COL_FILM_ID));
        film.setTitle(cursor.getString(COL_TITLE));
        film.setReleaseDate(cursor.getString(COL_RELEASE_DATE));
        film.setCoverPath(cursor.getString(COL_POSTER_PATH));
        film.setBackdropPath(cursor.getString(COL_BACKDROP_PATH));
        film.setVoteAverage(cursor.getFloat(COL_VOTE_AVERAGE));
        film.setOverview(cursor.getString(COL_OVERVIEW));
        return film;
    }

    private ContentValues prepareDirectorValues(Director director) {
        ContentValues values = new ContentValues();
        values.put(FilmCotract.DirectorEntry.COLUMN_FILM_ID, director.getFilmId());
        values.put(FilmCotract.DirectorEntry.COLUMN_DIRECTOR_NAME, director.getName());
        return values;
    }

    private Director getDirector(Cursor cursor) {
        Director director = new Director();
        director.setFilmId(cursor.getLong(DIRECTOR_COL_FILM_ID));
        director.setName(cursor.getString(DIRECTOR_COL_DIRECTOR_NAME));
        return director;
    }

    private ContentValues prepareCastValues(Cast cast, Long filmId) {
        ContentValues values = new ContentValues();
        values.put(FilmCotract.FilmCastEntry.COLUMN_FILM_ID, filmId);
        values.put(FilmCotract.FilmCastEntry.COLUMN_CHARACTER, cast.getCharacter());
        values.put(FilmCotract.FilmCastEntry.COLUMN_NAME, cast.getName());
        values.put(FilmCotract.FilmCastEntry.COLUMN_PROFILE_PATH, cast.getProfilePath());
        return values;
    }

    private Cast getCast(Cursor cursor) {
        Cast cast = new Cast();
        cast.setCharacter(cursor.getString(CAST_COL_CHARACTER));
        cast.setName(cursor.getString(CAST_COL_NAME));
        cast.setProfilePath(cursor.getString(CAST_COL_PROFILE_PATH));
        return cast;
    }

    private ContentValues prepareGenreValues(Genre genre) {
        ContentValues values = new ContentValues();
        values.put(FilmCotract.GenreEntry.COLUMN_GENRE_ID, genre.getId());
        values.put(FilmCotract.GenreEntry.COLUMN_NAME, genre.getName());
        values.put(FilmCotract.GenreEntry.COLUMN_SHOW, genre.isShow() ? 1 : 0);
        return values;
    }

    private Genre getGenre(Cursor cursor) {
        Genre genre = new Genre();
        genre.setId(cursor.getLong(GENRE_COL_GENRE_ID));
        genre.setName(cursor.getString(GENRE_COL_NAME));
        genre.setShow(cursor.getInt(GENRE_COL_SHOW) > 0);
        return genre;
    }
}
