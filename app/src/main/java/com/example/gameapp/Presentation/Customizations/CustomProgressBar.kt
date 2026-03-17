package com.example.gameapp.Presentation.Customizations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gameapp.Presentation.Styles.mainColor
import ir.kaaveh.sdpcompose.sdp


@Composable
fun DisplayProgressBar(modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = mainColor, modifier = Modifier.size(20.sdp))
    }
}