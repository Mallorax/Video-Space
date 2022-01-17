package pl.patrykzygo.videospace.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class GenreDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var db: VideoSpaceDatabase
    lateinit var dao: GenreDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = db.genreDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndReadGenreTest() = runBlockingTest {
        val genre = GenreEntity(1, "genre")
        dao.insertGenre(genre)
        val requestedGenre = dao.getGenre(genre.id)
        assertThat(requestedGenre?.name)
            .isEqualTo(genre.name)
    }

    @Test
    fun updateAndReadTest() = runBlockingTest {
        val genre1 = GenreEntity(1, "genre")
        val genre2 = GenreEntity(1, "genre2")
        dao.insertGenre(genre1)
        dao.insertGenre(genre2)
        val requestedGenre = dao.getGenre(1)
        assertThat(requestedGenre?.name)
            .isEqualTo(genre2.name)
    }

    @Test
    fun insertMultipleGenresTest() = runBlockingTest {
        val genre1 = GenreEntity(1, "genre1")
        val genre2 = GenreEntity(2, "genre2")
        val genre3 = GenreEntity(3, "genre3")
        val genre4 = GenreEntity(4, "genre4")
        val genres = listOf(genre1, genre2, genre3)
        dao.insertGenres(*genres.toTypedArray(), genre4)
        val requestedGenres = dao.getGenres()
        assertThat(requestedGenres).containsAtLeastElementsIn(genres)
    }

    @Test
    fun readNotExistTest() = runBlockingTest {
        val requestedGenre = dao.getGenre(1)
        assertThat(requestedGenre?.id).isEqualTo(null)
    }

    @Test
    fun readGenresWhenTableIsEmptyTest() = runBlockingTest {
        val requestedGenres = dao.getGenres()
        assertThat(requestedGenres).isEmpty()
    }


}