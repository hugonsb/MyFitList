package com.happs.myfitlist.viewmodel.treino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.room.TreinoRepository
import com.happs.myfitlist.state.TreinoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine

class TreinoViewModel(
    private val treinoRepository: TreinoRepository
) : ViewModel() {

    private val _treinoState = MutableStateFlow(TreinoState())
    val treinoState: StateFlow<TreinoState> = _treinoState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                treinoRepository.getUsuario(),
                treinoRepository.getPlanosTreino(),
                treinoRepository.getDiasTreino(treinoState.value.planoTreinoPrincipal.id)
            ) { _, _, _ ->
                atualizarEstado()
            }.collectLatest { }
        }
    }

    private suspend fun atualizarEstado() {
        val usuarioFlow = treinoRepository.getUsuario()
        val usuario = usuarioFlow.first()
        val planoTreinoPrincipalFlow = if (usuario.idPlanoTreinoPrincipal != -1) {
            treinoRepository.getPlanoTreino(usuario.idPlanoTreinoPrincipal)
        } else {
            flowOf(null)
        }

        val planoTreinoPrincipal = planoTreinoPrincipalFlow.first()

        val diasTreino = if (planoTreinoPrincipal != null && planoTreinoPrincipal.id != -1) {
            treinoRepository.getDiasTreino(planoTreinoPrincipal.id).first()
        } else {
            emptyList()
        }

        val diasComExercicios = diasTreino.map { diaTreino ->
            val exercicios = treinoRepository.getExercicios(diaTreino.id).first()
            Pair(diaTreino, exercicios)
        }.toTypedArray()

        _treinoState.update { currentState ->
            currentState.copy(
                usuario = usuario,
                planoTreinoPrincipal = planoTreinoPrincipal ?: PlanoTreino(
                    id = -1,
                    nome = "",
                    idUsuario = -1
                ),
                listaPlanosTreino = treinoRepository.getPlanosTreino().first(),
                diasComExercicios = diasComExercicios
            )
        }
    }

    fun atualizarPlanoTreinoPrincipal(usuarioId: Int, planoTreinoId: Int) {
        viewModelScope.launch {
            treinoRepository.updatePlanoTreinoPrincipal(usuarioId, planoTreinoId)
            atualizarEstado()
        }
    }

    fun excluirPlanoTreino(planoTreino: PlanoTreino) {
        viewModelScope.launch {
            val usuario = treinoRepository.getUsuario().first()
            val isPlanoPrincipal = usuario.idPlanoTreinoPrincipal == planoTreino.id

            treinoRepository.removePlanoTreino(planoTreino)

            if (isPlanoPrincipal) {
                val planosRestantes = treinoRepository.getPlanosTreino().first()
                val novoPlanoPrincipalId =
                    planosRestantes.firstOrNull()?.id ?: -1
                atualizarPlanoTreinoPrincipal(usuario.id, novoPlanoPrincipalId)
            }
            atualizarEstado()
        }
    }
}