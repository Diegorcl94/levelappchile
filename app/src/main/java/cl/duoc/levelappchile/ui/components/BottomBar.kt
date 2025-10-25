package cl.duoc.levelappchile.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

private data class NavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun BottomBar(nav: NavHostController) {
    val items = listOf(
        NavItem("home", "Inicio", Icons.Filled.Home),
        NavItem("catalog", "Catálogo", Icons.Filled.Store),
        NavItem("news", "Noticias", Icons.Filled.Newspaper),
        NavItem("profile", "Perfil", Icons.Filled.Person)
    )

    val backStack by nav.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    nav.navigate(item.route) {
                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
