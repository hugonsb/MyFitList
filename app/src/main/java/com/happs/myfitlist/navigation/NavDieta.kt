package com.happs.myfitlist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.happs.myfitlist.view.dieta.CriarPlanoAlimentarView
import com.happs.myfitlist.view.dieta.DietaView
import com.happs.myfitlist.view.dieta.EditarPlanoAlimentarVIew

@Composable
fun NavDieta() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "dieta",
    ) {
        composable("dieta") { DietaView(navController) }
        composable("criar_plano_alimentar") { CriarPlanoAlimentarView(navController) }
        composable(
            route = "editar_plano/{planoDietaId}",
            arguments = listOf(navArgument("planoDietaId") { type = NavType.IntType }
            )) {
            EditarPlanoAlimentarVIew(navController, it.arguments!!.getInt("planoDietaId"))
        }
    }
}