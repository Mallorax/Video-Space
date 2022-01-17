package pl.patrykzygo.videospace.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genre: GenreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(vararg genre: GenreEntity)

    @Query("SELECT * FROM genres WHERE id = :id")
    suspend fun getGenre(id: Int): GenreEntity?

    @Query("SELECT * FROM genres")
    suspend fun getGenres(): List<GenreEntity>

}
