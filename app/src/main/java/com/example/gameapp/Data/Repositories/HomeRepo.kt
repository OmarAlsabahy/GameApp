package com.example.gameapp.Data.Repositories

import android.content.Context
import com.example.gameapp.Data.Api.ApiHelper
import com.example.gameapp.Data.Database.AppDatabase
import com.example.gameapp.Domain.Either
import com.example.gameapp.Domain.Models.Genre
import com.example.gameapp.Domain.Models.LastGamesEntity
import com.example.gameapp.Domain.Models.TrendingGameEntity
import com.example.gameapp.Domain.Network.NetworkConnectivity
import com.example.gameapp.Domain.OrderTypes
import com.example.gameapp.Domain.Repositories.IHomeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val api: ApiHelper,
    private val db: AppDatabase,
    private val context: Context,
    private val networkConnectivity: NetworkConnectivity
) : IHomeRepo {
    override suspend fun getTrendingGames(pageNumber: Int): Either<Flow<Response<ResponseBody>>, Flow<List<TrendingGameEntity>>> {
        return if (networkConnectivity.isInternetAvailable(context)) {

            Either.Left(flow {
                val result = api.getTrendingGames(pageNumber)
                emit(result)
            })
        } else {
            Either.Right(getCachedTrendingGames())
        }
    }

    override suspend fun getLastGames(pageNumber: Int): Either<Flow<Response<ResponseBody>>, Flow<List<LastGamesEntity>>> {
        return if (networkConnectivity.isInternetAvailable(context)) {

            Either.Left(flow {
                val result = api.getOrderedGames(
                    OrderTypes.added.name, pageNumber
                )
                emit(result)
            })
        } else {
            Either.Right(getCachedLastGames())
        }
    }

    override suspend fun loadMoreGames(url: String): Response<ResponseBody> = api.loadMoreGames(url)

    override suspend fun saveTrendingGames(games: List<TrendingGameEntity>) {
        db.getTrendingDao().insertAllTrendingGames(games)
    }

    override suspend fun saveLastGames(games: List<LastGamesEntity>) {
        db.getLastGamesDao().insertAllLastGames(games)
    }

    override fun getTrendingGamesByName(name: String): Flow<List<TrendingGameEntity>> =
        db.getTrendingDao().getTrendingGameByName(name)

    override fun getLastGamesByName(name: String): Flow<List<LastGamesEntity>> =
        db.getLastGamesDao().getLastGameByName(name)

    override fun getCachedTrendingGames(): Flow<List<TrendingGameEntity>> =
        db.getTrendingDao().getAllTrendingGames()

    override fun getCachedLastGames(): Flow<List<LastGamesEntity>> =
        db.getLastGamesDao().getAllLastGames()

    override suspend fun getGenres(): Either<Flow<Response<ResponseBody>>, Flow<List<Genre>>>{
        return if (networkConnectivity.isInternetAvailable(context)) {

            Either.Left(flow {
                val result = api.getGenres()
                emit(result)
            })
        }else{
            Either.Right(getCachedGenres())
        }
    }

    override suspend fun getGamesByGenre(genreId: Int, pageNumber: Int): Response<ResponseBody> = api.getGamesByGenre(genreId, pageNumber)
    
    override suspend fun saveGenres(genres: List<Genre>) {
        db.getGenreDao().insertAllGenres(genres)
    }

    override fun getCachedGenres(): Flow<List<Genre>> {
        return db.getGenreDao().getAllGenres()
    }

    override fun getTrendingGamesByGenre(genreId: Int): Flow<List<TrendingGameEntity>> {
        return db.getTrendingDao().getTrendingGamesByGenre(genreId)
    }
}