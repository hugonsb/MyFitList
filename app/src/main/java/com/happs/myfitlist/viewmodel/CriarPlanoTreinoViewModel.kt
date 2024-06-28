package com.happs.myfitlist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.room.treino.TreinoRepository
import com.happs.myfitlist.state.CriarPlanoTreinoState
import com.happs.myfitlist.util.cadastro_plano_treino.DiasList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CriarPlanoTreinoViewModel(
    private val treinoRepository: TreinoRepository
) : ViewModel() {

    private val _criarPlanoTreinoState = MutableStateFlow(CriarPlanoTreinoState())
    val criarPlanoTreinoState: StateFlow<CriarPlanoTreinoState> =
        _criarPlanoTreinoState.asStateFlow()

    private var idUsuario: Int = 1 // valor default, pode ser atualizado dinamicamente

    fun setUsuarioId(id: Int) {
        idUsuario = id
    }

    fun setNomePlanoTreino(nome: String) {
        _criarPlanoTreinoState.update { currentState ->
            currentState.copy(nomePlanoTreino = nome)
        }
    }

    fun setGrupoMuscular(dia: Int, nome: String) {
        _criarPlanoTreinoState.update { currentState ->
            val updatedList = currentState.grupoMuscular.toMutableList().apply {
                this[dia] = nome
            }
            currentState.copy(grupoMuscular = updatedList)
        }
    }

    fun setNomeExercicio(dia: Int, nome: String) {
        _criarPlanoTreinoState.update { currentState ->
            val updatedList = currentState.nomeExercicio.toMutableList().apply {
                this[dia] = nome
            }
            currentState.copy(nomeExercicio = updatedList)
        }
    }

    fun setNumeroSeries(dia: Int, numeroSeries: String) {
        _criarPlanoTreinoState.update { currentState ->
            val updatedList = currentState.numeroSeries.toMutableList().apply {
                this[dia] = numeroSeries
            }
            currentState.copy(numeroSeries = updatedList)
        }
    }

    fun setNumeroRepeticoes(dia: Int, numeroRepeticoes: String) {
        _criarPlanoTreinoState.update { currentState ->
            val updatedList = currentState.numeroRepeticoes.toMutableList().apply {
                this[dia] = numeroRepeticoes
            }
            currentState.copy(numeroRepeticoes = updatedList)
        }
    }

    fun adicionarExercicio(dia: Int, exercicio: Exercicio) {
        _criarPlanoTreinoState.update { currentState ->
            currentState.exerciciosList[dia].add(exercicio)
            currentState
        }
    }

    fun removerExercicio(dia: Int, exercicio: Exercicio) {
        _criarPlanoTreinoState.update { currentState ->
            val updatedExercicioList = currentState.exerciciosList.toMutableList().apply {
                val diaExercicios = this[dia].toMutableList()
                diaExercicios.remove(exercicio)
                this[dia] = diaExercicios
            }
            currentState.copy(exerciciosList = updatedExercicioList)
        }
    }

    suspend fun savePlanoTreino(): Pair<Boolean, String> {
        return try {
            val state = _criarPlanoTreinoState.value
            val planoTreino = PlanoTreino(
                nome = state.nomePlanoTreino,idUsuario = 1 // ALTERE ESSE ID AQUI PRA SER DINAMICO E N DAR PROBLEMA
            )

            if (state.nomePlanoTreino.isEmpty()){
                throw Exception("Nome do plano não pode ser vazio")
            }

            if (state.grupoMuscular.any { it.isEmpty() }) {
                throw Exception("Preencha os campos de grupo muscular")
            }

            val planoTreinoId = treinoRepository.addPlanoTreino(planoTreino).toInt()

            DiasList.dias.forEachIndexed { i,dia ->
                val diaTreino = DiaTreino(
                    dia = dia,
                    grupoMuscular = state.grupoMuscular[i],
                    idPlanoTreino = planoTreinoId
                )
                val idDiaTreino = treinoRepository.addDiaTreino(diaTreino).toInt()

                state.exerciciosList[i].forEach { exercicio ->
                    val exercicioComIdAtualizado = exercicio.copy(idDiaTreino = idDiaTreino)
                    treinoRepository.addExercicio(exercicioComIdAtualizado)
                }
            }

            // Resetar o estado após salvar
            _criarPlanoTreinoState.update { CriarPlanoTreinoState() }
            Pair(true, "Salvo com sucesso") // Retorna sucesso com mensagem
        } catch (e: Exception) {
            Log.e("CriarPlanoTreino", "Erro: ${e.message}")
            Pair(false, e.message.toString()) // Retorna erro com mensagem
        }
    }
}
