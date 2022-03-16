package pl.patrykzygo.videospace.ui.user_lists

import androidx.lifecycle.ViewModel
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider

class UserListsViewModel constructor(
    private val repo: LocalStoreRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

}