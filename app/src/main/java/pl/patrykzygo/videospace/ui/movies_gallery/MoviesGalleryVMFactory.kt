package pl.patrykzygo.videospace.ui.movies_gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSource

class MoviesGalleryVMFactory(val repo: MoviesPagingSource) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MoviesPagingSource::class.java)
            .newInstance(repo)
    }
}