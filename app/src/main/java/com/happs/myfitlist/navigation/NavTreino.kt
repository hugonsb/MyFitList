package com.happs.myfitlist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.happs.myfitlist.view.treino.CriarPlanoTreinoView
import com.happs.myfitlist.view.treino.EditarPlanoTreinoView
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
        composable(
            route = "editar_plano/{planoTreinoId}",
            arguments = listOf(navArgument("planoTreinoId") { type = NavType.IntType }
            )) {
            EditarPlanoTreinoView(navController, it.arguments!!.getInt("planoTreinoId"))
        }
    }
}