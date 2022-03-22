package pl.patrykzygo.videospace.ui.delegate

import pl.patrykzygo.videospace.others.MovieStatus

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