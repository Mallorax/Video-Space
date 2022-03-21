package pl.patrykzygo.videospace.ui.movie_search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.MainDispatcherRule
import pl.patrykzygo.videospace.TestDispatcherProvider
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.repository.FakeLocalStoreGenresRepository
import pl.patrykzygo.videospace.util.fakeGenreList
import pl.patrykzygo.videospace.util.getOrAwaitValueTest

@ExperimentalCoroutinesApi
class SearchMovieViewModelTest {

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchMovieViewModel
    private val genreRepo = FakeLocalStoreGenresRepository(fakeGenreList().toMutableList())

    @Before
    fun setup() {
        viewModel = SearchMovieViewModel(
            genreRepo,
            TestDispatcherProvider()
        )
    }

    @Test
    fun `getAllGenres is success with genres posted`() {
        val expected = genreRepo.genres
        viewModel.getAllGenre()
        val actual = viewModel.genres.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `getAllGenres is failure with error message posted`() {
        val expected = "test error"
        genreRepo.genres = mutableListOf()
        viewModel.getAllGenre()
        val actual = viewModel.errorMessage.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `addIncludedGenre, genre is added to included`() {
        val genreName = "1"
        viewModel.getAllGenre()
        viewModel.addIncludedGenre(genreName)
        val expected =
            viewModel.genres.getOrAwaitValueTest().filter { t -> t.genreName == genreName }
        assertThat(viewModel.includedGenres).isEqualTo(expected)
    }

    @Test
    fun `removeIncludedGenre, is removed from included`() {
        viewModel.getAllGenre()
        viewModel.addIncludedGenre("1")
        viewModel.addIncludedGenre("2")
        viewModel.addIncludedGenre("3")
        val expected = viewModel.genres.getOrAwaitValueTest().filter { t ->
            t.genreName == "1" ||
                    t.genreName == "2"
        }
        viewModel.removeIncludedGenre("3")
        assertThat(viewModel.includedGenres).isEqualTo(expected)
    }

    @Test
    fun `addExcludedGenre, is added to excluded`() {
        val genreName = "1"
        viewModel.getAllGenre()
        viewModel.addExcludedGenre(genreName)
        val expected =
            viewModel.genres.getOrAwaitValueTest().filter { t -> t.genreName == genreName }
        assertThat(viewModel.excludedGenres).isEqualTo(expected)
    }

    @Test
    fun `removeExcludedGenre, is removed from excluded`() {
        viewModel.getAllGenre()
        viewModel.addExcludedGenre("1")
        viewModel.addExcludedGenre("2")
        viewModel.addExcludedGenre("3")
        val expected = viewModel.genres.getOrAwaitValueTest().filter { t ->
            t.genreName == "1" ||
                    t.genreName == "2"
        }
        viewModel.removeExcludedGenre("3")
        assertThat(viewModel.excludedGenres).isEqualTo(expected)
    }

    @Test
    fun `submitRequest, request is submitted correctly`() {
        val minScore = 1
        val minVotes = "3"
        val expectedIncludedGenres = "1,2"
        val expectedExcludedGenres = "3,4"
        val expectedRequest = DiscoverMovieRequest(
            expectedIncludedGenres,
            expectedExcludedGenres,
            minScore,
            minVotes.toInt()
        )
        var actualRequest: DiscoverMovieRequest? = null
        viewModel.getAllGenre()
        viewModel.addIncludedGenre("1")
        viewModel.addIncludedGenre("2")
        viewModel.addExcludedGenre("3")
        viewModel.addExcludedGenre("4")
        viewModel.requestMoviesLiveEvent.observeForever{ actualRequest = it}
        viewModel.submitRequest(minScore, minVotes)
        assertThat(actualRequest).isEqualTo(expectedRequest)
    }

    @Test
    fun `submitRequest has throws error with incorrect vote count`(){
        viewModel.submitRequest(1, "sadasd")
        val expectedMessage = "Vote count has to be a number"
        val actualMessage = viewModel.submitRequestInputErrorMessage.getOrAwaitValueTest()
        assertThat(actualMessage).isEqualTo(expectedMessage)
    }

    @Test
    fun `submitRequest minScore is null`(){
        val expectedMessage = "Something wrong with minimum score"
        viewModel.submitRequest(null, "1")
        val actualMessage = viewModel.submitRequestInputErrorMessage.getOrAwaitValueTest()
        assertThat(actualMessage).isEqualTo(expectedMessage)
    }
}