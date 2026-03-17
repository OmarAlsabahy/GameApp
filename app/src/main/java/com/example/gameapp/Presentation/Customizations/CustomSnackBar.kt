package com.example.gameapp.Presentation.Customizations

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun DisplaySnackBar(state: SnackbarHostState, message: String) {
    LaunchedEffect(message) {
        state.showSnackbar(message)
    }
}