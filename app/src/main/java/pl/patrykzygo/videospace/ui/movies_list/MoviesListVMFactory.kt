package pl.patrykzygo.videospace.ui.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.patrykzygo.videospace.repository.MoviesPagingSource

class MoviesListVMFactory(val repo: MoviesPagingSource): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MoviesPagingSource::class.java)
            .newInstance(repo)
    }
}