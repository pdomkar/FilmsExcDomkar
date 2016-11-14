package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Petr on 2. 11. 2016.
 */

public class FilmDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "films.db";
    //public static final String DATABASE_NAME = "films-test.db";
    private static final int DATABASE_VERSION = 22;

    public FilmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FILMS_TABLE = "CREATE TABLE " + FilmCotract.FilmEntry.TABLE_NAME + " (" +
                FilmCotract.FilmEntry._ID + " INTEGER PRIMARY KEY," +
                FilmCotract.FilmEntry.COLUMN_FILM_ID + " INTEGER NOT NULL, " +
                FilmCotract.FilmEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FilmCotract.FilmEntry.COLUMN_RELEASE_DATE + " TEXT," +
                FilmCotract.FilmEntry.COLUMN_POSTER_PATH + " TEXT," +
                FilmCotract.FilmEntry.COLUMN_BACKDROP_PATH + " TEXT," +
                FilmCotract.FilmEntry.COLUMN_VOTE_AVERAGE + " REAL," +
                FilmCotract.FilmEntry.COLUMN_OVERVIEW + " TEXT," +
                "UNIQUE (" + FilmCotract.FilmEntry.COLUMN_FILM_ID + ") ON CONFLICT REPLACE" +
                " );";

        final String SQL_CREATE_DIRECTORS_TABLE = "CREATE TABLE " + FilmCotract.DirectorEntry.TABLE_NAME + " (" +
                FilmCotract.DirectorEntry._ID + " INTEGER PRIMARY KEY," +
                FilmCotract.DirectorEntry.COLUMN_FILM_ID + " INTEGER NOT NULL, " +
                FilmCotract.DirectorEntry.COLUMN_DIRECTOR_NAME + " TEXT, " +
                "UNIQUE (" + FilmCotract.DirectorEntry.COLUMN_FILM_ID + ") ON CONFLICT REPLACE" +
                " );";

        final String SQL_CREATE_FILM_CAST_TABLE = "CREATE TABLE " + FilmCotract.FilmCastEntry.TABLE_NAME + " (" +
                FilmCotract.FilmCastEntry._ID + " INTEGER PRIMARY KEY," +
                FilmCotract.DirectorEntry.COLUMN_FILM_ID + " INTEGER NOT NULL, " +
                FilmCotract.FilmCastEntry.COLUMN_CHARACTER + " TEXT, " +
                FilmCotract.FilmCastEntry.COLUMN_NAME + " TEXT, " +
                FilmCotract.FilmCastEntry.COLUMN_PROFILE_PATH + " TEXT );";

        final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE " + FilmCotract.GenreEntry.TABLE_NAME + " (" +
                FilmCotract.GenreEntry._ID + " INTEGER PRIMARY KEY," +
                FilmCotract.GenreEntry.COLUMN_GENRE_ID + " INTEGER NOT NULL, " +
                FilmCotract.GenreEntry.COLUMN_NAME + " TEXT, " +
                FilmCotract.GenreEntry.COLUMN_SHOW + " INTEGER NOT NULL, " +
                "UNIQUE (" + FilmCotract.GenreEntry.COLUMN_GENRE_ID + ") ON CONFLICT REPLACE" +
                ");";

        db.execSQL(SQL_CREATE_FILMS_TABLE);
        db.execSQL(SQL_CREATE_DIRECTORS_TABLE);
        db.execSQL(SQL_CREATE_FILM_CAST_TABLE);
        db.execSQL(SQL_CREATE_GENRE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FilmCotract.FilmEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FilmCotract.DirectorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FilmCotract.FilmCastEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FilmCotract.GenreEntry.TABLE_NAME);
        onCreate(db);
    }

}
