package com.happs.myfitlist.viewmodel.dieta

import android.util.Log
import androidx.lifecycle.ViewModel
import com.happs.myfitlist.model.dieta.DiaDieta
import com.happs.myfitlist.model.dieta.PlanoDieta
import com.happs.myfitlist.model.dieta.Refeicao
import com.happs.myfitlist.room.TreinoRepository
import com.happs.myfitlist.state.PlanoAlimentarState
import com.happs.myfitlist.util.DiasList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

class CriarPlanoAlimentarViewModel(
    private val treinoRepository: TreinoRepository
) : ViewModel() {

    private val _criarPlanoAlimentarState = MutableStateFlow(PlanoAlimentarState())
    val criarPlanoAlimentarState: StateFlow<PlanoAlimentarState> = _criarPlanoAlimentarState.asStateFlow()

    fun setNomePlanoAlimentar(nome: String) {
        _criarPlanoAlimentarState.update { currentState ->
            currentState.copy(nomePlanoAlimentar = nome)
        }
    }

    fun setTipoRefeicao(dia: Int, tipo: String) {
        _criarPlanoAlimentarState.update { currentState ->
            val updatedList = currentState.tipoRefeicao.toMutableList().apply {
                this[dia] = tipo
            }
            currentState.copy(tipoRefeicao = updatedList)
        }
    }

    fun setDetalhesRefeicao(dia: Int, detalhes: String) {
        _criarPlanoAlimentarState.update { currentState ->
            val updatedList = currentState.detalhesRefeicao.toMutableList().apply {
                this[dia] = detalhes
            }
            currentState.copy(detalhesRefeicao = updatedList)
        }
    }

    fun adicionarRefeicao(dia: Int, refeicao: Refeicao) {
        _criarPlanoAlimentarState.update { currentState ->
            currentState.refeicoesList[dia].add(refeicao)
            currentState
        }
    }

    fun removerRefeicao(dia: Int, refeicao: Refeicao) {
        _criarPlanoAlimentarState.update { currentState ->
            val updatedRefeicaoList = currentState.refeicoesList.toMutableList().apply {
                val diaRefeicoes = this[dia].toMutableList()
                diaRefeicoes.remove(refeicao)
                this[dia] = diaRefeicoes
            }
            currentState.copy(refeicoesList = updatedRefeicaoList)
        }
    }

    suspend fun savePlanoAlimentar(): Pair<Boolean, String> {

        return try {
            val usuarioId = treinoRepository.getUsuario().first().id

            val state = _criarPlanoAlimentarState.value
            val planoDieta = PlanoDieta(
                nome = state.nomePlanoAlimentar,
                idUsuario = usuarioId
            )

            if (state.nomePlanoAlimentar.isEmpty()) {
                throw Exception("Nome do plano não pode ser vazio")
            }

            val planoDietaId = treinoRepository.addPlanoDieta(planoDieta).toInt()

            DiasList.dias.forEachIndexed { i, dia ->
                val diaDieta = DiaDieta(
                    dia = dia,
                    idPlanoDieta = planoDietaId
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
            Log.e("CriarPlanoAlimentar", "Erro: ${e.message}")
            Pair(false, e.message.toString()) // Retorna erro com mensagem
        }
    }
}