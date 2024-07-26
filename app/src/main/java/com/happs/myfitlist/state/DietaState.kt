package com.happs.myfitlist.state

import com.happs.myfitlist.model.dieta.DiaDieta
import com.happs.myfitlist.model.dieta.PlanoDieta
import com.happs.myfitlist.model.dieta.Refeicao
import com.happs.myfitlist.model.usuario.Usuario

data class DietaState(
    val usuario: Usuario = Usuario(
        nome = "",
        idade = 0,
        peso = 0.0f,
        idPlanoTreinoPrincipal = -1,
        idPlanoDietaPrincipal = -1,
    ),
    val planoDietaPrincipal: PlanoDieta = PlanoDieta(nome = "", idUsuario = -1),
    val listaPlanosDieta: List<PlanoDieta> = emptyList(),
    val diasComRefeicoes: Array<Pair<DiaDieta, List<Refeicao>>> = emptyArray(),
)