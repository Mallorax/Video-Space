package pl.patrykzygo.videospace.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(moviesDao: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg moviesDao: MovieEntity)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE movie_id = :id")
    suspend fun getMovieWithId(id: Int): MovieEntity?

    @Query("SELECT * FROM movies WHERE status = :status")
    fun getAllMoviesWithStatus(status: String): Flow<List<MovieEntity>>
}