package com.happs.myfitlist.state

import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.model.usuario.Usuario

data class TreinoState(
    val usuario: Usuario = Usuario(
        nome = "",
        idade = 0,
        peso = 0.0f,
        idPlanoTreinoPrincipal = -1
    ),
    val planoTreinoPrincipal: PlanoTreino = PlanoTreino(nome = "", idUsuario = -1),
    val listaPlanosTreino: List<PlanoTreino> = emptyList(),
    val diasComExercicios: Array<Pair<DiaTreino, List<Exercicio>>> = emptyArray()
)