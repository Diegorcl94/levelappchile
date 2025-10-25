package cl.duoc.levelappchile.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import cl.duoc.levelappchile.ui.components.BottomBar

@Composable
fun AppScaffold(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route?.substringBefore("/") ?: ""
    val hideBarRoutes = setOf("login", "register", "reset")
    val showBottomBar = !hideBarRoutes.contains(route)

    Scaffold(
        bottomBar = { if (showBottomBar) BottomBar(navController) }
    ) { padding ->
        // NavHost vive aquí adentro
        Navigation(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}