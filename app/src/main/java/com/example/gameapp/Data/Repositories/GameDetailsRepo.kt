package com.example.gameapp.Data.Repositories

import com.example.gameapp.Data.Api.ApiHelper
import com.example.gameapp.Domain.Repositories.IGameDetailsRepo
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class GameDetailsRepo @Inject constructor(private val api: ApiHelper): IGameDetailsRepo {
    override suspend fun getGameDetails(id: Int): Response<ResponseBody> = api.getGameDetails(id)
    override suspend fun getSimilarGames(id: Int): Response<ResponseBody>
    = api.getSimilarGames(id)
}