package com.happs.myfitlist.room

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
}