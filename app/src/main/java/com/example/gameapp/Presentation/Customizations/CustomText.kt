package com.example.gameapp.Presentation.Customizations

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.example.gameapp.R

@Composable
fun CustomText(title: String, fontColor: Color?=null, fontSize: TextUnit,
               fontWeight: FontWeight?=null, modifier: Modifier?=null,maxLines:Int?=null){
    Text(title, color = fontColor ?: Color.White, fontSize = fontSize,
        fontWeight = fontWeight?: FontWeight.Normal, fontFamily = FontFamily(listOf(
            Font(R.font.poppin_regular)
        )),
        modifier = modifier?: Modifier, maxLines = maxLines?:1
    )
}