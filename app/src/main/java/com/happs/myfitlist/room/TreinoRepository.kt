package com.happs.myfitlist.room

import com.happs.myfitlist.model.dieta.DiaDieta
import com.happs.myfitlist.model.dieta.PlanoDieta
import com.happs.myfitlist.model.dieta.Refeicao
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.model.usuario.Usuario
import kotlinx.coroutines.flow.Flow

interface TreinoRepository {

    fun getUsuario(): Flow<Usuario>
    suspend fun addUser(usuario: Usuario)
    suspend fun updateUser(usuario: Usuario)
    suspend fun removeUser(usuario: Usuario)

    // treino

    suspend fun updatePlanoTreinoPrincipal(usuarioId: Int, planoTreinoId: Int)
    fun getPlanoTreino(idPlanoTreino: Int): Flow<PlanoTreino>
    fun getPlanosTreino(): Flow<List<PlanoTreino>>
    suspend fun addPlanoTreino(planoTreino: PlanoTreino): Long
    suspend fun updatePlanoTreino(planoTreino: PlanoTreino)
    suspend fun removePlanoTreino(planoTreino: PlanoTreino)

    fun getDiasTreino(idPlanoTreino: Int): Flow<List<DiaTreino>>
    suspend fun addDiaTreino(diaTreino: DiaTreino): Long
    suspend fun updateDiaTreino(diaTreino: DiaTreino)
    suspend fun removeDiaTreino(diaTreino: DiaTreino)

    fun getExercicios(idDiaTreino: Int): Flow<List<Exercicio>>
    suspend fun addExercicio(exercicio: Exercicio)
    suspend fun updateExercicio(exercicio: Exercicio)
    suspend fun removeExercicio(exercicio: Exercicio)

    // dieta

    suspend fun updatePlanoDietaPrincipal(usuarioId: Int, planoDietaId: Int)
    fun getPlanoDieta(idPlanoDieta: Int): Flow<PlanoDieta>
    fun getPlanosDieta(): Flow<List<PlanoDieta>>
    suspend fun addPlanoDieta(planoDieta: PlanoDieta): Long
    suspend fun updatePlanoDieta(planoDieta: PlanoDieta)
    suspend fun removePlanoDieta(planoDieta: PlanoDieta)

    fun getDiasDieta(idPlanoDieta: Int): Flow<List<DiaDieta>>
    suspend fun addDiaDieta(diaDieta: DiaDieta): Long
    suspend fun updateDiaDieta(diaDieta: DiaDieta)
    suspend fun removeDiaDieta(diaDieta: DiaDieta)

    fun getRefeicoes(idDiaDieta: Int): Flow<List<Refeicao>>
    suspend fun addRefeicao(refeicao: Refeicao)
    suspend fun updateRefeicao(refeicao: Refeicao)
    suspend fun removeRefeicao(refeicao: Refeicao)

    suspend fun deleteAllData()
}