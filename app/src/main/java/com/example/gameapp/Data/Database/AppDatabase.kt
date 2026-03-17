package com.example.gameapp.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gameapp.Data.Database.Dao.GenreDao
import com.example.gameapp.Data.Database.Dao.LastGamesDao
import com.example.gameapp.Data.Database.Dao.TrendingDao
import com.example.gameapp.Domain.Models.Genre
import com.example.gameapp.Domain.Models.LastGamesEntity
import com.example.gameapp.Domain.Models.TrendingGameEntity

@Database(entities = [TrendingGameEntity::class, LastGamesEntity::class,
    Genre::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getTrendingDao(): TrendingDao
    abstract fun getLastGamesDao(): LastGamesDao
    abstract fun getGenreDao(): GenreDao
}