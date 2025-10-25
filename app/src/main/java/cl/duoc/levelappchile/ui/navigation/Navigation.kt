package cl.duoc.levelappchile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cl.duoc.levelappchile.ui.screens.auth.LoginScreen
import cl.duoc.levelappchile.ui.screens.auth.RegisterScreen
import cl.duoc.levelappchile.ui.screens.auth.ResetPasswordScreen
import cl.duoc.levelappchile.ui.screens.catalog.CatalogScreen
import cl.duoc.levelappchile.ui.screens.detail.ProductDetailScreen
import cl.duoc.levelappchile.ui.screens.news.NewsScreen
import cl.duoc.levelappchile.ui.screens.profile.ProfileScreen
import cl.duoc.levelappchile.ui.screens.sell.SellUsedScreen
import cl.duoc.levelappchile.ui.screens.home.HomeScreen as HomeHomeScreen

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        // Auth
        composable("login")    { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("reset")    { ResetPasswordScreen(navController) }

        // Home
        composable("home") { HomeHomeScreen(nav = navController) }

        // News
        composable("news") { NewsScreen() }

        // Catálogo
        composable("catalog") { CatalogScreen(navController) }

        // Detalle
        composable(
            route = "detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(id = productId, nav = navController)
        }

        // Vender usado
        composable("sell") { SellUsedScreen(navController) }

        // Perfil
        composable("profile") { ProfileScreen(navController) }
    }
}