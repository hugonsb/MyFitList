package com.happs.myfitlist.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.room.treino.TreinoRepository
import com.happs.myfitlist.state.PlanoTreinoState
import com.happs.myfitlist.util.cadastro_plano_treino.DiasList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarPlanoViewModel(
    private val treinoRepository: TreinoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _editarPlanoState = MutableStateFlow(PlanoTreinoState())
    val editarPlanoState: StateFlow<PlanoTreinoState> = _editarPlanoState.asStateFlow()

    private val planoTreinoId: Int = savedStateHandle.get<Int>("planoTreinoId") ?: -1

    init {
        if (planoTreinoId != -1) {
            viewModelScope.launch {
                val planoTreino = treinoRepository.getPlanoTreino(planoTreinoId).first()
                val diasTreino = treinoRepository.getDiasTreino(planoTreinoId).first()

                val grupoMuscular = diasTreino.map { it.grupoMuscular }.toMutableList()
                val exerciciosList = diasTreino.map { diaTreino ->
                    treinoRepository.getExercicios(diaTreino.id).first().toMutableList()
                }.toMutableList()

                _editarPlanoState.update { currentState ->
                    currentState.copy(
                        nomePlanoTreino = planoTreino.nome,
                        grupoMuscular = grupoMuscular,
                        exerciciosList = exerciciosList
                    )
                }
            }
        }
    }

    fun setNomePlanoTreino(nome: String) {
        _editarPlanoState.update { currentState ->
            currentState.copy(nomePlanoTreino = nome)
        }
    }

    fun setGrupoMuscular(dia: Int, nome: String) {
        _editarPlanoState.update { currentState ->
            val updatedList = currentState.grupoMuscular.toMutableList().apply {
                this[dia] = nome
            }
            currentState.copy(grupoMuscular = updatedList)
        }
    }

    fun setNomeExercicio(dia: Int, nome: String) {
        _editarPlanoState.update { currentState ->
            val updatedList = currentState.nomeExercicio.toMutableList().apply {
                this[dia] = nome
            }
            currentState.copy(nomeExercicio = updatedList)
        }
    }

    fun setNumeroSeries(dia: Int, numeroSeries: String) {
        _editarPlanoState.update { currentState ->
            val updatedList = currentState.numeroSeries.toMutableList().apply {
                this[dia] = numeroSeries
            }
            currentState.copy(numeroSeries = updatedList)
        }
    }

    fun setNumeroRepeticoes(dia: Int, numeroRepeticoes: String) {
        _editarPlanoState.update { currentState ->
            val updatedList = currentState.numeroRepeticoes.toMutableList().apply {
                this[dia] = numeroRepeticoes
            }
            currentState.copy(numeroRepeticoes = updatedList)
        }
    }

    fun adicionarExercicio(dia: Int, exercicio: Exercicio) {
        _editarPlanoState.update { currentState ->
            currentState.exerciciosList[dia].add(exercicio)
            currentState
        }
    }

    fun removerExercicio(dia: Int, exercicio: Exercicio) {
        _editarPlanoState.update { currentState ->
            val updatedExercicioList = currentState.exerciciosList.toMutableList().apply {
                val diaExercicios = this[dia].toMutableList()
                diaExercicios.remove(exercicio)
                this[dia] = diaExercicios
            }
            currentState.copy(exerciciosList = updatedExercicioList)
        }
    }

    suspend fun editarPlanoTreino(planoTreinoId: Int): Pair<Boolean, String> {
        return try {

            val state = _editarPlanoState.value

            val planoTreino = treinoRepository.getPlanoTreino(planoTreinoId).first()

            // Atualiza o PlanoTreino
            treinoRepository.updatePlanoTreino(planoTreino.copy(nome = state.nomePlanoTreino))

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
                    grupoMuscular = state.grupoMuscular[i],
                    idPlanoTreino = planoTreino.id
                )
                val idDiaTreino = treinoRepository.addDiaTreino(diaTreino).toInt()

                state.exerciciosList[i].forEach { exercicio ->
                    val exercicioComIdAtualizado = exercicio.copy(idDiaTreino = idDiaTreino)
                    treinoRepository.addExercicio(exercicioComIdAtualizado)
                }
            }

            _editarPlanoState.update { PlanoTreinoState() } // Reseta o estado após salvar
            Pair(true, "Editado com sucesso")

        } catch (e: Exception) {
            Log.e("CriarPlanoTreino", "Erro ao editar: ${e.message}")
            Pair(false, e.message.toString())
        }
    }
}