package com.happs.myfitlist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.happs.myfitlist.view.CadastroView

@Composable
fun NavCadastro() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "cadastro",
    ) {
        composable("cadastro") { CadastroView(navController) }
        composable("home") { NavHomeManager() }
    }
}