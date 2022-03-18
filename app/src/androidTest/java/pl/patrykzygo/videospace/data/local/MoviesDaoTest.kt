package pl.patrykzygo.videospace.data.local

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.AndroidMainDispatcherRule
import pl.patrykzygo.videospace.util.createMovieEntity
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class MoviesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mainDispatcherRule = AndroidMainDispatcherRule()

    @Inject
    lateinit var db: VideoSpaceDatabase
    lateinit var dao: MoviesDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = db.moviesDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndReadMovieTest() = runTest {
        val movie = createMovieEntity(1)
        var requestedMovie: List<MovieEntity> = listOf()
        val job = launch(mainDispatcherRule.dispatcher) {
            dao.insertFavourite(movie)
            requestedMovie = dao.getAllMovies()
        }
        job.join()
        assertThat(requestedMovie).contains(movie)
    }

    @Test
    fun updateAndReadMovieTest() = runTest {
        val movie = createMovieEntity(1)
        val movie2 = createMovieEntity(2, isFavourite = true)
        var requestedMovies: List<MovieEntity> = listOf()
        val job = launch(mainDispatcherRule.dispatcher) {
            dao.insertFavourite(movie)
            dao.insertFavourite(movie2)
            requestedMovies = dao.getAllFavourites()
        }
        job.join()
        assertThat(requestedMovies).doesNotContain(movie)
    }

    @Test
    fun insertMultipleMoviesTest() = runTest {
        val movie = createMovieEntity(1, isFavourite = true)
        val movie2 = createMovieEntity(2, isFavourite = true)
        val movie3 = createMovieEntity(3, isFavourite = true)
        val movie4 = createMovieEntity(4, isFavourite = true)
        val movies = mutableListOf(movie, movie2, movie3)
        var requestedMovies: List<MovieEntity> = listOf()
        val job = launch(mainDispatcherRule.dispatcher) {
            dao.insertFavourites(*movies.toTypedArray(), movie4)
            requestedMovies = dao.getAllMovies()
        }
        movies.add(movie4)
        job.join()
        assertThat(requestedMovies).containsExactlyElementsIn(movies)
    }

    @Test
    fun readMoviesWhenTableIsEmptyTest() = runTest {
        var requestedMovies: List<MovieEntity> = listOf()
        val job = launch(mainDispatcherRule.dispatcher) {
            requestedMovies = dao.getAllFavourites()
        }
        job.join()
        assertThat(requestedMovies).isEmpty()
    }
}