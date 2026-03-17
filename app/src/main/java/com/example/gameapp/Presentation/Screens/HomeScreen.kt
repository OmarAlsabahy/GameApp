package com.example.gameapp.Presentation.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gameapp.Domain.Models.GameResult
import com.example.gameapp.Domain.Models.Genre
import com.example.gameapp.Domain.Models.LastGamesEntity
import com.example.gameapp.Domain.Models.TrendingGameEntity
import com.example.gameapp.Domain.Network.NetworkResult
import com.example.gameapp.Presentation.Customizations.CustomLastGameCard
import com.example.gameapp.Presentation.Customizations.CustomSearchField
import com.example.gameapp.Presentation.Customizations.CustomText
import com.example.gameapp.Presentation.Customizations.CustomTrendingGameCard
import com.example.gameapp.Presentation.Customizations.DisplayProgressBar
import com.example.gameapp.Presentation.Customizations.DisplaySnackBar
import com.example.gameapp.Presentation.Customizations.GenreFilterItem
import com.example.gameapp.Presentation.Styles.scaffoldBackgroundColor
import com.example.gameapp.Presentation.ViewModels.Home.HomeViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun HomeScreen(navigate: (id: Int) -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val trendingState by viewModel.trendingGamesState.collectAsStateWithLifecycle()
    val cachedTrendingState by viewModel.cachedTrendingGamesState.collectAsStateWithLifecycle()
    val lastState by viewModel.lastGamesState.collectAsStateWithLifecycle()
    val cachedLastGameState by viewModel.cachedLastGamesState.collectAsStateWithLifecycle()
    val genresState by viewModel.genresState.collectAsStateWithLifecycle()
    val cachedGenresState by viewModel.cachedGenresState.collectAsStateWithLifecycle()

    val snackBarHost = remember { SnackbarHostState() }
    var isPageLoaded by rememberSaveable { mutableStateOf(false) }
    var isTrendingLoading by rememberSaveable { mutableStateOf(false) }
    var isLastLoading by rememberSaveable { mutableStateOf(false) }
    val trendingListState = rememberLazyListState()
    val lastListState = rememberLazyListState()
    val searchValue = rememberSaveable { mutableStateOf("") }
    var selectedGenreId = rememberSaveable { mutableStateOf<Int?>(null) }

    LaunchedEffect(isPageLoaded) {
        if (!isPageLoaded){
            viewModel.loadPage()
        }
    }

    LaunchedEffect(trendingListState) {
        snapshotFlow { trendingListState.layoutInfo }
            .collect { layoutInfo->
                val totalCount = layoutInfo.totalItemsCount
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index?:0
                if (totalCount > 0 && lastVisibleItem >= totalCount - 5 && !isTrendingLoading){
                    isTrendingLoading = true
                    viewModel.loadTrendingGamesNextPage()
                }
            }
    }

    LaunchedEffect(lastListState) {
        snapshotFlow { lastListState.layoutInfo }
            .collect { layoutInfo->
                val totalCount = layoutInfo.totalItemsCount
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index?:0
                if (totalCount > 0 && lastVisibleItem >= totalCount - 5 && !isLastLoading){
                    isLastLoading = true
                    viewModel.loadLastGamesNextPage()
                }
            }
    }

    Scaffold(
        containerColor = scaffoldBackgroundColor,
        snackbarHost = { SnackbarHost(snackBarHost) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.sdp, vertical = 17.sdp)
                .fillMaxSize()
        ) {
            CustomSearchField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.sdp)
                    .padding(top = 10.sdp)
                    .height(50.sdp),
                valueState = searchValue,
                onValueChange = { value ->
                    if(value.isNotEmpty()){
                        searchValue.value = value
                        viewModel.search(value)
                    }else{
                        searchValue.value = ""
                        viewModel.clearCachedData()
                        viewModel.loadPage()
                    }
                }
            )
            Spacer(modifier = Modifier.height(15.sdp))

            DisplayGenreList(if (cachedGenresState!=null && cachedGenresState!!.isNotEmpty())cachedGenresState else genresState.data?.results
                        ,selectedGenreId,viewModel)
            Spacer(modifier = Modifier.height(15.sdp))

            CustomText("Trending Games", fontSize = 16.ssp)

            if (cachedTrendingState != null && cachedTrendingState!!.isNotEmpty()){
                DisplayTrendingGames(cachedTrendingState, Modifier.fillMaxWidth(),
                    trendingListState)
            }else{
                when (val state = trendingState) {
                    is NetworkResult.Loading -> {
                        DisplayProgressBar(modifier = Modifier.fillMaxWidth().padding(top = 14.sdp))
                    }

                    is NetworkResult.Error -> {
                        DisplaySnackBar(snackBarHost, state.error ?: "Unknown Error")
                        isTrendingLoading = false
                    }

                    is NetworkResult.Success -> {
                        Spacer(modifier = Modifier.height(14.sdp))
                        DisplayTrendingGames(state.data?.gameResults, Modifier.fillMaxWidth(),
                            trendingListState, onTap = {id->
                                navigate(id)
                            })
                        isTrendingLoading = false
                        isPageLoaded = true
                    }
                    else -> {}
                }
            }
            Spacer(modifier = Modifier.height(11.sdp))
            CustomText("Popular Games", fontSize = 16.ssp)
            Spacer(modifier = Modifier.height(14.sdp))
            if (cachedLastGameState == null || cachedLastGameState!!.isEmpty()){
                when(val state = lastState){
                    is NetworkResult.Loading -> {
                        DisplayProgressBar(modifier = Modifier.fillMaxWidth())
                    }
                    is NetworkResult.Error -> {
                        DisplaySnackBar(snackBarHost, state.error ?: "Unknown Error")
                        isLastLoading = false
                    }
                    is NetworkResult.Success -> {
                        DisplayLastGames(state.data?.gameResults, Modifier.fillMaxWidth(),
                            lastListState, onTap = {id->
                                navigate(id)
                            })
                        isLastLoading = false
                        isPageLoaded=true
                    }
                    else -> {}
                }
            }else{
                DisplayLastGames(cachedLastGameState, Modifier.fillMaxWidth(),
                    lastListState)
            }
        }
    }
}



@Composable
fun DisplayLastGames(gamesResponse: List<GameResult>?, modifier: Modifier, state: LazyListState,
                     onTap:(id:Int)-> Unit) {
    LazyColumn(modifier = modifier, state = state) {
        items(gamesResponse ?: emptyList()){ game ->
            CustomLastGameCard(modifier = Modifier.padding(vertical = 10.sdp).fillMaxWidth(),
                game = game, onTap = onTap)
        }
    }
}

@Composable
fun DisplayLastGames(gamesResponse: List<LastGamesEntity>?, modifier: Modifier, state: LazyListState) {
    LazyColumn(modifier = modifier, state = state) {
        items(gamesResponse ?: emptyList()){ game ->
            CustomLastGameCard(modifier = Modifier.padding(vertical = 10.sdp).fillMaxWidth(),
                game = game)
        }
    }
}

@Composable
fun DisplayTrendingGames(
    gamesResponse: List<GameResult>?,
    modifier: Modifier,
    trendingListState: LazyListState,
    onTap: (id:Int) -> Unit
) {
    if (gamesResponse!= null) {
        LazyRow(modifier = modifier, state = trendingListState) {
            items(gamesResponse) { game ->
                CustomTrendingGameCard(
                    modifier = Modifier
                        .padding(horizontal = 10.sdp)
                        .size(width = 150.sdp, height = 200.sdp),
                    game = game,
                    onTap = onTap
                )
            }
        }
    }
}
@Composable
fun DisplayTrendingGames(
    gamesResponse: List<TrendingGameEntity>?,
    modifier: Modifier,
    trendingListState: LazyListState,
) {
    if (gamesResponse!= null) {
        LazyRow(modifier = modifier, state = trendingListState) {
            items(gamesResponse) { game ->
                CustomTrendingGameCard(
                    modifier = Modifier
                        .padding(horizontal = 10.sdp)
                        .size(width = 150.sdp, height = 200.sdp),
                    game = game
                )
            }
        }
    }
}

@Composable
fun DisplayGenreList(genres: List<Genre>?,selectedGenreId: MutableState<Int?>,viewModel: HomeViewModel){
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(genres?.size?:0) { index ->
            GenreFilterItem(
                genre = genres?.get(index)!!,
                isSelected = selectedGenreId.value == genres.get(index).id,
                onClick = {
                    if (selectedGenreId.value == genres.get(index).id) {
                        selectedGenreId.value = null
                        viewModel.loadPage()
                    } else {
                        selectedGenreId.value = genres.get(index).id
                        if (genres.get(index).id != null) {
                            viewModel.getGamesByGenre(genres.get(index).id!!)
                        }
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HomeScreenPreview() {
    HomeScreen(navigate = { })
}
