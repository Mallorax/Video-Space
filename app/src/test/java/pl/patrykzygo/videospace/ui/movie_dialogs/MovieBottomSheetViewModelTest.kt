package pl.patrykzygo.videospace.ui.movie_dialogs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Spy
import pl.patrykzygo.videospace.data.mapMovieToMovieEntity
import pl.patrykzygo.videospace.repository.FakeLocalStoreRepository
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.util.getOrAwaitValueTest
import pl.patrykzygo.videospace.util.provideMovieWithId

@ExperimentalCoroutinesApi

class MovieBottomSheetViewModelTest {

    private lateinit var viewModel: MovieBottomSheetViewModel

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @Spy
    private lateinit var repository: LocalStoreRepository

    @Before
    fun setup() {
        repository = spy(FakeLocalStoreRepository())
        viewModel = MovieBottomSheetViewModel(repository)

    }

    @Test
    fun `test check if set movie sets movie`() {
        val movie = provideMovieWithId(1)
        viewModel.setMovie(movie)
        val value = viewModel.movie.getOrAwaitValueTest()
        assertThat(value).isEqualTo(movie)
    }

    @Test
    fun `check if null set movie returns null`() {
        val movie = null
        viewModel.setMovie(movie)
        val value = viewModel.movie.getOrAwaitValueTest()
        assertThat(value).isNull()

    }

    @Test
    fun `test if isLiked is toggled to opposite`() {
        val movie = provideMovieWithId(1)
        val isFavourite = movie.isFavourite
        viewModel.setMovie(movie)
        viewModel.changeIsMovieLiked()
        val value = viewModel.movie.getOrAwaitValueTest()
        assertThat(value?.isFavourite).isEqualTo(!isFavourite)
    }

    @Test
    fun `test if isLiked toggle was saved to repo`() = runTest {
        val movie = provideMovieWithId(1)
        viewModel.setMovie(movie)
        viewModel.changeIsMovieLiked()
        verify(repository).insertFavourite(mapMovieToMovieEntity(movie))
    }

    @Test
    fun `test if isSavedToWatchLater is toggled to opposite`() {
        val movie = provideMovieWithId(1)
        val isSavedToWatchLater = movie.isOnWatchLater
        viewModel.setMovie(movie)
        viewModel.changeIsMovieSavedToWatchLater()
        val value = viewModel.movie.getOrAwaitValueTest()
        assertThat(value?.isOnWatchLater).isEqualTo(!isSavedToWatchLater)
    }

    @Test
    fun `test if isSavedToWatchLater toggle was saved to repo`() = runTest {
        val movie = provideMovieWithId(1)
        viewModel.setMovie(movie)
        viewModel.changeIsMovieSavedToWatchLater()
        verify(repository).insertFavourite(mapMovieToMovieEntity(movie))
    }

    @Test
    fun `test is favourite check when movie is favourite`() = runTest {
        val movie = provideMovieWithId(1)
        movie.isFavourite = true
        repository.insertFavourite(mapMovieToMovieEntity(movie))
        movie.isFavourite = false
        viewModel.setMovie(movie)
        val value = viewModel.movie.getOrAwaitValueTest()
        assertThat(value?.isFavourite).isTrue()
    }

    @Test
    fun `test is on watch later when movie is on watch later`() = runTest {
        val movie = provideMovieWithId(1)
        movie.isOnWatchLater = true
        repository.insertFavourite(mapMovieToMovieEntity(movie))
        movie.isOnWatchLater = false
        viewModel.setMovie(movie)
        val value = viewModel.movie.getOrAwaitValueTest()
        assertThat(value?.isOnWatchLater).isTrue()
    }


}