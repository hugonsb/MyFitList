package com.happs.myfitlist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.happs.myfitlist.view.configuracoes.ConfiguracoesView
import com.happs.myfitlist.view.configuracoes.EditarDadosPessoaisView

@Composable
fun NavConfiguracoes(navControllerCadastro: NavController) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "configuracoes",
    ) {
        composable("configuracoes") { ConfiguracoesView(navController, navControllerCadastro) }
        composable("editar_dados_pessoais") { EditarDadosPessoaisView(navController) }
    }
}