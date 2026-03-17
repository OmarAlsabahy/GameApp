package com.example.gameapp.Presentation.Customizations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.gameapp.Domain.Models.GameResult
import com.example.gameapp.Domain.Models.LastGamesEntity
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CustomLastGameCard(modifier: Modifier, game: GameResult,onTap:(id:Int)-> Unit){
    Card(modifier = modifier.clickable(onClick = {
        onTap(game.id?:0)
    }), shape = RoundedCornerShape(25.sdp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )) {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.sdp)) {
            GlideImage(modifier = Modifier.weight(weight = 1f).clip(shape = RoundedCornerShape(18.sdp)),
                model = if (game.background_image==null || game.background_image.isEmpty())"https://as2.ftcdn.net/v2/jpg/07/91/22/59/1000_F_791225927_caRPPH99D6D1iFonkCRmCGzkJPf36QDw.jpg"
                else game.background_image, contentDescription = "gameImage")

            Spacer(modifier = Modifier.width(16.sdp))

            Column(modifier = Modifier.weight(weight = 1f), horizontalAlignment = Alignment.Start) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    CustomText(title = game.name?:"", fontSize = 13.ssp, fontWeight = FontWeight.SemiBold,
                        fontColor = Color.Black)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, modifier = Modifier.size(10.sdp)
                        , contentDescription = "starIcon", tint = Color.Yellow)
                    CustomText(title = game.rating.toString(), fontSize = 10.ssp, fontColor = Color.Black)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CustomLastGameCard(modifier: Modifier, game: LastGamesEntity) {
    Card(
        modifier = modifier, shape = RoundedCornerShape(25.sdp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.sdp)
        ) {
            GlideImage(
                modifier = Modifier.weight(weight = 1f).clip(shape = RoundedCornerShape(18.sdp)),
                model = if (game.background_image == null || game.background_image.isEmpty()) "https://as2.ftcdn.net/v2/jpg/07/91/22/59/1000_F_791225927_caRPPH99D6D1iFonkCRmCGzkJPf36QDw.jpg"
                else game.background_image, contentDescription = "gameImage"
            )

            Spacer(modifier = Modifier.width(16.sdp))

            Column(modifier = Modifier.weight(weight = 1f), horizontalAlignment = Alignment.Start) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        title = game.name ?: "",
                        fontSize = 13.ssp,
                        fontWeight = FontWeight.SemiBold,
                        fontColor = Color.Black
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        modifier = Modifier.size(10.sdp),
                        contentDescription = "starIcon",
                        tint = Color.Yellow
                    )
                    CustomText(
                        title = game.rating.toString(),
                        fontSize = 10.ssp,
                        fontColor = Color.Black
                    )
                }
            }
        }
    }
}