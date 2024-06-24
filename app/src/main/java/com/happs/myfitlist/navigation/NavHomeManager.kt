package com.happs.myfitlist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.happs.myfitlist.view.ConfiguracoesView
import com.happs.myfitlist.view.DietaView
import com.happs.myfitlist.view.HidratacaoView
import com.happs.myfitlist.view.TreinoView

@Composable
fun NavHomeManager() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "treino",
    ) {
        composable("treino") { TreinoView() }
        composable("dieta") { DietaView() }
        composable("hidratacao") { HidratacaoView() }
        composable("configuracoes") { ConfiguracoesView() }
    }
}