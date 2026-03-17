package com.example.gameapp.Presentation.ViewModels.Home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameapp.Domain.Models.GameDetailsResponse
import com.example.gameapp.Domain.Models.GamesResponse
import com.example.gameapp.Domain.Network.NetworkResult
import com.example.gameapp.Domain.Repositories.IGameDetailsRepo
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(private val repository: IGameDetailsRepo): ViewModel() {
    private val _gameDetailsState =
        mutableStateOf<NetworkResult<GameDetailsResponse>>(NetworkResult.Initial())
    val gameDetailsState : State<NetworkResult<GameDetailsResponse>>
        get() = _gameDetailsState
    private val _gameSimilarState =
        mutableStateOf<NetworkResult<GamesResponse>>(NetworkResult.Initial())
    val gameSimilarState : State<NetworkResult<GamesResponse>>
        get() = _gameSimilarState

     fun getGameDetails(id: Int){
        _gameDetailsState.value = NetworkResult.Loading()
        try {
            viewModelScope.launch(Dispatchers.IO){
                val result = repository.getGameDetails(id)
                withContext(Dispatchers.Main){
                    if (result.isSuccessful&&result.body()!=null){
                        val jsonString = result.body()!!.string()
                        val gameDetailsResponse = Gson()
                            .fromJson(jsonString, GameDetailsResponse::class.java)
                        _gameDetailsState.value = NetworkResult.Success(gameDetailsResponse)
                    }else{
                        _gameDetailsState.value = NetworkResult.Error(result.message())
                    }
                }
            }
        }catch (e: Exception){
            _gameDetailsState.value = NetworkResult.Error(e.message.toString())
        }
    }

     fun getSimilarGames(genreId:Int){
        _gameSimilarState.value = NetworkResult.Loading()
        try{
            viewModelScope.launch(Dispatchers.IO){
                val result = repository.getSimilarGames(genreId)
                if (result.isSuccessful&&result.body()!=null){
                    val jsonString = result.body()!!.string()
                    val gameDetailsResponse = Gson()
                        .fromJson(jsonString, GamesResponse::class.java)
                    _gameSimilarState.value = NetworkResult.Success(gameDetailsResponse)
                }else{
                    _gameSimilarState.value = NetworkResult.Error(result.message())
                }
            }

        }catch (e:Exception){
            _gameSimilarState.value = NetworkResult.Error(e.message.toString())
        }
    }
}