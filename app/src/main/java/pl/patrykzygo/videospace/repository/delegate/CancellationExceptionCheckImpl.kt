package pl.patrykzygo.videospace.repository.delegate

import kotlin.coroutines.cancellation.CancellationException

class CancellationExceptionCheckImpl : CancellationExceptionCheck {

    //This is used to make sure that CancellationException will be ignored
    //while catching exceptions with coroutines
    override fun checkForCancellationException(e: Exception) {
        if (e is CancellationException) {
            throw e
        }
    }
}