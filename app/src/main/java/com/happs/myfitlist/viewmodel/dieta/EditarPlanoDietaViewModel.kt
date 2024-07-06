package com.happs.myfitlist.viewmodel.dieta

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happs.myfitlist.model.dieta.DiaDieta
import com.happs.myfitlist.model.dieta.Refeicao
import com.happs.myfitlist.room.TreinoRepository
import com.happs.myfitlist.state.PlanoDietaState
import com.happs.myfitlist.util.DiasList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarPlanoDietaViewModel(
    private val treinoRepository: TreinoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _editarPlanoDietaState = MutableStateFlow(PlanoDietaState())
    val editarPlanoDietaState: StateFlow<PlanoDietaState> = _editarPlanoDietaState.asStateFlow()

    private val planoDietaId: Int = savedStateHandle.get<Int>("planoDietaId") ?: -1

    init {
        viewModelScope.launch {
            val planoDieta = treinoRepository.getPlanoDieta(planoDietaId).first()
            val diasDieta = treinoRepository.getDiasDieta(planoDietaId).first()

            val refeicoesList = diasDieta.map { diaRefeicao ->
                treinoRepository.getRefeicoes(diaRefeicao.id).first().toMutableList()
            }.toMutableList()

            _editarPlanoDietaState.update { currentState ->
                currentState.copy(
                    nomePlanoDieta = planoDieta.nome,
                    refeicoesList = refeicoesList
                )
            }
        }
    }

    fun setNomePlanoDieta(nome: String) {
        _editarPlanoDietaState.update { currentState ->
            currentState.copy(nomePlanoDieta = nome)
        }
    }

    fun setTipoRefeicao(dia: Int, tipo: String) {
        _editarPlanoDietaState.update { currentState ->
            val updatedList = currentState.tipoRefeicao.toMutableList().apply {
                this[dia] = tipo
            }
            currentState.copy(tipoRefeicao = updatedList)
        }
    }

    fun setDetalhesRefeicao(dia: Int, detalhes: String) {
        _editarPlanoDietaState.update { currentState ->
            val updatedList = currentState.detalhesRefeicao.toMutableList().apply {
                this[dia] = detalhes
            }
            currentState.copy(detalhesRefeicao = updatedList)
        }
    }

    fun adicionarRefeicao(dia: Int, refeicao: Refeicao) {
        _editarPlanoDietaState.update { currentState ->
            currentState.refeicoesList[dia].add(refeicao)
            currentState
        }
    }

    fun removerRefeicao(dia: Int, refeicao: Refeicao) {
        _editarPlanoDietaState.update { currentState ->
            val updatedRefeicaoList = currentState.refeicoesList.toMutableList().apply {
                val diaRefeicoes = this[dia].toMutableList()
                diaRefeicoes.remove(refeicao)
                this[dia] = diaRefeicoes
            }
            currentState.copy(refeicoesList = updatedRefeicaoList)
        }
    }

    suspend fun editarPlanoDieta(planoDietaId: Int): Pair<Boolean, String> {

        return try {
            val state = _editarPlanoDietaState.value

            if (state.nomePlanoDieta.isEmpty()) {
                throw Exception("Nome do plano não pode ser vazio")
            }

            val planoDieta = treinoRepository.getPlanoDieta(planoDietaId).first()

            treinoRepository.updatePlanoDieta(planoDieta.copy(nome = state.nomePlanoDieta))

            val diasDietaAntigos = treinoRepository.getDiasDieta(planoDieta.id).first()
            diasDietaAntigos.forEach { diaDieta ->
                val refeicoesAntigas = treinoRepository.getRefeicoes(diaDieta.id).first()
                refeicoesAntigas.forEach { refeicao ->
                    treinoRepository.removeRefeicao(refeicao)
                }
                treinoRepository.removeDiaDieta(diaDieta)
            }

            DiasList.dias.forEachIndexed { i, dia ->
                val diaDieta = DiaDieta(
                    dia = dia,
                    idPlanoDieta = planoDieta.id
                )
                val idDiaDieta = treinoRepository.addDiaDieta(diaDieta).toInt()

                state.refeicoesList[i].forEach { refeicao ->
                    val refeicaoComIdAtualizado = refeicao.copy(idDiaDieta = idDiaDieta)
                    treinoRepository.addRefeicao(refeicaoComIdAtualizado)
                }
            }
            Pair(true, "Editado com sucesso")
        } catch (e: Exception) {
            Log.e("EditarPlanoDieta", "Erro ao editar: ${e.message}")
            Pair(false, e.message.toString())
        }
    }
}
