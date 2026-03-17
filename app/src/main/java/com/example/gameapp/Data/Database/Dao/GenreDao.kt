package com.example.gameapp.Data.Database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gameapp.Domain.Models.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGenres(genres: List<Genre>)
    @Query("SELECT * FROM genre")
     fun getAllGenres(): Flow<List<Genre>>
}