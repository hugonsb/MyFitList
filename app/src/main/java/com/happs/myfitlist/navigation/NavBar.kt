package com.happs.myfitlist.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.happs.myfitlist.R
import com.happs.myfitlist.ui.theme.MyWhite

@Composable
fun NavBar(navController: NavHostController) {

    val items = BottomNavItem.items()

    NavigationBar(
        //modifier = Modifier.height(70.dp),
        containerColor = MaterialTheme.colorScheme.primary.copy(0.95f)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MyWhite,
                    unselectedIconColor = Color.White.copy(0.85f),
                    selectedTextColor = MyWhite,
                    unselectedTextColor = Color.White.copy(0.85f),
                    indicatorColor = MaterialTheme.colorScheme.tertiary.copy(0.3f)
                ),
                label = { Text(screen.label, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.label,
                        modifier = Modifier.size(30.dp)
                    )
                },
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

sealed class BottomNavItem(val route: String, val icon: Painter, val label: String) {
    class Treino(icon: Painter) : BottomNavItem("treino", icon, "Treino")
    class Dieta(icon: Painter) : BottomNavItem("dieta", icon, "Dieta")
    class Configuracoes(icon: Painter) : BottomNavItem("configuracoes", icon, "Configuracoes")

    companion object {
        @Composable
        fun items(): List<BottomNavItem> {
            return listOf(
                Treino(painterResource(id = R.drawable.nav_icon_treino)),
                Dieta(painterResource(id = R.drawable.nav_icon_dieta)),
                Configuracoes(painterResource(id = R.drawable.nav_icon_configuracoes))
            )
        }
    }
}