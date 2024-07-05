package com.happs.myfitlist.state

import com.happs.myfitlist.model.dieta.Refeicao

data class PlanoDietaState(
    val nomePlanoDieta: String = "",
    val refeicoesList: MutableList<MutableList<Refeicao>> = MutableList(7) { mutableListOf() },
    val tipoRefeicao: MutableList<String> = MutableList(7) { "" },
    val detalhesRefeicao: MutableList<String> = MutableList(7) { "" }
)