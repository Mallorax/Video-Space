package pl.patrykzygo.videospace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.patrykzygo.videospace.repository.LocalStoreRepository
import pl.patrykzygo.videospace.repository.MoviesPagingSource
import pl.patrykzygo.videospace.ui.movie_details.MovieDetailsViewModel
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryViewModel
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    val localRepo: LocalStoreRepository,
    val moviesPagingSource: MoviesPagingSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            return modelClass.getConstructor(LocalStoreRepository::class.java)
                .newInstance(localRepo)
        }
        if (modelClass.isAssignableFrom(MoviesGalleryViewModel::class.java)) {
            return modelClass.getConstructor(MoviesPagingSource::class.java)
                .newInstance(moviesPagingSource)
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}