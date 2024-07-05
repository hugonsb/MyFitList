package com.happs.myfitlist.viewmodel.dieta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happs.myfitlist.model.dieta.PlanoDieta
import com.happs.myfitlist.room.TreinoRepository
import com.happs.myfitlist.state.DietaState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DietaViewModel(
    private val treinoRepository: TreinoRepository
) : ViewModel() {
    private val _dietaState = MutableStateFlow(DietaState())
    val dietaState: StateFlow<DietaState> = _dietaState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                treinoRepository.getUsuario(),
                treinoRepository.getPlanosDieta(),
                treinoRepository.getDiasDieta(dietaState.value.planoDietaPrincipal.id)
            ) { _, _, _ ->
                atualizarEstado()
            }.collectLatest { }
        }
    }

    private suspend fun atualizarEstado() {
        val usuarioFlow = treinoRepository.getUsuario()
        val usuario = usuarioFlow.first()
        val planoDietaPrincipalFlow = if (usuario.idPlanoDietaPrincipal != -1) {
            treinoRepository.getPlanoDieta(usuario.idPlanoDietaPrincipal)
        } else {
            flowOf(null)
        }

        val planoDietaPrincipal = planoDietaPrincipalFlow.first()

        val diasDieta = if (planoDietaPrincipal != null && planoDietaPrincipal.id != -1) {
            treinoRepository.getDiasDieta(planoDietaPrincipal.id).first()
        } else {
            emptyList()
        }

        val diasComRefeicoes = diasDieta.map { diaDieta ->
            val refeicoes = treinoRepository.getRefeicoes(diaDieta.id).first()
            Pair(diaDieta, refeicoes)
        }.toTypedArray()

        _dietaState.update { currentState ->
            currentState.copy(
                usuario = usuario,
                planoDietaPrincipal = planoDietaPrincipal ?: PlanoDieta(
                    id = -1,
                    nome = "",
                    idUsuario = -1
                ),
                listaPlanosDieta = treinoRepository.getPlanosDieta().first(),
                diasComRefeicoes = diasComRefeicoes
            )
        }
    }

    fun atualizarPlanoDietaPrincipal(usuarioId: Int, planoDietaId: Int) {
        viewModelScope.launch {
            treinoRepository.updatePlanoDietaPrincipal(usuarioId, planoDietaId)
            atualizarEstado()
        }
    }

    fun excluirPlanoDieta(planoDieta: PlanoDieta) {
        viewModelScope.launch {
            val usuario = treinoRepository.getUsuario().first()
            val isPlanoPrincipal = usuario.idPlanoDietaPrincipal == planoDieta.id

            treinoRepository.removePlanoDieta(planoDieta)

            if (isPlanoPrincipal) {
                val planosRestantes = treinoRepository.getPlanosDieta().first()
                val novoPlanoPrincipalId =
                    planosRestantes.firstOrNull()?.id ?: -1
                atualizarPlanoDietaPrincipal(usuario.id, novoPlanoPrincipalId)
            }
        }
    }
}