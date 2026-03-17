package com.example.gameapp.Data.Database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gameapp.Domain.Models.TrendingGameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTrendingGames(games: List<TrendingGameEntity>)

    @Query("SELECT * FROM trending_games")
    fun getAllTrendingGames(): Flow<List<TrendingGameEntity>>

    @Query("SELECT * FROM trending_games WHERE name LIKE '%' || :name || '%'")
    fun getTrendingGameByName(name: String): Flow<List<TrendingGameEntity>>

    @Query("SELECT * FROM trending_games WHERE genreId = :genreId")
    fun getTrendingGamesByGenre(genreId: Int): Flow<List<TrendingGameEntity>>
}