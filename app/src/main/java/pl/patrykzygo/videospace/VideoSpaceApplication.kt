package pl.patrykzygo.videospace

import android.app.Application
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import pl.patrykzygo.videospace.workers.MovieGenreWorker
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class VideoSpaceApplication : Application() {

    //Not sure if launching work manager here is good idea
    //However I don't want to have any android related things in viewmodels
    //in order to have easier time testing, thus I've decided to put it here
    //at least for now

    override fun onCreate() {
        super.onCreate()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<MovieGenreWorker>(
            1, TimeUnit.HOURS,
            30, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            ).build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}