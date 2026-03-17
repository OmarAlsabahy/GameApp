package com.example.gameapp.Presentation.Customizations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.gameapp.Domain.Models.GameResult
import com.example.gameapp.Domain.Models.TrendingGameEntity
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@OptIn(ExperimentalGlideComposeApi::class)
@Composable fun CustomTrendingGameCard(modifier: Modifier, game: GameResult,onTap:(id:Int)-> Unit){
    Card(modifier = modifier.clickable(onClick ={
        onTap(game.id?:0)
    }), shape = RoundedCornerShape(15.sdp)) {
        Box(modifier = Modifier.fillMaxSize()){
            if (game.background_image!=null){
                GlideImage(
                    model = if (game.background_image==null || game.background_image.isEmpty())"https://as2.ftcdn.net/v2/jpg/07/91/22/59/1000_F_791225927_caRPPH99D6D1iFonkCRmCGzkJPf36QDw.jpg"
                    else game.background_image,
                    contentDescription = "gameImage",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.sdp).align(Alignment.BottomCenter), horizontalAlignment = Alignment.Start) {
                    CustomText(game.name?:"", fontSize = 12.ssp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(5.sdp))
                    CustomText(game.released?:"", fontSize = 8.ssp)
                    Spacer(modifier = Modifier.height(5.sdp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = "rateStar",
                            tint = Color.Yellow, modifier = Modifier.size(10.sdp))

                        CustomText(game.rating.toString(), fontSize = 8.ssp)
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable fun CustomTrendingGameCard(modifier: Modifier, game: TrendingGameEntity){
    Card(modifier = modifier, shape = RoundedCornerShape(15.sdp)) {
        Box(modifier = Modifier.fillMaxSize()){
            if (game.background_image!=null){
                GlideImage(
                    model = if (game.background_image==null || game.background_image.isEmpty())"https://as2.ftcdn.net/v2/jpg/07/91/22/59/1000_F_791225927_caRPPH99D6D1iFonkCRmCGzkJPf36QDw.jpg"
                    else game.background_image,
                    contentDescription = "gameImage",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.sdp).align(Alignment.BottomCenter), horizontalAlignment = Alignment.Start) {
                    CustomText(game.name?:"", fontSize = 12.ssp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(5.sdp))
                    CustomText(game.released?:"", fontSize = 8.ssp)
                    Spacer(modifier = Modifier.height(5.sdp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = "rateStar",
                            tint = Color.Yellow, modifier = Modifier.size(10.sdp))

                        CustomText(game.rating.toString(), fontSize = 8.ssp)
                    }
                }
            }
        }
    }
}