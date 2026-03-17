package com.example.gameapp.Presentation.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.gameapp.Domain.Models.GameDetailsResponse
import com.example.gameapp.Domain.Models.GameResult
import com.example.gameapp.Domain.Network.NetworkResult
import com.example.gameapp.Presentation.Customizations.CustomSearchField
import com.example.gameapp.Presentation.Customizations.CustomText
import com.example.gameapp.Presentation.Customizations.CustomTrendingGameCard
import com.example.gameapp.Presentation.Customizations.DisplayProgressBar
import com.example.gameapp.Presentation.Customizations.DisplaySnackBar
import com.example.gameapp.Presentation.Styles.mainColor
import com.example.gameapp.Presentation.Styles.scaffoldBackgroundColor
import com.example.gameapp.Presentation.ViewModels.Home.GameDetailsViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun GameDetailsScreen(viewModel: GameDetailsViewModel = hiltViewModel(),
                      id: Int,navigate:(url:String)-> Unit) {
    val gameDetailsState = viewModel.gameDetailsState
    val similarGamesState = viewModel.gameSimilarState
    val snackBarHost = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(id) {
        viewModel.getGameDetails(id)
    }

    LaunchedEffect(gameDetailsState.value) {
        val state = gameDetailsState.value
        if (state is NetworkResult.Success) {
            val genreId = state.data?.genres?.firstOrNull()?.id ?: 0
            viewModel.getSimilarGames(genreId)
        }
    }

    Scaffold(
        containerColor = scaffoldBackgroundColor,
        snackbarHost = { SnackbarHost(snackBarHost) },
        floatingActionButton = {
            Button(modifier = Modifier.fillMaxWidth().padding(horizontal = 54.sdp),
                shape = RoundedCornerShape(36.sdp), colors = ButtonDefaults.buttonColors(
                    containerColor = mainColor
                ), onClick = {
                    if (gameDetailsState.value is NetworkResult.Success && gameDetailsState.value.data?.website!=null){
                        navigate(gameDetailsState.value.data?.website!!)
                    }
                }) {
                CustomText(title = "Download Game", fontSize = 16.ssp, fontColor = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {

            when (val state = gameDetailsState.value) {
                is NetworkResult.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(250.sdp), contentAlignment = Alignment.Center) {
                        DisplayProgressBar(Modifier.size(100.sdp))
                    }
                }

                is NetworkResult.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CustomText(title = state.error ?: "Unknown Error", fontSize = 16.ssp)
                    }
                    DisplaySnackBar(snackBarHost, state.error ?: "Unknown Error")
                }

                is NetworkResult.Success -> {
                    DisplayGameDetails(Modifier.fillMaxWidth().height(250.sdp), state.data)
                    DisplayGameDescription(
                        state.data?.description, Modifier
                            .fillMaxWidth()
                            .offset(y = (-20).sdp)
                    )
                }

                else -> {}
            }
            Spacer(modifier = Modifier.height(10.sdp))
            CustomText(
                title = "Similar Games",
                fontSize = 16.ssp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.sdp)
            )
            Spacer(modifier = Modifier.height(9.sdp))
            when (val state = similarGamesState.value) {
                is NetworkResult.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        DisplayProgressBar(Modifier.size(50.sdp))
                    }
                }

                is NetworkResult.Error -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CustomText(title = state.error ?: "Unknown Error", fontSize = 16.ssp)
                    }
                    DisplaySnackBar(snackBarHost, state.error ?: "Unknown Error")
                }

                is NetworkResult.Success -> {
                    DisplaySimilarGames(state.data?.gameResults, Modifier.fillMaxWidth().padding(horizontal = 16.sdp))
                }

                else -> {}
            }
        }
    }
}

@Composable
fun DisplaySimilarGames(games: List<GameResult>?, modifier: Modifier) {
    LazyRow(modifier = modifier) {
        items(count = games?.size ?: 0) { index ->
            val game = games?.get(index)
            if (game != null) {
                CustomTrendingGameCard(
                    modifier = Modifier
                        .width(100.sdp)
                        .height(117.sdp)
                        .padding(end = 10.sdp),
                    game = game
                ) { }
            }
        }
    }
}

@Composable
fun DisplayGameDetails(modifier: Modifier, gameDetails: GameDetailsResponse?) {
    Box(modifier = modifier) {
        DisplayGameDetailsTopImage(gameDetails?.background_image, modifier = Modifier.fillMaxSize())
        DisplayGameNameSection(
            gameDetails?.name, gameDetails?.released, gameDetails?.rating,
            Modifier
                .fillMaxWidth()
                .padding(start = 16.sdp, end = 16.sdp, bottom = 30.sdp)
                .align(alignment = Alignment.BottomCenter)
        )
    }
}

@Composable
fun DisplayGameDescription(description: String?, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = scaffoldBackgroundColor
        ), shape = RoundedCornerShape(topStart = 33.sdp, topEnd = 33.sdp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.sdp)) {
            CustomText("About Game", fontSize = 16.ssp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.sdp))
            CustomText(
                description ?: "", fontSize = 13.ssp, fontWeight = FontWeight.Medium,
                fontColor = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun DisplayGameNameSection(name: String?, released: String?, rating: Double?, modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            CustomText(title = name ?: "", fontSize = 16.ssp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.sdp))
            CustomText(
                title = released ?: "", fontSize = 13.ssp, fontColor = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }

        Card(
            shape = RoundedCornerShape(26.sdp), colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 7.sdp, vertical = 2.sdp)
            ) {
                Icon(
                    Icons.Filled.Star, modifier = Modifier.size(14.sdp),
                    tint = Color.Yellow, contentDescription = "starImage"
                )
                Spacer(modifier = Modifier.width(3.sdp))
                CustomText(
                    title = rating.toString(), fontSize = 14.ssp, fontColor = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DisplayGameDetailsTopImage(image: String?, modifier: Modifier) {
    GlideImage(
        model = image ?: "", contentDescription = "gameImage",
        modifier = modifier, contentScale = ContentScale.Crop
    )
}
