package com.happs.myfitlist.viewmodel.treino

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.room.RepositoryResponse
import com.happs.myfitlist.room.TreinoRepository
import com.happs.myfitlist.state.PlanoTreinoState
import com.happs.myfitlist.util.DiasList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarPlanoTreinoViewModel(
    private val treinoRepository: TreinoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _editarPlanoTreinoState =
        MutableStateFlow<RepositoryResponse<PlanoTreinoState>>(RepositoryResponse.Loading)
    val editarPlanoTreinoState: StateFlow<RepositoryResponse<PlanoTreinoState>> =
        _editarPlanoTreinoState.asStateFlow()

    private val planoTreinoId: Int = savedStateHandle.get<Int>("planoTreinoId") ?: -1

    init {
        viewModelScope.launch {
            _editarPlanoTreinoState.value = RepositoryResponse.Loading
            try {
                val planoTreino = treinoRepository.getPlanoTreino(planoTreinoId).first()
                val diasTreino = treinoRepository.getDiasTreino(planoTreinoId).first()

                val grupoMuscular = diasTreino.map { it.grupoMuscular }.toMutableList()
                val exerciciosList = diasTreino.map { diaTreino ->
                    treinoRepository.getExercicios(diaTreino.id).first().toMutableList()
                }.toMutableList()

                _editarPlanoTreinoState.update {
                    RepositoryResponse.Success(
                        PlanoTreinoState(
                            nomePlanoTreino = planoTreino.nome,
                            grupoMuscular = grupoMuscular,
                            exerciciosList = exerciciosList
                        )
                    )
                }
            } catch (e: Exception) {
                _editarPlanoTreinoState.value =
                    RepositoryResponse.Error("Erro editarPlanoState: ${e.message}")
            }
        }
    }

    fun setNomePlanoTreino(nome: String) {
        _editarPlanoTreinoState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> RepositoryResponse.Success(
                    currentState.data.copy(
                        nomePlanoTreino = nome
                    )
                )

                else -> currentState // Do nothing for Loading or Error states
            }
        }
    }

    fun setGrupoMuscular(dia: Int, nome: String) {
        _editarPlanoTreinoState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> {
                    val updatedList = currentState.data.grupoMuscular.toMutableList().apply {
                        this[dia] = nome
                    }
                    RepositoryResponse.Success(currentState.data.copy(grupoMuscular = updatedList))
                }

                else -> {
                    currentState
                }
            }
        }
    }

    fun setNomeExercicio(dia: Int, nome: String) {
        _editarPlanoTreinoState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> {
                    val updatedList = currentState.data.nomeExercicio.toMutableList().apply {
                        this[dia] = nome
                    }
                    RepositoryResponse.Success(currentState.data.copy(nomeExercicio = updatedList))
                }

                else -> {
                    currentState
                }
            }
        }
    }

    fun setNumeroSeries(dia: Int, numeroSeries: String) {
        _editarPlanoTreinoState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> {
                    val updatedList = currentState.data.numeroSeries.toMutableList().apply {
                        this[dia] = numeroSeries
                    }
                    RepositoryResponse.Success(currentState.data.copy(numeroSeries = updatedList))
                }

                else -> {
                    currentState
                }
            }
        }
    }

    fun setNumeroRepeticoes(dia: Int, numeroRepeticoes: String) {
        _editarPlanoTreinoState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> {
                    val updatedList = currentState.data.numeroRepeticoes.toMutableList().apply {
                        this[dia] = numeroRepeticoes
                    }
                    RepositoryResponse.Success(currentState.data.copy(numeroRepeticoes = updatedList))
                }

                else -> {
                    currentState
                }
            }
        }
    }

    fun adicionarExercicio(dia: Int, exercicio: Exercicio) {
        _editarPlanoTreinoState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> {
                    currentState.data.exerciciosList[dia].add(exercicio)
                    currentState
                }

                else -> {
                    currentState
                }
            }
        }
    }

    fun removerExercicio(dia: Int, exercicio: Exercicio) {
        _editarPlanoTreinoState.update { currentState ->
            when (currentState) {
                is RepositoryResponse.Success -> {
                    val updatedExercicioList =
                        currentState.data.exerciciosList.toMutableList().apply {
                            val diaExercicios = this[dia].toMutableList()
                            diaExercicios.remove(exercicio)
                            this[dia] = diaExercicios
                        }
                    RepositoryResponse.Success(
                        currentState.data.copy(exerciciosList = updatedExercicioList)
                    )
                }

                else -> {
                    currentState
                }
            }
        }
    }

    suspend fun editarPlanoTreino(planoTreinoId: Int): Pair<Boolean, String> {

        return try {
            val state = _editarPlanoTreinoState.value

            if (state is RepositoryResponse.Success) {

                val nomePlanoTreino = state.data.nomePlanoTreino
                val grupoMuscularList = state.data.grupoMuscular

                if (nomePlanoTreino.isEmpty()) {
                    throw Exception("Nome do plano não pode ser vazio")
                }

                if (grupoMuscularList.any { it.isEmpty() }) {
                    throw Exception("Preencha os campos de grupo muscular")
                }

                val planoTreino = treinoRepository.getPlanoTreino(planoTreinoId).first()

                treinoRepository.updatePlanoTreino(planoTreino.copy(nome = nomePlanoTreino))

                // Remove os DiasTreino e Exercicios antigos
                val diasTreinoAntigos = treinoRepository.getDiasTreino(planoTreino.id).first()
                diasTreinoAntigos.forEach { diaTreino ->
                    val exerciciosAntigos = treinoRepository.getExercicios(diaTreino.id).first()
                    exerciciosAntigos.forEach { exercicio ->
                        treinoRepository.removeExercicio(exercicio)
                    }
                    treinoRepository.removeDiaTreino(diaTreino)
                }

                // Adiciona os novos DiasTreino e Exercicios
                DiasList.dias.forEachIndexed { i, dia ->
                    val diaTreino = DiaTreino(
                        dia = dia,
                        grupoMuscular = grupoMuscularList[i],
                        idPlanoTreino = planoTreino.id
                    )
                    val idDiaTreino = treinoRepository.addDiaTreino(diaTreino).toInt()

                    state.data.exerciciosList[i].forEach { exercicio ->
                        val exercicioComIdAtualizado = exercicio.copy(idDiaTreino = idDiaTreino)
                        treinoRepository.addExercicio(exercicioComIdAtualizado)
                    }
                }

                Pair(true, "Editado com sucesso")
            } else {
                Pair(false, "Estado inválido")
            }

        } catch (e: Exception) {
            Log.e("EditarPlanoTreino", "Erro ao editar: ${e.message}")
            Pair(false, e.message.toString())
        }
    }
}