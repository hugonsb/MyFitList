package com.happs.myfitlist.state

import com.happs.myfitlist.model.treino.Exercicio

data class PlanoTreinoState(
    val nomePlanoTreino: String = "",
    //cada posiçao nas listas se refere ao dia da semana, 1 = domingo, 2 = segunda ...
    val grupoMuscular: MutableList<String> = MutableList(7) { "" },
    val exerciciosList: MutableList<MutableList<Exercicio>> = MutableList(7) { mutableListOf() },
    val nomeExercicio: MutableList<String> = MutableList(7) { "" },
    val numeroSeries: MutableList<String> = MutableList(7) { "" },
    val numeroRepeticoes: MutableList<String> = MutableList(7) { "" },
)