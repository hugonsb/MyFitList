package com.happs.myfitlist.viewmodel.dieta

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happs.myfitlist.model.dieta.DiaDieta
import com.happs.myfitlist.model.dieta.Refeicao
import com.happs.myfitlist.room.RepositoryResponse
import com.happs.myfitlist.room.TreinoRepository
import com.happs.myfitlist.state.PlanoAlimentarState
import com.happs.myfitlist.util.DiasList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarPlanoAlimentarViewModel(
    private val treinoRepository: TreinoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _editarPlanoAlimentarState =
        MutableStateFlow<RepositoryResponse<PlanoAlimentarState>>(RepositoryResponse.Loading)
    val editarPlanoAlimentarState: StateFlow<RepositoryResponse<PlanoAlimentarState>> =
        _editarPlanoAlimentarState.asStateFlow()

    private val planoDietaId: Int = savedStateHandle.get<Int>("planoDietaId") ?: -1

    init {
        viewModelScope.launch {
            _editarPlanoAlimentarState.value = RepositoryResponse.Loading
            try {
                val planoDieta = treinoRepository.getPlanoDieta(planoDietaId).first()
                val diasDieta = treinoRepository.getDiasDieta(planoDietaId).first()

                val refeicoesList = diasDieta.map { diaRefeicao ->
                    treinoRepository.getRefeicoes(diaRefeicao.id).first().toMutableList()
                }.toMutableList()

                _editarPlanoAlimentarState.update {
                    RepositoryResponse.Success(
                        PlanoAlimentarState(
                            nomePlanoAlimentar = planoDieta.nome,
                            refeicoesList = refeicoesList
                        )
                    )
                }
            } catch (e: Exception) {
                _editarPlanoAlimentarState.value =
                    RepositoryResponse.Error("Erro editarPlanoAlimentarState: ${e.message}")
            }
        }
    }

    fun setNomePlanoAlimentar(nome: String) {
        _editarPlanoAlimentarState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> RepositoryResponse.Success(
                    currentState.data.copy(
                        nomePlanoAlimentar = nome
                    )
                )

                else -> currentState // Do nothing for Loading or Error states
            }
        }
    }

    fun setTipoRefeicao(dia: Int, tipo: String) {
        _editarPlanoAlimentarState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> {
                    val updatedList = currentState.data.tipoRefeicao.toMutableList().apply {
                        this[dia] = tipo
                    }
                    RepositoryResponse.Success(currentState.data.copy(tipoRefeicao = updatedList))
                }

                else -> {
                    currentState
                }
            }
        }
    }

    fun setDetalhesRefeicao(dia: Int, detalhes: String) {
        _editarPlanoAlimentarState.update { currentState ->

            when (currentState) {
                is RepositoryResponse.Success -> {
                    val updatedList = currentState.data.detalhesRefeicao.toMutableList().apply {
                        this[dia] = detalhes
                    }
                    RepositoryResponse.Success(currentState.data.copy(detalhesRefeicao = updatedList))
                }

                else -> {
                    currentState
                }
            }
        }
    }

    fun adicionarRefeicao(dia: Int, refeicao: Refeicao) {
        _editarPlanoAlimentarState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> {
                    currentState.data.refeicoesList[dia].add(refeicao)
                    currentState
                }

                else -> {
                    currentState
                }
            }
        }
    }

    fun removerRefeicao(dia: Int, refeicao: Refeicao) {
        _editarPlanoAlimentarState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> {
                    val updatedRefeicaoList =
                        currentState.data.refeicoesList.toMutableList().apply {
                            val diaRefeicoes = this[dia].toMutableList()
                            diaRefeicoes.remove(refeicao)
                            this[dia] = diaRefeicoes
                        }
                    RepositoryResponse.Success(
                        currentState.data.copy(refeicoesList = updatedRefeicaoList)
                    )
                }

                else -> {
                    currentState
                }
            }
        }
    }

    suspend fun editarPlanoAlimentar(planoDietaId: Int): Pair<Boolean, String> {

        return try {
            val state = _editarPlanoAlimentarState.value
            if (state is RepositoryResponse.Success) {

                val nomePlanoDieta = state.data.nomePlanoAlimentar

                if (nomePlanoDieta.isEmpty()) {
                    throw Exception("Nome do plano não pode ser vazio")
                }

                val planoDieta = treinoRepository.getPlanoDieta(planoDietaId).first()
                treinoRepository.updatePlanoDieta(planoDieta.copy(nome = nomePlanoDieta))

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
                    state.data.refeicoesList[i].forEach { refeicao ->
                        val refeicaoComIdAtualizado = refeicao.copy(idDiaDieta = idDiaDieta)
                        treinoRepository.addRefeicao(refeicaoComIdAtualizado)
                    }
                }

                Pair(true, "Editado com sucesso")
            } else {
                Pair(false, "Estado inválido")
            }
        } catch (e: Exception) {
            Log.e("EditarPlanoAlimentar", "Erro ao editar: ${e.message}")
            Pair(false, e.message.toString())
        }
    }
}