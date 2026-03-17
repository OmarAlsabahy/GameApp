package com.example.gameapp.Data.Api

import com.example.gameapp.Data.Api.EndPoints
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiHelper {
    @GET(EndPoints.games)
    suspend fun getTrendingGames(@Query("page")pageNumber: Int): Response<ResponseBody>
    @GET(EndPoints.games)
    suspend fun getOrderedGames(@Query("ordering") order: String,@Query("page")pageNumber: Int): Response<ResponseBody>
    @GET
    suspend fun loadMoreGames(@Url url: String): Response<ResponseBody>

    @GET("${EndPoints.games}/{id}")
    suspend fun getGameDetails(@Path("id") id: Int): Response<ResponseBody>

    @GET(EndPoints.games)
    suspend fun getSimilarGames(@Query("genres")genreId: Int)
    : Response<ResponseBody>

    @GET(EndPoints.genres)
    suspend fun getGenres(): Response<ResponseBody>
    
    @GET(EndPoints.games)
    suspend fun getGamesByGenre(@Query("genres") genreId: Int, @Query("page") pageNumber: Int): Response<ResponseBody>
}