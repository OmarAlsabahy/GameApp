package com.example.gameapp.Presentation.ViewModels.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameapp.Domain.Either
import com.example.gameapp.Domain.Models.GameResult
import com.example.gameapp.Domain.Models.GamesResponse
import com.example.gameapp.Domain.Models.Genre
import com.example.gameapp.Domain.Models.GenreResponse
import com.example.gameapp.Domain.Models.LastGamesEntity
import com.example.gameapp.Domain.Models.TrendingGameEntity
import com.example.gameapp.Domain.Network.NetworkResult
import com.example.gameapp.Domain.Repositories.IHomeRepo
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IHomeRepo
) : ViewModel() {
    private val _trendingGamesState = MutableStateFlow<NetworkResult<GamesResponse>>(NetworkResult.Initial())
    val trendingGamesState: StateFlow<NetworkResult<GamesResponse>> = _trendingGamesState.asStateFlow()

    private val _lastGamesState = MutableStateFlow<NetworkResult<GamesResponse>>(NetworkResult.Initial())
    val lastGamesState: StateFlow<NetworkResult<GamesResponse>> = _lastGamesState.asStateFlow()

    private val _genresState = MutableStateFlow<NetworkResult<GenreResponse>>(NetworkResult.Initial())
    val genresState: StateFlow<NetworkResult<GenreResponse>> = _genresState.asStateFlow()

    private val _cachedTrendingGamesState = MutableStateFlow<List<TrendingGameEntity>?>(null)
    val cachedTrendingGamesState: StateFlow<List<TrendingGameEntity>?> = _cachedTrendingGamesState.asStateFlow()

    private val _cachedLastGamesState = MutableStateFlow<List<LastGamesEntity>?>(null)
    val cachedLastGamesState: StateFlow<List<LastGamesEntity>?> = _cachedLastGamesState.asStateFlow()
    
    private val _cachedGenresState = MutableStateFlow<List<Genre>?>(null)
    val cachedGenresState: StateFlow<List<Genre>?> = _cachedGenresState.asStateFlow()
    
    private val pageNumber = 1

    var trendingJob: Job? = null
    var lastGamesJob: Job? = null
    var searchTrendingJob: Job? = null
    var searchLastJob: Job? = null
    var genreJob: Job? = null

    fun loadPage(){
        trendingJob?.cancel()
        lastGamesJob?.cancel()
        searchTrendingJob?.cancel()
        searchLastJob?.cancel()
        genreJob?.cancel()
        getGenres()
        getTrendingGames(pageNumber)
        getLastGames(pageNumber)
    }

    private fun getGenres() {
        _genresState.value = NetworkResult.Loading()
        genreJob=viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getGenres()
                when(result){
                    is Either.Left->{
                        result.data.collect {
                            if (it.isSuccessful && it.body() != null) {
                                val jsonString = it.body()!!.string()
                                val genreResponse = Gson().fromJson(jsonString, GenreResponse::class.java)
                                _genresState.value = NetworkResult.Success(genreResponse)
                                if (genreResponse.results!=null){
                                    saveGenres(genreResponse.results)
                                }
                            } else {
                                _genresState.value = NetworkResult.Error(it.message())
                            }
                        }
                    }

                    is Either.Right->{
                        result.data2.collect { genres ->
                            _cachedGenresState.value = genres
                        }
                    }
                }
            } catch (e: Exception) {
                genreJob = viewModelScope.launch(Dispatchers.IO){
                    val result = repository.getCachedGenres()
                    result.collect { genres->
                        _cachedGenresState.value = genres
                    }
                }
                _genresState.value = NetworkResult.Error(e.message.toString())
            }
        }
    }

    private fun saveGenres(genres: List<Genre>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveGenres(genres)
        }
    }

    private fun getTrendingGames(pageNumber: Int) {
        _trendingGamesState.value = NetworkResult.Loading()
        trendingJob = viewModelScope.launch(Dispatchers.IO){
            try {
                val result = repository.getTrendingGames(pageNumber)
                when(result){
                    is Either.Left->{
                        result.data.collect {
                            if (it.isSuccessful && it.body() != null){
                                val jsonString = it.body()!!.string()
                                val gamesResponse = Gson().fromJson(jsonString, GamesResponse::class.java)

                                _trendingGamesState.value = NetworkResult.Success(gamesResponse)
                                saveTrending(gamesResponse.gameResults?:emptyList())
                            } else {
                                _trendingGamesState.value = NetworkResult.Error(it.message())
                            }
                        }
                    }

                    is Either.Right->{
                        result.data2.collect { games ->
                            _cachedTrendingGamesState.value = games
                        }
                    }
                }
            } catch (e: Exception){
                // If exception occurs (likely network), try to get from cache
                trendingJob=viewModelScope.launch(Dispatchers.IO){
                    val result = repository.getCachedTrendingGames()
                    result.collect { games->
                        _cachedTrendingGamesState.value = games
                    }
                }
                _trendingGamesState.value = NetworkResult.Error(e.message.toString())
            }
        }
    }

    private fun saveTrending(gamesResponse: List<GameResult>) {
        if (gamesResponse.isNotEmpty()){
            val localTrendingList = gamesResponse.map { item ->
                TrendingGameEntity(
                    id = item.id ?: 0,
                    name = item.name ?: "",
                    released = item.released ?: "",
                    background_image = item.background_image ?: "",
                    rating = item.rating ?: 0.0,
                    genreId = item.genres?.getOrNull(0)?.id
                )
            }
            if (localTrendingList.isNotEmpty()){
                viewModelScope.launch(Dispatchers.IO){
                    repository.saveTrendingGames(localTrendingList)
                }
            }
        }
    }

    fun getLastGames(pageNumber: Int) {
        _lastGamesState.value = NetworkResult.Loading()
        lastGamesJob = viewModelScope.launch(Dispatchers.IO){
            try{
                val result = repository.getLastGames(pageNumber)
                when(result){
                    is Either.Left->{
                       result.data.collect {
                           if (it.isSuccessful && it.body() != null){
                               val jsonString = it.body()!!.string()
                               val gamesResponse = Gson().fromJson(jsonString, GamesResponse::class.java)

                               _lastGamesState.value = NetworkResult.Success(gamesResponse)
                               saveLastGames(gamesResponse.gameResults?:emptyList())
                           }
                           else{
                               _lastGamesState.value = NetworkResult.Error(it.message())
                           }
                       }
                    }
                    is Either.Right->{
                        result.data2.collect { games ->
                            _cachedLastGamesState.value = games
                        }
                    }
                }
            } catch (e: Exception){
                // If exception occurs (likely network), try to get from cache
                lastGamesJob = viewModelScope.launch(Dispatchers.IO){
                    val result = repository.getCachedLastGames()
                    result.collect { games->
                        _cachedLastGamesState.value = games
                    }
                }
                _lastGamesState.value = NetworkResult.Error(e.message.toString())
            }
        }
    }

    private fun saveLastGames(gamesResponse: List<GameResult>) {
        if (gamesResponse.isNotEmpty()){
            val localLastList = gamesResponse.map { item ->
                LastGamesEntity(
                    id = item.id ?: 0,
                    name = item.name ?: "",
                    released = item.released ?: "",
                    background_image = item.background_image ?: "",
                    rating = item.rating ?: 0.0
                )
            }
            if (localLastList.isNotEmpty()){
                viewModelScope.launch(Dispatchers.IO){
                    repository.saveLastGames(localLastList)
                }
            }
        }
    }

    fun loadTrendingGamesNextPage(){
        val currentState = _trendingGamesState.value
        if (currentState.data != null && currentState.data.next != null){
            viewModelScope.launch (Dispatchers.IO){
                try{
                    val result = repository.loadMoreGames(currentState.data.next!!)
                    if (result.isSuccessful && result.body() != null){
                        val jsonString = result.body()!!.string()
                        val gamesResponse = Gson().fromJson(jsonString, GamesResponse::class.java)
                        val oldGames = currentState.data.gameResults.orEmpty()
                        val newGames = gamesResponse.gameResults ?: emptyList()
                        val totalGames = oldGames + newGames

                        _trendingGamesState.value = NetworkResult.Success(
                            currentState.data.copy(
                                gameResults = totalGames,
                                next = gamesResponse.next
                            )
                        )
                        saveTrending(totalGames)
                    } else {
                        _trendingGamesState.value = NetworkResult.Error(result.message())
                    }
                } catch (e: Exception){
                    _trendingGamesState.value = NetworkResult.Error(e.message.toString())
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        trendingJob?.cancel()
        lastGamesJob?.cancel()
        searchTrendingJob?.cancel()
        searchLastJob?.cancel()
        genreJob?.cancel()
    }

    fun loadLastGamesNextPage(){
        val currentState = _lastGamesState.value
        if (currentState.data != null && currentState.data.next != null){
            viewModelScope.launch(Dispatchers.IO){
                try {
                    val result = repository.loadMoreGames(currentState.data.next!!)
                    if (result.isSuccessful && result.body() != null){
                        val jsonString = result.body()!!.string()
                        val gamesResponse = Gson().fromJson(jsonString, GamesResponse::class.java)
                        val oldGames = currentState.data.gameResults.orEmpty()
                        val newGames = gamesResponse.gameResults ?: emptyList()
                        val totalGames = oldGames + newGames

                        _lastGamesState.value = NetworkResult.Success(
                            currentState.data.copy(
                                gameResults = totalGames,
                                next = gamesResponse.next
                            )
                        )
                        saveLastGames(totalGames)
                    } else {
                        _lastGamesState.value = NetworkResult.Error(result.message())
                    }
                } catch (e: Exception){
                    _lastGamesState.value = NetworkResult.Error(e.message.toString())
                }
            }
        }
    }

    fun getGamesByGenre(genreId: Int) {
        _trendingGamesState.value = NetworkResult.Loading()
        _lastGamesState.value = NetworkResult.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getGamesByGenre(genreId, 1)
                if (result.isSuccessful && result.body() != null) {
                    val jsonString = result.body()!!.string()
                    val gamesResponse = Gson().fromJson(jsonString, GamesResponse::class.java)
                    _trendingGamesState.value = NetworkResult.Success(gamesResponse)
                    _lastGamesState.value = NetworkResult.Success(gamesResponse)
                } else {
                    // If network fails or is unavailable, try to get from cache
                    repository.getTrendingGamesByGenre(genreId).collect { games ->
                         _cachedTrendingGamesState.value = games
                    }
                }
            } catch (e: Exception) {
                // If exception occurs (likely network), try to get from cache
                repository.getTrendingGamesByGenre(genreId).collect { games ->
                     _cachedTrendingGamesState.value = games
                }
            }
        }
    }

    fun search(name: String){
        searchTrending(name)
        searchLast(name)
    }

    private fun searchTrending(name:String){
        searchTrendingJob=viewModelScope.launch(Dispatchers.IO){
            try{
                repository.getTrendingGamesByName(name).collect { result ->
                    _cachedTrendingGamesState.value = result
                }
            } catch (e:Exception){
                _cachedTrendingGamesState.value = null
            }
        }
    }

    private fun searchLast(name:String){
        searchLastJob=viewModelScope.launch(Dispatchers.IO){
            try{
                repository.getLastGamesByName(name).collect { result ->
                    _cachedLastGamesState.value = result
                }
            } catch (e:Exception){
                _cachedLastGamesState.value = null
            }
        }
    }

    fun clearCachedData(){
        _cachedTrendingGamesState.value = null
        _cachedLastGamesState.value = null
    }
}
