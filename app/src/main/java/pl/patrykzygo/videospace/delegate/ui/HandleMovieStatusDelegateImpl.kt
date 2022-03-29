package pl.patrykzygo.videospace.delegate.ui

import pl.patrykzygo.videospace.constants.MovieStatus

class HandleMovieStatusDelegateImpl : HandleMovieStatusDelegate {
    override fun handleMovieStatus(status: String?): String {
        return when (status) {
            "Watching" -> MovieStatus.WATCHING
            "Completed" -> MovieStatus.COMPLETED
            "Plan to Watch" -> MovieStatus.PLAN_TO_WATCH
            "On Hold" -> MovieStatus.ON_HOLD
            "Dropped" -> MovieStatus.DROPPED
            else -> {
                throw IllegalArgumentException()
            }
        }
    }
}