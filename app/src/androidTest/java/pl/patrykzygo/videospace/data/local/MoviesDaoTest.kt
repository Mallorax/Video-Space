package pl.patrykzygo.videospace.data.local

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.AndroidMainDispatcherRule
import pl.patrykzygo.videospace.constants.MovieStatus
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
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        db.close()
        Dispatchers.resetMain()
    }

    @Test
    fun insertAndReadMovieTest() = runTest {
        val movie = createMovieEntity(1)
        var requestedMovie: List<MovieEntity> = listOf()
        val job = launch(mainDispatcherRule.dispatcher) {
            dao.insertMovie(movie)
            requestedMovie = dao.getAllMovies()
        }
        job.join()
        assertThat(requestedMovie).contains(movie)
    }

    @Test
    fun updateAndReadMoviesWithStatusTest() = runTest {
        val movie = createMovieEntity(1, MovieStatus.PLAN_TO_WATCH)
        val movie2 = createMovieEntity(2, MovieStatus.ON_HOLD)
        var requestedMovies: List<MovieEntity> = listOf()
        val job = launch(mainDispatcherRule.dispatcher) {
            dao.insertMovie(movie)
            dao.insertMovie(movie2)
            requestedMovies = dao.getAllMoviesWithStatus(MovieStatus.ON_HOLD)
        }
        job.join()
        assertThat(requestedMovies).doesNotContain(movie)
    }

    @Test
    fun insertMultipleMoviesTest() = runTest {
        val movie = createMovieEntity(1)
        val movie2 = createMovieEntity(2)
        val movie3 = createMovieEntity(3)
        val movie4 = createMovieEntity(4)
        val movies = mutableListOf(movie, movie2, movie3)
        var requestedMovies: List<MovieEntity> = listOf()
        val job = launch(mainDispatcherRule.dispatcher) {
            dao.insertMovies(*movies.toTypedArray(), movie4)
            requestedMovies = dao.getAllMovies()
        }
        movies.add(movie4)
        job.join()
        assertThat(requestedMovies).containsExactlyElementsIn(movies)
    }

    @Test
    fun returnsMoviesWithStatus() = runTest {
        val expected = listOf(
            MovieEntity(1, "1", status = MovieStatus.PLAN_TO_WATCH, releaseDate = ""),
            MovieEntity(2, "2", status = MovieStatus.PLAN_TO_WATCH, releaseDate = ""),
            MovieEntity(3, "3", status = MovieStatus.PLAN_TO_WATCH, releaseDate = "")
        )
        val trash = listOf(
            MovieEntity(4, "1", status = MovieStatus.ON_HOLD, releaseDate = ""),
            MovieEntity(5, "1", status = MovieStatus.DROPPED, releaseDate = ""),
            MovieEntity(6, "1", status = MovieStatus.UNASSIGNED, releaseDate = "")
        )
        dao.insertMovies(*expected.toTypedArray())
        dao.insertMovies(*trash.toTypedArray())
        val actual = dao.getAllMoviesWithStatus(MovieStatus.PLAN_TO_WATCH)
        assertThat(actual).containsExactlyElementsIn(expected)
    }

    @Test
    fun readMoviesWhenTableIsEmptyTest() = runTest {
        var requestedMovies: List<MovieEntity> = listOf()
        val job = launch(mainDispatcherRule.dispatcher) {
            requestedMovies = dao.getAllMovies()
        }
        job.join()
        assertThat(requestedMovies).isEmpty()
    }
}