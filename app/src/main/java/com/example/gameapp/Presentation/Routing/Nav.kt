package com.example.gameapp.Presentation.Routing

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gameapp.Presentation.Screens.GameDetailsScreen
import com.example.gameapp.Presentation.Screens.GameWebView
import com.example.gameapp.Presentation.Screens.HomeScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun Nav(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppRoutes.home) {
        composable(AppRoutes.home) {
            HomeScreen(navigate = {id->
                navController.navigate("${AppRoutes.gameDetails}/${id}")
            })
        }
        composable("${AppRoutes.gameDetails}/{id}",
            arguments = listOf(navArgument("id", builder = {
                type = NavType.IntType
            }))){
            GameDetailsScreen(id = it.arguments?.getInt("id")?:0){url->
                val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                navController.navigate("${AppRoutes.gameWebView}/$encodedUrl")
            }
        }

        composable("${AppRoutes.gameWebView}/{url}",
            arguments = listOf(
                navArgument("url", builder = {
                    type= NavType.StringType
                })
            )){
            val encodedUrl = it.arguments?.getString("url")?:""
            val decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
            GameWebView(url = decodedUrl)
        }

    }
}