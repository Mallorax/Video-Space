package pl.patrykzygo.videospace.delegate.repos

interface CancellationExceptionCheck {

    fun checkForCancellationException(e: Exception)
}