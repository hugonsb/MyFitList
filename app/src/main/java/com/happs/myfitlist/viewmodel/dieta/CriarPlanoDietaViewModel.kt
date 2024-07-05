package com.happs.myfitlist.viewmodel.dieta

import android.util.Log
import androidx.lifecycle.ViewModel
import com.happs.myfitlist.model.dieta.DiaDieta
import com.happs.myfitlist.model.dieta.PlanoDieta
import com.happs.myfitlist.model.dieta.Refeicao
import com.happs.myfitlist.room.TreinoRepository
import com.happs.myfitlist.state.PlanoDietaState
import com.happs.myfitlist.util.DiasList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

class CriarPlanoDietaViewModel(
    private val treinoRepository: TreinoRepository
) : ViewModel() {

    private val _criarPlanoDietaState = MutableStateFlow(PlanoDietaState())
    val criarPlanoDietaState: StateFlow<PlanoDietaState> = _criarPlanoDietaState.asStateFlow()

    fun setNomePlanoDieta(nome: String) {
        _criarPlanoDietaState.update { currentState ->
            currentState.copy(nomePlanoDieta = nome)
        }
    }

    fun setTipoRefeicao(dia: Int, tipo: String) {
        _criarPlanoDietaState.update { currentState ->
            val updatedList = currentState.tipoRefeicao.toMutableList().apply {
                this[dia] = tipo
            }
            currentState.copy(tipoRefeicao = updatedList)
        }
    }

    fun setDetalhesRefeicao(dia: Int, detalhes: String) {
        _criarPlanoDietaState.update { currentState ->
            val updatedList = currentState.detalhesRefeicao.toMutableList().apply {
                this[dia] = detalhes
            }
            currentState.copy(detalhesRefeicao = updatedList)
        }
    }

    fun adicionarRefeicao(dia: Int, refeicao: Refeicao) {
        _criarPlanoDietaState.update { currentState ->
            currentState.refeicoesList[dia].add(refeicao)
            currentState
        }
    }

    fun removerRefeicao(dia: Int, refeicao: Refeicao) {
        _criarPlanoDietaState.update { currentState ->
            val updatedRefeicaoList = currentState.refeicoesList.toMutableList().apply {
                val diaRefeicoes = this[dia].toMutableList()
                diaRefeicoes.remove(refeicao)
                this[dia] = diaRefeicoes
            }
            currentState.copy(refeicoesList = updatedRefeicaoList)
        }
    }

    suspend fun savePlanoDieta(): Pair<Boolean, String> {
        return try {

            val usuarioId = treinoRepository.getUsuario().first().id

            val state = _criarPlanoDietaState.value
            val planoDieta = PlanoDieta(
                nome = state.nomePlanoDieta,
                idUsuario = usuarioId
            )

            if (state.nomePlanoDieta.isEmpty()) {
                throw Exception("Nome do plano não pode ser vazio")
            }

            val planoDietaId = treinoRepository.addPlanoDieta(planoDieta).toInt()

            DiasList.dias.forEachIndexed { i, dia ->
                val diaDieta = DiaDieta(
                    dia = dia,
                    idPlanoTreino = planoDietaId
                )
                val idDiaDieta = treinoRepository.addDiaDieta(diaDieta).toInt()

                state.refeicoesList[i].forEach { refeicao ->
                    val refeicaoComIdAtualizado = refeicao.copy(idDiaDieta = idDiaDieta)
                    treinoRepository.addRefeicao(refeicaoComIdAtualizado)
                }
            }

            treinoRepository.updatePlanoDietaPrincipal(
                usuarioId = usuarioId,
                planoDietaId = planoDietaId
            )

            Pair(true, "Salvo com sucesso") // Retorna sucesso com mensagem
        } catch (e: Exception) {
            Log.e("CriarPlanoDieta", "Erro: ${e.message}")
            Pair(false, e.message.toString()) // Retorna erro com mensagem
        }
    }
}