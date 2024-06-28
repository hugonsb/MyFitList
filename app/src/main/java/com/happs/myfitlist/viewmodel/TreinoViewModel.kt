package com.happs.myfitlist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.model.usuario.Usuario
import com.happs.myfitlist.room.treino.TreinoRepository
import com.happs.myfitlist.state.TreinoState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TreinoViewModel(
    private val treinoRepository: TreinoRepository
) : ViewModel() {

    private val _treinoState = MutableStateFlow(TreinoState())
    val treinoState: StateFlow<TreinoState> = _treinoState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val usuarioFlow = treinoRepository.getUsuario()
                val planoTreinoPrincipalFlow =
                    usuarioFlow.first().idPlanoTreinoPrincipal?.let { obterPlanoTreinoPrincipal(it) }
                        ?: flowOf(treinoState.value.planoTreinoPrincipal) // Valor padrão se nulo
                atualizarEstado(
                    usuarioFlow,
                    planoTreinoPrincipalFlow
                )
            } catch (e: Exception) {
                Log.e("TreinoViewModel", "ERRO $e")
            }
        }
    }


    private fun obterPlanoTreinoPrincipal(idPlanoTreino: Int): Flow<PlanoTreino> {
        return treinoRepository.getPlanoTreinoPrincipal(idPlanoTreino)
    }

    private suspend fun atualizarEstado(
        usuarioFlow: Flow<Usuario>,
        planoTreinoPrincipalFlow: Flow<PlanoTreino>
    ) {

        val planoTreinoPrincipal = planoTreinoPrincipalFlow.first()
        val usuario = usuarioFlow.first()
        val diasTreino = treinoRepository.getDiasTreino(planoTreinoPrincipal.id).first()

        // Criar o array de Pair<String, List<Exercicio>>
        val diasComExercicios = diasTreino.map { diaTreino ->
            val exercicios = treinoRepository.getExercicios(diaTreino.id).first()
            Pair(diaTreino, exercicios)
        }.toTypedArray() // Converter a lista para um array

        _treinoState.update { currentState ->
            currentState.copy(
                nomeUsuario = usuario.nome,
                planoTreinoPrincipal = planoTreinoPrincipal,
                listaPlanosTreino = treinoRepository.getPlanosTreino().first(),
                diasComExercicios = diasComExercicios
            )
        }
    }
}