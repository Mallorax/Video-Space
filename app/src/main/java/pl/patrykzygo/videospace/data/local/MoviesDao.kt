package pl.patrykzygo.videospace.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(moviesDao: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourites(vararg moviesDao: MovieEntity)

    @Query("SELECT * FROM movies")
    suspend fun getAllFavourites(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE movie_id = :id")
    suspend fun getFavourite(id: Int): MovieEntity
}