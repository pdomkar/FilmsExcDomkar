package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
 * Created by Petr on 2. 11. 2016.
 */

public class FilmCotract {
    public static final String CONTENT_AUTHORITY = "cz.muni.fi.pv256.movio2.uco_422217";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FILMS = "film";
    public static final String PATH_DIRECTORS = "director";
    public static final String PATH_FILM_CASTS = "cast";
    public static final String PATH_GENRES = "genre";


    public static final String DATE_FORMAT = "yyyyMMddHHmm";

    /**
     * Converts Date class to a string representation, used for easy comparison and database
     * lookup.
     *
     * @param date The input date
     * @return a DB-friendly representation of the date, using the format defined in DATE_FORMAT.
     */
    public static String getDbDateString(DateTime date) {
        return date.toString(DATE_FORMAT);
    }

    /**
     * Converts a dateText to a long Unix time representation
     *
     * @param dateText the input date string
     * @return the Date object
     */
    public static DateTime getDateFromDb(String dateText) {
        return DateTime.parse(dateText, DateTimeFormat.forPattern(DATE_FORMAT).withOffsetParsed());
    }

    public static final class FilmEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILMS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FILMS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FILMS;


        public static final String TABLE_NAME = "films";

        public static final String COLUMN_FILM_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";


        public static Uri buildWorkTimeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class DirectorEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DIRECTORS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_DIRECTORS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_DIRECTORS;


        public static final String TABLE_NAME = "directors";


        public static final String COLUMN_FILM_ID = "film_id";
        public static final String COLUMN_DIRECTOR_NAME = "name";

        public static Uri buildWorkTimeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class FilmCastEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILM_CASTS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FILM_CASTS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FILM_CASTS;


        public static final String TABLE_NAME = "casts";


        public static final String COLUMN_FILM_ID = "film_id";
        public static final String COLUMN_CHARACTER = "character";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PROFILE_PATH = "profile_path";

        public static Uri buildWorkTimeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class GenreEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRES).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_GENRES;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_GENRES;

        public static final String TABLE_NAME = "genres";

        public static final String COLUMN_GENRE_ID = "genre_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SHOW = "show";

        public static Uri buildWorkTimeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
