package pl.patrykzygo.videospace

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import pl.patrykzygo.videospace.workers.MovieGenreWorker
import pl.patrykzygo.videospace.workers.createStatusNotification
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class VideoSpaceApplication : Application(), Configuration.Provider {

    //Not sure if launching work manager here is good idea
    //However I don't want to have any android related things in viewmodels
    //in order to have easier time testing, thus I've decided to put it here
    //at least for now

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleWorker()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun scheduleWorker(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<MovieGenreWorker>(
            1, TimeUnit.DAYS,
            5, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        Timber.d("Work enqued")
    }
}