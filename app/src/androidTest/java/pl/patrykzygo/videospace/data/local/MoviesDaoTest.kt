package pl.patrykzygo.videospace.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class MoviesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
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
        val movie = MovieEntity(1, false, isOnWatchLater = false)
        dao.insertFavourite(movie)
        val requestedMovie = dao.getAllFavourites()
        assertThat(requestedMovie).contains(movie)
    }

    @Test
    fun updateAndReadMovieTest() = runTest {
        val movie = MovieEntity(1, false, isOnWatchLater = false)
        val movie2 = MovieEntity(1, true, isOnWatchLater = false)
        dao.insertFavourite(movie)
        dao.insertFavourite(movie2)
        val requestedMovie = dao.getAllFavourites()
        assertThat(requestedMovie).doesNotContain(movie)
    }

    @Test
    fun insertMultipleMoviesTest() = runTest {
        val movie = MovieEntity(1, false, isOnWatchLater = false)
        val movie2 = MovieEntity(2, true, isOnWatchLater = false)
        val movie3 = MovieEntity(3, false, isOnWatchLater = false)
        val movie4 = MovieEntity(4, true, isOnWatchLater = false)
        val movies = mutableListOf(movie, movie2, movie3)
        dao.insertFavourites(*movies.toTypedArray(), movie4)
        val requestedMovies = dao.getAllFavourites()
        movies.add(movie4)
        assertThat(requestedMovies).containsExactlyElementsIn(movies)
    }

    @Test
    fun readMoviesWhenTableIsEmptyTest() = runTest {
        val requestedMovies = dao.getAllFavourites()
        assertThat(requestedMovies).isEmpty()
    }
}