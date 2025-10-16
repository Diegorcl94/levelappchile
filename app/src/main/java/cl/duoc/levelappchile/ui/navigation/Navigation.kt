package cl.duoc.levelappchile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.levelappchile.ui.screens.auth.LoginScreen
import cl.duoc.levelappchile.ui.screens.auth.RegisterScreen
import cl.duoc.levelappchile.ui.screens.auth.ResetPasswordScreen
import cl.duoc.levelappchile.ui.screens.home.HomeScreen
import cl.duoc.levelappchile.ui.screens.catalog.CatalogScreen
import cl.duoc.levelappchile.ui.screens.news.NewsScreen
import cl.duoc.levelappchile.ui.screens.profile.ProfileScreen
import cl.duoc.levelappchile.ui.screens.sell.SellUsedScreen
import cl.duoc.levelappchile.ui.screens.detail.ProductDetailScreen

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "login") {
        composable("login") { LoginScreen(nav) }
        composable("register") { RegisterScreen(nav) }
        composable("reset") { ResetPasswordScreen(nav) }
        composable("home") { HomeScreen(nav) }
        composable("catalog") { CatalogScreen(nav) }
        composable("news") { NewsScreen(nav) }
        composable("profile") { ProfileScreen(nav) }
        composable("sell") { SellUsedScreen(nav) }
        composable("detail/{id}") { backStack ->
            ProductDetailScreen(id = backStack.arguments?.getString("id") ?: "", nav)
        }
    }
}