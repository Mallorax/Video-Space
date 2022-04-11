package pl.patrykzygo.videospace.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.constants.Paths
import pl.patrykzygo.videospace.networking.AuthenticationEntryPoint
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val auth: AuthenticationEntryPoint) : ViewModel() {

    private val _authEvent = LiveEvent<String>()
    val authEvent: LiveData<String> get() = _authEvent

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun launchAuthEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val authTokenResponse = auth.requestAuthToken()
            if (authTokenResponse.isSuccessful) {
                val requestToken = authTokenResponse.body()?.requestToken ?: return@launch
                _authEvent.postValue(Paths.AUTH_USER + requestToken)
            } else {
                _errorMessage.postValue(authTokenResponse.message())
            }
        }
    }
}