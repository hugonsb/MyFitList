package com.happs.myfitlist.state

import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino

data class TreinoState(
    val nomeUsuario: String = "",
    val planoTreinoPrincipal: PlanoTreino = PlanoTreino(nome = "", idUsuario = -1),
    val listaPlanosTreino: List<PlanoTreino> = emptyList(),
    val diasComExercicios: Array<Pair<DiaTreino, List<Exercicio>>> = emptyArray()
)