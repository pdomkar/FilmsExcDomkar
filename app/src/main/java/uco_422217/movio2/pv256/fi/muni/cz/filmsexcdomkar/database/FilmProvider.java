package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by Petr on 2. 11. 2016.
 */

public class FilmProvider extends ContentProvider {
    private static final String TAG = FilmProvider.class.getSimpleName();

    private static final int FILM = 100;
    private static final int FILM_ID = 101;
    private static final int DIRECTOR = 200;
    private static final int DIRECTOR_ID = 201;
    private static final int CAST = 300;
    private static final int CAST_ID = 301;
    private static final int GENRE = 400;
    private static final int GENRE_ID = 401;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FilmDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FilmCotract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FilmCotract.PATH_FILMS, FILM);
        matcher.addURI(authority, FilmCotract.PATH_FILMS + "/#", FILM_ID);
        matcher.addURI(authority, FilmCotract.PATH_DIRECTORS, DIRECTOR);
        matcher.addURI(authority, FilmCotract.PATH_DIRECTORS + "/#", DIRECTOR_ID);
        matcher.addURI(authority, FilmCotract.PATH_FILM_CASTS, CAST);
        matcher.addURI(authority, FilmCotract.PATH_FILM_CASTS + "/#", CAST_ID);
        matcher.addURI(authority, FilmCotract.PATH_GENRES, GENRE);
        matcher.addURI(authority, FilmCotract.PATH_GENRES + "/#", GENRE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FilmDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, Arrays.toString(selectionArgs));
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case FILM_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmCotract.FilmEntry.TABLE_NAME,
                        projection,
                        FilmCotract.FilmEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FILM: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmCotract.FilmEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case DIRECTOR_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmCotract.DirectorEntry.TABLE_NAME,
                        projection,
                        FilmCotract.DirectorEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case DIRECTOR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmCotract.DirectorEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CAST_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmCotract.FilmCastEntry.TABLE_NAME,
                        projection,
                        FilmCotract.FilmCastEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CAST: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmCotract.FilmCastEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case GENRE_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmCotract.FilmCastEntry.TABLE_NAME,
                        projection,
                        FilmCotract.FilmCastEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case GENRE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmCotract.GenreEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) throws SQLException {
        Log.d(TAG, values.toString());

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FILM: {
                long _id = db.insert(FilmCotract.FilmEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = FilmCotract.FilmEntry.buildWorkTimeUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case DIRECTOR: {
                long _id = db.insert(FilmCotract.DirectorEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = FilmCotract.DirectorEntry.buildWorkTimeUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CAST: {
                long _id = db.insert(FilmCotract.FilmCastEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = FilmCotract.FilmCastEntry.buildWorkTimeUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case GENRE: {
                long _id = db.insert(FilmCotract.GenreEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = FilmCotract.GenreEntry.buildWorkTimeUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FILM:
                rowsUpdated = db.update(FilmCotract.FilmEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case DIRECTOR:
                rowsUpdated = db.update(FilmCotract.DirectorEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CAST:
                rowsUpdated = db.update(FilmCotract.FilmCastEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case GENRE:
                Log.i("Profisdl", values.getAsInteger("show") + "");
                Log.i("asdf", selectionArgs[0]);
                rowsUpdated = db.update(FilmCotract.GenreEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case FILM:
                rowsDeleted = db.delete(FilmCotract.FilmEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DIRECTOR:
                rowsDeleted = db.delete(FilmCotract.DirectorEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CAST:
                rowsDeleted = db.delete(FilmCotract.FilmCastEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GENRE:
                rowsDeleted = db.delete(FilmCotract.GenreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FILM:
                return FilmCotract.FilmEntry.CONTENT_TYPE;
            case FILM_ID:
                return FilmCotract.FilmEntry.CONTENT_ITEM_TYPE;
            case DIRECTOR:
                return FilmCotract.DirectorEntry.CONTENT_TYPE;
            case DIRECTOR_ID:
                return FilmCotract.DirectorEntry.CONTENT_ITEM_TYPE;
            case CAST:
                return FilmCotract.FilmCastEntry.CONTENT_TYPE;
            case CAST_ID:
                return FilmCotract.FilmCastEntry.CONTENT_ITEM_TYPE;
            case GENRE:
                return FilmCotract.GenreEntry.CONTENT_TYPE;
            case GENRE_ID:
                return FilmCotract.GenreEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
