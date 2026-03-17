package com.example.gameapp.Domain.Repositories

import com.example.gameapp.Domain.Either
import com.example.gameapp.Domain.Models.Genre
import com.example.gameapp.Domain.Models.LastGamesEntity
import com.example.gameapp.Domain.Models.TrendingGameEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface IHomeRepo {
    suspend fun getTrendingGames(pageNumber:Int): Either<Flow<Response<ResponseBody>>, Flow<List<TrendingGameEntity>>>
    suspend fun getLastGames(pageNumber:Int): Either<Flow<Response<ResponseBody>>, Flow<List<LastGamesEntity>>>
    suspend fun loadMoreGames(url: String): Response<ResponseBody>
    suspend fun saveTrendingGames(games: List<TrendingGameEntity>)
    suspend fun saveLastGames(games: List<LastGamesEntity>)
    fun getTrendingGamesByName(name: String): Flow<List<TrendingGameEntity>>
    fun getLastGamesByName(name: String): Flow<List<LastGamesEntity>>
    fun getCachedTrendingGames(): Flow<List<TrendingGameEntity>>
    fun getCachedLastGames(): Flow<List<LastGamesEntity>>
    suspend fun getGenres(): Either<Flow<Response<ResponseBody>>, Flow<List<Genre>>>
    suspend fun getGamesByGenre(genreId: Int, pageNumber: Int): Response<ResponseBody>
    suspend fun saveGenres(genres: List<Genre>)
    fun getCachedGenres(): Flow<List<Genre>>
    fun getTrendingGamesByGenre(genreId: Int): Flow<List<TrendingGameEntity>>
}