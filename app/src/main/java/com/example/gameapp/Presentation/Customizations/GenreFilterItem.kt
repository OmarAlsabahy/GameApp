package com.example.gameapp.Presentation.Customizations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.gameapp.Domain.Models.Genre
import com.example.gameapp.Presentation.Styles.mainColor
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun GenreFilterItem(genre: Genre, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(end = 8.sdp)
            .clip(RoundedCornerShape(20.sdp))
            .background(if (isSelected) mainColor else Color.Gray.copy(alpha = 0.2f))
            .clickable { onClick() }
            .padding(horizontal = 16.sdp, vertical = 8.sdp),
        contentAlignment = Alignment.Center
    ) {
        CustomText(
            title = genre.name ?: "",
            fontSize = 12.ssp,
            fontColor = if (isSelected) Color.White else Color.LightGray
        )
    }
}