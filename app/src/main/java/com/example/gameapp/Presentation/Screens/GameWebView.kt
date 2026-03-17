package com.example.gameapp.Presentation.Screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.gameapp.Presentation.Styles.scaffoldBackgroundColor

@Composable
fun GameWebView(url: String){
    Scaffold(
        containerColor = scaffoldBackgroundColor
    ) {innerPadding->
        AndroidView(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            factory = {context->
                WebView(context)
                    .apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl(url)
                    }
            }
        )
    }
}