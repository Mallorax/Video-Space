package pl.patrykzygo.videospace.ui.movie_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.ui.DispatchersProvider
import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.repository.RepositoryResponse

class SearchMovieViewModel constructor(
    private val repo: LocalStoreRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> get() = _genres

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getAllGenres(){
        viewModelScope.launch(dispatchersProvider.io) {
            val repoResponse = repo.getAllGenres()
            if (repoResponse.status == RepositoryResponse.Status.SUCCESS){
                _genres.postValue(repoResponse.data!!)
            }else{
                _errorMessage.postValue(repoResponse.message!!)
            }
        }
    }
}