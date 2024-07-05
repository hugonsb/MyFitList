package com.happs.myfitlist.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.happs.myfitlist.view.ConfiguracoesView

@Composable
fun NavHomeManager() {
    val navController = rememberNavController()
    Scaffold(bottomBar = { NavBar(navController) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "nav_treino",
            Modifier.padding(innerPadding)
        ) {
            composable("nav_treino") { NavTreino() }
            composable("dieta") { NavDieta() }
            composable("configuracoes") { ConfiguracoesView() }
        }
    }
}