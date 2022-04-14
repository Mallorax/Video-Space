package pl.patrykzygo.videospace.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.network.auth.CreateSessionBody
import pl.patrykzygo.videospace.networking.AuthenticationEntryPoint
import javax.inject.Inject

@HiltViewModel
class LoggedInViewModel @Inject constructor(private val auth: AuthenticationEntryPoint) :
    ViewModel() {

    private val _sessionEvent = LiveEvent<String>()
    val sessionEvent: LiveData<String> get() = _sessionEvent

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun createSession(requestToken: String) {
        val sessionBody = CreateSessionBody(requestToken)
        viewModelScope.launch(Dispatchers.IO) {
            val response = auth.createSessionId(body = sessionBody)
            if (response.isSuccessful && response.body() != null) {
                _sessionEvent.postValue(response.body()!!.sessionId)
                _message.postValue(response.body()!!.success.toString())
            } else {
                _message.postValue(response.message())
            }
        }
    }

}