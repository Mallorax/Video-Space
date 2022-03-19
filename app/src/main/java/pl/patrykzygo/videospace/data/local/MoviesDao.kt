package pl.patrykzygo.videospace.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(moviesDao: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg moviesDao: MovieEntity)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE movie_id = :id")
    suspend fun getMovieWithId(id: Int): MovieEntity?

    @Query("SELECT * FROM movies WHERE status = :status")
    suspend fun getAllMoviesWithStatus(status: String): List<MovieEntity>
}