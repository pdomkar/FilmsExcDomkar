package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;


import android.test.AndroidTestCase;
import android.util.Log;


import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmCotract;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.database.FilmManager;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Cast;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Director;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;

/**
 * Created by Petr on 11. 11. 2016.
 */

public class TestFilmManager extends AndroidTestCase {
    private static final String TAG = TestFilmManager.class.getSimpleName();

    private FilmManager mManager;

    @Override
    protected void setUp() throws Exception {
        mManager = new FilmManager(mContext);
        deleteAll();
    }

    @Override
    public void tearDown() throws Exception {
        deleteAll();
    }

    private void deleteAll() {
        mContext.getContentResolver().delete(
                FilmCotract.FilmEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                FilmCotract.GenreEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                FilmCotract.DirectorEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                FilmCotract.FilmCastEntry.CONTENT_URI,
                null,
                null
        );
    }


    //FILMS TESTS
    public void testCreateFilm() throws Exception {
        Film film = new Film(1L, "Harry poter", "12-32-2019", "sdfsdfsfs", "sdfsfsfsdf", 4.5f, "sdflslf jfsldfjsdf", new int[]{4, 2, 77});
        mManager.createFilm(film);

        List<Film> result = mManager.findFilmsById(film.getId());

        assertTrue(result.size() == 1);
        assertEquals(film.getTitle(), result.get(0).getTitle());
    }

    public void testUpdateFilm() throws Exception {
        Film film = new Film(1L, "Harry poter", "12-32-2019", "sdfsdfsfs", "sdfsfsfsdf", 4.5f, "sdflslf jfsldfjsdf", new int[]{4, 2, 77});
        mManager.createFilm(film);

        film.setTitle("Harry poter edit");
        mManager.updateFilm(film);

        List<Film> result = mManager.findFilmsById(film.getId());

        assertTrue(result.size() == 1);
        assertEquals(film.getTitle(), result.get(0).getTitle());
    }


    public void testDeleteFilm() throws Exception {
        Film film = new Film(1L, "Harry poter", "12-32-2019", "sdfsdfsfs", "sdfsfsfsdf", 4.5f, "sdflslf jfsldfjsdf", new int[]{4, 2, 77});
        mManager.createFilm(film);

        List<Film> result = mManager.findFilmsById(film.getId());
        assertTrue(result.size() == 1);

        mManager.deleteFilm(film);

        List<Film> result2 = mManager.findFilmsById(film.getId());
        assertTrue(result2.size() == 0);
    }

    public void testFindFilmById() throws Exception {
        Film film = new Film(1L, "Harry poter", "12-32-2019", "sdfsdfsfs", "sdfsfsfsdf", 4.5f, "sdflslf jfsldfjsdf", new int[]{4, 2, 77});
        Film film2 = new Film(2L, "Pan prstenu", "12-32-2019", "sdfsdfsfs", "sdfsfsfsdf", 4.5f, "sdflslf jfsldfjsdf", new int[]{4, 2, 77});
        mManager.createFilm(film);
        mManager.createFilm(film2);

        List<Film> result = mManager.findFilmsById(film.getId());

        assertTrue(result.size() == 1);
        assertTrue(result.get(0) instanceof Film);
        assertEquals(film.getTitle(), result.get(0).getTitle());
    }

    public void testFindAllFilm() throws Exception {
        Film film = new Film(1L, "Harry poter", "12-32-2019", "sdfsdfsfs", "sdfsfsfsdf", 4.5f, "sdflslf jfsldfjsdf", new int[]{4, 2, 77});
        Film film2 = new Film(2L, "Pan prstenu", "12-32-2019", "sdfsdfsfs", "sdfsfsfsdf", 4.5f, "sdflslf jfsldfjsdf", new int[]{4, 2, 77});
        mManager.createFilm(film);
        mManager.createFilm(film2);

        List<Film> result = mManager.findFilms();

        assertTrue(result.size() == 2);
        assertTrue(result.get(0) instanceof Film);
        assertTrue(result.get(1) instanceof Film);
    }

    //GENRES TESTS
    public void testCreateGenre() throws Exception {
        Genre genre = new Genre(1L, "Action", true);
        mManager.createGenre(genre);

        List<Genre> result = mManager.findGenreById(genre.getId());

        assertTrue(result.size() == 1);
        assertEquals(genre.getName(), result.get(0).getName());
    }

    public void testUpdateGenre() throws Exception {
        Genre genre = new Genre(1L, "Action", true);
        mManager.createGenre(genre);

        genre.setShow(false);
        mManager.updateGenre(genre);

        List<Genre> result = mManager.findGenreById(genre.getId());

        assertTrue(result.size() == 1);
        assertEquals(genre.isShow(), result.get(0).isShow());
    }

    public void testFindGenreById() throws Exception {
        Genre genre = new Genre(1L, "Action", true);
        Genre genre2 = new Genre(2L, "Adventure", false);
        mManager.createGenre(genre);
        mManager.createGenre(genre2);

        List<Genre> result = mManager.findGenreById(genre.getId());

        assertTrue(result.size() == 1);
        assertTrue(result.get(0) instanceof Genre);
        assertEquals(genre.getName(), result.get(0).getName());
    }

    public void testFindAllGenre() throws Exception {
        Genre genre = new Genre(1L, "Action", true);
        Genre genre2 = new Genre(2L, "Adventure", false);
        mManager.createGenre(genre);
        mManager.createGenre(genre2);

        List<Genre> result = mManager.findGenres();

        assertTrue(result.size() == 2);
        assertTrue(result.get(0) instanceof Genre);
        assertTrue(result.get(1) instanceof Genre);
    }

    public void testFindGenreByShow() throws Exception {
        Genre genre = new Genre(1L, "Action", true);
        Genre genre2 = new Genre(2L, "Adventure", false);
        mManager.createGenre(genre);
        mManager.createGenre(genre2);

        List<Genre> result = mManager.findGenresByShow(false);

        assertTrue(result.size() == 1);
        assertTrue(result.get(0) instanceof Genre);
        assertEquals(genre2.getId(), result.get(0).getId());
    }

    //DIRECTORS TESTS
    public void testCreateDirector() throws Exception {
        Director director = new Director(1L, "Petr");
        mManager.createDirector(director);

        List<Director> result = mManager.findDirectorByFilmId(director.getFilmId());

        assertTrue(result.size() == 1);
        assertEquals(director.getName(), result.get(0).getName());
    }

    public void testDeleteDirector() throws Exception {
        Director director = new Director(1L, "Petr");
        mManager.createDirector(director);

        List<Director> result = mManager.findDirectorByFilmId(director.getFilmId());
        assertTrue(result.size() == 1);

        mManager.deleteDirector(director);

        List<Director> result2 = mManager.findDirectorByFilmId(director.getFilmId());
        assertTrue(result2.size() == 0);
    }

    public void testFindDirectorByFilmId() throws Exception {
        Director director = new Director(1L, "Petr");
        Director director2 = new Director(1L, "Pavel");
        Director director3 = new Director(2L, "Kořen");
        mManager.createDirector(director);
        mManager.createDirector(director2);
        mManager.createDirector(director3);

        List<Director> result = mManager.findDirectorByFilmId(2L);
        assertTrue(result.size() == 1);
        assertTrue(result.get(0) instanceof Director);
    }


    //CASTS TESTS
    public void testCreateCast() throws Exception {
        Cast cast = new Cast("job", "Petr", "sdflkjgljdafgdg");
        mManager.createCast(cast, 1L);

        List<Cast> result = mManager.findCastByFilmId(Long.valueOf(1));

        assertTrue(result.size() == 1);
        assertEquals(cast.getName(), result.get(0).getName());
    }

    public void testDeleteCast() throws Exception {
        Cast cast = new Cast("job", "Petr", "sdflkjgljdafgdg");
        mManager.createCast(cast, 1L);

        List<Cast> result = mManager.findCastByFilmId(Long.valueOf(1));
        assertTrue(result.size() == 1);

        mManager.deleteCast(1L);

        List<Cast> result2 = mManager.findCastByFilmId(Long.valueOf(1));
        assertTrue(result2.size() == 0);
    }

    public void testFindCastByFilmId() throws Exception {
        Cast cast = new Cast("job", "Petr", "sdflkjgldgdfgjdafgdg");
        Cast cast2 = new Cast("job", "Pavel", "sdflkjgljdafgdg");
        mManager.createCast(cast, 1L);
        mManager.createCast(cast, 2L);
        mManager.createCast(cast2, 2L);

        List<Cast> result = mManager.findCastByFilmId(2L);

        assertTrue(result.size() == 2);
        assertTrue(result.get(0) instanceof Cast);
    }

}
