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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TreinoState

        return diasComExercicios.contentEquals(other.diasComExercicios)
    }

    override fun hashCode(): Int {
        return diasComExercicios.contentHashCode()
    }
}