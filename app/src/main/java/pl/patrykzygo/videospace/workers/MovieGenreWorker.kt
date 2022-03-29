package pl.patrykzygo.videospace.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.withContext
import pl.patrykzygo.videospace.constants.WorkerConstants
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.delegate.repos.CacheGenresDelegate
import pl.patrykzygo.videospace.delegate.repos.CacheGenresDelegateImpl
import pl.patrykzygo.videospace.delegate.repos.CancellationExceptionCheck
import pl.patrykzygo.videospace.delegate.repos.CancellationExceptionCheckImpl
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider

class MovieGenreWorker(
    private val context: Context, private val params: WorkerParameters,
    private val entryPoint: GenresEntryPoint, private val dao: GenreDao,
    private val dispatchersProvider: DispatchersProvider
) : CoroutineWorker(context, params),
    CacheGenresDelegate by CacheGenresDelegateImpl(),
    CancellationExceptionCheck by CancellationExceptionCheckImpl() {

    override suspend fun doWork(): Result {
        val result = withContext(dispatchersProvider.io) {
            makeStatusNotification("Syncing data...", context)
             try {
                val networkResponse = entryPoint.getGenresForMovies()
                if (networkResponse.isSuccessful) {
                    val mappedNetworkResponse = networkResponse.body()?.genres
                    val outputGenreEntities = cacheRemoteGenres(mappedNetworkResponse, dao)
                    val outputData =
                        workDataOf(WorkerConstants.KEY_GENRES_LIST_WORKER to outputGenreEntities)
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