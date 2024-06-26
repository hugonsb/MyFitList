package com.happs.myfitlist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.happs.myfitlist.view.CadastroView
import com.happs.myfitlist.view.treino.CriarPlanoTreinoView
import com.happs.myfitlist.view.treino.TreinoView

@Composable
fun NavTreino() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "treino",
    ) {
        composable("treino") { TreinoView(navController) }
        composable("criar_plano_treino") { CriarPlanoTreinoView(navController) }
    }
}