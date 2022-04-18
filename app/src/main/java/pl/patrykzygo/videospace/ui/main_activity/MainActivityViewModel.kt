package pl.patrykzygo.videospace.ui.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.constants.Paths
import pl.patrykzygo.videospace.data.network.auth.CreateSessionBody
import pl.patrykzygo.videospace.networking.AuthenticationEntryPoint
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject constructor(private val userAuthEndpoint: AuthenticationEntryPoint) :
    ViewModel() {

    private val _authEvent = LiveEvent<String>()
    val authEvent: LiveData<String> get() = _authEvent

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String> get() = _sessionId

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _wasTabShown = MutableLiveData<Boolean>()
    val wasTabShown: LiveData<Boolean> get() = _wasTabShown

    private var requestToken: String? = null

    fun setWasTabShown(wasTabShown: Boolean) {
        _wasTabShown.value = wasTabShown
    }

    fun restoreSession(sessionId: String) {
        _sessionId.value = sessionId
    }

    fun launchAuthEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val authTokenResponse = userAuthEndpoint.requestAuthToken()
            if (authTokenResponse.isSuccessful) {
                requestToken = authTokenResponse.body()?.requestToken ?: return@launch
                _authEvent.postValue(Paths.AUTH_USER + requestToken)
            } else {
                _errorMessage.postValue(authTokenResponse.message())
            }
        }
    }


    fun createSession() {
        requestToken?.let {
            val sessionBody = CreateSessionBody(requestToken!!)
            viewModelScope.launch(Dispatchers.IO) {
                val response = userAuthEndpoint.createSessionId(body = sessionBody)
                if (response.isSuccessful && response.body() != null) {
                    _sessionId.postValue(response.body()!!.sessionId)
                    _message.postValue(response.body()!!.success.toString())
                } else {
                    _message.postValue(response.message())
                }
            }
        }
    }

}