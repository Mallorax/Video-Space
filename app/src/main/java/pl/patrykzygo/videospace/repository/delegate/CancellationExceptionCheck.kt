package pl.patrykzygo.videospace.repository.delegate

interface CancellationExceptionCheck {

    fun checkForCancellationException(e: Exception)
}