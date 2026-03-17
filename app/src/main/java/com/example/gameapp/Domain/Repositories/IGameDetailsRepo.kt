package com.example.gameapp.Domain.Repositories

import okhttp3.ResponseBody
import retrofit2.Response

interface IGameDetailsRepo {
    suspend fun getGameDetails(id:Int): Response<ResponseBody>
    suspend fun getSimilarGames(genreId: Int): Response<ResponseBody>
}