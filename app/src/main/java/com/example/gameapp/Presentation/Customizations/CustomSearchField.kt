package com.example.gameapp.Presentation.Customizations

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun CustomSearchField(modifier: Modifier, onValueChange: (String) -> Unit,valueState: MutableState<String>){
    TextField(value = valueState.value, onValueChange = onValueChange,
        modifier = modifier, colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedPlaceholderColor = Color.LightGray,
            unfocusedTextColor = Color.Black
        ), placeholder = {
            CustomText(title = "search", fontSize = 10.ssp, fontColor = Color.LightGray)
        }, shape = RoundedCornerShape(35.sdp), prefix = {
            Icon(Icons.Outlined.Search, modifier = Modifier.size(25.sdp),
                contentDescription = "searchIcon", tint = Color.Black.copy(alpha = 0.7f))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}