package pl.patrykzygo.videospace.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext
import pl.patrykzygo.videospace.constants.WorkerConstants
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.delegate.repos.CacheGenresDelegate
import pl.patrykzygo.videospace.delegate.repos.CacheGenresDelegateImpl
import pl.patrykzygo.videospace.delegate.repos.CancellationExceptionCheck
import pl.patrykzygo.videospace.delegate.repos.CancellationExceptionCheckImpl
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider
import timber.log.Timber

@HiltWorker
class MovieGenreWorker @AssistedInject constructor(
    @Assisted private val context: Context, @Assisted private val params: WorkerParameters,
    private val entryPoint: GenresEntryPoint, private val dao: GenreDao,
    private val dispatchersProvider: DispatchersProvider
) : CoroutineWorker(context, params),
    CacheGenresDelegate by CacheGenresDelegateImpl(),
    CancellationExceptionCheck by CancellationExceptionCheckImpl() {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(1, createStatusNotification("Data synced", context))
    }

    override suspend fun doWork(): Result {
        val result = withContext(dispatchersProvider.io) {
            setProgress(workDataOf("Progress" to 0))
            Timber.v("doingWork")
            try {

                val networkResponse = entryPoint.getGenresForMovies()
                if (networkResponse.isSuccessful) {
                    setProgress(workDataOf("Progress" to 50))
                    val mappedNetworkResponse = networkResponse.body()?.genres
                    val outputGenreEntities = cacheRemoteGenres(mappedNetworkResponse, dao)
                    val outputData =
                        workDataOf(WorkerConstants.KEY_GENRES_LIST_WORKER to outputGenreEntities)
                    setProgress(workDataOf("Progress" to 100))
                    setForeground(getForegroundInfo())
                    return@withContext Result.success(outputData)
                } else {
                    return@withContext Result.failure()
                }
            } catch (e: Exception) {
                checkForCancellationException(e)
                return@withContext Result.failure()
            }
        }
        return result
    }

}