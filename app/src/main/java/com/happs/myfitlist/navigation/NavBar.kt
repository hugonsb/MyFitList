package com.happs.myfitlist.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Treino,
        BottomNavItem.Dieta,
        BottomNavItem.Hidratacao,
        BottomNavItem.Configuracoes,
    )

    NavigationBar(
        modifier = Modifier.height(50.dp),
        containerColor = Color.Gray,
        tonalElevation = 3.dp,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White,
                    indicatorColor = Color.Gray
                ),
                label = { Text(screen.label, fontSize = 8.sp) },
                alwaysShowLabel = false,
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // // evitar abrir novamente a mesma tela ao reselecionar mesmo item
                        launchSingleTop = true
                        // restaura o estado ao voltar para a tela anterior
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Treino : BottomNavItem("treino", Icons.Default.Home, "Treino")
    data object Dieta : BottomNavItem("dieta", Icons.Default.Home, "Dieta")
    data object Hidratacao : BottomNavItem("hidratacao", Icons.Default.Home, "Hidratacao")
    data object Configuracoes : BottomNavItem("configuracoes", Icons.Default.Home, "Configuracoes")
}