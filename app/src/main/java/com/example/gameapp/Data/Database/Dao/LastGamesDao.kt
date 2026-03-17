package com.example.gameapp.Data.Database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gameapp.Domain.Models.LastGamesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LastGamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLastGames(games: List<LastGamesEntity>)

    @Query("SELECT * FROM last_games")
    fun getAllLastGames(): Flow<List<LastGamesEntity>>

    @Query("SELECT * FROM last_games WHERE name LIKE '%' || :name || '%'")
    fun getLastGameByName(name: String): Flow<List<LastGamesEntity>>
}